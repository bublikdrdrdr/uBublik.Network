package ubublik.network.models.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ubublik.network.db.HibernateUtil;
import ubublik.network.models.FriendRelation;
import ubublik.network.models.Gender;
import ubublik.network.models.security.User;
import ubublik.network.rest.entities.Search;
import ubublik.network.rest.entities.SearchOrder;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    public boolean haveFriendRelation(User user1, User user2){
        Session session = HibernateUtil.getSession();
        try {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            javax.persistence.Query query = em.createQuery("SELECT COUNT(ffr.id) FROM FriendRelation ffr\n" +
                    "WHERE (ffr.sender=:user1 AND ffr.receiver=user2) OR (ffr.sender=user2 AND ffr.receiver=user1)");
            query.setParameter("user1", user1);
            query.setParameter("user2", user2);
            long res = (Long)query.getSingleResult();
            return res == 2L;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public int getUserFriendsCount(User user){
        Session session = HibernateUtil.getSession();
        try {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            javax.persistence.Query query = em.createQuery("SELECT COUNT(ffr.receiver) FROM FriendRelation ffr\n" +
                    "     JOIN FriendRelation sfr on sfr.receiver=ffr.sender and sfr.sender=ffr.receiver\n" +
                    "     WHERE ffr.sender= :id AND ffr.receiver=sfr.sender");
            query.setParameter("id", user);
            return ((Long)query.getSingleResult()).intValue();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }


    public List<User> getUserFriends(User user, int offset, int count){
        Session session = HibernateUtil.getSession();
        try {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            javax.persistence.Query query = em.createQuery("SELECT ffr.receiver FROM FriendRelation ffr\n" +
                    "     JOIN FriendRelation sfr on sfr.receiver=ffr.sender and sfr.sender=ffr.receiver\n" +
                    "     WHERE ffr.sender= :id AND ffr.receiver=sfr.sender");
            query.setParameter("id", user);
            query.setFirstResult(offset);
            query.setMaxResults(count);
            return (List<User>) query.getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public Integer searchCount(User user, Search search) {
        Session session = HibernateUtil.getSession();
        try {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            String sql = ("SELECT COUNT(ffr.receiver) FROM FriendRelation ffr\n" +
                    "     JOIN FriendRelation sfr on sfr.receiver=ffr.sender and sfr.sender=ffr.receiver\n" +
                    "     WHERE (ffr.sender= :user AND ffr.receiver=sfr.sender)");
            if (search.getName()!=null){
                sql+= "AND (lower(concat(ffr.receiver.name, ' '))LIKE lower('%"+search.getName()+"%'))";
            }
            if (search.getCity()!=null){
                sql+= "AND (lower(ffr.receiver.profile.city)LIKE lower('"+search.getCity()+"'))";
            }
            if (search.getCountry()!=null){
                sql+= "AND (lower(ffr.receiver.profile.country)LIKE lower('"+search.getCountry()+"'))";
            }
            if (search.getAge_from()!=null){
                sql+= "AND (ffr.receiver.profile.dob <= :agefrom)";
            }
            if (search.getAge_to()!=null){
                sql+= "AND (ffr.receiver.profile.dob >= :ageto)";
            }
            if (search.getGenderObject()!= Gender.NULL){
                sql+= "AND (u.profile.gender = :gender)";
            }

            javax.persistence.Query query = em.createQuery(sql);
            query.setParameter("user", user);

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
            return ((Long)query.getSingleResult()).intValue();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public List<User> searchFriends(User user, Search search) {
        Session session = HibernateUtil.getSession();
        try {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            String sql = ("SELECT ffr.receiver FROM FriendRelation ffr\n" +
                    "     JOIN FriendRelation sfr on sfr.receiver=ffr.sender and sfr.sender=ffr.receiver\n" +
                    "     WHERE (ffr.sender= :user AND ffr.receiver=sfr.sender)");
            if (search.getName()!=null){
                sql+= "AND (lower(concat(ffr.receiver.name, ' '))LIKE lower('%"+search.getName()+"%'))";
            }
            if (search.getCity()!=null){
                sql+= "AND (lower(ffr.receiver.profile.city)LIKE lower('"+search.getCity()+"'))";
            }
            if (search.getCountry()!=null){
                sql+= "AND (lower(ffr.receiver.profile.country)LIKE lower('"+search.getCountry()+"'))";
            }
            if (search.getAge_from()!=null){
                sql+= "AND (ffr.receiver.profile.dob <= :agefrom)";
            }
            if (search.getAge_to()!=null){
                sql+= "AND (ffr.receiver.profile.dob >= :ageto)";
            }
            if (search.getGenderChar()!='N'){
                sql+= "AND (ffr.receiver.profile.gender = '"+search.getGenderChar()+"')";
            }
            if (search.getGenderObject()!=Gender.NULL){
                sql+= "AND (u.profile.gender = :gender)";
            }
            if (search.getOrderEnum()!= SearchOrder.NONE){
                sql+="ORDER BY ";
                switch (search.getOrderEnum()){
                    case DATE:
                        sql+="ffr.date";
                        break;
                    case DATE_DESC:
                        sql+="ffr.date DESC";
                        break;
                    case NAME:
                        sql+="ffr.receiver.name";
                        break;
                    case NAME_DESC:
                        sql+="ffr.receiver.name DESC";
                        break;
                    case SURNAME:
                        sql+="ffr.receiver.surname";
                        break;
                    case SURNAME_DESC:
                        sql+="ffr.receiver.surname DESC";
                        break;
                }
            }

            javax.persistence.Query query = em.createQuery(sql);
            query.setParameter("user", user);

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
            if (search.getGender()!=null){
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

  /*  public List<User> getUserFriendsV3(User user){
        Session session = HibernateUtil.getSession();
        try{
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<FriendRelation> criteriaQuery = criteriaBuilder.createQuery(FriendRelation.class);
            Root<FriendRelation> ffr = criteriaQuery.from(FriendRelation.class);
            Join<FriendRelation, User> uj = ffr.join("sender");
            Join<FriendRelation, User> sfs = uj.join("receiver");
            sfs.on(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(sfs.get("receiver"), ffr.get("sender")),
                            criteriaBuilder.equal(sfs.get("sender"), ffr.get("receiver"))
                    )
            );
            criteriaQuery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(ffr.get("receiver"), sfs.get("sender")),
                            criteriaBuilder.equal(ffr.get("sender"), user)
                    )
            );
            criteriaQuery.select(ffr);
            criteriaQuery.distinct(true);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }*/

  /*  public boolean haveFriendRelation(User user1, User user2){
        Session session = HibernateUtil.getSession();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            session.
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<FriendRelation> friendRelationRoot = criteriaQuery.from(FriendRelation.class);
            Join<FriendRelation, User> userSenderJoin = friendRelationRoot.join("sender");

            criteriaQuery.select(criteriaBuilder.count(friendRelationRoot));
            criteriaQuery.distinct(true);

            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }*/


}
