package capgemini.demo.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String homePage(Authentication authentication, Model model) {
        final OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        if (principal != null) {
            final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            // Get the registration ID (google, github, etc.)
            String provider = oauthToken.getAuthorizedClientRegistrationId();

            // Customize the greeting based on the authentication provider
            String greetingMessage = "Welcome " + principal.getAttribute("name") + "!";

            if ("google".equals(provider)) {
                greetingMessage += " Thanks for logging in with Google!";
            } else if ("github".equals(provider)) {
                greetingMessage += " Thanks for logging in with GitHub!";
            }

            model.addAttribute("greeting", greetingMessage);
        }

        return "home"; // Return the home.html template
    }
}
