package ubublik.network.models.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ubublik.network.db.HibernateUtil;
import ubublik.network.models.Image;
import ubublik.network.models.ProfilePicture;
import ubublik.network.models.security.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class ImageDao {


    public long saveImage(Image image){
        image.setId(null);
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            long id = (long)session.save(image);
            transaction.commit();
            return id;
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public Image getImageById(long id){
        Session session = HibernateUtil.getSession();
        try{
            Image image = session.get(Image.class, id);
            return image;
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public void removeImage(Image image){
        Session session = HibernateUtil.getSession();
        try {
            Transaction transaction = session.beginTransaction();
            image.setRemoved(true);
            image.setData(null);//
            session.save(image);
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public List<ProfilePicture> getProfilePictures(User user){
        Session session = HibernateUtil.getSession();
        try{
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ProfilePicture> criteriaQuery = criteriaBuilder.createQuery(ProfilePicture.class);
            Root<ProfilePicture> profilePictureRoot = criteriaQuery.from(ProfilePicture.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            criteriaQuery.where(criteriaBuilder.equal(profilePictureRoot.get("user"), user));
            criteriaQuery.select(profilePictureRoot);
            criteriaQuery.distinct(true);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public long addProfilePicture(Image image){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            long iid = (long)session.save(image);
            image.setId(iid);
            ProfilePicture profilePicture = new ProfilePicture(image.getOwner(), image);
            long id = (long)session.save(profilePicture);
            return id;
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public ProfilePicture getProfilePictureById(long id){
        Session session = HibernateUtil.getSession();
        try{
             return session.get(ProfilePicture.class, id);
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public void removeProfilePicture(ProfilePicture profilePicture){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.remove(profilePicture);
            removeImage(profilePicture.getImage());
            transaction.commit();
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }
}
