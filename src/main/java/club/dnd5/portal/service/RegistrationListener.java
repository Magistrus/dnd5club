package club.dnd5.portal.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import club.dnd5.portal.model.user.User;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private UserService service;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);

		String recipientAddress = user.getEmail();
		String subject = "Потверждение регистрации";
		
		String confirmationUrl = "https://dev.dnd5.club/registration/confirm?token=" + token;
		String message = "Потвердите ваш email адресс перейдя по ссылке:";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setFrom("dnd5club@gmail.com");
		email.setSubject(subject);
		email.setText(String.format("%s %s", message,  confirmationUrl));
		mailSender.send(email);
	}
}