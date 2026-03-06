package vn.uit.edu.msshop.image.application.port.out;

import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.TimeStamp;

public interface GetSignaturePort {
    public String getSignature(DataType dataType, TimeStamp timeStamp);
}
