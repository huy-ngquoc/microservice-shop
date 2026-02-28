package vn.uit.edu.msshop.account.application.service;





import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.application.dto.query.AccountView;
import vn.uit.edu.msshop.account.application.exception.AccountNotFoundException;
import vn.uit.edu.msshop.account.application.mapper.AccountViewMapper;
import vn.uit.edu.msshop.account.application.port.in.FindAccountUseCase;
import vn.uit.edu.msshop.account.application.port.out.LoadAccountPort;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;

@Service
@RequiredArgsConstructor
public class FindAccountService implements FindAccountUseCase{
    private final LoadAccountPort loadPort;
    private final AccountViewMapper accountViewMapper;

    @Override
    @Transactional
    public AccountView findAccountById(@NotNull AccountId accountId) {
        return this.loadPort.loadById(accountId).map(this.accountViewMapper::toView).orElseThrow(
            ()->new AccountNotFoundException(accountId));
    }

}
