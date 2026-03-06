package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.dto.CourseDto;
import BrainRise.BrainRise.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository <Course,Long> {
    List<Course> findByIsApprovedTrue();
    List<Course> findAllByApprovedTrue();
    Optional<Course> findByName(String name);



}
