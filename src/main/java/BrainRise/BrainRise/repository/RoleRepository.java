package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,RoleRepository> {
    Optional<Role> findByName(String name);
}
