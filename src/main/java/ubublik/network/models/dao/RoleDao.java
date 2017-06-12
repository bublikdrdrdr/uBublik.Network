package ubublik.network.models.dao;

import org.springframework.stereotype.Repository;
import ubublik.network.models.Role;
import ubublik.network.models.RoleName;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Bublik on 12-Jun-17.
 */
@Repository
@Transactional
public class RoleDao {

    public List<Role> getRoles(){
        return null;
    }

    public Role getRoleByRoleName(RoleName roleName){
        return null;
    }
}
