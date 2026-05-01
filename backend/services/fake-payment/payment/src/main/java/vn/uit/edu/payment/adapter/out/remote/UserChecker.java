package vn.uit.edu.payment.adapter.out.remote;

import org.springframework.stereotype.Component;

import vn.uit.edu.payment.application.port.out.CheckUserPort;
import vn.uit.edu.payment.domain.model.valueobject.UserId;
@Component
public class UserChecker implements CheckUserPort {

    @Override
    public void checkUser(UserId userId) {
        
    }

}
