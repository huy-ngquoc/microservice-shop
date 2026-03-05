package vn.uit.edu.msshop.account.adapter.in.web.request;

import java.util.UUID;

import org.antlr.v4.runtime.misc.NotNull;

public record CreateAccountRequest(
    @NotNull
    UUID id,
    String name, 
    String email,
    String password,
    String role,
    String status
) {

}
