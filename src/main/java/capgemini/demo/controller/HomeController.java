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
        final boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated && authentication instanceof OAuth2AuthenticationToken oauthToken) {
            final OAuth2User principal = (OAuth2User) authentication.getPrincipal();

            final String provider = oauthToken.getAuthorizedClientRegistrationId();

            String greetingMessage = "Welcome " + principal.getAttribute("name") + "!";

            if ("google".equals(provider)) {
                greetingMessage += " Thanks for logging in with Google!";
            } else if ("github".equals(provider)) {
                greetingMessage += " Thanks for logging in with GitHub!";
            }

            model.addAttribute("greeting", greetingMessage);
            model.addAttribute("provider", provider);
        }

        return "home"; // Return home.html template
    }
}

