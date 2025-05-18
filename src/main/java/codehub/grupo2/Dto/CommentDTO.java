package codehub.grupo2.Dto;

import java.time.LocalDate;

import codehub.grupo2.DB.Entity.Post;
import codehub.grupo2.DB.Entity.UserName;

public record CommentDTO(Long id, String text, LocalDate date, UserNameDTO user, Long postId) {}
