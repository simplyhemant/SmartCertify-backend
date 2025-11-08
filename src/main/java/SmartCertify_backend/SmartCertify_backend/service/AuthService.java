package SmartCertify_backend.SmartCertify_backend.service;

import SmartCertify_backend.SmartCertify_backend.entity.User;
import SmartCertify_backend.SmartCertify_backend.exception.UserException;
import SmartCertify_backend.SmartCertify_backend.request.LoginRequest;
import SmartCertify_backend.SmartCertify_backend.request.RegisterRequest;
import SmartCertify_backend.SmartCertify_backend.response.AuthResponse;

public interface AuthService {

    String register(RegisterRequest req) throws UserException;

    String login(LoginRequest request) throws UserException;

}
