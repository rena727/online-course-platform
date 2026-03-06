package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByMentorId(Long mentorId);
}
