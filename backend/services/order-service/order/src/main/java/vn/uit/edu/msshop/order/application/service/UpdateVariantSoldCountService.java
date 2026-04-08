package vn.uit.edu.msshop.order.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.order.application.dto.command.IncreaseVariantSoldCountCommand;
import vn.uit.edu.msshop.order.application.dto.command.UpdateVariantSoldCountCommand;
import vn.uit.edu.msshop.order.application.dto.query.VariantSoldCountView;
import vn.uit.edu.msshop.order.application.mapper.VariantSoldCountViewMapper;
import vn.uit.edu.msshop.order.application.port.in.UpdateVariantSoldCountUseCase;
import vn.uit.edu.msshop.order.application.port.out.CreateVariantSoldCountPort;
import vn.uit.edu.msshop.order.application.port.out.FindVariantSoldCountPort;
import vn.uit.edu.msshop.order.application.port.out.UpdateVariantSoldCountPort;
import vn.uit.edu.msshop.order.domain.model.VariantSoldCount;
import vn.uit.edu.msshop.order.domain.model.valueobject.SoldCount;
import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

@Service
@RequiredArgsConstructor
public class UpdateVariantSoldCountService implements UpdateVariantSoldCountUseCase {
    private final VariantSoldCountViewMapper mapper;
    private final FindVariantSoldCountPort findPort;
    private final UpdateVariantSoldCountPort updatePort;
    
    private final CreateVariantSoldCountPort createPort;
    @Override
    public VariantSoldCountView update(UpdateVariantSoldCountCommand command) {
        
        return mapper.toView(updateSoldCount(command));
    }
    private VariantSoldCount updateSoldCount(UpdateVariantSoldCountCommand command) {
        final var variantSoldCount = findPort.findById(command.id());
        if(variantSoldCount.isEmpty()) {
            
            final var draft = VariantSoldCount.Draft.builder().id(command.id()).soldCount(command.soldCount()).build();
            return createPort.create(VariantSoldCount.create(draft));
        } 
        final var updateInfo = VariantSoldCount.UpdateInfo.builder().id(command.id()).soldCount(command.soldCount()).build();
        return updatePort.update(variantSoldCount.get().applyUpdateInfo(updateInfo));
    }


    @Override
    public List<VariantSoldCountView> updateMany(List<IncreaseVariantSoldCountCommand> commands) {
        List<VariantSoldCount> listSoldCounts = findPort.findByIdInList(commands.stream().map(IncreaseVariantSoldCountCommand::id).toList());
       List<VariantSoldCount> toSaves= new ArrayList<>();
        for(IncreaseVariantSoldCountCommand command: commands) {
            VariantSoldCount v = findByIdInList(command.id(), listSoldCounts);
            if(v==null) {
                final var updateCommand= new UpdateVariantSoldCountCommand(command.id(), new SoldCount(command.amount().value()));
                toSaves.add(updateSoldCount(updateCommand));
            }
            else {
                final var updateCommand= new UpdateVariantSoldCountCommand(command.id(), new SoldCount(command.amount().value()+v.getSoldCount().value()));
                toSaves.add(updateSoldCount(updateCommand));
            }
        }
        return updatePort.updateMany(toSaves).stream().map(mapper::toView).toList();

    }
    private VariantSoldCount findByIdInList(VariantId id, List<VariantSoldCount> soldCounts) {
        for(VariantSoldCount v: soldCounts) {
            if(id.value().equals(v.getId().value())) {
                return v;
            }
        }
        return null;
    }


}
