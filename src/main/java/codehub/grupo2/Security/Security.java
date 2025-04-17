package codehub.grupo2.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class Security {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/logout").denyAll()
                .requestMatchers("/", "/init", "/login", "/register", "/topic", "/topic/{id}", 
                    "/post", "/showMoreP/{id}", "/home", "/css/**", "/js/**", "/images/**", 
                    "/favicon.ico", "/error").permitAll()
                .requestMatchers("/deleteTopic", "/deletePost", "/deleteComment").hasRole("ADMIN")
                .requestMatchers("/acc", "/showPassword", "/hidePassword", "/deleteUserConfirmation", 
                    "/deleteUserDefinitive", "/editProfile", "/updateProfile", "/uploadProfilePicture", 
                    "/addTopic", "/addPost").hasRole("USER")
                .requestMatchers("/**").denyAll()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/home")
                .loginProcessingUrl("/loginPage")
                .defaultSuccessUrl("/init", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
    
        return http.build();
    }
}