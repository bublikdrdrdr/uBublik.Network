package ubublik.network.models.security.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ubublik.network.db.HibernateUtil;
import ubublik.network.exceptions.DuplicateUsernameException;
import ubublik.network.exceptions.UserDataFormatException;
import ubublik.network.models.security.Role;
import ubublik.network.models.security.RoleName;
import ubublik.network.models.security.User;
import ubublik.network.services.UserDataValidator;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Repository
@Transactional
public class UserDao{

    @Autowired
    UserDataValidator userDataValidator;

    @Autowired
    RoleDao roleDao;

    @Autowired
    PasswordEncoder passwordEncoder;


    public User getUserByNickname(String nickname)throws UsernameNotFoundException{
        try {
            Session session = HibernateUtil.getSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.where(criteriaBuilder.equal(root.get("nickname"), nickname));
            User user = session.createQuery(criteriaQuery).getSingleResult();
            return user;
        } catch (NoResultException nre){
            throw new UsernameNotFoundException("Username not found");
        } catch (Exception e){
            return null;
        }
    }

    public long registerUser(User userData)
            throws
            DuplicateUsernameException,
            UserDataFormatException{
        User existingUser = getUserByNickname(userData.getNickname());
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

        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = new User(
                userData.getNickname(),
                userData.getName(),
                userData.getSurname(),
                userData.getPassword(),
                getUserBasicRoles(),
                true,
                new Date());
        try {
            Transaction transaction = session.beginTransaction();
            long id = (long)session.save(user);
            transaction.commit();
            return id;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    private List<Role> getUserBasicRoles(){
        LinkedList<Role> linkedList = new LinkedList<>();
        linkedList.add(roleDao.getRoleByRoleName(RoleName.ROLE_USER));
        return linkedList;
    }
}
