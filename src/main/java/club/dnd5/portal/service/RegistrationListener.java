package club.dnd5.portal.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
		try {
			this.confirmRegistration(event);
		} catch (UnknownHostException e) {
		}
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) throws UnknownHostException {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);

		String recipientAddress = user.getEmail();
		String subject = "Потверждение регистрации";
		
		String confirmationUrl = "https://" + InetAddress.getLocalHost().getHostAddress() + "/registration/confirm?token=" + token;
		String message = "Потвердите ваш email адресс перейдя по ссылке:";

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setFrom("dnd5club@gmail.com");
		email.setSubject(subject);
		email.setText(String.format("%s %s", message,  confirmationUrl));
		mailSender.send(email);
	}
}