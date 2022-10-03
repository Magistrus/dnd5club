package club.dnd5.portal.controller;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import club.dnd5.portal.repository.VerificationToken;
import club.dnd5.portal.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/reset/password")
	public String getResetPasswordForm(Model model, @RequestParam String token) {
		VerificationToken vereficationToken = userService.getVerificationToken(token);
		if (vereficationToken == null) {
			model.addAttribute("message", "Неверный токен!");
			return "user/invalid_token";
		}
		if (vereficationToken.getExpiryDate().before(new Date(Calendar.getInstance().getTime().getTime()))){
			model.addAttribute("message", "Время использование токена истекло!");
			return "user/invalid_token";
		}
		return "user/reset_password";
	}
}
