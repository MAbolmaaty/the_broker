package com.emupapps.the_broker.utils.web_service;

import com.emupapps.the_broker.models.ProfileModelResponse;
import com.emupapps.the_broker.models.RealEstate;
import com.emupapps.the_broker.models.account_bank.BankAccountModelResponse;
import com.emupapps.the_broker.models.auction_bid.request.AuctionBidModelRequest;
import com.emupapps.the_broker.models.auction_bid.response.AuctionBidModelResponse;
import com.emupapps.the_broker.models.auction_join.AuctionJoinModelResponse;
import com.emupapps.the_broker.models.contact_us.request.ContactUsModelRequest;
import com.emupapps.the_broker.models.contact_us.response.ContactUsModelResponse;
import com.emupapps.the_broker.models.districts.DistrictsModelResponse;
import com.emupapps.the_broker.models.document_add.AddDocumentModelResponse;
import com.emupapps.the_broker.models.document_delete.DeleteDocumentModelResponse;
import com.emupapps.the_broker.models.email_confirm.request.ConfirmEmailModelRequest;
import com.emupapps.the_broker.models.email_confirm.response.ConfirmEmailModelResponse;
import com.emupapps.the_broker.models.fav.request.FavModelRequest;
import com.emupapps.the_broker.models.fav.response.FavModelResponse;
import com.emupapps.the_broker.models.favorites.FavoritesModelResponse;
import com.emupapps.the_broker.models.info_update.UpdateInfoModelResponse;
import com.emupapps.the_broker.models.info_user.UserInfoModelResponse;
import com.emupapps.the_broker.models.login.request.LoginModelRequest;
import com.emupapps.the_broker.models.login.response.LoginModelResponse;
import com.emupapps.the_broker.models.notifications.NotificationsModelResponse;
import com.emupapps.the_broker.models.owner_contact.request.ContactOwnerModelRequest;
import com.emupapps.the_broker.models.owner_contact.response.ContactOwnerModelResponse;
import com.emupapps.the_broker.models.request_ownership.request.RequestOwnershipModelRequest;
import com.emupapps.the_broker.models.request_ownership.response.RequestOwnershipModelResponse;
import com.emupapps.the_broker.models.password_forget.request.ForgetPasswordModelRequest;
import com.emupapps.the_broker.models.password_forget.response.ForgetPasswordModelResponse;
import com.emupapps.the_broker.models.password_reset.request.ResetPasswordModelRequest;
import com.emupapps.the_broker.models.password_reset.response.ResetPasswordModelResponse;
import com.emupapps.the_broker.models.payment_card_add.request.PaymentCardAddModelRequest;
import com.emupapps.the_broker.models.payment_card_add.response.PaymentCardAddModelResponse;
import com.emupapps.the_broker.models.payment_card_default.PaymentCardDefaultModelResponse;
import com.emupapps.the_broker.models.payment_card_delete.PaymentCardDeleteModelResponse;
import com.emupapps.the_broker.models.payment_cards.PaymentCardsModelResponse;
import com.emupapps.the_broker.models.privacy_policy.PrivacyPolicyModelResponse;
import com.emupapps.the_broker.models.real_estate.RealEstateModelResponse;
import com.emupapps.the_broker.models.real_estate_categories.RealEstateCategoriesModelResponse;
import com.emupapps.the_broker.models.real_estate_requests_user.RealEstateRequestsUserModelResponse;
import com.emupapps.the_broker.models.real_estate_statuses.RealEstateStatusesModelResponse;
import com.emupapps.the_broker.models.real_estates_owned.RealEstatesOwnedModelResponse;
import com.emupapps.the_broker.models.real_estates_rented.RentedRealEstatesModelResponse;
import com.emupapps.the_broker.models.regions.RegionsModelResponse;
import com.emupapps.the_broker.models.register.AuthenticationModelResponse;
import com.emupapps.the_broker.models.report.request.ReportModelRequest;
import com.emupapps.the_broker.models.report.response.ReportModelResponse;
import com.emupapps.the_broker.models.request_maintenance.request.RequestMaintenanceModelRequest;
import com.emupapps.the_broker.models.request_maintenance.response.RequestMaintenanceModelResponse;
import com.emupapps.the_broker.models.request_modify.request.ModifyRequestModelRequest;
import com.emupapps.the_broker.models.request_modify.response.ModifyRequestModelResponse;
import com.emupapps.the_broker.models.request_rent.request.RequestRentModelRequest;
import com.emupapps.the_broker.models.request_rent.response.RequestRentModelResponse;
import com.emupapps.the_broker.models.request_submitted.RequestSubmittedModelResponse;
import com.emupapps.the_broker.models.request_termination.request.RequestTerminationModelRequest;
import com.emupapps.the_broker.models.request_termination.response.RequestTerminationModelResponse;
import com.emupapps.the_broker.models.requests_user.UserRequestsModelResponse;
import com.emupapps.the_broker.models.search.request.SearchModelRequest;
import com.emupapps.the_broker.models.search.response.SearchModelResponse;
import com.emupapps.the_broker.models.slides.SlidesModelResponse;
import com.emupapps.the_broker.models.unfavorite.request.UnFavoriteModelRequest;
import com.emupapps.the_broker.models.unfavorite.response.UnFavoriteModelResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface TheBrokerApi {

    @Multipart
    @POST("auth/local/register")
    Call<AuthenticationModelResponse> register(@Part("data") RequestBody data,
                                               @Part MultipartBody.Part profilePicture);

    @FormUrlEncoded
    @POST("auth/local")
    Call<AuthenticationModelResponse> login(@Field("identifier") String identifier,
                                   @Field("password") String password);

    @GET("users/me")
    Call<ProfileModelResponse> profile(@Header("Authorization") String authorization);

    @GET("api/GetServices")
    Call<SlidesModelResponse> getSlides();

    @GET("real-estates")
    Call<List<RealEstate>> getRealEstates();

    @FormUrlEncoded
    @POST("api/ServiceAll")
    Call<RealEstateStatusesModelResponse> getStatuses(@Field("lang") String locale);

    @FormUrlEncoded
    @POST("api/CategoryAll")
    Call<RealEstateCategoriesModelResponse> getCategories(@Field("lang") String locale);

    @FormUrlEncoded
    @POST("api/AreaAll")
    Call<RegionsModelResponse> getRegions(@Field("lang") String locale);

    @FormUrlEncoded
    @POST("api/Neighborhood")
    Call<DistrictsModelResponse> getDistricts(@Field("lang") String locale);

    @POST("api/AdvancedSearch")
    Call<SearchModelResponse> search(@Body SearchModelRequest body);

    @FormUrlEncoded
    @POST("api/GetOneAkarDetails")
    Call<RealEstateModelResponse> getRealEstate(@Field("akar_id") String realEstateId);

    @POST("api/Fav")
    Call<FavModelResponse> favorite(@Body FavModelRequest body);

    @POST("api/NotFav")
    Call<UnFavoriteModelResponse> unFavorite(@Body UnFavoriteModelRequest body);

    @FormUrlEncoded
    @POST("api/getFav")
    Call<FavoritesModelResponse> getFavorites(@Field("user_id") String userId);

    @POST("api/MakeComplaint")
    Call<ReportModelResponse> report(@Body ReportModelRequest body);

    @POST("api/OwnerCommunication")
    Call<ContactOwnerModelResponse> contactOwner(@Body ContactOwnerModelRequest body);

    @FormUrlEncoded
    @POST("api/GetUserOrder")
    Call<UserRequestsModelResponse> getUserRequests(@Field("user_id") String userId);

    @POST("api/OwnerOrder")
    Call<RequestOwnershipModelResponse> requestOwnership(@Body RequestOwnershipModelRequest body);

    @FormUrlEncoded
    @POST("api/GetOneOrder")
    Call<RequestSubmittedModelResponse> getSubmittedRequest(@Field("order_id") String requestId);

    @POST("api/EditOrder")
    Call<ModifyRequestModelResponse> modifyRequest(@Body ModifyRequestModelRequest body);

    @POST("api/RentOrder")
    Call<RequestRentModelResponse> requestRent(@Body RequestRentModelRequest body);

    @POST("api/AuctionOffer")
    Call<AuctionBidModelResponse> bet(@Body AuctionBidModelRequest body);

    @POST("api/Rules")
    Call<PrivacyPolicyModelResponse> getPrivacyPolicies();

    @POST("api/EmailCode")
    Call<ConfirmEmailModelResponse> confirmEmail(@Body ConfirmEmailModelRequest body);

    @POST("api/ForgetPassword")
    Call<ForgetPasswordModelResponse> forgetPassword(@Body ForgetPasswordModelRequest body);

    @POST("api/RestPassword")
    Call<ResetPasswordModelResponse> resetPassword(@Body ResetPasswordModelRequest body);

    @POST("api/ConnectWithUs")
    Call<ContactUsModelResponse> contactUs(@Body ContactUsModelRequest body);

    @FormUrlEncoded
    @POST("api/OwnerAkar")
    Call<RealEstatesOwnedModelResponse> getRealEstatesOwned(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("api/RentAkar")
    Call<RentedRealEstatesModelResponse> getRentedRealEstates(@Field("user_id") String userId);

    @Multipart
    @POST("api/UpdateInformatio")
    Call<UpdateInfoModelResponse> updateInfo(@Part("user_id") RequestBody userId,
                                             @Part("name") RequestBody username,
                                             @Part("email") RequestBody email,
                                             @Part("phone") RequestBody phoneNumber,
                                             @Part("code") RequestBody phoneCode,
                                             @Part("birthday") RequestBody birthday,
                                             @Part("area") RequestBody address,
                                             @Part("lang") RequestBody locale,
                                             @Part MultipartBody.Part profilePhoto);

    @Multipart
    @POST("api/UserDoc")
    Call<AddDocumentModelResponse> addDocumentation(@Part MultipartBody.Part document,
                                                    @Part("user_id") RequestBody userId);

    @FormUrlEncoded
    @POST("api/DeleteDoc")
    Call<DeleteDocumentModelResponse> deleteDocument(@Field("doc_id") String document);

    @Streaming
    @GET()
    Call<ResponseBody> download(@Url String url);

    @FormUrlEncoded
    @POST("api/OfferInsurance")
    Call<BankAccountModelResponse> getBankAccount(@Field("akar_id") String realEstateId);

    @Multipart
    @POST("api/UploadBankOffer")
    Call<AuctionJoinModelResponse> joinAuction(@Part MultipartBody.Part file,
                                               @Part("user_id") RequestBody userId,
                                               @Part("akar_id") RequestBody realEstateId);

    @POST("api/PaymentCard")
    Call<PaymentCardAddModelResponse> addPaymentCard(@Body PaymentCardAddModelRequest body);

    @FormUrlEncoded
    @POST("api/GetPaymentCard")
    Call<PaymentCardsModelResponse> paymentCards(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("api/DeletedPaymentCard")
    Call<PaymentCardDeleteModelResponse> deletePaymentCard(@Field("doc_id") String cardId);

    @FormUrlEncoded
    @POST("api/PaymentCardMain")
    Call<PaymentCardDefaultModelResponse> defaultPaymentCard(@Field("id") String cardId);

    @POST("api/EndTerminationOrder")
    Call<RequestTerminationModelResponse> terminateContract(@Body RequestTerminationModelRequest body);

    @POST("api/PrepareOrder")
    Call<RequestMaintenanceModelResponse> maintenance(@Body RequestMaintenanceModelRequest body);

    @FormUrlEncoded
    @POST("api/OrdersAkar")
    Call<RealEstateRequestsUserModelResponse> realEstateRequests(@Field("akar_id") String realEstateId);

    @FormUrlEncoded
    @POST("api/GetNotification")
    Call<NotificationsModelResponse> getNotifications(@Field("user_id") String userId);
}
