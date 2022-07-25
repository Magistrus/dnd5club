package club.dnd5.portal.controller.rest;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import club.dnd5.portal.dto.user.OldUserDto;
import club.dnd5.portal.service.UserService;

@RestController
public class AdminRestController {
	@Autowired
	private UserService service;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/data/users")
	public DataTablesOutput<OldUserDto> getData(@Valid DataTablesInput input) {
		if (!input.getColumns().get(2).getSearch().getValue().isEmpty()) {
			 Set<String> roles= Arrays.stream(input.getColumns().get(2).getSearch().getValue().split("\\|"))
				.filter(s -> !s.isEmpty()).collect(Collectors.toSet());
			return service.findByRoles(input, roles);
		}
		return service.findAll(input);
	}
}