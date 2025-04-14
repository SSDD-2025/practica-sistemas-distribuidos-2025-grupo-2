package codehub.grupo2.Security;

import java.io.IOException;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import codehub.grupo2.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
        http.authenticationProvider(authenticationProvider(userService));
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/topic", "/topic/{id}", "/post", "/showMoreP/{id}", "/login", "/register", "/init", "/").permitAll()
                .requestMatchers("/home", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers("/deleteTopic", "/deletePost", "/deleteComment").hasRole("ADMIN")
                .requestMatchers("/acc", "/showPassword", "/hidePassword", 
                                 "/deleteUserConfirmation", "/deleteUserDefinitive",
                                 "/editProfile", "/updateProfile", "/uploadProfilePicture").hasRole("USER")
                .requestMatchers("/addTopic", "/addPost", "/createComment").authenticated()
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
                .permitAll()
            );
        return http.build();
    }


}
