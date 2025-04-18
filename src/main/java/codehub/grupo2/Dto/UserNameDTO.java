package codehub.grupo2.Dto;

import java.util.List;

import codehub.grupo2.DB.Entity.Post;

public record UserNameDTO(
    Long id,
    String username,
    String password,
    String email,
    boolean profilePicture,
    List<Post> posts,
    List<String> roles) {
}