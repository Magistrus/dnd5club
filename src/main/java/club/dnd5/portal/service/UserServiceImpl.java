package club.dnd5.portal.service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import club.dnd5.portal.dto.user.UserDto;
import club.dnd5.portal.model.user.Role;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.VerificationToken;
import club.dnd5.portal.repository.user.RoleRepository;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.repository.user.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByName(username);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public DataTablesOutput<UserDto> findAll(@Valid DataTablesInput input) {
		return userRepository.findAll(input, UserDto::new);
	}

	@Override
	public DataTablesOutput<UserDto> findByRoles(@Valid DataTablesInput input, Set<String> roles) {
		Specification<User> specification = (root, query, cb) -> {
			Join<User, Role> join = root.join("roles", JoinType.INNER);
			return join.get("name").in(roles);
		};
		return userRepository.findAll(input, specification, specification, UserDto::new);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

	@Override
	public void saveRegisteredUser(User user) throws UserAlreadyExistException {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new UserAlreadyExistException("Пользователь с таким электронным адресом уже зарегистрирован");
		}
		if (userRepository.findByName(user.getName()).isPresent()) {
			throw new UserAlreadyExistException("Пользователь с таким именем уже зарегистрирован");
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(roleRepository.findAllById(Collections.singleton(1L)));
		userRepository.save(user);
	}

	@Override
	public VerificationToken getVerificationToken(String token) {
		return tokenRepository.findByToken(token);
	}

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
	}
}