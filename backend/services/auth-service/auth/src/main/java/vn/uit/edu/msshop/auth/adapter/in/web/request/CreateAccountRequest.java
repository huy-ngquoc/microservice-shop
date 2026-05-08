package vn.uit.edu.msshop.auth.adapter.in.web.request;


public record CreateAccountRequest(
    String name, 
    String email,
    String password,
    String role,
String shippingAddress,
String phoneNumber) {

}
