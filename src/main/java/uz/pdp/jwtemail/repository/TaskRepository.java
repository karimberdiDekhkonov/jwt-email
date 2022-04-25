package uz.pdp.jwtemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.jwtemail.entity.Task;

import java.util.UUID;
@RepositoryRestResource(path = "product")
public interface TaskRepository extends JpaRepository<Task, UUID> {
}
