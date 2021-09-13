package club.dnd5.portal.service;

import java.util.Optional;

import club.dnd5.portal.model.user.User;


public interface UserService {
	void save(User user);

	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
}
