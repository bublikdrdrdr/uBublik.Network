package ubublik.network.db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import ubublik.network.models.*;
import ubublik.network.models.security.Role;
import ubublik.network.models.security.User;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = getSessionFactory();
    private static StandardServiceRegistry registry;

        public static SessionFactory getSessionFactory() {
            try {
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

                Map<String, String> settings = new HashMap<>();
                settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/network");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "root");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.FORMAT_SQL, "true");
                registryBuilder.applySettings(settings);
                MetadataSources metadataSources = new MetadataSources(
                        registryBuilder.build());
                //entity classes mapping
                metadataSources.addAnnotatedClass(User.class);
                metadataSources.addAnnotatedClass(Role.class);
                metadataSources.addAnnotatedClass(Profile.class);
                metadataSources.addAnnotatedClass(Message.class);
                metadataSources.addAnnotatedClass(FriendRelation.class);
                metadataSources.addAnnotatedClass(Image.class);
                metadataSources.addAnnotatedClass(ProfilePicture.class);


                SessionFactory sessionFactory = metadataSources
                        .getMetadataBuilder().build()
                        .getSessionFactoryBuilder().build();
                return sessionFactory;

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                return null;
            }
        }

        public static Session getSession()
            throws HibernateException {
            if (sessionFactory==null) throw new HibernateException("SessionFactory is null");
            return sessionFactory.openSession();
        }

    public static EntityManager getEntityManager(){
        return getSession().getEntityManagerFactory().createEntityManager();
    }
}