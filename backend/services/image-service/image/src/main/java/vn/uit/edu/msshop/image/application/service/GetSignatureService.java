package vn.uit.edu.msshop.image.application.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.image.adapter.in.web.mapper.ImageWebMapper;
import vn.uit.edu.msshop.image.adapter.in.web.response.SignatureResponse;
import vn.uit.edu.msshop.image.application.dto.command.GetSignatureCommand;
import vn.uit.edu.msshop.image.application.port.in.GetSignatureUseCase;
import vn.uit.edu.msshop.image.application.port.out.GetSignaturePort;
@Service
@RequiredArgsConstructor
public class GetSignatureService implements GetSignatureUseCase {
    private final GetSignaturePort getSignaturePort;
    private final ImageWebMapper mapper;
    @Override
    public SignatureResponse getSignature(GetSignatureCommand command) {
        String signature = getSignaturePort.getSignature( command.timeStamp());
        return mapper.toResponse(signature, command.timeStamp().value());
    }

}
