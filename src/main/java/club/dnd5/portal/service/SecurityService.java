package club.dnd5.portal.service;

public interface SecurityService {
	String findLoggedInUsername();
	void autologin(String name, String password);
}