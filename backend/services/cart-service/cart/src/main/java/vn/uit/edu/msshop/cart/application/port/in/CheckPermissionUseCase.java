package vn.uit.edu.msshop.cart.application.port.in;

public interface CheckPermissionUseCase {
    public boolean isSameUser(
            String userIdHeader,
            String userIdFromCart);

    public boolean isUser(
            String userRole);

    public boolean isAdmin(
            String userRole);
}
