package club.dnd5.portal.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import club.dnd5.portal.model.user.User;

@Repository
public interface UserRepository extends DataTablesRepository<User, Long> {
	Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);

	@Query("SELECT count(u) FROM User u LEFT JOIN u.roles r WHERE r.name = :role")
	long countByRoles(@Param("role") String role);
}