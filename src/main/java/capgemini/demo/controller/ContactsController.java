package capgemini.demo.controller;

import capgemini.demo.service.GoogleContactService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ContactsController {

    private final GoogleContactService googleContactService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ContactsController(final GoogleContactService googleContactService) {
        this.googleContactService = googleContactService;
    }

    @GetMapping("/contacts")
    public String getContacts(final OAuth2AuthenticationToken authentication, final Model model) {
        final String jsonResponse = googleContactService.getContacts(authentication);
        final List<String> contactNames = new ArrayList<>();

        try {
            final JsonNode root = objectMapper.readTree(jsonResponse);
            final JsonNode connections = root.path("connections");

            if (connections.isArray()) {
                for (JsonNode contact : connections) {
                    String displayName = "Unknown";
                    if (contact.has("names") && !contact.get("names").isEmpty()) {
                        displayName = contact.get("names").get(0).path("displayName").asText("Unknown");
                    }
                    contactNames.add(displayName);
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load contacts.");
        }

        model.addAttribute("contactNames", contactNames);
        return "contacts";
    }
}
