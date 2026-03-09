package vn.uit.edu.msshop.auth.domain.dto.request;


public record CreateAccountRequest(
    String name, 
    String email,
    String password,
    String role) {

}
