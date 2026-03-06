package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.models.Mentor;
import BrainRise.BrainRise.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor,Long> {
    Optional<Mentor> findByEmail(String email);
    Optional<Mentor> findByUser(User user);



}