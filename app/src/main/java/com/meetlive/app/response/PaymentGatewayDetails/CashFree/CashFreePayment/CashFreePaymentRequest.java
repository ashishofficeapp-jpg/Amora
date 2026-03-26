package com.meetlive.app.response.PaymentGatewayDetails.CashFree.CashFreePayment;

public class CashFreePaymentRequest {
    private String order_id;
    private String plan_id;

    public CashFreePaymentRequest(String order_id, String plan_id) {
        this.order_id = order_id;
        this.plan_id = plan_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }
}
