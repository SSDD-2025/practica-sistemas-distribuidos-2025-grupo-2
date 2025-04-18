package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.PostDTO;
import codehub.grupo2.Service.PostService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Posts")
public class ControlRestPost {

    @Autowired
    private PostService PostService;

    @GetMapping("/")
    public Collection<PostDTO> getPosts() {

        return PostService.getAllPostDTO();
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable long id) {

        return PostService.getPostByIdDTO(id);
    }

    @PostMapping("/")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO PostDTO) {

        PostService.registerPostDTO(PostDTO.user(), PostDTO.title(), PostDTO.text(), PostDTO.topic());
        PostDTO = PostService.getPostByIdDTO(PostDTO.id());

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(PostDTO.id()).toUri();

        return ResponseEntity.created(location).body(PostDTO);
    }

    @DeleteMapping("/{id}")
    public PostDTO deletePost(@PathVariable long id) {
        PostDTO dto = PostService.getPostByIdDTO(id);
        return PostService.deletePostDTO(dto.title());
    }
   
}
