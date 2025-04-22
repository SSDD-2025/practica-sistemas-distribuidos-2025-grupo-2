package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.PostDTO;
import codehub.grupo2.Service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Posts")
public class ControlRestPost {

    @Autowired
    private PostService PostService;

   

    @Operation(summary = "Get all posts")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    @GetMapping("/")
    public Collection<PostDTO> getPosts() {

        return PostService.getAllPostDTO();
    }

    @Operation(summary = "Get a post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable long id) {

        return PostService.getPostByIdDTO(id);
    }

    @Operation(summary = "Create a new post")
    @ApiResponse(responseCode = "201", description = "Post created successfully")
    @PostMapping("/")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostDTO postDTO) {
        if (postDTO == null || postDTO.user() == null || postDTO.title() == null || postDTO.text() == null || postDTO.topic() == null) {
            throw new IllegalArgumentException("The PostDTO and its required fields must not be null");
        }

        PostDTO createdPost = PostService.registerPostDTO(postDTO.user(), postDTO.title(), postDTO.text(), postDTO.topic());
        if (createdPost == null) {
            throw new IllegalStateException("Failed to create the post");
        }

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdPost.id()).toUri();
        return ResponseEntity.created(location).body(createdPost);
}

    @Operation(summary = "Delete a post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable long id) {
    PostDTO dto = PostService.getPostByIdDTO(id);
    
    if (dto == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    PostDTO deletedPost = PostService.deletePostDTO(dto.title());
    return ResponseEntity.ok(deletedPost);
}
}

