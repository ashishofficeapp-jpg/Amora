package com.meetlive.app.response;

import java.util.List;

public class WalletHistoryResponse {

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

        List<WalletHistory> walletHistory;

        public List<WalletHistory> getWalletHistory() {
            return walletHistory;
        }
    }


    public static class WalletHistory {
        int id, credit, debit;
        String created_at, transaction_des;

        public int getId() {
            return id;
        }

        public int getCredit() {
            return credit;
        }

        public int getDebit() {
            return debit;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getTransaction_des() {
            return transaction_des;
        }
    }
}