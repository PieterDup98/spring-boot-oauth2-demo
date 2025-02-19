package capgemini.demo.controller;

import capgemini.demo.service.GoogleContactService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

    private final GoogleContactService googleContactService;

    public ContactsController(GoogleContactService googleContactService) {
        this.googleContactService = googleContactService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_https://www.googleapis.com/auth/contacts.readonly')")
    public String getContacts(OAuth2AuthenticationToken authentication) {
        return googleContactService.getContacts(authentication);
    }
}
