package co.ac.uk.doctor.repositories.jpa;

import co.ac.uk.doctor.entities.jpa.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {
    Optional<Admin> getAdminByAdminEmail(String email);
}
