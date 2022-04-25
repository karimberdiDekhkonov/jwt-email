package uz.pdp.jwtemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.jwtemail.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findAllByRoleName(String roleName);
}
