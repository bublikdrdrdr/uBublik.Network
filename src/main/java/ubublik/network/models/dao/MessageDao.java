package ubublik.network.models.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ubublik.network.db.HibernateUtil;
import ubublik.network.models.Message;
import ubublik.network.models.security.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Bublik on 24-Jun-17.
 */
@Repository
@Transactional
public class MessageDao {

    public Message getMessageById(long id){
        Session session = HibernateUtil.getSession();
        try {
            return session.get(Message.class, id);
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public long saveMessage(Message message){
        Session session = HibernateUtil.getSession();
        try {
            Transaction transaction = session.beginTransaction();
            long id =  (long)session.save(message);
            transaction.commit();
            return id;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public void removeMessage(Message message){
        Session session = HibernateUtil.getSession();
        try {
            session.remove(message);
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Message> getUserSentMessages(User user){
        return user.getSentMessages();
    }

    public List<Message> getUserReceivedMessages(User user){
        return user.getReceivedMessages();
    }

    public Message getLastMessage(User user){
        Session session = HibernateUtil.getSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Message> criteriaQuery = criteriaBuilder.createQuery(Message.class);
            Root<Message> messageRoot = criteriaQuery.from(Message.class);
            criteriaQuery.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(messageRoot.get("sender"), user),
                            criteriaBuilder.equal(messageRoot.get("receiver"), user)
                    )
            );
            criteriaQuery.orderBy(criteriaBuilder.desc(messageRoot.get("messageDate")));
            criteriaQuery.select(messageRoot);
            criteriaQuery.distinct(true);
            return session.createQuery(criteriaQuery).setMaxResults(1).getSingleResult();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Message> getDialogMessages(User owner, User friend, int offset, int count)//змінити назву собеседника
    {
        EntityManager em = HibernateUtil.getEntityManager();
        String sql = "SELECT * FROM Messages m\n" +
                "WHERE \n" +
                "(m.sender=:me AND m.receiver=:user AND m.deletedBySender=0) OR (m.sender=:user and m.receiver=:me and m.deletedByReceiver=0)" +
                "ORDER BY m.messageDate";// TODO: 28-Jun-17 try to order by id
        Query query = em.createQuery(sql);
        query.setParameter("user", friend);
        query.setParameter("me", owner);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return (List<Message>) query.getResultList();
    }

    /*
    SELECT * FROM messages mm
JOIN (SELECT u.id, MAX(m.message_date) as max_date FROM messages m
	JOIN users u ON u.id=m.sender or u.id=m.receiver
	WHERE ((m.sender=4 and m.receiver=u.id AND m.deleted_by_sender=0) OR (m.receiver=4 and m.sender=u.id and m.deleted_by_receiver=0)) and u.id<>4
	GROUP BY u.id) AS recent ON
mm.message_date=recent.max_date
and (mm.receiver=recent.id OR mm.sender=recent.id)
ORDER BY max_date desc
     */
    // FIXME: 28-Jun-17  JOIN (SELECT ... does not work
    public List<Message> getDialogs(User owner, int offset, int count){
        EntityManager em = HibernateUtil.getEntityManager();
        String sql = "    SELECT mm FROM Messages mm " +
                "JOIN (SELECT u, MAX(m.messageDate) as max_date FROM Messages m " +
                "JOIN Users u ON u.id=m.sender or u.id=m.receiver " +
                "WHERE ((m.sender=:user and m.receiver=u.id AND m.deletedBySender=0) OR (m.receiver=:user and m.sender=u.id and m.deletedByReceiver=0)) and u<>:user " +
                "GROUP BY u.id) AS recent ON " +
                "mm.messageDate=recent.max_date " +
                "and (mm.receiver=recent.id OR mm.sender=recent.id) " +
                "ORDER BY max_date desc";
        Query query = em.createQuery(sql);
        query.setParameter("user", owner);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return (List<Message>) query.getResultList();
    }

    public int getDialogsCount(User owner){
        EntityManager em = HibernateUtil.getEntityManager();
        String sql = "SELECT COUNT(mm.id) FROM Messages mm " +
                "JOIN (SELECT u, MAX(m.messageDate) as max_date FROM Messages m " +
                "JOIN Users u ON u.id=m.sender or u.id=m.receiver " +
                "WHERE ((m.sender=:user and m.receiver=u.id AND m.deletedBySender=0) OR (m.receiver=:user and m.sender=u.id and m.deletedByReceiver=0)) and u<>:user " +
                "GROUP BY u.id) AS recent ON " +
                "mm.messageDate=recent.max_date " +
                "and (mm.receiver=recent.id OR mm.sender=recent.id) " +
                "ORDER BY max_date desc";
        Query query = em.createQuery(sql);
        query.setParameter("user", owner);
        return ((Long)query.getSingleResult()).intValue();
    }

}
