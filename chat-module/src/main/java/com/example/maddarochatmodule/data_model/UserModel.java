package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("activation")
    @Expose
    private String activation;
    @SerializedName("activation_status")
    int activationStatus;
    @SerializedName("password_reset_code")
    @Expose
    private Object passwordResetCode;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("social_type")
    @Expose
    private String socialType;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("city_id")
    @Expose
    private Object cityId;
    @SerializedName("area_id")
    @Expose
    private Object areaId;
    @SerializedName("date_of_birth")
    @Expose
    private Object dateOfBirth;
    @SerializedName("blood_type")
    @Expose
    private String bloodType;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("has_password")
    @Expose
    private Boolean hasPassword;
    @SerializedName("image")
    @Expose
    private ImageModel image;
    @SerializedName("national_id_image")
    @Expose
    private ImageModel nationalIdImage;
//    @SerializedName("country")
//    @Expose
//    private CountryModel country;
//    @SerializedName("city")
//    @Expose
//    private CityModel city;
//    @SerializedName("area")
//    @Expose
//    private AreaModel area;
    @SerializedName("invitation_code")
    @Expose
    private String invitationCode;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        if (phone.contains("-"))
            return phone.split("-")[1];
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneCode() {
        return phone.split("-")[0];
    }

    public String getActivation() {
        return activation;
    }

    public Object getPasswordResetCode() {
        return passwordResetCode;
    }

    public Integer getType() {
        return type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getSocialType() {
        return socialType;
    }

    public String getSocialId() {
        return socialId;
    }

    public int getActivationStatus() {
        return activationStatus;
    }

//    public void setActivationStatus(@AccountStatus int activationStatus) {
//        this.activationStatus = activationStatus;
//    }

    public String getCountryId() {
        return countryId;
    }

    public Object getCityId() {
        return cityId;
    }

    public Object getAreaId() {
        return areaId;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public Integer getGender() {
        return gender;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Boolean getHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(Boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public ImageModel getImage() {
        return image;
    }

    public ImageModel getNationalIdImage() {
        return nationalIdImage;
    }

//    public CountryModel getCountry() {
//        return country;
//    }
//
//    public CityModel getCity() {
//        return city;
//    }
//
//    public AreaModel getArea() {
//        return area;
//    }

    public String getInvitationCode() {
        return invitationCode;
    }
}
