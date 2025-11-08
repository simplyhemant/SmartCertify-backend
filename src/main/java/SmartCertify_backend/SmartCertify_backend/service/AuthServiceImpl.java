package SmartCertify_backend.SmartCertify_backend.service;

import SmartCertify_backend.SmartCertify_backend.config.JwtProvider;
import SmartCertify_backend.SmartCertify_backend.entity.User;
import SmartCertify_backend.SmartCertify_backend.enums.UserRole;
import SmartCertify_backend.SmartCertify_backend.exception.InvalidCredentialsException;
import SmartCertify_backend.SmartCertify_backend.exception.UserAlreadyExistsException;
import SmartCertify_backend.SmartCertify_backend.exception.UserException;
import SmartCertify_backend.SmartCertify_backend.repo.UserRepository;
import SmartCertify_backend.SmartCertify_backend.request.LoginRequest;
import SmartCertify_backend.SmartCertify_backend.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public String register(RegisterRequest request) throws UserException {

        validateRegistrationRequest(request);

        if (userRepo.existsByEmail(request.getEmail().toLowerCase())) {
            throw new UserException("Email is already registered.");
        }

        if (userRepo.existsByUserName(request.getUserName())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setUserName(request.getUserName());

        if (request.getInstituteName() != null && !request.getInstituteName().trim().isEmpty()) {
            user.setUserRole(UserRole.ROLE_INSTITUTION);
            user.setInstituteName(request.getInstituteName().trim());
        } else {
            user.setUserRole(UserRole.ROLE_STUDENT);
            user.setInstituteName(null);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepo.save(user);

        String token = jwtProvider.generateTokenForOAuth(
                savedUser.getEmail(),
                savedUser.getUserRole().name()
        );

        return token;
    }

    @Override
    public String login(LoginRequest request) throws UserException {

        try {
            // Authenticate the user using Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase(),
                            request.getPassword()
                    )
            );

            // Fetch the user from DB
            User user = userRepo.findByEmail(request.getEmail().toLowerCase())
                    .orElseThrow(() -> new UserException("User not found with email: " + request.getEmail()));

            // Generate JWT token (same as register)
            String token = jwtProvider.generateTokenForOAuth(user.getEmail(), user.getUserRole().name());

            return token;

        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid email or password.");
        } catch (UserException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error during login.", ex);
        }
    }

    private void validateRegistrationRequest(RegisterRequest request) throws UserException {
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new UserException("First name is required");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new UserException("Last name is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new UserException("Email is required");
        }
        if (!isValidEmail(request.getEmail())) {
            throw new UserException("Invalid email format");
        }
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            throw new UserException("Username is required");
        }
        if (request.getUserName().length() < 3) {
            throw new UserException("Username must be at least 3 characters long");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new UserException("Password is required");
        }
        if (request.getPassword().length() < 6) {
            throw new UserException("Password must be at least 6 characters long");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

}
