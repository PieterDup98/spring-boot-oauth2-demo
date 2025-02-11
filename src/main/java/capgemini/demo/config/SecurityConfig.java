package capgemini.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class SecurityConfig {

    @Value("${admin.users}")
    private List<String> adminUsers;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin").hasAuthority("SCOPE_ADMIN") // Restrict admin
                        .requestMatchers("/user").authenticated()  // Allow any authenticated user
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo ->
                        userInfo.userService(customOAuth2UserService())
                ))
                .logout(logout -> logout.logoutSuccessUrl("/"));

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            Set<SimpleGrantedAuthority> authorities = (Set<SimpleGrantedAuthority>) oAuth2User.getAuthorities();
            Set<SimpleGrantedAuthority> authoritiesAltered = new HashSet<>(authorities);

            String username = oAuth2User.getAttribute("login"); // GitHub username

            if (adminUsers.contains(username)) {
                authoritiesAltered.add(new SimpleGrantedAuthority("SCOPE_ADMIN"));
            }

            return new CustomOAuth2User(oAuth2User, authoritiesAltered);
        };
    }

    public static class CustomOAuth2User implements OAuth2User {
        private final OAuth2User delegate;
        private final Set<SimpleGrantedAuthority> authorities;

        public CustomOAuth2User(OAuth2User delegate, Set<SimpleGrantedAuthority> authorities) {
            this.delegate = delegate;
            this.authorities = authorities;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return delegate.getAttributes();
        }

        @Override
        public Set<SimpleGrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getName() {
            return delegate.getName();
        }
    }
}