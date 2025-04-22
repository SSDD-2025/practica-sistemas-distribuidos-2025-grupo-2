package codehub.grupo2.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import codehub.grupo2.Security.jwt.JwtRequestFilter;
import codehub.grupo2.Security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class Security {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
    
        http
            .securityMatcher("/api/**")
            .exceptionHandling(handling -> handling
                .authenticationEntryPoint(unauthorizedHandlerJwt)
                .accessDeniedPage("/accessDenied") 
            );
    
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/api/UserNames/acc").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/UserNames/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/UserNames/").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/UserNames/").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/UserNames/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/Comments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/Comments/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/Comments/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/Posts/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/Posts/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/Posts/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/Topics/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/Topics/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/Topics/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/v3/api-docs*/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .anyRequest().permitAll()
            );
    
        http.formLogin(formLogin -> formLogin.disable());
        http.csrf(csrf -> csrf.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
    
        http
            .exceptionHandling(handling -> handling
                .accessDeniedPage("/accessDenied")
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/init", "/login", "/register", "/topic", "/topic/{id}", 
                    "/post", "/showMoreP/{id}", "/home", "/css/**", "/js/**", "/images/**", 
                    "/favicon.ico", "/error", "/adminLogin", "/guest", "/logout", "/accessDenied").permitAll()
                .requestMatchers("/deleteTopic", "/admin/deleteTopic", "/admin/deletePost", "/admin/deleteComment", "/admin/deleteUser").hasRole("ADMIN")
                .requestMatchers("/acc", "/showPassword", "/hidePassword", "/deleteUserConfirmation", 
                    "/deleteUserDefinitive", "/editProfile", "/updateProfile", "/SonacaWasHere", 
                    "/uploadProfilePicture", "/addTopic", "/addPost", "/createComment", 
                    "/deleteProfilePicture", "/deletePost", "/init/logout", 
                    "/deleteComment", "/search/**").hasRole("USER")
                .requestMatchers("/adminPanel", "/adminPanel/**").hasRole("ADMIN")
                .requestMatchers("/v3/api-docs*/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
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
                .permitAll()
            );
    
        return http.build();
    }
}