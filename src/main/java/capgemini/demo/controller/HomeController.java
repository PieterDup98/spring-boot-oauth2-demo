package capgemini.demo.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String homePage(Authentication authentication, Model model) {
        final boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated && authentication instanceof OAuth2AuthenticationToken oauthToken) {
            final OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            final String name = Optional.ofNullable((String) principal.getAttribute("name")).orElse("User");
            boolean hasDemoUserRole = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch("DEMO_USER"::equals);

            String greetingMessage = switch (oauthToken.getAuthorizedClientRegistrationId()) {
                case "google" -> "Welcome " + name + "! Thanks for logging in with Google!";
                case "github" -> "Welcome " + name + "! Thanks for logging in with GitHub!";
                default -> "Welcome " + name + "!";
            };

            model.addAttribute("greeting", greetingMessage);
            model.addAttribute("shouldShowContactsDemo", hasDemoUserRole);
        }

        return "home"; // Return home.html template
    }
}

