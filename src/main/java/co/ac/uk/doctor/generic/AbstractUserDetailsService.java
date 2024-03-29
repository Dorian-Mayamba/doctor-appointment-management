package co.ac.uk.doctor.generic;

import co.ac.uk.doctor.constants.RoleConstants;
import co.ac.uk.doctor.entities.jpa.Role;
import co.ac.uk.doctor.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

abstract public class AbstractUserDetailsService<T> implements IUserDetailsService<T> {

    @Autowired
    private RoleService roleService;

    @Override
    public Role getRole(String roleName) {
        Long roleId = RoleConstants.getRoles().get(roleName);
        return roleService.findRoleById(roleId);
    }
}
