package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import codehub.grupo2.Dto.CommentDTO;
import codehub.grupo2.Service.APIService.APICommentService;
import jakarta.validation.Valid;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Comments")
public class ControlRestComment {

    @Autowired
    private APICommentService CommentService;

    @GetMapping("/")
    public Collection<CommentDTO> getComments() {
        return CommentService.getAllCommentsDTO();
    }

    @GetMapping("/{id}")
    public CommentDTO getComment(@PathVariable long id) {

        return CommentService.getCommentByIdDTO(id);
    }

    @PostMapping("/")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDTO commentDTO) {
        if (commentDTO == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la solicitud no puede estar vacío");
        }

        try {
            CommentDTO result = CommentService.registerCommentDTO(commentDTO); 
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el comentario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public CommentDTO deleteComment(@PathVariable long id) {
        CommentDTO dto = CommentService.getCommentByIdDTO(id);
        CommentService.deleteComment(dto.id());
        return dto;
    }
   
}
