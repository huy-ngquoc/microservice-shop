package vn.uit.edu.msshop.image.application.port.out;

import vn.uit.edu.msshop.image.domain.model.valueobject.TimeStamp;

public interface GetSignaturePort {
    public String getSignature( TimeStamp timeStamp);
}
