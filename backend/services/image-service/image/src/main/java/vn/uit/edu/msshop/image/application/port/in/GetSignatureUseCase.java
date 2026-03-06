package vn.uit.edu.msshop.image.application.port.in;

import vn.uit.edu.msshop.image.adapter.in.web.response.SignatureResponse;
import vn.uit.edu.msshop.image.application.dto.command.GetSignatureCommand;

public interface GetSignatureUseCase {
    public SignatureResponse getSignature(GetSignatureCommand command);
}
