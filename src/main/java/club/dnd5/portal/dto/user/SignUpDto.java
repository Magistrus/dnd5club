package club.dnd5.portal.dto.user;

import lombok.Data;

@Data
public class SignUpDto {
    private String name;
    private String email;
    private String password;
}
