package club.dnd5.portal.controller.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.user.UserDto;
import club.dnd5.portal.service.UserService;

@RestController
public class AdminRestController {
	@Autowired
	private UserService service;

	@GetMapping("/admin/data/users")
	public DataTablesOutput<UserDto> getData(@Valid DataTablesInput input) {
		return service.findAll(input);
	}
}