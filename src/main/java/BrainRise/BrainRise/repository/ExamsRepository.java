package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.models.Course;
import BrainRise.BrainRise.models.Exams;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamsRepository extends JpaRepository<Exams,Long> {
}
