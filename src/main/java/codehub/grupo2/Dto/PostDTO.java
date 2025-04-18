package codehub.grupo2.Dto;
import java.time.LocalDate;
import java.util.List;

import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;

public record PostDTO(
        Long id,
        LocalDate date,
        String title,
        String text,
        UserName user,
        List<Comment> comments,
        Topic topic
) {
}