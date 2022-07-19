package club.dnd5.portal.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import club.dnd5.portal.model.user.Role;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.user.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {
	    boolean accountNonExpired = true;
	    boolean credentialsNonExpired = true;
	    boolean accountNonLocked = true;
		Optional<User> user = usersRepository.findByEmailOrUsername(userNameOrEmail, userNameOrEmail);
		if (!user.isPresent()) {
            throw new UsernameNotFoundException(
              "Не найден пользователь: " + userNameOrEmail);
        }
		User foundUser = user.get();
        return new org.springframework.security.core.userdetails.User(
        		foundUser.getEmail(), 
        		foundUser.getPassword(), 
        		foundUser.isEnabled(), 
                accountNonExpired, 
                credentialsNonExpired, 
                accountNonLocked, 
                getAuthorities(foundUser));
	}

	private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
		String[] userRoles = user.getRoles().stream()
				.map(Role::getName)
				.map(s -> "ROLE_" + s)
				.toArray(String[]::new);
		return AuthorityUtils.createAuthorityList(userRoles);
	}
}