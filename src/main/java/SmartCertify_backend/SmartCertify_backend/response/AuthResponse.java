package SmartCertify_backend.SmartCertify_backend.response;

import SmartCertify_backend.SmartCertify_backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	
	private String jwt;
	
	private boolean status;
	
	private String message;

	private String userRole;
}
