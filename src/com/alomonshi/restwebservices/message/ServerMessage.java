package com.alomonshi.restwebservices.message;

public class ServerMessage {
    public static final String INTERNALERRORMESSAGE = "خطا! لطفاً ورودی ها را چک نمایید.";
    public static final String SUCCESSMESSAGE = "عملیات با موفقیت صورت پذیرفت";
    public static final String FAULTMESSAGE = "خطا در انجام عملیات";
    public static final String INPUTCHECK = "لطفاً ورودی ها را چک نمایید.";
    public static final String ACCESSFAULT = "متأسفانه شما اجازه تغییر در این قسمت را ندارید.";

    //******Error in reserve time services******//
    public static final String RESERVETIMEERROR_01 = "متأسفانه در بازه زمانی مورد نظر وقت های رزرو شده وجود دارند، ابتدا زمان های مورد نظر را کنسل نمایید.";
    public static final String RESERVETIMEERROR_02 = "متأسفانه امکان رزرو وقت در این ساعت وجود ندارد.";
    public static final String RESERVETIMEERROR_03 = "متأسفانه زمان سروویس های انتخابی از زمان وقت قابل رزرو بیشتر است.";
    public static final String RESERVETIMEERROR_04 = "متأسفانه وقت مورد نظر در دسترس نمی باشد";
    public static final String RESERVETIMEERROR_05 = "در این روز نوبتی جهت رزرو وجود ندارد";
}
