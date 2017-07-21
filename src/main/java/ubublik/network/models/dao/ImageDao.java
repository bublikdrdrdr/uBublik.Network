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
            session.close();
            return id;
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }

    public Image getImageById(long id){
        Session session = HibernateUtil.getSession();
        try{
            Image image = session.get(Image.class, id);
            session.close();
            return image;
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }

    public void removeImage(Image image){
        Session session = HibernateUtil.getSession();
        try {
            Transaction transaction = session.beginTransaction();
            image.setRemoved(true);
            image.setData(null);//
            session.save(image);
            session.close();
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }

    public List<ProfilePicture> getProfilePictures(User user, int offset, int size){
        Session session = HibernateUtil.getSession();
        try{
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ProfilePicture> criteriaQuery = criteriaBuilder.createQuery(ProfilePicture.class);
            Root<ProfilePicture> profilePictureRoot = criteriaQuery.from(ProfilePicture.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            criteriaQuery.where(criteriaBuilder.equal(profilePictureRoot.get("user"), user));
            criteriaQuery.select(profilePictureRoot);
            criteriaQuery.distinct(true);
            List<ProfilePicture> list = session.createQuery(criteriaQuery).setFirstResult(offset).setMaxResults(size).getResultList();
            session.close();
            return list;
        } catch (Exception e) {
            session.close();
            throw e;
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
            session.close();
            return id;
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }

    public ProfilePicture getProfilePictureById(long id){
        Session session = HibernateUtil.getSession();
        try{
             ProfilePicture profilePicture = session.get(ProfilePicture.class, id);
            session.close();
            return profilePicture;
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }

    public void removeProfilePicture(ProfilePicture profilePicture){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.remove(profilePicture);
            removeImage(profilePicture.getImage());
            transaction.commit();
            session.close();
        } catch (Exception e) {
            session.close();
            throw e;
        }
    }
}
