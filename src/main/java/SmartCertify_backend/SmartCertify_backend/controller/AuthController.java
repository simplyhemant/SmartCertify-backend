package SmartCertify_backend.SmartCertify_backend.controller;

import SmartCertify_backend.SmartCertify_backend.entity.User;
import SmartCertify_backend.SmartCertify_backend.exception.UserException;
import SmartCertify_backend.SmartCertify_backend.repo.UserRepository;
import SmartCertify_backend.SmartCertify_backend.request.LoginRequest;
import SmartCertify_backend.SmartCertify_backend.request.RegisterRequest;
import SmartCertify_backend.SmartCertify_backend.response.AuthResponse;
import SmartCertify_backend.SmartCertify_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) throws UserException {
        String token = authService.register(request);

        String userRole = (request.getInstituteName() != null && !request.getInstituteName().isBlank())
                ? "ROLE_INSTITUTION"
                : "ROLE_STUDENT";

        AuthResponse response = new AuthResponse(
                token,
                true,
                "Registration successful!",
                userRole
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) throws UserException {

        String token = authService.login(request);

        User user = userRepo.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new UserException("User not found with email: " + request.getEmail()));

        String userRole = user.getUserRole().name();

        AuthResponse response = new AuthResponse(
                token,
                true,
                "Login successful!",
                userRole
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
