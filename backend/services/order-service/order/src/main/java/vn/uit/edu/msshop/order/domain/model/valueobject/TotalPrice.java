package vn.uit.edu.msshop.order.domain.model.valueobject;
public record TotalPrice(long value) { 
    public TotalPrice{
        if(value<0) {
            throw new IllegalArgumentException("Invalid total price");
        }
    }

}
