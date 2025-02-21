package codehub.grupo2.DB;

import org.springframework.data.jpa.repository.JpaRepository;

import codehub.grupo2.DB.Entity.*;

public interface UserRepository extends JpaRepository<UserName, Long> {
    UserName findByUsername(String username);
}
