package club.dnd5.portal.service;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import club.dnd5.portal.model.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private static final long serialVersionUID = 2982502699174728935L;

	private String appUrl;
	private Locale locale;
	private User user;

	public OnRegistrationCompleteEvent(User user, Locale locale, String appUrl) {
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}
}