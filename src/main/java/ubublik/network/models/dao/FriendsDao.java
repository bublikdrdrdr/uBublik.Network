package ubublik.network.models.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ubublik.network.db.HibernateUtil;
import ubublik.network.models.FriendRelation;
import ubublik.network.models.security.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Bublik on 17-Jun-17.
 */
@Repository
@Transactional
public class FriendsDao {

    public FriendRelation getFriendRelationById(long id){
        Session session = HibernateUtil.getSession();
        try{
            return session.get(FriendRelation.class, id);
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public long saveFriendRelation(FriendRelation friendRelation){
        Session session = HibernateUtil.getSession();
        try {
            Transaction transaction = session.beginTransaction();
            long id =  (long)session.save(friendRelation);
            transaction.commit();
            return id;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public List<FriendRelation> getAllUserFriendRelations(User user){
        Session session = HibernateUtil.getSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<FriendRelation> criteriaQuery = criteriaBuilder.createQuery(FriendRelation.class);
            Root<FriendRelation> profilePictureRoot = criteriaQuery.from(FriendRelation.class);

            criteriaQuery.where(criteriaBuilder.equal(profilePictureRoot.get("user"), user));
            criteriaQuery.select(profilePictureRoot);
            criteriaQuery.distinct(true);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }


}
