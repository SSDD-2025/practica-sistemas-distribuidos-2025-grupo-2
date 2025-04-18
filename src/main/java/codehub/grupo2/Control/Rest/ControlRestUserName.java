package codehub.grupo2.Control.Rest;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestParam;

import codehub.grupo2.Dto.UserNameDTO;
import codehub.grupo2.Service.UserService;



@RestController
@RequestMapping("/api/UserNames")
public class ControlRestUserName {
	
	@Autowired
	private UserService userService;

	@GetMapping("/acc")
	public UserNameDTO me() {
		return userService.getLoggedUserDTO();
	}
    @GetMapping("/")
	public Collection<UserNameDTO> getUserNames() {

		return userService.getAllUsersDTO();
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

