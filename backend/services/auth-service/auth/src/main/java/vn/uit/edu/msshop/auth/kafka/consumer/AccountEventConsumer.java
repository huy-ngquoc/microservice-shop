package vn.uit.edu.msshop.auth.kafka.consumer;

import org.keycloak.admin.client.Keycloak;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.auth.domain.event.AccountId;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "account-topic-fail", groupId = "account-group")
public class AccountEventConsumer {
    private final Keycloak keycloak;

    @KafkaHandler
    public void handleAccountCreatedFail(AccountId accountId) {
        try {
        keycloak.realm("ms_shop").users().get(accountId.value().toString()).remove();
        
        log.info("Đã xóa thành công User có ID: {}",accountId.value().toString());
    } catch (Exception e) {
        
        throw new RuntimeException("Không thể xóa người dùng trong Keycloak");
    }
    }
}
