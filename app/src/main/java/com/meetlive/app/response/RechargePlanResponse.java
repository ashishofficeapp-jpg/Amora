package com.meetlive.app.response;

import java.io.Serializable;
import java.util.List;

public class RechargePlanResponse implements Serializable {

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


    public static class Result implements Serializable {

        List<Data> data;
        String upi_id;

        public List<Data> getData() {
            return data;
        }

        public String getUpi_id() {
            return upi_id;
        }
    }

    public static class Data implements Serializable {
        int id, amount, status, points, validity_in_days, type;
        String name, image;

        public Data(int id, int amount, int status, int points, int validity_in_days, int type) {
            this.id = id;
            this.amount = amount;
            this.status = status;
            this.points = points;
            this.validity_in_days = validity_in_days;
            this.type = type;

        }

        public int getId() {
            return id;
        }

        public int getAmount() {
            return amount;
        }

        public int getStatus() {
            return status;
        }

        public int getPoints() {
            return points;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public int getValidity_in_days() {
            return validity_in_days;
        }

        public int getType() {
            return type;
        }
    }
}
