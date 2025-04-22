package codehub.grupo2.Control.Rest;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/api/UserNames")
public class ControlRestUserName {
	
	@Autowired
	private UserService userService;
@Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "Login a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid username or password")
    })
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


    @Operation(summary = "Get a user logged")
    @ApiResponse(responseCode = "200", description = "User found")
	@GetMapping("/acc")
	public UserNameDTO me() {
		return userService.getLoggedUser();
	}

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/")
	public Collection<UserNameDTO> getUserNames() {

		return userService.getAllUsersDTO();
	}

    @Operation(summary = "Get a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
	@GetMapping("/{id}")
	public UserNameDTO getUser(@PathVariable long id) {
		return userService.getUserByIdDTO(id); 
	}

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
	@PostMapping("/")
	public ResponseEntity<UserNameDTO> createUserName(@RequestBody UserNameDTO UserNameDTO) {

		userService.registerUsername(UserNameDTO.username(), UserNameDTO.password(), UserNameDTO.email());

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(UserNameDTO.id()).toUri();

		return ResponseEntity.created(location).body(UserNameDTO);
	}

    @Operation(summary = "Delete a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
	@DeleteMapping("/{id}")
	public void deleteUserName(@PathVariable long id) {
        userService.deleteUser(userService.getUserByIdDTO(id).username());
	}
    @PutMapping("/{id}")
	public UserNameDTO replaceUser(@PathVariable long id, @RequestBody UserNameDTO updatedUserDTO) throws SQLException {

		 userService.editUser(updatedUserDTO, id);
         return updatedUserDTO;
	}

}

