package ubublik.network.models.dao;


import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ubublik.network.db.HibernateUtil;
import ubublik.network.models.Image;

@Repository
@Transactional
public class ImageDao {


    public long addImage(Image image){
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
        image.setRemoved(true);
    }
}
