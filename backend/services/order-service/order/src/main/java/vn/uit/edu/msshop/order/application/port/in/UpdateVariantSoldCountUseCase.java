package vn.uit.edu.msshop.order.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.order.application.dto.command.IncreaseVariantSoldCountCommand;
import vn.uit.edu.msshop.order.application.dto.command.UpdateVariantSoldCountCommand;
import vn.uit.edu.msshop.order.application.dto.query.VariantSoldCountView;

public interface UpdateVariantSoldCountUseCase {
    public VariantSoldCountView update(UpdateVariantSoldCountCommand command);
    public List<VariantSoldCountView> updateMany(List<IncreaseVariantSoldCountCommand> commands);
}
