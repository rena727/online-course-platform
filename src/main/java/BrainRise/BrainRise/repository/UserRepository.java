package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.isVerified = false AND r.name = :roleName")
    List<User> findUnverifiedUsersByRole(@Param("roleName") String roleName);
    Optional<User> findByEmail(String email);

}
