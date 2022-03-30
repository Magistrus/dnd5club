package club.dnd5.portal.service;

import org.springframework.stereotype.Service;

public interface SecurityService {
	String findLoggedInUsername();
	void autologin(String name, String password);
}