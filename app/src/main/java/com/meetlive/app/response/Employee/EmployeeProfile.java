package com.meetlive.app.response.Employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meetlive.app.response.Country;
import com.meetlive.app.response.PrivatePhoto;
import com.meetlive.app.response.PublicPhoto;

import java.util.List;

public class EmployeeProfile {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("unique_key")
    @Expose
    private String uniqueKey;
    @SerializedName("verification_code")
    @Expose
    private String verificationCode;
    @SerializedName("reset_key")
    @Expose
    private String resetKey;
    @SerializedName("login_by")
    @Expose
    private String loginBy;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country_id")
    @Expose
    private int countryId;
    @SerializedName("profile_photo")
    @Expose
    private String profilePhoto;
    @SerializedName("sample_video")
    @Expose
    private String sampleVideo;
    @SerializedName("login_count")
    @Expose
    private int loginCount;
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("verified")
    @Expose
    private boolean verified;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("public_photos")
    @Expose
    private List<PublicPhoto> publicPhotos = null;
    @SerializedName("private_photos")
    @Expose
    private List<PrivatePhoto> privatePhotos = null;
    @SerializedName("employee_languages")
    @Expose
    private List<EmployeeLanguage> employeeLanguages = null;
    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("coin_per_minute")
    @Expose
    private int coinperminute;
    @SerializedName("unique_user_id")
    @Expose
    private String uniqueuserid;
    @SerializedName("online_status")
    @Expose
    private int onlineStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public String getLoginBy() {
        return loginBy;
    }

    public void setLoginBy(String loginBy) {
        this.loginBy = loginBy;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getSampleVideo() {
        return sampleVideo;
    }

    public void setSampleVideo(String sampleVideo) {
        this.sampleVideo = sampleVideo;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public List<PublicPhoto> getPublicPhotos() {
        return publicPhotos;
    }

    public void setPublicPhotos(List<PublicPhoto> publicPhotos) {
        this.publicPhotos = publicPhotos;
    }

    public List<PrivatePhoto> getPrivatePhotos() {
        return privatePhotos;
    }

    public void setPrivatePhotos(List<PrivatePhoto> privatePhotos) {
        this.privatePhotos = privatePhotos;
    }

    public List<EmployeeLanguage> getEmployeeLanguages() {
        return employeeLanguages;
    }

    public void setEmployeeLanguages(List<EmployeeLanguage> employeeLanguages) {
        this.employeeLanguages = employeeLanguages;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getCoinperminute() {
        return coinperminute;
    }

    public void setCoinperminute(int coinperminute) {
        this.coinperminute = coinperminute;
    }

    public String getUniqueuserid() {
        return uniqueuserid;
    }

    public void setUniqueuserid(String uniqueuserid) {
        this.uniqueuserid = uniqueuserid;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
