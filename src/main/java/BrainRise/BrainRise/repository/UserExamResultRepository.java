package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.models.UserExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserExamResultRepository extends JpaRepository<UserExamResult,Long> {
    List<UserExamResult> findByUserId(Long userId);
}
