package codehub.grupo2.Control.Rest;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import codehub.grupo2.Dto.TopicDTO;
import codehub.grupo2.Service.TopicService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Topics")
public class ControlRestTopic {

	@Autowired
	private TopicService TopicService;

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