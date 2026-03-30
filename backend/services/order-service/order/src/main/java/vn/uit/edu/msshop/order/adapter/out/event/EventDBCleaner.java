package vn.uit.edu.msshop.order.adapter.out.event;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventDBCleaner {
    private final EventDocumentRepository eventDocumentRepo;
    @Scheduled(cron="0 0 0 * * ?")
    public void cleanUpOldReceivedEvent() {
        Instant threshold = Instant.now().minus(7, ChronoUnit.DAYS);
        eventDocumentRepo.deleteByReceiveAtBefore(threshold);
    }
}
