package vn.uit.edu.msshop.account.adapter.in.web.request;

public record CreateAccountRequest(
    
    String name, 
    String email,
    String password,
    String role,
    String status,
    String shippingAddress,
    String phoneNumber,
    String firstName,
    String lastName
) {

}
