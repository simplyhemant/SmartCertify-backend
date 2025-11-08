package SmartCertify_backend.SmartCertify_backend.service;

import SmartCertify_backend.SmartCertify_backend.request.LoginRequest;
import SmartCertify_backend.SmartCertify_backend.request.RegisterRequest;
import SmartCertify_backend.SmartCertify_backend.response.AuthResponse;

public interface AuthService {

    AuthResponse registerUser(RegisterRequest request);

    AuthResponse loginUser(LoginRequest request);

}
