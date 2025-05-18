package codehub.grupo2.Dto;
import java.time.LocalDate;
import java.util.List;

import codehub.grupo2.DB.Entity.Comment;
import codehub.grupo2.DB.Entity.Topic;
import codehub.grupo2.DB.Entity.UserName;

public record PostDTO(Long id, String title, String text, LocalDate date, UserNameDTO user, TopicDTO topic) {}
