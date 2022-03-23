package club.dnd5.portal.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import club.dnd5.portal.dto.UserRegForm;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.service.SecurityService;
import club.dnd5.portal.service.UserService;


@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("user", new UserRegForm());
		return "user/registration";
	}

	@PostMapping("/registration")
	public String registration(@Valid @ModelAttribute("user") UserRegForm userForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "user/registration";
		}
		if(userService.findByUsername(userForm.getName()).isPresent())
		{
			ObjectError error = new ObjectError("userExist", "Пользователь с таким именем уже зарегистрирован");
			bindingResult.addError(error);
			return "user/registration";
		}
		if(userService.findByEmail(userForm.getEmail()).isPresent())
		{
			ObjectError error = new ObjectError("userExist", "Пользователь с таким электронным адресом уже зарегистрирован");
			bindingResult.addError(error);
			return "user/registration";
		}
		
		userService.save(new User(userForm));
		securityService.autologin(userForm.getName(), userForm.getPasswordConfirm());
		return "redirect:/profile";
	}

	@GetMapping("/login")
	public String login(Model model, String error, String logout) {
		if (error != null) {
			model.addAttribute("error", "Имя пользователя или пароль неверны.");
		}
		if (logout != null) {
			model.addAttribute("message", "Вы успешно вышли из системы.");
		}
		return "user/login";
	}
	
	@GetMapping("/loginform")
	public String getLoginForm(Model model, String error, String logout) {
		return "user/login :: form";
	}
}