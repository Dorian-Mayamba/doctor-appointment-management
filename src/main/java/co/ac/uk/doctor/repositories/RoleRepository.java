package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findRoleByType(String type);

    Role findRoleById(Long id);
}
