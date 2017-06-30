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

    public Integer getDialogMessagesCount(User owner, User user)
    {
        EntityManager em = HibernateUtil.getEntityManager();
        String sql = "SELECT COUNT(m.id) FROM Messages m\n" +
                "WHERE \n" +
                "(m.sender=:me AND m.receiver=:user AND m.deletedBySender=0) OR (m.sender=:user and m.receiver=:me and m.deletedByReceiver=0)";
        Query query = em.createQuery(sql);
        query.setParameter("user", user);
        query.setParameter("me", owner);
        return ((Number)query.getResultList()).intValue();
    }

    public List<Message> getDialogMessages(User owner, User user, int offset, int count)
    {
        EntityManager em = HibernateUtil.getEntityManager();
        String sql = "SELECT * FROM Messages m\n" +
                "WHERE \n" +
                "(m.sender=:me AND m.receiver=:user AND m.deletedBySender=0) OR (m.sender=:user and m.receiver=:me and m.deletedByReceiver=0)" +
                "ORDER BY m.id";
        Query query = em.createQuery(sql);
        query.setParameter("user", user);
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
    public List<Message> getDialogs(User owner, int offset, int count){
        EntityManager em = HibernateUtil.getEntityManager();
        Query query = em.createNativeQuery("SELECT mm.* FROM messages mm\n" +
                "JOIN (SELECT u.id, MAX(m.message_date) as max_date FROM messages m\n" +
                "\tJOIN users u ON u.id=m.sender or u.id=m.receiver\n" +
                "\tWHERE ((m.sender=:user and m.receiver=u.id AND m.deleted_by_sender=0) OR (m.receiver=:user and m.sender=u.id and m.deleted_by_receiver=0)) and u.id<>:user\n" +
                "\tGROUP BY u.id) AS recent ON\n" +
                "mm.message_date=recent.max_date\n" +
                "and (mm.receiver=recent.id OR mm.sender=recent.id)\n" +
                "ORDER BY max_date desc", Message.class);
        query.setParameter("user", owner);
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }

    public int getDialogsCount(User owner){
        Session session = HibernateUtil.getSession();
        Query query = session.createNativeQuery("SELECT COUNT(mm.id) FROM messages mm\n" +
                "JOIN (SELECT u.id, MAX(m.message_date) as max_date FROM messages m\n" +
                "\tJOIN users u ON u.id=m.sender or u.id=m.receiver\n" +
                "\tWHERE ((m.sender=:user and m.receiver=u.id AND m.deleted_by_sender=0) OR (m.receiver=:user and m.sender=u.id and m.deleted_by_receiver=0)) and u.id<>:user\n" +
                "\tGROUP BY u.id) AS recent ON\n" +
                "mm.message_date=recent.max_date\n" +
                "and (mm.receiver=recent.id OR mm.sender=recent.id)\n" +
                "ORDER BY max_date desc");
        query.setParameter("user", owner);
        return ((Number)query.getSingleResult()).intValue();
    }

    public int removeDialog(User owner, User user){
        Session session = HibernateUtil.getSession();
        try {
            int count = 0;
            EntityManager em = HibernateUtil.getEntityManager();
            String sql = "UPDATE Message m SET deletedBySender = '1'" +
                    "WHERE (m.sender=:me AND m.receiver=:user AND m.deletedBySender=0)";
            Query query = em.createQuery(sql);
            query.setParameter("me", owner);
            query.setParameter("user", user);
            count+= query.executeUpdate();

            sql = "UPDATE Message m SET deletedByReceiver = '1'" +
                    "WHERE (m.receiver=:me AND m.sender=:user AND m.deletedByReceiver=0)";
            query = em.createQuery(sql);
            query.setParameter("me", owner);
            query.setParameter("user", user);
            count+=query.executeUpdate();
            return count;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

}
