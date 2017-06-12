package ubublik.network.models.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ubublik.network.db.HibernateUtil;
import ubublik.network.exceptions.DuplicateUsernameException;
import ubublik.network.exceptions.UserDataFormatException;
import ubublik.network.models.Role;
import ubublik.network.models.RoleName;
import ubublik.network.models.User;
import ubublik.network.services.UserDataValidator;

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

    public User getUserByNickname(String nickname){
        try {
            Session session = HibernateUtil.getSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(
                    criteriaQuery.from(User.class))
                    .where(criteriaBuilder.equal(root.get("nickname"), nickname));
            User user = session.createQuery(criteriaQuery).getSingleResult();
            return user;
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

        if (userDataValidator.validate(UserDataValidator.DataType.NICKNAME, userData.getNickname()))
            throw new UserDataFormatException("Bad username format");
        // TODO: 12-Jun-17 check all user data

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
