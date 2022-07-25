package club.dnd5.portal.service;

import java.util.Optional;
import java.util.Set;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import club.dnd5.portal.dto.user.OldUserDto;
import club.dnd5.portal.model.user.Role;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.VerificationToken;
import club.dnd5.portal.repository.user.UserRepository;
import club.dnd5.portal.repository.user.VerificationTokenRepository;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Override
	public Optional<User> findByUsername(String username) {
		return userRepository.findByName(username);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public DataTablesOutput<OldUserDto> findAll(@Valid DataTablesInput input) {
		return userRepository.findAll(input, OldUserDto::new);
	}

	@Override
	public DataTablesOutput<OldUserDto> findByRoles(@Valid DataTablesInput input, Set<String> roles) {
		Specification<User> specification = (root, query, cb) -> {
			Join<User, Role> join = root.join("roles", JoinType.INNER);
			return join.get("name").in(roles);
		};
		return userRepository.findAll(input, specification, specification, OldUserDto::new);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}


	@Override
	public VerificationToken getVerificationToken(String token) {
		return tokenRepository.findByToken(token);
	}
}