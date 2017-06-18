package ubublik.network.models.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ubublik.network.db.HibernateUtil;
import ubublik.network.models.Gender;
import ubublik.network.models.Profile;
import ubublik.network.models.security.User;
import ubublik.network.services.UserDataValidator;

import javax.transaction.Transactional;


@Repository
@Transactional
public class ProfileDao{

    @Autowired
    UserDataValidator userDataValidator;


    public Profile getProfileById(long id)throws IllegalArgumentException {
        if (id<1) throw new IllegalArgumentException("Id starts from 1, idiot...");//debiloodporne :)
        try {
            Session session = HibernateUtil.getSession();
            Profile profile = session.get(Profile.class, id);
            //if (profile==null) throw new NoSuchElementException("Profile not found");
            return profile;
        } catch (Exception e){
            throw e;
        }
    }

    public Profile getUserProfile(User user){
        //o rly?
        return user.getProfile();
    }

    public void editProfile(Profile profile){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.save(profile);
            transaction.commit();
        } catch (Exception e)
        {
            throw e;
        } finally {
            session.close();
        }
    }

    public long createProfileForUser(User user){//for users without profile (admins)
        if (user.getProfile()!=null) throw new IllegalArgumentException("User already has a profile");
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            Profile profile = new Profile(user, null, null, null, Gender.NULL, null);
            long id = (long)session.save(profile);
            transaction.commit();
            return id;
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }

    public void removeProfile(Profile profile){
        Session session = HibernateUtil.getSession();
        try{
            Transaction transaction = session.beginTransaction();
            session.remove(profile);
            transaction.commit();
        } catch (Exception e){
            throw e;
        } finally {
            session.close();
        }
    }
}