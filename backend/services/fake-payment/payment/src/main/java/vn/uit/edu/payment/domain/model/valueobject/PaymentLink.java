package vn.uit.edu.payment.domain.model.valueobject;
public record PaymentLink(String value) {
    public PaymentLink {
        if(value==null||value.isBlank()){
            throw new IllegalArgumentException("Invalid value");
        }
    }

}
