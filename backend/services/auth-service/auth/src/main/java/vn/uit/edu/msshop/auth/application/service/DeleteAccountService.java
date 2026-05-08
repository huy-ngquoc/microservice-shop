package vn.uit.edu.msshop.auth.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.application.port.in.DeleteAccountUseCase;
import vn.uit.edu.msshop.auth.application.port.out.DeleteAccountPort;
import vn.uit.edu.msshop.auth.domain.event.AccountId;
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteAccountService implements DeleteAccountUseCase{
    private final DeleteAccountPort deletePort;
    @Override
    public void deleteAccount(AccountId id) {
        try {
            deletePort.deleteAccount(id);
        
        log.info("Đã xóa thành công User có ID: {}",id.value().toString());
    } catch (Exception e) {
        
        throw new RuntimeException("Không thể xóa người dùng trong Keycloak");
    }
    }

}
