package ubublik.network.models.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ubublik.network.db.HibernateUtil;
import ubublik.network.models.Post;
import ubublik.network.models.security.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class PostDao {

    public Post getPostById(long id)throws IllegalArgumentException {
        try {
            Session session = HibernateUtil.getSession();
            Post post = session.get(Post.class, id);
            return post;
        } catch (Exception e){
            throw e;
        }
    }

    public long savePost(Post post){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            long id = (long)session.save(post);
            transaction.commit();
            return id;
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public void removePost(Post post){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.remove(post);
            transaction.commit();
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Post> getUserPosts(User user, int offset, int size){
        EntityManager em = HibernateUtil.getEntityManager();
        String sql = "SELECT * FROM Post p\n" +
                "WHERE \n" +
                "p.user=:user" +
                "ORDER BY p.id desc";
        Query query = em.createQuery(sql, Post.class);
        query.setParameter("user", user);
        query.setFirstResult(offset);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public int getUserPostsCount(User user){
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("SELECT COUNT(p.id) FROM Post" +
                "WHERE p.user=:user" +
                "ORDER BY p.id desc");
        query.setParameter("user", user);
        return ((Number)query.getSingleResult()).intValue();
    }
}
