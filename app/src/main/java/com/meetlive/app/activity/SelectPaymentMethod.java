package com.meetlive.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.cashfree.pg.CFPaymentService;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.meetlive.app.R;
import com.meetlive.app.databinding.ActivitySelectPaymentMethodBinding;
import com.meetlive.app.dialog.PaymentCompletedDialog;
import com.meetlive.app.response.CreatePaymentResponse;
import com.meetlive.app.response.PaymentGatewayDetails.CashFree.CFToken.CfTokenResponce;
import com.meetlive.app.response.PaymentGatewayDetails.CashFree.CashFreePayment.CashFreePaymentRequest;
import com.meetlive.app.response.PaymentSelector.PaymentSelectorData;
import com.meetlive.app.response.PaymentSelector.PaymentSelectorResponce;
import com.meetlive.app.response.RechargePlanResponse;
import com.meetlive.app.response.ReportResponse;
import com.meetlive.app.response.WalletRechargeResponse;
import com.meetlive.app.retrofit.ApiManager;
import com.meetlive.app.retrofit.ApiResponseInterface;
import com.meetlive.app.utils.Constant;
import com.meetlive.app.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.billingclient.api.Purchase.PurchaseState.PURCHASED;
import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_BANK_CODE;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_CVV;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_HOLDER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_MM;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_NUMBER;
import static com.cashfree.pg.CFPaymentService.PARAM_CARD_YYYY;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_NOTIFY_URL;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.cashfree.pg.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.cashfree.pg.CFPaymentService.PARAM_UPI_VPA;
import static com.cashfree.pg.CFPaymentService.PARAM_WALLET_CODE;

public class SelectPaymentMethod extends BaseActivity implements ApiResponseInterface, PaymentResultListener {

    ApiManager apiManager;
    SessionManager sessionManager;
    RechargePlanResponse.Data selectedPlan;

