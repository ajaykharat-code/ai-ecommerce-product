package com.scaler.userauth.controller;

import com.scaler.userauth.entity.User;
import com.scaler.userauth.repository.UserRepository;
import com.scaler.userauth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/success")
    public AuthResponse oauth2Success(OAuth2AuthenticationToken token) {
        OAuth2User oAuth2User = token.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        
        // Find or create user
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setProvider("GOOGLE");
            user.setRole("ROLE_USER");
            user = userRepository.save(user);
        }
        
        String jwt = jwtService.generateToken(user.getId());
        return new AuthResponse(jwt);
    }
}
