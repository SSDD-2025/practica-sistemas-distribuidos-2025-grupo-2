package codehub.grupo2.Control.Rest;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usernames")
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

	@Operation(summary = "Get the logged-in user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "User retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@GetMapping("/acc")
	public ResponseEntity<UserNameDTO> me() {
		UserNameDTO user = userService.getLoggedUser();
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	@Operation(summary = "Get all users")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
			@ApiResponse(responseCode = "204", description = "No users found")
	})
	@GetMapping("/")
	public ResponseEntity<Collection<UserNameDTO>> getUserNames() {
		Collection<UserNameDTO> users = userService.getAllUsersDTO();
		if (users.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(users);
	}

	@Operation(summary = "Get a user by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "User retrieved successfully"),
			@ApiResponse(responseCode = "403", description = "Forbidden: Can only access your own user data"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<UserNameDTO> getUser(@PathVariable long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserNameDTO user = userService.getUserByIdDTO(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!user.username().equals(username)) {
			return ResponseEntity.status(403).body(null);
		}
		
		return ResponseEntity.ok(user);
	}

	@Operation(summary = "Create a new user")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "User created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid user data")
	})
	@PostMapping("/")
	public ResponseEntity<UserNameDTO> createUserName(@Valid @RequestBody UserNameDTO userNameDTO) {
		if (userNameDTO == null || userNameDTO.username() == null || userNameDTO.password() == null || userNameDTO.email() == null) {
			return ResponseEntity.badRequest().body(null);
		}

		String result = userService.registerUsername(userNameDTO.username(), userNameDTO.password(), userNameDTO.email());
		if (!result.equals(userNameDTO.username())) {
			return ResponseEntity.badRequest().body(null);
		}

		UserNameDTO createdUser = userService.getUser(userNameDTO.username());
		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdUser.id()).toUri();
		return ResponseEntity.created(location).body(createdUser);
	}

	@Operation(summary = "Delete a user by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "User deleted successfully"),
			@ApiResponse(responseCode = "403", description = "Forbidden: Can only delete your own user"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUserName(@PathVariable long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserNameDTO user = userService.getUserByIdDTO(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!user.username().equals(username)) {
			return ResponseEntity.status(403).build();
		}
		
		userService.deleteUserDTO(user.username());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Replace a user by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "User updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid user data"),
			@ApiResponse(responseCode = "403", description = "Forbidden: Can only update your own user"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PutMapping("/{id}")
	public ResponseEntity<UserNameDTO> replaceUser(@PathVariable long id, @Valid @RequestBody UserNameDTO updatedUserDTO) throws SQLException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserNameDTO user = userService.getUserByIdDTO(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!user.username().equals(username)) {
			return ResponseEntity.status(403).body(null);
		}
		
		int result = userService.editUser(updatedUserDTO, id);
		if (result == 1) {
			return ResponseEntity.badRequest().body(null);
		}
		
		UserNameDTO updated = userService.getUserByIdDTO(id);
		return ResponseEntity.ok(updated);
	}

	@Operation(summary = "Get a user's profile picture by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Profile picture retrieved successfully"),
			@ApiResponse(responseCode = "403", description = "Forbidden: Can only access your own profile picture"),
			@ApiResponse(responseCode = "404", description = "User or profile picture not found")
	})
	@GetMapping("/{id}/image")
	public ResponseEntity<Object> getUserProfilePicture(@PathVariable Long id) throws SQLException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserNameDTO user = userService.getUserByIdDTO(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!user.username().equals(username)) {
			return ResponseEntity.status(403).body(null);
		}
		
		Resource profilePicture = userService.getUserProfilePicture(id);
		if (profilePicture == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
				.body(profilePicture);
	}

	@Operation(summary = "Delete a user's profile picture by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Profile picture deleted successfully"),
			@ApiResponse(responseCode = "403", description = "Forbidden: Can only delete your own profile picture"),
			@ApiResponse(responseCode = "404", description = "User or profile picture not found")
	})
	@DeleteMapping("/{id}/image")
	public ResponseEntity<Object> deleteUserImage(@PathVariable long id) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserNameDTO user = userService.getUserByIdDTO(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!user.username().equals(username)) {
			return ResponseEntity.status(403).build();
		}
		
		try {
			userService.deleteUserImage(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Set a user's profile picture by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Profile picture created successfully"),
			@ApiResponse(responseCode = "403", description = "Forbidden: Can only set your own profile picture"),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	@PostMapping("/{id}/image")
	public ResponseEntity<Object> createUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserNameDTO user = userService.getUserByIdDTO(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!user.username().equals(username)) {
			return ResponseEntity.status(403).build();
		}
		
		URI location = fromCurrentRequest().build().toUri();
		userService.createUserImage(id, location, imageFile.getInputStream(), imageFile.getSize());
		return ResponseEntity.created(location).build();
	}

	@Operation(summary = "Update a user's profile picture by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Profile picture updated successfully"),
			@ApiResponse(responseCode = "403", description = "Forbidden: Can only update your own profile picture"),
			@ApiResponse(responseCode = "404", description = "User or profile picture not found")
	})
	@PutMapping("/{id}/image")
	public ResponseEntity<Object> replaceUserImage(@PathVariable long id, @RequestParam MultipartFile imageFile) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		UserNameDTO user = userService.getUserByIdDTO(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		
		if (!user.username().equals(username)) {
			return ResponseEntity.status(403).build();
		}
		
		try {
			userService.replaceUserImage(id, imageFile.getInputStream(), imageFile.getSize());
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}