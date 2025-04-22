package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.TopicDTO;
import codehub.grupo2.Service.APIService.APITopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Topics")
public class ControlRestTopic {

	@Autowired
	private APITopicService TopicService;

	@Operation(summary = "Get all topics")
	@ApiResponse(responseCode = "200", description = "Topics retrieved successfully")
	@GetMapping("/")
	public Collection<TopicDTO> getTopics() {

		return TopicService.getAllTopicsDTO();
	}

	@Operation(summary = "Get a topic by ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Topic retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "Topic not found")
	})
	@GetMapping("/{id}")
	public Optional<TopicDTO> getTopic(@PathVariable long id) {

		return TopicService.getTopicByIdDTO(id);
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
			@ApiResponse(responseCode = "404", description = "Topic not found")
	})
	@DeleteMapping("/{id}")
	public TopicDTO deleteTopic(@PathVariable long id) {
        return TopicService.deleteTopicDTO(id);
	}

}

