package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.DB.UserRepository;
import codehub.grupo2.DB.Entity.UserName;
import codehub.grupo2.Dto.CommentDTO;
import codehub.grupo2.Service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/comments")
public class ControlRestComment {

    @Autowired
    private CommentService CommentService;
    
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Get all comments")
    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    @ApiResponse(responseCode = "204", description = "No comments found")
    @GetMapping("/")
    public ResponseEntity<Collection<CommentDTO>> getComments() {
        if (CommentService.getAllCommentsDTO().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CommentService.getAllCommentsDTO());
    }

    @Operation(summary = "Get a comment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long id) {
        CommentDTO dto = CommentService.getCommentByIdDTO(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create a new comment")
    @ApiResponse(responseCode = "201", description = "Comment created successfully")
   @PostMapping("/")
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO) {
        if (commentDTO == null) {
            throw new IllegalArgumentException("The CommentDTO cannot be null");
        }

        CommentDTO createdComment = CommentService.registerCommentDTO(commentDTO);
        if (createdComment == null) {
            throw new IllegalStateException("The comment could not be created");
        }

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(createdComment.id()).toUri();
        return ResponseEntity.created(location).body(createdComment);
    }

    @Operation(summary = "Delete a comment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden: User is not the owner of the comment"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
    
        int deletedComment = CommentService.deleteCommentByUserAPI(id, username);
        if (deletedComment == 1) {
            return ResponseEntity.notFound().build(); 
        }
        else if (deletedComment == 2) {
            return ResponseEntity.status(403).body("You are not the owner of this comment"); 
        }else{
        return ResponseEntity.ok().body("Comment deleted successfully"); 
        }

    }

}

