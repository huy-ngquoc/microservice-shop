package vn.uit.edu.payment.application.port.out;

import vn.payos.model.webhooks.Webhook;



public interface PayOsWebHookPort {
    public void handlePayOSWebHook(Webhook body);
}
