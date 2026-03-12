package vn.uit.edu.payment.application.port.out;

import vn.payos.type.Webhook;

public interface PayOsWebHookPort {
    public void handlePayOSWebHook(Webhook body);
}
