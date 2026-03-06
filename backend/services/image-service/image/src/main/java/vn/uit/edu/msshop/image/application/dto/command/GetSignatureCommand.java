package vn.uit.edu.msshop.image.application.dto.command;

import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.TimeStamp;

public record GetSignatureCommand(DataType dataType, TimeStamp timeStamp) {
    
}
