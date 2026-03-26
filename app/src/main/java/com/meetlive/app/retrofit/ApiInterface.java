package com.meetlive.app.retrofit;

import com.meetlive.app.body.CallRecordBody;
import com.meetlive.app.body.CallRecordBodyAudio;
import com.meetlive.app.response.AddRemoveFavResponse;
import com.meetlive.app.response.AgoraTokenResponse;
import com.meetlive.app.response.Call.ResultCall;
import com.meetlive.app.response.Chat.RequestChatList;
import com.meetlive.app.response.Chat.ResultChatList;
import com.meetlive.app.response.ChatPurchaseValidity;
import com.meetlive.app.response.ChatRoom.RequestChatRoom;
import com.meetlive.app.response.ChatRoom.ResultChatRoom;
import com.meetlive.app.response.CreatePaymentResponse;
import com.meetlive.app.response.DeleteImageResponse;
import com.meetlive.app.response.DisplayGiftCount.GiftCountResult;
import com.meetlive.app.response.Employee.RequestIdForEmployee;
import com.meetlive.app.response.Employee.ResultEmployeeId;
import com.meetlive.app.response.FavNew.FavNewResponce;
import com.meetlive.app.response.FcmTokenResponse;
import com.meetlive.app.response.Friendship.RequestFriendShipStatus;
import com.meetlive.app.response.Friendship.ResultFriendShipStatus;
import com.meetlive.app.response.GetUserLevelResponse.GetLevelResponce;
import com.meetlive.app.response.LoginResponse;
import com.meetlive.app.response.LogoutResponce;
import com.meetlive.app.response.MessageCallData.MessageCallDataRequest;
import com.meetlive.app.response.MessageCallData.MessageCallDataResponce;
import com.meetlive.app.response.Misscall.RequestMissCall;
import com.meetlive.app.response.Misscall.ResultMissCall;
import com.meetlive.app.response.MyResponse;
import com.meetlive.app.response.NewWallet.WalletResponce;
import com.meetlive.app.response.OnCamResponse;
import com.meetlive.app.response.OnlineStatusResponse;
import com.meetlive.app.response.PaymentGatewayDetails.CashFree.CFToken.CfTokenResponce;
import com.meetlive.app.response.PaymentGatewayDetails.CashFree.CashFreePayment.CashFreePaymentRequest;
import com.meetlive.app.response.PaymentGatewayDetails.PaymentGatewayResponce;
import com.meetlive.app.response.PaymentSelector.PaymentSelectorResponce;
import com.meetlive.app.response.ProfileDetailsResponse;
import com.meetlive.app.response.Rating.RatingResponce;
import com.meetlive.app.response.RechargePlanResponse;
import com.meetlive.app.response.RemainingGiftCard.RemainingGiftCardResponce;
import com.meetlive.app.response.Report.ResultReportIssue;
import com.meetlive.app.response.ReportResponse;
import com.meetlive.app.response.RequestGiftRequest.RequestGiftRequest;
import com.meetlive.app.response.RequestGiftRequest.RequestGiftResponce;
import com.meetlive.app.response.Sender;
import com.meetlive.app.response.TopReceiver.TopReceiverResponse;
import com.meetlive.app.response.UpdateProfileResponse;
import com.meetlive.app.response.UserListResponse;
import com.meetlive.app.response.UserListResponseNew.UserListResponseNewData;
import com.meetlive.app.response.VoiceCall.VoiceCallResponce;
import com.meetlive.app.response.WalletBalResponse;
import com.meetlive.app.response.WalletRechargeResponse;
import com.meetlive.app.response.coin.ResultCoinPlan;
import com.meetlive.app.response.gift.ResultGift;
import com.meetlive.app.response.gift.SendGiftRequest;
import com.meetlive.app.response.gift.SendGiftResult;
import com.meetlive.app.response.message.RequestAllMessages;
import com.meetlive.app.response.message.RequestMessageRead;
import com.meetlive.app.response.message.ResultMessage;
import com.meetlive.app.response.message.ResultMessageRead;
import com.meetlive.app.response.message.ResultSendMessage;
import com.meetlive.app.response.order.RequestPlaceOrder;
import com.meetlive.app.response.order.ResultPlaceOrder;
import com.meetlive.app.response.videoplay.VideoPlayResponce;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface ApiInterface {
    @FormUrlEncoded
    @POST("loginlocal")
    Call<LoginResponse> loginUser(@Field("username") String username, @Field("password") String password, @Header("Appid") int Appid);

    @POST("logout")
    Call<LogoutResponce> logout(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid);

    @FormUrlEncoded
    @POST("registerlocal")
    Call<LoginResponse> guestRegister(@Field("login_type") String login_type,
                                      @Field("device_id") String device_id, @Header("Appid") int Appid);

    @FormUrlEncoded
    @POST("updateFCMToken")
    Call<FcmTokenResponse> registerFcmToken(@Header("Authorization") String token,
                                            @Header("Accept") String accept, @Header("Appid") int Appid, @Field("token") String fcmToken);


    @GET("userlist")
    Call<UserListResponse> getUserList(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid, @Query("q") String q,
                                       @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @GET("userlist")
    Call<UserListResponse> searchUser(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                      @Query("page") String p, @Query("per_page_records") String lim);

    @GET("top-receiver-giver")
    Call<TopReceiverResponse> getWinnerList(@Header("Authorization") String token, @Header("Accept") String accept,
                                            @Query("finding") String finding,@Query("interval") String interval);

    @GET("userListWithLatestCall")
    Call<UserListResponse> getUserListLastCall(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid, @Query("q") String q,
                                       @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    @FormUrlEncoded
    @POST("registerlocal")
    Call<LoginResponse> loginFbGoogle(@Field("name") String name, @Field("login_type") String login_type,
                                      @Field("username") String username, @Field("email") String email, @Header("Appid") int Appid);


    @GET("get-gift-count")
    Call<GiftCountResult> getGiftCountForHost(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid,
                                              @Query("id") String convId);
    @GET("getNearbyList")
    Call<UserListResponse> getNearbyList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                         @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid, @Header("Appid") int Appid);
    //new function for show rating female 6/5/21
    @GET("getprofiledata")
    Call<UserListResponseNewData> getRateCountForHost(@Header("Authorization") String token, @Header("Accept") String accept,
                                                      @Query("id") String host_profileId);

    @GET("report-user")
    Call<ReportResponse> reportUser(@Header("Authorization") String token, @Header("Accept") String accept,
                                    @Query("report_to") String id, @Query("is_user_block") String b,
                                    @Query("description") String des, @Query("input_description") String in_des);

    @GET("getPopularList")
    Call<UserListResponse> getPopulartList(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                           @Query("page") String p, @Query("per_page_records") String lim, @Query("language_id") String lanid);

    //use new api for view profile page 10/5/21
    @GET("getprofiledata")
    Call<UserListResponseNewData> getProfileData(@Header("Authorization") String token, @Header("Accept") String accept, @Query("q") String q,
                                                 @Query("page") String p, @Query("id") String id, @Query("language_id") String lanid);

    @POST("details")
    Call<ProfileDetailsResponse> getProfileDetails(@Header("Authorization") String token, @Header("Accept") String accept);


    @GET("add_remove_fav")
    Call<AddRemoveFavResponse> doFavourite(@Header("Authorization") String token, @Query("favorite_id") int favorite_id);

    @GET("favorites-new")
    Call<FavNewResponce> getFavListNew(@Header("Authorization") String token, @Header("Accept") String accept, @Query("page") String page);

    @GET("points")
    Call<WalletBalResponse> getWalletBalance(@Header("Authorization") String token, @Header("Accept") String accept);

    @GET("getApiKeysAndBalance")
    Call<PaymentGatewayResponce> getPaymentData(@Header("Authorization") String token,
                                                @Header("Accept") String accept);

    @POST("user-busy")
    Call<Object> sendCallRecord(@Header("Authorization") String token,
                                @Header("Accept") String accept,
                                @Body CallRecordBody callRecordBody);

    //new api for guest Edit image 29/5/21
    @Multipart
    @POST("update-guest-profile")
    Call<Object> upDateGuestProfile(@Header("Authorization") String token,
                                    @Header("Accept") String accept, @Header("Appid") int Appid,
                                    @Part("name") RequestBody name,
                                    @Part MultipartBody.Part profile_pic);


    @GET("getEmployeeVideoRandomly")
    Call<VideoPlayResponce> getVideoplay(@Header("Authorization") String token,
                                         @Header("Accept") String accept, @Header("Appid") int Appid,
                                         @Query("user_id") String convId);

    @GET("video-list")
    Call<OnCamResponse> getVideoList(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid);

    @POST("call-alert")
    Call<ResultMissCall> sendMissCallData(@Header("Appid") int Appid,
                                          @Body RequestMissCall requestMissCall
    );

    @GET("getGifts")
    Call<ResultGift> getGift(
            @Header("Authorization") String header1, @Header("Appid") int Appid
    );

    @Multipart
    @POST("send-message")
    Call<ResultSendMessage> sendMessageGift(
            @Header("Appid") int Appid,
            @Part("UserId") RequestBody UserId,
            @Part("conversationId") RequestBody conversationId,
            @Part("_id") RequestBody id,
            @Part("name_1") RequestBody name_1,
            @Part("senderProfilePic") RequestBody senderProfilePic,
            @Part("senderType") RequestBody senderType,
            @Part("receiverId") RequestBody receiverId,
            @Part("receiverName") RequestBody receiverName,
            @Part("receiverImageUrl") RequestBody receiverImageUrl,
            @Part("receiverType") RequestBody receiverType,
            @Part("body") RequestBody body,
            @Part("isFriendAccept") RequestBody isFriendAccept,
            @Part("gift_coins") RequestBody giftCoins,
            @Part("mimeType") RequestBody mimeType
    );

    @POST("ask_for_gift")
    Call<RequestGiftResponce> sendGiftRequestFromHost(
            @Header("Authorization") String header1, @Header("Appid") int Appid,
            @Body RequestGiftRequest requestGiftRequest
    );

    @POST("send_gift")
    Call<SendGiftResult> sendGift(
            @Header("Authorization") String header1, @Header("Appid") int Appid,
            @Body SendGiftRequest requestGift
    );


    @Multipart
    @POST("send-message")
    Call<ResultSendMessage> sendMessage(
            @Header("Appid") int Appid,
            @Part("UserId") RequestBody UserId,
            @Part("conversationId") RequestBody conversationId,
            @Part("_id") RequestBody id,
            @Part("name_1") RequestBody name_1,
            @Part("senderProfilePic") RequestBody senderProfilePic,
            @Part("senderType") RequestBody senderType,
            @Part("receiverId") RequestBody receiverId,
            @Part("receiverName") RequestBody receiverName,
            @Part("receiverImageUrl") RequestBody receiverImageUrl,
            @Part("receiverType") RequestBody receiverType,
            @Part("body") RequestBody body,
            @Part("isFriendAccept") RequestBody isFriendAccept,
            @Part("mimeType") RequestBody mimeType
    );

    @POST("getuserdetails")
    Call<MessageCallDataResponce> getMessageCallData(@Header("Authorization") String token,
                                                     @Header("Accept") String accept, @Header("Appid") int Appid,
                                                     @Body MessageCallDataRequest messageCallDataRequest);

    @FormUrlEncoded
    @POST("updateWalletAndCallToZero")
    Call<Object> callCutByHost(@Header("Authorization") String token,
                               @Header("Accept") String accept, @Header("Appid") int Appid,
                               @Field("unique_id") String unique_id);

    @GET("remaininggiftcard")
    Call<RemainingGiftCardResponce> getRemainingGiftCardResponce(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid);

    @GET("planlist")
    Call<RechargePlanResponse> getRechargeList(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid);

    @GET("getPaymentGatewayZeeplive")
    Call<PaymentSelectorResponce> getPaymentSelector(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid);

    @FormUrlEncoded
    @POST("createpaymentMeetlive")
    Call<CreatePaymentResponse> createPayment(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid, @Field("plan_id") int plan_id);

    @POST("cash-free-payment")
    Call<Object> cashFreePayment(
            @Header("Authorization") String token,
            @Header("Accept") String accept, @Header("Appid") int Appid,
            @Body CashFreePaymentRequest cashFreePaymentRequest);


    @FormUrlEncoded
    @POST("checkpayment")
    Call<ReportResponse> verifyPayment(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid,
                                       @Field("transaction_id") String transaction_id, @Field("order_id") String order_id);


    @FormUrlEncoded
    @POST("point_update_new")
    Call<WalletRechargeResponse> rechargeWallet(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid,
                                                @Field("razorpay_id") String id, @Field("plan_id") String planId, @Field("plan_type") String plan_type);

    @GET("getCashFreeToken")
    Call<CfTokenResponce> getCfToken(@Header("Authorization") String token,
                                     @Header("Accept") String accept, @Header("Appid") int Appid,
                                     @Query("amount") String amount,
                                     @Query("plan_id") String plan_id);

    @POST("create-chat-room")
    Call<ResultChatRoom> createChatRoom(
            @Header("Content-Type") String header,
            @Body RequestChatRoom requestChatRoom, @Header("Appid") int Appid
    );

    @POST("chat-validity")
    Call<ChatPurchaseValidity> isChatServicePurchased(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid);

    //chatActivity
    @POST("getEmployeeById")
    Call<ResultEmployeeId> getEmployeeDataByID(
            @Header("Auth-Token") String header1,
            @Header("Csrf-Token") String header2, @Header("Appid") int Appid,
            @Body RequestIdForEmployee requestIdForEmployee
    );

    @Multipart
    @POST("send-message")
    Call<ResultSendMessage> sendMessageFromSocket(
            @Header("Appid") int Appid,
            @Part("UserId") RequestBody UserId,
            @Part("conversationId") RequestBody conversationId,
            @Part("_id") RequestBody id,
            @Part("name_1") RequestBody name_1,
            @Part("senderProfilePic") RequestBody senderProfilePic,
            @Part("senderType") RequestBody senderType,
            @Part("receiverId") RequestBody receiverId,
            @Part("receiverName") RequestBody receiverName,
            @Part("receiverImageUrl") RequestBody receiverImageUrl,
            @Part("receiverType") RequestBody receiverType,
            @Part("body") RequestBody body,
            @Part("isFriendAccept") RequestBody isFriendAccept,
            @Part("mimeType") RequestBody mimeType,
            @Part("isBotMessage") RequestBody isBotMessage,
            @Part("from") RequestBody from,
            @Part("to") RequestBody to,
            @Part("action_name") RequestBody action_name,
            @Part("action_id") RequestBody action_id,
            @Part("is_bot_reply") RequestBody is_bot_reply,
            @Part("bot_action") RequestBody bot_action,
            @Part("bot_message_id") RequestBody botmessageid,
            @Part("reply_id") RequestBody reply_id
    );

    @PUT("update-read-messages")
    Call<ResultMessageRead> readMessagefunction(
            @Body RequestMessageRead requestMessageRead, @Header("Appid") int Appid
    );

    @POST("checkFriendShipStatus")
    Call<ResultFriendShipStatus> getFriendShipStatus(
            @Header("Auth-Token") String header1,
            @Header("Csrf-Token") String header2, @Header("Appid") int Appid,
            @Body RequestFriendShipStatus requestFriendShipStatus
    );

    @POST("getReportIssues")
    Call<ResultReportIssue> getReportIssues(
            @Header("Auth-Token") String header1,
            @Header("Csrf-Token") String header2, @Header("Appid") int Appid
    );

    @Multipart
    @POST("send-message")
    Call<ResultSendMessage> sendMessageAudio(
            @Header("Appid") int Appid,
            @Part("UserId") RequestBody UserId,
            @Part("conversationId") RequestBody conversationId,
            @Part("_id") RequestBody id,
            @Part("name_1") RequestBody name_1,
            @Part("senderProfilePic") RequestBody senderProfilePic,
            @Part("senderType") RequestBody senderType,
            @Part("receiverId") RequestBody receiverId,
            @Part("receiverName") RequestBody receiverName,
            @Part("receiverImageUrl") RequestBody receiverImageUrl,
            @Part("receiverType") RequestBody receiverType,
            @Part MultipartBody.Part files,
            @Part("isFriendAccept") RequestBody isFriendAccept,
            @Part("audio_duration") RequestBody audioDuration
    );

    @POST("getSubscriptionPlans")
    Call<ResultCoinPlan> getCoinPlan(
            @Header("Auth-Token") String header1,
            @Header("Csrf-Token") String header2, @Header("Appid") int Appid
    );

    @POST("placeOrder")
    Call<ResultPlaceOrder> placeOrder(
            @Header("Auth-Token") String header1,
            @Header("Csrf-Token") String header2, @Header("Appid") int Appid,
            @Body RequestPlaceOrder requestPlaceOrder
    );


    @POST("message-trails")
    Call<ResultMessage> getAllMessages(
            @Header("Appid") int Appid,
            @Body RequestAllMessages requestAllMessages
    );


    @GET("getToken")
    Call<AgoraTokenResponse> getAgoraToken(@Header("Authorization") String token,
                                           @Header("Accept") String accept, @Header("Appid") int Appid,
                                           @Query("connecting_user_id") int id,
                                           @Query("outgoing_time") String outgoingTime,
                                           @Query("convId") String convId);

    @GET("dialCall")
    Call<ResultCall> getDailCallRequest(@Header("Authorization") String token,
                                        @Header("Accept") String accept, @Header("Appid") int Appid,
                                        @Query("connecting_user_id") int id,
                                        @Query("outgoing_time") String outgoingTime,
                                        @Query("convId") String convId,
                                        @Query("call_rate") int callRate,
                                        @Query("is_free_call") boolean isFreeCall,
                                        @Query("rem_gift_cards") String remGiftCards);


    @GET("dialaudioCall")
    Call<VoiceCallResponce> dailVoiceCall(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid,
                                          @Query("audio_call_rate") String audio_call_rate, @Query("connecting_user_id") String connecting_user_id,
                                          @Query("outgoing_time") String outgoing_time, @Query("convId") String convId);


    @POST("audiocallStatus")
    Call<Object> sendCallRecordAudio(@Header("Authorization") String token,
                                     @Header("Accept") String accept, @Header("Appid") int Appid,
                                     @Body CallRecordBodyAudio callRecordBody);


    @GET("online-status-update")
    Call<OnlineStatusResponse> manageOnlineStatus(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid, @Query("is_online") int is_online);


    @Headers({"Content-Type:application/json", "Authorization:key=AAAAp8dcL_E:APA91bEuvdc3xgr2tWVas358OFTMck3kn53KSk0GWzDGXdhfRZhvVvYNDRnqSYfXfkXyQakB5rlSWFBP917Ln8ksHpIZriq-7govX0uYtyjYV4UqMagoQczefDni83cF068E2rWWKpqp"})
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender sender, @Header("Appid") int Appid);


    @Headers({"Accept: application/json"})
    @GET("delete-profile-image")
    Call<DeleteImageResponse> deleteProfileImage(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid, @Query("id") String id);

    @Headers({"Accept: application/json"})
    @GET("select-profile-pic")
    Call<DeleteImageResponse.Result> setProfileImage(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid, @Query("id") String id);

    @Multipart
    @POST("update-profile")
    Call<UpdateProfileResponse> updateProfileDetails(@Header("Authorization") String token,
                                                     @Header("Accept") String accept, @Header("Appid") int Appid,
                                                     @Part MultipartBody.Part picToUpload, @Part("is_album") boolean is_album);

    @FormUrlEncoded
    @POST("update-profile")
    Call<UpdateProfileResponse> updateProfileDetails(@Header("Authorization") String token,
                                                     @Header("Accept") String accept, @Header("Appid") int Appid,
                                                     @Field("name") String name,
                                                     @Field("city") String city,
                                                     @Field("dob") String dob,
                                                     @Field("about_user") String about_user);


    @GET("wallet-history-latest")
    Call<WalletResponce> getWalletHistory(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid);

    @FormUrlEncoded
    @POST("addrating")
    Call<RatingResponce> getUserRating(@Header("Authorization") String token,
                                       @Header("Accept") String accept, @Header("Appid") int Appid,
                                       @Field("host_id") String host_id,
                                       @Field("rate") String rate,
                                       @Field("tag") String tag);


    @GET("getLevels")
    Call<GetLevelResponce> getUserLevelHistory(@Header("Authorization") String token, @Header("Accept") String accept, @Header("Appid") int Appid);

    @GET("deleteUserAccount")
    Call<Object> getAccountDelete(@Header("Authorization") String token,
                                  @Header("Accept") String accept, @Header("Appid") int Appid);

    @POST("my-chat-list")
    Call<ResultChatList> getChatList(
            @QueryMap Map<String, String> options,
            @Body RequestChatList requestChatList
    );

}