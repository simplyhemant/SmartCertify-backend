package SmartCertify_backend.SmartCertify_backend.request;

import SmartCertify_backend.SmartCertify_backend.enums.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String instituteName;
    private String password;
}
