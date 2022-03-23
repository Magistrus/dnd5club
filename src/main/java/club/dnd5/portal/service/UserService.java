package club.dnd5.portal.service;

import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import club.dnd5.portal.dto.user.UserDto;
import club.dnd5.portal.model.user.User;

@Service
public interface UserService {
	@Transactional
	void save(User user);

	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);

	DataTablesOutput<UserDto> findAll(@Valid DataTablesInput input);

	DataTablesOutput<UserDto> findByRoles(@Valid DataTablesInput input, Set<String> roles);
}