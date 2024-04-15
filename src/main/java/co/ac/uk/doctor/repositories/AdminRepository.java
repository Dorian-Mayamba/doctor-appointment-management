package co.ac.uk.doctor.repositories;

import co.ac.uk.doctor.userdetails.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {
    Optional<Admin> getAdminByAdminEmail(String email);
}
