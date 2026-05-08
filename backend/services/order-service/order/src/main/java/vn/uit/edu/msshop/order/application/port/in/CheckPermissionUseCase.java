package vn.uit.edu.msshop.order.application.port.in;
public interface CheckPermissionUseCase {
    public boolean isSameUser(String userIdHeader, String userIdFromOrder);
    public boolean isUser(String userRole);
    public boolean isAdmin(String userRole);

}
