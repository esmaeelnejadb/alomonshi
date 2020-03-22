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
    public static final String RESERVETIMEERROR_06 = "در این بازه نوبت های رزرو وجود ندارد";

    //******Error in unit services******//
    public static final String UNITERROR_01 = "متأسفانه در این مجموعه واحدی تعریف نشده است.";
    public static final String UNITERROR_02 = "متأسفانه در واحد مورد نظر نوبت های رزرو شده وجود دارد، ابتدا این نوبت ها را لغو نمایید. ";

    //******Error in service services******//
    public static final String SERVICEERROR_01 = "متأسفانه در این واحد خدماتی تعریف نشده است.";
    public static final String SERVICEERROR_02 = "متأسفانه در خدمت مورد نظر نوبت های رزرو شده وجود دارد، ابتدا این نوبت ها را لغو نمایید. ";

    //******Error in comment services******//
    public static final String COMMENTERROR_01 = "متأسفانه در این واحد نظری ثبت نشده است.";

    //******Error in admin management services******//
    public static final String ADMINERROR_01 = "متأسفانه در این مجموعه مدیری ثبت نشده است.";
    public static final String ADMINERROR_02 = "متأسفانه این شماره تماس ثبت نشده است. دارنده شماره باید ابتدا در وب سایت ثبت نام نماید.";
    public static final String ADMINERROR_03 = "ادمین انتخابی، قبلاً به عنوان ادمین این مجموعه تعریف نشده است.";
    public static final String ADMINERROR_04 = "فهرست واحد های انتخابی خالی است.";
    public static final String ADMINERROR_05 = "ادمین انتخابی، قبلاً به عنوان ادمین این مجموعه تعریف شده است.";

    //******Error in admin report services******//
    public static final String REPORTERROR_01 = "متأسفانه برای این واحد خدمتی تعریف نشده است.";
}
