package BrainRise.BrainRise.repository;

import BrainRise.BrainRise.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long> {

    // Ən son yaradılan 3 bloqu gətirir
    List<Blog> findFirst3ByOrderByIdDesc();
}
