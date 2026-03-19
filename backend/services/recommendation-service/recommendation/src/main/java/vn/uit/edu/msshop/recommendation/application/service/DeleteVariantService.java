package vn.uit.edu.msshop.recommendation.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.recommendation.application.dto.command.DeleteVariantCommand;
import vn.uit.edu.msshop.recommendation.application.port.in.DeleteVariantUseCase;
import vn.uit.edu.msshop.recommendation.application.port.out.DeleteVariantPort;

@Service
@RequiredArgsConstructor
public class DeleteVariantService implements DeleteVariantUseCase {
    private final DeleteVariantPort deletePort;

    @Override
    public void delete(DeleteVariantCommand command) {
        deletePort.deleteById(command.id());
    }

}
