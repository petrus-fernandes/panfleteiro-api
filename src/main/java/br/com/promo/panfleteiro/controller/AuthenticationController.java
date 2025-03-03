package br.com.promo.panfleteiro.controller;

import br.com.promo.panfleteiro.entity.User;
import br.com.promo.panfleteiro.infra.security.TokenService;
import br.com.promo.panfleteiro.repository.UserRepository;
import br.com.promo.panfleteiro.response.AuthenticationRequest;
import br.com.promo.panfleteiro.response.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class AuthenticationController {

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
    public ResponseEntity login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authenticationRequest.login(), authenticationRequest.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody @Valid AuthenticationRequest authenticationRequest) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest registerRequest) {
        if (userRepository.findByLogin(registerRequest.login()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.password());
        User user = new User(registerRequest.login(), encryptedPassword, registerRequest.role());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
