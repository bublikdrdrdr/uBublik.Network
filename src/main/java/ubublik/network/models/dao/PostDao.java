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
        Session session = HibernateUtil.getSession();
        try {
            Post post = session.get(Post.class, id);
            session.close();
            return post;
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }

    public long savePost(Post post){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            long id = (long)session.save(post);
            transaction.commit();
            session.close();
            return id;
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }

    public void removePost(Post post){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.remove(post);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }

    public List<Post> getUserPosts(User user, int offset, int size) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String sql = "SELECT * FROM Post p\n" +
                    "WHERE \n" +
                    "p.user=:user" +
                    "ORDER BY p.id desc";
            Query query = em.createQuery(sql, Post.class);
            query.setParameter("user", user);
            query.setFirstResult(offset);
            query.setMaxResults(size);
            List<Post> list = query.getResultList();
            em.close();
            return list;
        } catch (Exception e) {
            em.close();
            throw e;
        }
    }

    public int getUserPostsCount(User user) {
        Session session = HibernateUtil.getSession();
        try {
            Query query = session.createQuery("SELECT COUNT(p.id) FROM Post" +
                    "WHERE p.user=:user" +
                    "ORDER BY p.id desc");
            query.setParameter("user", user);
            int result = ((Number) query.getSingleResult()).intValue();
            session.close();
            return result;
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }
}
