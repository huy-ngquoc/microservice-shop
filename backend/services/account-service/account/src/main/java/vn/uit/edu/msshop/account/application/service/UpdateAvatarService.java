package vn.uit.edu.msshop.account.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.uit.edu.msshop.account.application.dto.command.UpdateAvatarCommand;
import vn.uit.edu.msshop.account.application.exception.AccountNotFoundException;
import vn.uit.edu.msshop.account.application.port.in.UpdateAvatarUseCase;
import vn.uit.edu.msshop.account.application.port.out.LoadAccountPort;
import vn.uit.edu.msshop.account.application.port.out.PublishAccountEventPort;
import vn.uit.edu.msshop.account.application.port.out.SaveAccountPort;
import vn.uit.edu.msshop.account.domain.event.kafka.DeleteOldImageEvent;
import vn.uit.edu.msshop.account.domain.event.kafka.RollbackImageEvent;
import vn.uit.edu.msshop.account.domain.model.Account;
import vn.uit.edu.msshop.account.domain.model.valueobject.Avatar;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAvatarService implements UpdateAvatarUseCase {
    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final PublishAccountEventPort publishPort;

    @Override
    public void updateAvatar(UpdateAvatarCommand command) {
        try {
        
            final Account account = this.loadAccountPort.loadById(command.id()).orElseThrow(()->new AccountNotFoundException(command.id()));
            if(account.getAvatar()!=null) {
                publishPort.sendDeleteOldImageEvent(new DeleteOldImageEvent(account.getAvatar().publicId().value()));
            }
            Avatar avatar = new Avatar(command.publicId(),command.url(),command.imageSize());
            account.setAvatar(avatar);
            
            saveAccountPort.save(account);
    }
    catch(Exception e) {
        e.printStackTrace();
        publishPort.sendRollbackImageEvent(new RollbackImageEvent(command.publicId().value()));

    }



    }

}
