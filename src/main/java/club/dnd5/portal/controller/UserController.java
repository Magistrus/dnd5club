package club.dnd5.portal.controller;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import club.dnd5.portal.dto.UserRegForm;
import club.dnd5.portal.model.user.User;
import club.dnd5.portal.repository.VerificationToken;
import club.dnd5.portal.repository.user.RoleRepository;
import club.dnd5.portal.service.OnRegistrationCompleteEvent;
import club.dnd5.portal.service.UserAlreadyExistException;
import club.dnd5.portal.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Value("${git.commit.id}")
	private String version;

	@Value("${spring.mail.password}")
	private String password;
	
	@GetMapping("/admin/users")
	public String getUsers(Model model) {
		model.addAttribute("roles", roleRepository.findAll());
		model.addAttribute("version", version);
		return "/user/admin/users";
	}

	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("user", new UserRegForm());
		model.addAttribute("version", version);
		return "user/registration";
	}

	@PostMapping("/registration")
	public String registration(Model model, @Valid @ModelAttribute("user") UserRegForm userForm, BindingResult bindingResult, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return "user/registration";
		}
		if (userService.findByUsername(userForm.getName()).isPresent()) {
			ObjectError error = new ObjectError("userExist", "Пользователь с таким именем уже зарегистрирован");
			bindingResult.addError(error);
			return "user/registration";
		}
		User registered = new User(userForm);
		try {
			userService.saveRegisteredUser(registered);
		} catch (UserAlreadyExistException uaeEx) {
			ObjectError error = new ObjectError("userExist", uaeEx.getMessage());
			bindingResult.addError(error);
	        return "user/registration";
	    } 
		try {
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), request.getContextPath()));
		} catch (Exception exception) {
	        model.addAttribute("message", password);
			return "user/confirm";
		}
		return "redirect:/confirm";
	}

	@GetMapping("/registration/confirm")
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
	        String message = "Неверный токен!";
	        model.addAttribute("message", message);
	        return "forward:/confirm/bad";
	    }
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	final String messageValue = "Время потверждения истекло!";
	        model.addAttribute("message", messageValue);
	        return "forward:/confirm/bad";
	    } 
	    user.setEnabled(true); 
	    userService.saveUser(user);
	    return "redirect:/confirm/done"; 
	}

	@GetMapping("/confirm")
	public String getConfirm(Model model) {
        final String message = "Регистрация пошла успешно. На ваш электронный адрес отправлено письмо для потверждения регистрации.";
        model.addAttribute("message", message);
		return "user/confirm";
	}

	@GetMapping("/confirm/done")
	public String getConfirmDone(Model model) {
        final String message = "Ваш электронный адрес потвержден. Вы можете перейти к авторизации.";
        model.addAttribute("message", message);
		return "user/confirm";
	}
	
	@GetMapping("/confirm/bad")
	public String getConfirmBad() {
		return "user/confirm";
	}
	
	@GetMapping("/login")
	public String login(Model model, String error, String logout) {
		if (error != null) {
			model.addAttribute("error", "Электронный адресс или пароль неверны.");
		}
		if (logout != null) {
			model.addAttribute("message", "Вы успешно вышли из системы.");
		}
		model.addAttribute("version", version);
		return "user/login";
	}

	@GetMapping("/loginform")
	public String getLoginForm(Model model, String error, String logout) {
		return "user/login :: form";
	}
}