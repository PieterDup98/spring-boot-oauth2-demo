package capgemini.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GoogleContactService {

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public GoogleContactService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @PreAuthorize("hasAuthority('SCOPE_https://www.googleapis.com/auth/contacts.readonly')") //No point calling google if the user doesn't have this SCOPE.
    public String getContacts(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        String url = UriComponentsBuilder
                .fromUriString("https://people.googleapis.com/v1/people/me/connections")
                .queryParam("personFields", "names")
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url + "&access_token=" + accessToken, String.class);
    }
}
