package codehub.grupo2.Control.Rest;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.Dto.CommentDTO;
import codehub.grupo2.Service.CommentService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/Comments")
public class ControlRestComment {

    @Autowired
    private CommentService CommentService;

    @GetMapping("/")
    public Collection<CommentDTO> getComments() {

        return CommentService.getAllCommentsDTO();
    }

    @GetMapping("/{id}")
    public CommentDTO getComment(@PathVariable long id) {

        return CommentService.getCommentByIdDTO(id);
    }

    @PostMapping("/")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO CommentDTO) {

         CommentService.registerComment(CommentDTO.user(),CommentDTO.text(),CommentDTO.post());

        
        CommentDTO = CommentService.getCommentByIdDTO(CommentDTO.id());

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(CommentDTO.id()).toUri();

        return ResponseEntity.created(location).body(CommentDTO);
    }

    @DeleteMapping("/{id}")
    public CommentDTO deleteComment(@PathVariable long id) {
        CommentDTO dto = CommentService.getCommentByIdDTO(id);
        CommentService.deleteComment(dto.id());
        return dto;
    }
   
}
