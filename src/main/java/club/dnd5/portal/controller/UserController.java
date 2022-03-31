package club.dnd5.portal.controller;

import java.util.Calendar;
import java.util.Locale;

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
import club.dnd5.portal.service.SecurityService;
import club.dnd5.portal.service.UserAlreadyExistException;
import club.dnd5.portal.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Value("${git.commit.id}")
	private String version;

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
        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, 
          request.getLocale(), appUrl));
        String message = "Регистрация пошла успешно. На ваш электронный адрес <strong>" + registered.getEmail() + "</strong> отправлено письмо с для потверждения решистрации.";
        model.addAttribute("message", message);
        return "forward:/confirm/bad";
	}

	@GetMapping("/regitrationConfirm")
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
	        String message = "Неверный токен";
	        model.addAttribute("message", message);
	        return "forward:/confirm/bad";
	    }
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String messageValue = "Время потверждения истекло";
	        model.addAttribute("message", messageValue);
	        return "forward:/confirm/bad";
	    } 
	    user.setEnabled(true); 
	    userService.saveUser(user);
		securityService.autologin(user.getName(), user.getPassword());
	    return "redirect:/profile"; 
	}
	
	@GetMapping("/confirm/bad")
	public String getConfirm() {
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