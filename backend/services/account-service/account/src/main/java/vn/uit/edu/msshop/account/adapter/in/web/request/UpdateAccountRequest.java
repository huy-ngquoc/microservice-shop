package vn.uit.edu.msshop.account.adapter.in.web.request;

import java.util.UUID;

import org.antlr.v4.runtime.misc.NotNull;

import vn.uit.edu.msshop.account.adapter.in.web.request.common.ChangeRequest;

public record UpdateAccountRequest(
    @NotNull
    UUID id,
    ChangeRequest<String> name, 
    ChangeRequest<String> email, 
    ChangeRequest<String> password,
    ChangeRequest<String> role,
    ChangeRequest<String> status
) {

}
