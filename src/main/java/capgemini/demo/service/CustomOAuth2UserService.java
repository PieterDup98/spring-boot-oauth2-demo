package capgemini.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Value("${demo.user.nameSurname}")
    private String demoUserNameSurname;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        final OAuth2User oauth2User = super.loadUser(userRequest);

        String name = oauth2User.getAttribute("name");

        //Create set with existing priorities, so we can add to it.
        Set<GrantedAuthority> authorities = new HashSet<>(oauth2User.getAuthorities());

        if (name != null && name.equalsIgnoreCase(demoUserNameSurname)) {
            authorities.add(new SimpleGrantedAuthority("DEMO_USER"));
        }

        return new CustomOAuth2User(oauth2User, authorities);
    }
}

class CustomOAuth2User implements OAuth2User {

    private final OAuth2User delegate;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(OAuth2User delegate, Collection<? extends GrantedAuthority> authorities) {
        this.delegate = delegate;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
