package ubublik.network.models.security.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ubublik.network.db.HibernateUtil;
import ubublik.network.exceptions.DuplicateUsernameException;
import ubublik.network.exceptions.UserDataFormatException;
import ubublik.network.models.Gender;
import ubublik.network.models.Profile;
import ubublik.network.models.security.Role;
import ubublik.network.models.security.RoleName;
import ubublik.network.models.security.User;
import ubublik.network.rest.entities.Search;
import ubublik.network.rest.entities.SearchOrder;
import ubublik.network.services.UserDataValidator;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;


@Repository
@Transactional
public class UserDao{

    @Autowired
    UserDataValidator userDataValidator;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User getUserById(long id) throws HibernateException
    {
        Session session = HibernateUtil.getSession();
        try {
            return session.get(User.class, id);
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }


    public User getUserByNickname(String nickname)throws UsernameNotFoundException, HibernateException{
        Session session = HibernateUtil.getSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.where(criteriaBuilder.equal(root.get("nickname"), nickname));
            User user = session.createQuery(criteriaQuery).getSingleResult();
            return user;
        } catch (NoResultException nre){
            throw new UsernameNotFoundException("Username not found");
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }


    public long registerUser(User userData)
            throws
            DuplicateUsernameException,
            UserDataFormatException,
            HibernateException{
        User existingUser = null;
        try {
            existingUser = getUserByNickname(userData.getNickname());
        } catch (UsernameNotFoundException unfe){
        }
        if (existingUser!=null) throw new DuplicateUsernameException("Username already taken");

        if (!userDataValidator.validate(UserDataValidator.DataType.NICKNAME, userData.getNickname()))
            throw new UserDataFormatException("Bad username format");
        if (!userDataValidator.validate(UserDataValidator.DataType.NAME, userData.getName()))
            throw new UserDataFormatException("Bad name format");
        if (!userDataValidator.validate(UserDataValidator.DataType.NAME, userData.getSurname()))
            throw new UserDataFormatException("Bad surname format");
        if (!userDataValidator.validate(UserDataValidator.DataType.PASSWORD, userData.getPassword()))
            throw new UserDataFormatException("Bad password format");

        String passwordHash = passwordEncoder.encode(userData.getPassword());
        userData.setPassword(passwordHash);

        Session session = HibernateUtil.getSession();
        User user = new User(
                userData.getNickname(),
                userData.getName(),
                userData.getSurname(),
                userData.getPassword(),
                getUserBasicRoles(),
                true,
                new Date(),
                null);
        try {
            Transaction transaction = session.beginTransaction();
            long id = (long)session.save(user);//save user, because we need this object to create profile
            Profile profile = new Profile(user, null, null, null, Gender.NULL, null);
            session.save(profile);//save profile
            transaction.commit();
            return id;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public long registerAdmin(User user){
        return 0;
        // TODO: 17-Jun-17
    }

    private List<Role> getUserBasicRoles(){
        LinkedList<Role> linkedList = new LinkedList<>();
        linkedList.add(roleDao.getRoleByRoleName(RoleName.ROLE_USER));
        return linkedList;
    }

    public int searchCount(Search search){
        Session session = HibernateUtil.getSession();
        try {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            String sql = ("SELECT u FROM User u\n" +
                    "     WHERE (1=1)");
            if (search.getName()!=null){
                sql+= "AND (lower(concat(u.name, ' ', u.surname, ' ', u.name))LIKE lower('%"+search.getName()+"%'))";
            }
            if (search.getCity()!=null){
                sql+= "AND (lower(u.profile.city)LIKE lower('"+search.getCity()+"'))";
            }
            if (search.getCountry()!=null){
                sql+= "AND (lower(u.profile.country)LIKE lower('"+search.getCountry()+"'))";
            }
            if (search.getAge_from()!=null){
                sql+= "AND (u.profile.dob <= :agefrom)";
            }
            if (search.getAge_to()!=null){
                sql+= "AND (u.profile.dob >= :ageto)";
            }
            if (search.getGenderObject()!=Gender.NULL){
                sql+= "AND (u.profile.gender = :gender)";
            }
            javax.persistence.Query query = em.createQuery(sql);
            if (search.getAge_from()!=null){
                Calendar c = new GregorianCalendar();
                c.add(Calendar.YEAR, search.getAge_from()*-1);
                query.setParameter("agefrom",c.getTime(), TemporalType.DATE);
            }
            if (search.getAge_to()!=null){
                Calendar c = new GregorianCalendar();
                c.add(Calendar.YEAR, search.getAge_from()*-1);
                query.setParameter("ageto",c.getTime(), TemporalType.DATE);
            }
            if (search.getGender()!=null)
            {
                query.setParameter("gender", search.getGenderObject());
            }
            query.setFirstResult(search.getOffset());
            query.setMaxResults(search.getSize());
            return ((Long)query.getSingleResult()).intValue();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public List<User> searchUsers(Search search){
        Session session = HibernateUtil.getSession();
        try {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            String sql = ("SELECT u FROM User u\n" +
                    "     WHERE (1=1)");
            if (search.getName()!=null){
                sql+= "AND (lower(concat(u.name, ' ', u.surname, ' ', u.name))LIKE lower('%"+search.getName()+"%'))";
            }
            if (search.getCity()!=null){
                sql+= "AND (lower(u.profile.city)LIKE lower('"+search.getCity()+"'))";
            }
            if (search.getCountry()!=null){
                sql+= "AND (lower(u.profile.country)LIKE lower('"+search.getCountry()+"'))";
            }
            if (search.getAge_from()!=null){
                sql+= "AND (u.profile.dob <= :agefrom)";
            }
            if (search.getAge_to()!=null){
                sql+= "AND (u.profile.dob >= :ageto)";
            }
            if (search.getGenderObject()!=Gender.NULL){
                sql+= "AND (u.profile.gender = :gender)";
            }
            if (search.getOrderEnum()!= SearchOrder.NONE){
                sql+="ORDER BY ";
                switch (search.getOrderEnum()){
                    case DATE:
                        sql+="u.registered";
                        break;
                    case DATE_DESC:
                        sql+="u.registered DESC";
                        break;
                    case NAME:
                        sql+="u.name";
                        break;
                    case NAME_DESC:
                        sql+="u.name DESC";
                        break;
                    case SURNAME:
                        sql+="u.surname";
                        break;
                    case SURNAME_DESC:
                        sql+="u.surname DESC";
                        break;
                }
            }
            javax.persistence.Query query = em.createQuery(sql);
            if (search.getAge_from()!=null){
                Calendar c = new GregorianCalendar();
                c.add(Calendar.YEAR, search.getAge_from()*-1);
                query.setParameter("agefrom",c.getTime(), TemporalType.DATE);
            }
            if (search.getAge_to()!=null){
                Calendar c = new GregorianCalendar();
                c.add(Calendar.YEAR, search.getAge_from()*-1);
                query.setParameter("ageto",c.getTime(), TemporalType.DATE);
            }
            if (search.getGender()!=null)
            {
                query.setParameter("gender", search.getGenderObject());
            }
            query.setFirstResult(search.getOffset());
            query.setMaxResults(search.getSize());
            return (List<User>) query.getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

}
