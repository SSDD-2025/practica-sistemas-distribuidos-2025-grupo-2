package codehub.grupo2.Dto;

import java.util.List;
import java.sql.Blob;

import codehub.grupo2.DB.Entity.Post;

public record UserNameDTO(Long id, String username, String email, String password, List<String> roles) {}
