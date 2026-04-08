package vn.uit.edu.msshop.order.domain.model.valueobject;
public record Amount(int value) {
    public Amount {
        
        if(value<0) throw new IllegalArgumentException("Invalid amount");
    
    }
}
