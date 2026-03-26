package com.meetlive.app.response;

public class CreatePaymentResponse {

    boolean success;
    Result result;
    String error;

    public boolean isSuccess() {
        return success;
    }

    public Result getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public static class Result {
        int amount;
        Prefill prefill;
        Notes notes;
        Theme theme;
        String order_id, name, description, image, key;

        public int getAmount() {
            return amount;
        }

        public Prefill getPrefill() {
            return prefill;
        }

        public Notes getNotes() {
            return notes;
        }

        public Theme getTheme() {
            return theme;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImage() {
            return image;
        }

        public String getKey() {
            return key;
        }

    }

    public static class Prefill {
        String name, email, contact;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getContact() {
            return contact;
        }
    }

    public static class Notes {
        String address, plan_id, plan_name, merchant_order_id;
        int plan_amount, plan_points;

        public String getAddress() {
            return address;
        }

        public String getPlan_id() {
            return plan_id;
        }

        public String getPlan_name() {
            return plan_name;
        }

        public double getPlan_amount() {
            return plan_amount;
        }

        public String getMerchant_order_id() {
            return merchant_order_id;
        }

        public int getPlan_points() {
            return plan_points;
        }
    }

    public static class Theme {
        String color;

        public String getColor() {
            return color;
        }
    }
}