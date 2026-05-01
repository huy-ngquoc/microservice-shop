package vn.uit.edu.payment.application.port.out;

import java.util.UUID;

import vn.payos.model.webhooks.Webhook;



public interface PayOsWebHookPort {
    public void handlePayOSWebHook(Webhook body);
    public void fakeWebHook(UUID orderId);
}
