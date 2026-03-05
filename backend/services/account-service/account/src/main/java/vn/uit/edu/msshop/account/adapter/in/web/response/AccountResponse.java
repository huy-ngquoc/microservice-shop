package vn.uit.edu.msshop.account.adapter.in.web.response;
public record AccountResponse(
    String id,
    String name,
    String email,
    String password,
    String role,
    String status
) {

}
