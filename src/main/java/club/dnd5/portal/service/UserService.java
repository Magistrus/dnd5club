package club.dnd5.portal.service;

import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import club.dnd5.portal.dto.user.OldUserDto;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.VerificationToken;

@Service
public interface UserService {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);

	DataTablesOutput<OldUserDto> findAll(@Valid DataTablesInput input);

	DataTablesOutput<OldUserDto> findByRoles(@Valid DataTablesInput input, Set<String> roles);
	
	@Transactional
	void createVerificationToken(User user, String token);
	
	VerificationToken getVerificationToken(String token);
	
	@Transactional
	User saveRegisteredUser(User user) throws UserAlreadyExistException;
	@Transactional
	void saveUser(User user);
	@Transactional
	void remove(User user);
}