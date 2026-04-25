package vn.uit.edu.msshop.account.adapter.in.web.response;
public record AccountResponse(
    String id,
    String name,
    String email,
    String role,
    String status,
    String avatarUrl,
    String phoneNumber,
    String shippingAddress, 
    String keycloakId
) {

}
