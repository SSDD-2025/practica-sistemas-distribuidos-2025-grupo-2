package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.TopicDTO;
import codehub.grupo2.Service.APIService.APITopicService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Topics")
public class ControlRestTopic {

	@Autowired
	private APITopicService TopicService;

	@GetMapping("/")
	public Collection<TopicDTO> getTopics() {

		return TopicService.getAllTopicsDTO();
	}

	@GetMapping("/{id}")
	public Optional<TopicDTO> getTopic(@PathVariable long id) {

		return TopicService.getTopicByIdDTO(id);
	}

	@PostMapping("/")
	public ResponseEntity<TopicDTO> createTopic(@RequestBody TopicDTO topicDTO) {

		TopicDTO createdTopic = TopicService.newTopicDTO(topicDTO.topicName());

		URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdTopic.id()).toUri();

		return ResponseEntity.created(location).body(createdTopic);
	}

	@DeleteMapping("/{id}")
	public TopicDTO deleteTopic(@PathVariable long id) {
        return TopicService.deleteTopicDTO(id);
	}

}