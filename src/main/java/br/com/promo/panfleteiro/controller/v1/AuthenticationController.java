package br.com.promo.panfleteiro.controller.v1;

import br.com.promo.panfleteiro.entity.User;
import br.com.promo.panfleteiro.entity.UserRole;
import br.com.promo.panfleteiro.infra.security.TokenService;
import br.com.promo.panfleteiro.repository.UserRepository;
import br.com.promo.panfleteiro.request.LoginRequest;
import br.com.promo.panfleteiro.response.UserRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "v1/auth", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest) {
        logger.info("Login: {}", loginRequest.login());
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registrar")
    public ResponseEntity register(@RequestBody @Valid UserRequest userRequest) {
        if (userRepository.findByLogin(userRequest.login()) != null) return ResponseEntity.badRequest().body("Login já cadastrado");
        logger.info("Registering User: {}", userRequest.login());
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRequest.password());
        User user = new User(userRequest.login(), encryptedPassword, UserRole.valueOf(userRequest.role()));
        userRepository.save(user);
        logger.info("Registered User: {}", user.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/usuarios")
    public ResponseEntity updateUser(@RequestBody @Valid UserRequest userRequest) {
        logger.info("Updating User: {}", userRequest.login());
        User user = userRepository.findUserByLogin(userRequest.login());
        if (user == null) return ResponseEntity.badRequest().body("Usuário não encontrado");
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRequest.password());
        user.setPassword(encryptedPassword);
        user.setRole(UserRole.valueOf(userRequest.role()));
        userRepository.save(user);
        logger.info("Updated User with ID: {}, Role: {}", user.getId(), user.getRole());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuarios")
    public ResponseEntity findAllUsers() {
        logger.info("Listing all Users");
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        logger.info("Deleting User with ID: {}", id);
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
