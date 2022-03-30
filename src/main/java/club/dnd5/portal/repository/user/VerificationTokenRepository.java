package club.dnd5.portal.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
}