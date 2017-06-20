package ubublik.network.models.dao;

import org.hibernate.HibernateException;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Bublik on 17-Jun-17.
 */
@Repository
@Transactional
public class FriendsDao {

    public FriendRelation getFriendRelationById(long id) throws HibernateException{
        Session session = HibernateUtil.getSession();
        try{
            return session.get(FriendRelation.class, id);
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public long saveFriendRelation(FriendRelation friendRelation) throws HibernateException{
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

    public void removeFriendRelation(FriendRelation friendRelation) throws IllegalArgumentException, HibernateException{
        Session session = HibernateUtil.getSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.remove(friendRelation);
            transaction.commit();
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
            Root<FriendRelation> friendRelationRoot = criteriaQuery.from(FriendRelation.class);
            criteriaQuery.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(friendRelationRoot.get("sender"), user),
                            criteriaBuilder.equal(friendRelationRoot.get("receiver"), user)
                    )

            );
            criteriaQuery.select(friendRelationRoot);
            criteriaQuery.distinct(true);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public List<FriendRelation> getOutgoingFriendRequests(User user){
        Session session = HibernateUtil.getSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<FriendRelation> criteriaQuery = criteriaBuilder.createQuery(FriendRelation.class);
            Root<FriendRelation> friendRelationRoot = criteriaQuery.from(FriendRelation.class);
            criteriaQuery.where(criteriaBuilder.equal(friendRelationRoot.get("sender"), user));
            criteriaQuery.select(friendRelationRoot);
            criteriaQuery.distinct(true);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public List<FriendRelation> getIncomingFriendRequests(User user){
        Session session = HibernateUtil.getSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<FriendRelation> criteriaQuery = criteriaBuilder.createQuery(FriendRelation.class);
            Root<FriendRelation> friendRelationRoot = criteriaQuery.from(FriendRelation.class);
            criteriaQuery.where(criteriaBuilder.equal(friendRelationRoot.get("receiver"), user));
            criteriaQuery.select(friendRelationRoot);
            criteriaQuery.distinct(true);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     SELECT ffr.request_receiver FROM friend_relations ffr
     JOIN friend_relations sfr on sfr.request_receiver=ffr.request_sender and sfr.request_sender=ffr.request_receiver
     WHERE ffr.request_sender=? AND ffr.request_receiver=sfr.request_sender
     */
    public List<User> getUserFriends(User user){
        Session session = HibernateUtil.getSession();
        try {
            List<User> friends = new LinkedList<>();

            List<FriendRelation> sent = getOutgoingFriendRequests(user);
            List<FriendRelation> received = getIncomingFriendRequests(user);
// FIXME: 20-Jun-17 omg... i wanna get users result by sql query, but i don't know how to realize it in criteriaquery
            Iterator<FriendRelation> sentIterator = sent.iterator();
            while (sentIterator.hasNext()) {
                FriendRelation sCurrent = sentIterator.next();
                Iterator<FriendRelation> receivedIterator = received.iterator();
                while (receivedIterator.hasNext()) {
                    FriendRelation rCurrent = receivedIterator.next();
                    if (sCurrent.getReceiver().getId().equals(rCurrent.getSender().getId())) {
                        friends.add(rCurrent.getSender());
                        receivedIterator.remove();
                        sentIterator.remove();
                        break;
                    }
                }
            }
            return friends;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }


}
