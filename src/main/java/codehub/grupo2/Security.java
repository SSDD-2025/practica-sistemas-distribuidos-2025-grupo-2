package codehub.grupo2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public InMemoryUserDetailsManager UserDetailsManager() {
        UserDetails user = org.springframework.security.core.userdetails.User.withUsername("user")
                .password(passwordEncoder().encode("password")).roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }*/

    /* 
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(UserDetailsManager());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permitir acceso a todas las rutas sin autenticación
            )
            .csrf(csrf -> csrf.disable()); // Deshabilitar CSRF si es necesario para pruebas

        return http.build();
    }

}
