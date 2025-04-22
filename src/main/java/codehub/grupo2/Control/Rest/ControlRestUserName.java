package codehub.grupo2.Control.Rest;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.UserNameDTO;
import codehub.grupo2.Security.jwt.AuthResponse;
import codehub.grupo2.Security.jwt.JwtTokenProvider;
import codehub.grupo2.Security.jwt.LoginRequest;
import codehub.grupo2.Security.jwt.TokenType;
import codehub.grupo2.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/UserNames")
public class ControlRestUserName {
	
	@Autowired
	private UserService userService;
@Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Boolean isAuthenticated = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (!isAuthenticated) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(AuthResponse.Status.FAILURE, "Invalid username or password", "Authentication failed")
            );
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        response.addCookie(buildTokenCookie(TokenType.ACCESS, accessToken));
        response.addCookie(buildTokenCookie(TokenType.REFRESH, refreshToken));

        AuthResponse authResponse = new AuthResponse(
                AuthResponse.Status.SUCCESS,
                "Authentication successful",
                accessToken,
                refreshToken
        );
        return ResponseEntity.ok().body(authResponse);
    }

    private Cookie buildTokenCookie(TokenType type, String token) {
        Cookie cookie = new Cookie(type.cookieName, token);
        cookie.setMaxAge((int) type.duration.getSeconds());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }


	@GetMapping("/acc")
	public UserNameDTO me() {
		return userService.getLoggedUserDTO();
	}
    @GetMapping("/")
	public Collection<UserNameDTO> getUserNames() {

		return userService.getAllUsersDTO();
	}
    @PutMapping("/{id}")
    public ResponseEntity<?> replaceUserName(@PathVariable Long id, @Valid @RequestBody UserNameDTO updatedUserDTO) {
        if (updatedUserDTO == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la solicitud no puede estar vacío");
        }
        if (updatedUserDTO.id() != null && !id.equals(updatedUserDTO.id())) {
            return ResponseEntity.badRequest().body("El ID en la URL no coincide con el ID en el cuerpo");
        }

        try {
            int result = userService.editUser(
                updatedUserDTO.username(),
                updatedUserDTO.password(),
                updatedUserDTO.email(),
                id
            );

            if (result == 0) {

                UserNameDTO updatedUser = userService.getUserByIdDTO(id);
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.badRequest().body("No se pudo actualizar el usuario: username o email ya en uso, o validación fallida");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al procesar la solicitud: " + e.getMessage());
        }
    }
	@GetMapping("/{id}")
	public UserNameDTO getUser(@PathVariable long id) {
		return userService.getUserByIdDTO(id); 
	}

	@PostMapping("/")
	public ResponseEntity<UserNameDTO> createUserName(@RequestBody UserNameDTO UserNameDTO) {

		userService.registerUsername(UserNameDTO.username(), UserNameDTO.password(), UserNameDTO.email());

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(UserNameDTO.id()).toUri();

		return ResponseEntity.created(location).body(UserNameDTO);
	}

	@DeleteMapping("/{id}")
	public void deleteUserName(@PathVariable long id) {
        userService.deleteUser(userService.getUserByIdDTO(id).username());
	}

}