    String upiId;
    BillingClient billingClient;
    ActivitySelectPaymentMethodBinding binding;
    String customGpayPlan = "";
    private static final String TAG = "SelectPaymentMethod";
    String transactionId = "TID" + System.currentTimeMillis();
    String orderId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent_new));
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_payment_method);
        //  binding.setClickListener(EventHandler(this));

            /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());


        setToolbarTitle("Select Payment Method");
        selectedPlan = (RechargePlanResponse.Data) getIntent().getSerializableExtra("selected_plan");
        //   Log.e("selectedPlan", new Gson().toJson(selectedPlan));
        upiId = getIntent().getStringExtra("upi_id");

        if (selectedPlan.getType() == 2) {
            customGpayPlan = "video_call_" + selectedPlan.getAmount();

        } else if (selectedPlan.getType() == 6) {
            customGpayPlan = "text_chat_" + selectedPlan.getAmount();

        } else if (selectedPlan.getType() == 7) {
            //  customGpayPlan = "video_call_" + 100;
            customGpayPlan = "video_call_" + selectedPlan.getAmount();
        }

        binding.coins.setText(String.valueOf(selectedPlan.getPoints()));

        apiManager = new ApiManager(this, this);
        sessionManager = new SessionManager(this);

        if (sessionManager.getUserLocation().equals("India")) {
            binding.price.setText("₹ " + selectedPlan.getAmount());
        } else {
            binding.price.setText("$ " + selectedPlan.getAmount());
            binding.upi.setText("Debit Card/Credit Card");
            binding.gPay.setVisibility(View.VISIBLE);

            binding.upi.setVisibility(View.GONE);
            binding.gPay.setChecked(true);
        }

        binding.buttonPay.setOnClickListener(v -> {
            //Log.e("userLoation", sessionManager.getUserLocation());
            try {
                if (sessionManager.getUserLocation().equals("India")) {
                    checkSelectedPaymentMethod();
                } else {
                    /*if (binding.upi.isChecked()) {
                        startActivity(new Intent(SelectPaymentMethod.this, StripePaymentProcess.class)
                                .putExtra("planid", String.valueOf(selectedPlan.getId()))
                                .putExtra("planamount", String.valueOf(selectedPlan.getAmount())));
                        finish();
                    } else {
                        Log.e("selectedPlan", selectedPlan.getType() + "");
                        startGpayGateway();
                    }*/

                    startGpayGateway();
                }
            } catch (Exception e) {
            }
        });

        // Setup In app purchase (G-Pay)
        setUpBilling();
        if (sessionManager.getUserLocation().equals("India")) {
            apiManager.getPaymentSelector();
        } else {
            ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_gpay)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_ppay)).setVisibility(View.GONE);
            //  ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
            binding.llPayupi.setVisibility(View.GONE);
            binding.buttonPay.setVisibility(View.VISIBLE);
        }
        backPage();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.rlMain.setBackgroundResource(R.drawable.select_payment_method_new_bg);
            }
        }, 299);

    }



    void checkSelectedPaymentMethod() {
        int selectedID = binding.paymentRadioGroup.getCheckedRadioButtonId();

        apiManager.createPayment(selectedPlan.getId());

        // If selected id type equals to UPI Method
      /*  if (selectedID == 2131362361) {
            startUpiGateway();

        } else {
            startGpayGateway();
        }*/
    }


    /* void startUpiGateway() {
         // START PAYMENT INITIALIZATION
         mEasyUpiPayment = new EasyUpiPayment.Builder()
                 .with(SelectPaymentMethod.this)
                 .setPayeeVpa(upiId)
                 .setPayeeName("ZeepLive")
                 .setTransactionId(transactionId)
                 .setTransactionRefId(transactionId)
                 .setDescription(selectedPlan.getName())
                 .setAmount(selectedPlan.getAmount() + ".00")
                 .build();

         // Register Listener for Events
         mEasyUpiPayment.setPaymentStatusListener(SelectPaymentMethod.this);
         mEasyUpiPayment.setDefaultPaymentApp(PaymentApp.NONE);

         // Check if app exists or not
         if (mEasyUpiPayment.isDefaultAppExist()) {
             onAppNotFound();

         } else {
             // START PAYMENT
             mEasyUpiPayment.startPayment();
         }
     }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.e(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
//String inPayKey=key.equals("txStatus");
                    if (key.equals("txStatus")) {

                        if (bundle.getString(key).equals("SUCCESS")) {
                            CashFreePaymentRequest cashFreePaymentRequest = new CashFreePaymentRequest(orderIdToken, String.valueOf(selectedPlan.getId()));
                            apiManager.cashFreePayment(cashFreePaymentRequest);
                            finish();
                            // Log.e("customData", key + " : " + bundle.getString(key));
                        }
                        return;
                    }
                  /*  if (bundle.getString(key) != null) {
                        Log.e(TAG, key + " : " + bundle.getString(key));
                    }*/
                }
        }
    }

    public void startRazorPayGateway(CreatePaymentResponse.Result orderData) {

        CreatePaymentResponse.Result data = orderData;

        final Activity activity = this;
        final Checkout checkout = new Checkout();
        checkout.setKeyID(data.getKey());
        //  checkout.setKeyID("rzp_test_E5erobZUHcNr6o");
        //  Log.e("razKey", data.getKey());
        try {
            JSONObject options = new JSONObject();

            // Notes Object
            JSONObject notes = new JSONObject();
            notes.put("address", data.getNotes().getAddress());
            notes.put("plan_id", data.getNotes().getPlan_id());
            notes.put("plan_name", "" + data.getNotes().getPlan_name());
            notes.put("merchant_order_id", "" + data.getNotes().getMerchant_order_id());
            notes.put("plan_amount", "" + data.getNotes().getPlan_amount());
            notes.put("plan_points", "" + data.getNotes().getPlan_points());


            options.put("notes", notes);
            options.put("name", "Zeeplive");
            options.put("description", selectedPlan.getName());
            options.put("order_id", data.getOrder_id());
            options.put("theme.color", data.getTheme().getColor());
            options.put("currency", "INR");
            String amount = String.valueOf(data.getAmount());
            options.put("amount", amount);

            options.put("prefill.email", "abc@g.com");
            options.put("prefill.contact", "9999885574");

            //     options.put("prefill.method", "upi");
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            apiManager.verifyPayment(razorpayPaymentID, orderId);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    void startGpayGateway() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    queryPurchases();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    /*----------- In app purchase Starts From Here ----------*/
    void setUpBilling() {
        /*------------------- Setup Google In-app Billing ----------------------*/
        PurchasesUpdatedListener purchaseUpdateListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                try {
                    Log.e("playResponce", new Gson().toJson(billingResult.getResponseCode()));
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                        apiManager.rechargeWallet(purchases.get(0).getOrderId(), String.valueOf(selectedPlan.getId()));

                        for (Purchase purchase : purchases) {
                            handlePurchase(purchase);
                        }

                    } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                        // Handle an error caused by a user cancelling the purchase flow.
                        Toast.makeText(com.meetlive.app.activity.SelectPaymentMethod.this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle any other error codes.
                        Toast.makeText(com.meetlive.app.activity.SelectPaymentMethod.this, "Error occured", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        };

        try {
            billingClient = BillingClient.newBuilder(this)
                    .setListener(purchaseUpdateListener)
                    .enablePendingPurchases()
                    .build();
        } catch (Exception e) {

        }
    }

    void handlePurchase(Purchase purchase) {
        // Purchase retrieved from BillingClient#queryPurchases or your PurchasesUpdatedListener.

        // Verify the purchase.
        // Ensure entitlement was not already granted for this purchaseToken.
        // Grant entitlement to the user.

        ConsumeParams consumeParams =
                ConsumeParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                }
            }
        };

        handleNonConsumableProduct(purchase);
        billingClient.consumeAsync(consumeParams, listener);
    }

    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    void handleNonConsumableProduct(Purchase purchase) {
        if (purchase.getPurchaseState() == PURCHASED) {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
                }
            }

        }
    }

    void queryPurchases() {

        List<String> skuList = new ArrayList<>();
        skuList.add(customGpayPlan);
        Log.e("customGpayPlan", new Gson().toJson(customGpayPlan));
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        // Process the result.
                        Log.e("SkuDetails", new Gson().toJson(skuDetailsList));

                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !skuDetailsList.isEmpty()) {

                            // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
                            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetailsList.get(0)).build();

                            int responseCode = billingClient.launchBillingFlow(com.meetlive.app.activity.SelectPaymentMethod.this
                                    , billingFlowParams).getResponseCode();

                        } else {
                            Toast.makeText(com.meetlive.app.activity.SelectPaymentMethod.this, "This recharge not available on G-PAY", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /*----------- In app purchase Ends Here ----------*/



/*    @Override
    public void onTransactionSuccess() {
        // Payment Success
        apiManager.rechargeWallet(transactionId, String.valueOf(selectedPlan.getId()));
    }*/


    @Override
    public void isError(String errorCode) {
        Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void isSuccess(Object response, int ServiceCode) {
        try {
            if (ServiceCode == Constant.RECHARGE_WALLET) {
                WalletRechargeResponse rsp = (WalletRechargeResponse) response;

                // This Api is used before with simple UPI gateway
                new PaymentCompletedDialog(this, transactionId, selectedPlan.getAmount());
            }
            if (ServiceCode == Constant.CREATE_PAYMENT) {
                CreatePaymentResponse rsp = (CreatePaymentResponse) response;

                if (rsp.getResult().getOrder_id() != null && rsp.getResult().getOrder_id().length() > 1) {
                    orderId = rsp.getResult().getOrder_id();
                    startRazorPayGateway(rsp.getResult());
                }
            }
            if (ServiceCode == Constant.VERIFY_PAYMENT) {
                ReportResponse rsp = (ReportResponse) response;
                if (rsp.getResult() != null) {
                    new PaymentCompletedDialog(this, transactionId, selectedPlan.getAmount());
                }
            }
            if (ServiceCode == Constant.GET_PAYMENT_SELECTOR) {
                PaymentSelectorResponce rsp = (PaymentSelectorResponce) response;
                // Log.e("inActivity", rsp.getResult().getName());

                ArrayList<PaymentSelectorData> paymentSelectorDataArrayList = new ArrayList<>();

                paymentSelectorDataArrayList = rsp.getPaymentSelectorData();

                if (paymentSelectorDataArrayList.size() == 1) {
                    if (paymentSelectorDataArrayList.get(0).getName().equals("Razorpay")) {
                        binding.upi.setVisibility(View.VISIBLE);
                        binding.buttonPay.setVisibility(View.VISIBLE);
                        ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.GONE);
                        ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                        ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.GONE);
                    } else if (paymentSelectorDataArrayList.get(0).getName().equals("Cashfree")) {
                        ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                        //      apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));
                        binding.upi.setVisibility(View.GONE);
                        binding.buttonPay.setVisibility(View.GONE);
                        checkAvailPaymentMethod();
                    }
                } else {
                    binding.upi.setVisibility(View.VISIBLE);
                    binding.buttonPay.setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.VISIBLE);

                    ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                    //               apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));
                    binding.upi.setVisibility(View.VISIBLE);
                    binding.buttonPay.setVisibility(View.VISIBLE);
                    checkAvailPaymentMethod();
                }

           /* if (rsp.getResult().getName().equals("Razorpay")) {

                binding.upi.setVisibility(View.VISIBLE);
                binding.buttonPay.setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.GONE);
            } else if (rsp.getResult().getName().equals("Cashfree")) {
                ((LinearLayout) findViewById(R.id.ll_payupi)).setVisibility(View.VISIBLE);
                apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()));
                binding.upi.setVisibility(View.GONE);
                binding.buttonPay.setVisibility(View.GONE);
                checkAvailPaymentMethod();
            }*/
            }
            if (ServiceCode == Constant.GET_CFTOKEN) {
                CfTokenResponce rsp = (CfTokenResponce) response;
                token = rsp.getCftoken();
                orderIdToken = rsp.getOrderId();
                appIDCashFree = rsp.getAppId();
                orderAmountCashfree = rsp.getDeductableAmout();
                cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
                cfPaymentService.setOrientation(0);

                if (carryParmentProcessOf.equals("gpay")) {
                    cfPaymentService.gPayPayment(this, getInputParams(), token, stage);
                } else if (carryParmentProcessOf.equals("phonepay")) {
                    cfPaymentService.phonePePayment(this, getInputParams(), token, stage);
                }
            }
        } catch (Exception e) {
        }
    }

    private void checkAvailPaymentMethod() {
        if (appInstalledOrNot("com.phonepe.app")) {
            ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.VISIBLE);
//            ((View) findViewById(R.id.v_gpay)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_ppay)).setVisibility(View.VISIBLE);

        } else {
            ((LinearLayout) findViewById(R.id.ll_phonepe)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_ppay)).setVisibility(View.GONE);
        }

        if (appInstalledOrNot("com.google.android.apps.nbu.paisa.user")) {
            ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.VISIBLE);
            ((View) findViewById(R.id.v_gpay)).setVisibility(View.VISIBLE);

        } else {
            ((LinearLayout) findViewById(R.id.ll_gpay)).setVisibility(View.GONE);
            ((View) findViewById(R.id.v_gpay)).setVisibility(View.GONE);
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    String token = "TOKEN_DATA";
    String stage = "PROD";
    String orderIdToken = "";
    String appIDCashFree = "";
    String orderAmountCashfree = "";

    private Map<String, String> getInputParams() {

        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = appIDCashFree;
        String orderId = orderIdToken;


        orderAmountCashfree = String.valueOf(selectedPlan.getAmount());

        String orderNote = "Test Order";
        String customerName = "John Doe";
        String customerPhone = "9900012345";
        String customerEmail = "test@gmail.com";

        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmountCashfree);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        params.put(PARAM_NOTIFY_URL, "https://zeep.live/api/verifyPayment/");

        return params;
    }

    CFPaymentService cfPaymentService;



    enum SeamlessMode {
        CARD, WALLET, NET_BANKING, UPI_COLLECT, PAY_PAL
    }

    SeamlessMode currentMode = SeamlessMode.CARD;

    private Map<String, String> getSeamlessCheckoutParams() {
        Map<String, String> params = getInputParams();
        switch (currentMode) {
            case CARD:
                params.put(PARAM_PAYMENT_OPTION, "card");
                params.put(PARAM_CARD_NUMBER, "4434260000000008");//Replace Card number
                params.put(PARAM_CARD_MM, "05"); // Card Expiry Month in MM
                params.put(PARAM_CARD_YYYY, "2025"); // Card Expiry Year in YYYY
                params.put(PARAM_CARD_HOLDER, "John Doe"); // Card Holder name
                params.put(PARAM_CARD_CVV, "123"); // Card CVV
                break;
            case WALLET:
                params.put(PARAM_PAYMENT_OPTION, "wallet");
                params.put(PARAM_WALLET_CODE, "4007"); // Put one of the wallet codes mentioned here https://dev.cashfree.com/payment-gateway/payments/wallets
                break;
            case NET_BANKING:
                params.put(PARAM_PAYMENT_OPTION, "nb");
                params.put(PARAM_BANK_CODE, "3333"); // Put one of the bank codes mentioned here https://dev.cashfree.com/payment-gateway/payments/netbanking
//                params.put(PARAM_NOTIFY_URL, "https://zeep.live/api/verifyPayment/");
                break;
            case UPI_COLLECT:
                params.put(PARAM_PAYMENT_OPTION, "upi");
                params.put(PARAM_UPI_VPA, "VALID_VPA");
                //               params.put(PARAM_NOTIFY_URL, "https://zeep.live/api/verifyPayment/");
                break;
            case PAY_PAL:
                params.put(PARAM_PAYMENT_OPTION, "paypal");
                break;
        }
        return params;
    }

    private String carryParmentProcessOf = "";

    public void pay_UPI(View view) {
        cfPaymentService.upiPayment(this, getInputParams(), token, stage);
    }

    public void Gpay(View view) {
        carryParmentProcessOf = "gpay";
        apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));

        //  cfPaymentService.gPayPayment(this, getInputParams(), token, stage);
    }

    public void phonepe_pay(View view) {
        carryParmentProcessOf = "phonepay";
        apiManager.getCfToken(String.valueOf(selectedPlan.getAmount()), String.valueOf(selectedPlan.getId()));
        //   cfPaymentService.phonePePayment(this, getInputParams(), token, stage);

    }

    public void card_pay(View view) {
        //    cfPaymentService.doPayment(this, getSeamlessCheckoutParams(), token, stage);

      /*  startActivity(new Intent(SelectPaymentMethod.this, CashfreeCardPaymentActivity.class)
                .putExtra("token", token)
                .putExtra("selectId", String.valueOf(selectedPlan.getAmount()))
                .putExtra("orderIdToken", orderIdToken));
        finish();*/
    }

    public void nb_pay(View view) {
        currentMode = SeamlessMode.NET_BANKING;
        cfPaymentService.doPayment(this, getSeamlessCheckoutParams(), token, stage);
    }


    private void backPage() {
        binding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.rlMain.setBackgroundResource(0);
                        finish();
                    }
                }, 250);

            }
        });
    }



    public void gpay_check_box(View view) {
        if (binding.llGpayCheckBox.isChecked ()){
            binding.llPhonepeCheckBox.setChecked(false);
        }
    }
    public void phonepey_check_box(View view) {
        if (binding.llPhonepeCheckBox.isChecked ()){
            binding.llGpayCheckBox.setChecked(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.rlMain.setBackgroundResource(0);
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down);
    }
}
