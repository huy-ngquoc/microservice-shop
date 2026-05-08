package vn.uit.edu.msshop.order.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.order.application.port.out.CheckUserPort;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckUserService implements CheckUserPort {

    @Override
    public boolean isUserAvailable(UUID userId) {
        return true;
    }

}
