package club.dnd5.portal.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.user.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

	Optional<User> findByEmail(String email);
}
