package club.dnd5.portal.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import club.dnd5.portal.model.user.User;

@Component
public class EmailService {
	@Autowired
	private Environment environment;
	
	@Autowired
	private UserService service;

	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void confirmRegistration(User user) {
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);

		String recipientAddress = user.getEmail();
		String subject = "Потверждение регистрации";
		
		String confirmationUrl = "https://dev.ttg.club/confirm/email?token=" + token;
		String message = "Потвердите ваш email адресс перейдя по ссылке:";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setFrom("support@ttg.club");
		email.setSubject(subject);
		email.setText(String.format("%s %s", message,  confirmationUrl));
		mailSender.send(email);
	}
	
	@Async
	public void changePassword(User user) {
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);

		String recipientAddress = user.getEmail();
		String subject = "Сброс пароля на ttg.club";
		String[] profiles = this.environment.getActiveProfiles();
		String confirmationUrl;
		if("dev".equals(profiles[0])) {
			confirmationUrl = "https://dev.ttg.club/reset/password?token=" + token;	
		} else {
			confirmationUrl = "https://ttg.club/reset/password?token=" + token;
		}
		
		String message = "Для сброса пароля перейдите по ссылке и введите новый пароль:";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setFrom("support@ttg.club");
		email.setSubject(subject);
		email.setText(String.format("%s %s", message,  confirmationUrl));
		mailSender.send(email);
	}
}