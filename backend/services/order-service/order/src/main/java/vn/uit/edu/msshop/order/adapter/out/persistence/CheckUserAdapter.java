package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.UUID;

import vn.uit.edu.msshop.order.application.port.out.CheckUserPort;

public class CheckUserAdapter implements CheckUserPort {

    @Override
    public boolean isUserAvailable(UUID userId) {
       //TODO
       return true;
    }

}
