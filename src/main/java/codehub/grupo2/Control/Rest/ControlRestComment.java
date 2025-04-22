package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.CommentDTO;
import codehub.grupo2.Service.APIService.APICommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Comments")
public class ControlRestComment {

    @Autowired
    private APICommentService CommentService;

    @Operation(summary = "Get all comments")
    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    @GetMapping("/")
    public Collection<CommentDTO> getComments() {
        return CommentService.getAllCommentsDTO();
    }

    @Operation(summary = "Get a comment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{id}")
    public CommentDTO getComment(@PathVariable long id) {

        return CommentService.getCommentByIdDTO(id);
    }

    @Operation(summary = "Create a new comment")
    @ApiResponse(responseCode = "201", description = "Comment created successfully")
    @PostMapping("/")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO CommentDTO) {

         CommentService.registerComment(CommentDTO.user(),CommentDTO.text(),CommentDTO.post());

        
        CommentDTO = CommentService.getCommentByIdDTO(CommentDTO.id());

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(CommentDTO.id()).toUri();

        return ResponseEntity.created(location).body(CommentDTO);
    }

    @Operation(summary = "Delete a comment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public CommentDTO deleteComment(@PathVariable long id) {
        CommentDTO dto = CommentService.getCommentByIdDTO(id);
        CommentService.deleteComment(dto.id());
        return dto;
    }
   
}

