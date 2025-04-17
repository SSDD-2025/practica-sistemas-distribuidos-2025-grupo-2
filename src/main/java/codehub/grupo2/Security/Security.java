package codehub.grupo2.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import codehub.grupo2.DB.Entity.UserName;

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
                    "/favicon.ico", "/error","/adminLogin").permitAll()
                .requestMatchers("/deleteTopic", "/deletePost", "/deleteComment").hasRole("ADMIN")
                .requestMatchers("/acc", "/showPassword", "/hidePassword", "/deleteUserConfirmation", 
                    "/deleteUserDefinitive", "/editProfile", "/updateProfile", "/uploadProfilePicture", 
                    "/addTopic", "/addPost","/createComment").hasRole("USER")
                .requestMatchers("/adminPanel", "/adminPanel/**").hasRole("ADMIN")
                .requestMatchers("/**").denyAll()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/home")
                .loginProcessingUrl("/loginPage")
                .successHandler((request, response, authentication) -> {
                    boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
                    if (isAdmin) {
                        response.sendRedirect("/adminPanel");
                    } else {
                        response.sendRedirect("/init");
                    }
                })
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