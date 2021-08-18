package com.example.maddarochatmodule.network;


import com.example.maddarochatmodule.BuildConfig;

public class Endpoints {

    // Base
    public static final int APP_TYPE = 1;
    public static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String CHAT_BASE_URL = BuildConfig.CHAT_URL;


    //upload
    public static final String UPLOAD_URL = "api/v1/uploads/create";

    //Socket
    public static final String SOCKET = CHAT_BASE_URL.concat("chat");
    public static final String CHAT_ROOMS = CHAT_BASE_URL.concat("api/v1/chat-rooms");
    public static final String CHAT_UNREAD_MESSAGE = CHAT_BASE_URL.concat("api/v1/chat-rooms/unread-messages/all");
    public static final String INTERNAL_CHAT_ROOM = CHAT_BASE_URL.concat("api/v1/chat-rooms");
    public static final String INTERNAL_CHAT_ROOM_RESERVATION_REFERENCE = CHAT_BASE_URL.concat("api/v1/chat-rooms/get-by-ref");
    public static final String CHAT_ROOM_MESSAGES = CHAT_BASE_URL.concat("api/v1/chat-rooms/load-messages");

    //Auth
    public static final String LOGIN = "api/v1/user/login";
    public static final String LOGIN_SOCIAL = "api/v1/user/social-media-login";
    public static final String REGISTER = "api/v1/user/register";
    public static final String REGISTER_SOCIAL = "api/v1/user/social-media-register";
    public static final String ACTIVATION = "api/v1/user/activate";
    public static final String RESEND_CODE = "api/v1/user/resend-activation-code";
    public static final String FORGET_PASSWORD = "api/v1/user/password/email";
    public static final String ACTIVATE_FORGET_PASSWORD = "api/v1/user/password/code";
    public static final String RESET_PASSWORD = "api/v1/user/password/reset";
    public static final String CHANGE_PASSWORD = "api/v1/user/change-password";
    public static final String LOGOUT = "api/v1/user/logout";

    //Home
    public static final String HOME = "api/v1/patient-application/app/home";

    //Specializations
    public static final String ALL_SPECIALIZATIONS = "api/v1/patient/specialization";

    // Doctors
    public static final String SPECIALIZATION_DOCTORS = "api/v1/patient/services/actions/specialization";
    public static final String SINGLE_DOCTOR = "api/v1/patient-application/app/provider";
    public static final String PROVIDER_LIST = "api/v1/patient-application/app/get-type-providers";
    public static final String PROVIDER_FILTRATION = "api/v1/patient-application/app/providers/action/filtration";

    //Profile
    public static final String UPDATE_PROFILE = "api/v1/user/update-profile";
    public static final String GET_PROFILE = "api/v1/user/details";
    public static final String REQUEST_UPDATE_PHONE = "api/v1/user/request-update-phone";
    public static final String UPDATE_PHONE = "api/v1/user/update-phone";

    //Notifications
    public static final String NOTIFICATION_TOKEN = "api/v1/user/update-device-token";
    public static final String NOTIFICATIONS = "api/v1/app/notifications";
    public static final String NOTIFICATION_COUNTER = "api/v1/app/notifications/actions/count";

    //Service
    public static final String INTERNAL_SERVICE = "/api/v1/patient/services";
    public static final String SERVICES = "/api/v1/patient/services/actions/service-category";
    public static final String SERVICE_FILTRATION = "/api/v1/patient/services/actions/filtration";
    public static final String BOOKING = "/api/v1/user/reservation";
    public static final String BOOK_NURSE = "/api/v1/user/create-reservation-request";
    public static final String SERVICE_FEES = "api/v1/provider/services/action/get-service-fees";
    public static final String FAVOURITE = "api/v1/user/app/favourite";
    public static final String RESCHEDULE = "api/v1/user/reservation/actions/reschedule";

    //Blood Bank
    public static final String BLOOD_BANK = "/api/v1/app/blood-bank";

    //Offers
    public static final String ALL_OFFERS = "/api/v1/patient-application/app/offers";

    //Reviews
    public static final String PROVIDER_REVIEWS = "/api/v1/app/provider-reviews";
    public static final String SERVICE_REVIEWS = "/api/v1/app/service-reviews";
    public static final String NON_REVIEWED_RESERVATIONS = "/api/v1/user/unrated-reservations";
    public static final String SCORE_REVIEW = "/api/v1/app/reviews";

    //Settings
    public static final String FAQ = "api/v1/app/user/faq";
    public static final String CONTACT_US = "api/v1/contact_us/save";
    public static final String SETTINGS = "api/v1/configuration/settings";
    public static final String CONFIG = "api/v1/configuration";
    public static final String LANG = "api/v1/user/language";

    //Reservation
    public static final String RESERVATIONS = "api/v1/user/reservation";
    public static final String CONFIRM_RESERVATION = "api/v1/user/confirm-reservation";
    public static final String UPDATE_RESERVATION = "api/v1/user/reservation";
    public static final String CANCEL_BROADCAST = "api/v1/user/reservation-requests";

    //Files
    public static final String FILES = "api/v1/user/user-documents";

    //Payment
    public static final String WALLET = "api/v1/user/wallet";
    public static final String TRANSACTION = "api/v1/user/transactions";
    public static final String TRANSFER = "api/v1/user/exchange-points";
    public static final String REQUEST_PAYMENT = "api/v1/user/reservation/actions/pay";

    //Blogs
    public static final String BLOGS = "api/v1/blog";
    public static final String BLOG_SPECIALIZATIONS = "api/v1/blog/actions/specializations";


}
