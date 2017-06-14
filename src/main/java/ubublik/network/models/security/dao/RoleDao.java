package ubublik.network.models.security.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ubublik.network.db.HibernateUtil;
import ubublik.network.models.security.Role;
import ubublik.network.models.security.RoleName;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Bublik on 12-Jun-17.
 */
@Repository
@Transactional
public class RoleDao {

    public List<Role> getRoles(){
        try {
            Session session = HibernateUtil.getSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
            Root<Role> root = criteriaQuery.from(Role.class);
            criteriaQuery.select(root);
            List<Role> roles = session.createQuery(criteriaQuery).list();
            return roles;
        } catch (Exception e){
            return null;
        }
    }

    public Role getRoleByRoleName(RoleName roleName){
        // FIXME: 14-Jun-17 use separate hibernate selection without getRoles() method
        List<Role> roles = getRoles();
        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()){
            Role temp = iterator.next();
            if (temp.getName().equals(roleName))
            {
                return temp;
            }
        }
        throw new NoSuchElementException("Role not found");
    }
}
