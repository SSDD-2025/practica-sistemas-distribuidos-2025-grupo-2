package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.TopicDTO;
import codehub.grupo2.Service.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Topics")
public class ControlRestTopic {

	@Autowired
	private TopicService TopicService;

	@Operation(summary = "Get all topics")
	@ApiResponse(responseCode = "200", description = "Topics retrieved successfully")
	@ApiResponse(responseCode = "204", description = "No topics found")
	@GetMapping("/")
	public ResponseEntity<Collection<TopicDTO>> getTopics() {
		Collection<TopicDTO> topics = TopicService.getAllTopicsDTO();
		if (topics.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(topics);
	}

	@Operation(summary = "Get a topic by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Topic retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "Topic not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<TopicDTO> getTopic(@PathVariable long id) {
		Optional<TopicDTO> dto = TopicService.getTopicByIdDTO(id);
		if (dto.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto.get());
	}

	@Operation(summary = "Create a new topic")
	@ApiResponse(responseCode = "201", description = "Topic created successfully")
	@PostMapping("/")
	public ResponseEntity<TopicDTO> createTopic(@RequestBody TopicDTO topicDTO) {

		TopicDTO createdTopic = TopicService.newTopicDTO(topicDTO.topicName());

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdTopic.id()).toUri();

		return ResponseEntity.created(location).body(createdTopic);
	}

	@Operation(summary = "Delete a topic by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Topic deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Topic not found"),
			@ApiResponse(responseCode = "403", description = "Access denied")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<TopicDTO> deleteTopic(@PathVariable long id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
		if (!username.equals("admin")) {
			return ResponseEntity.status(403).build(); 
		}
		Optional<TopicDTO> dto = TopicService.getTopicByIdDTO(id);
		if (dto.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		TopicService.deleteTopic(dto.get().id());
		return ResponseEntity.ok(dto.get());
	}

}

