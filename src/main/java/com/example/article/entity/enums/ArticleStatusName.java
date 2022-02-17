package com.example.article.entity.enums;

public enum ArticleStatusName {
    /**-------------------ADMIN TOMONIDAN MAQOLALARGA BERILADIGAN STATUSLAR------------*/
            START,                              //ADMIN TOMONIDAN TASDIQLANGAN
            BEGIN_CHECK,                        //TEKSHIRISH JARAYONIDA
            PREPARING_FOR_PUBLICATION,          //REDACTORGA biriktirilgan  nashirga tayorlanmoqda
            PREPARED_FOR_PUBLICATION,           //NASHRGA TAYYOR
            PUBLISHED,                          //NASHR ETILGAN
            REJECTED,                           //CHOP ETISHGA NOLOYIQ
            RECYCLE,                            //MAQOLANI QAYTA ISHLOVGA BERISH

    /**-----------REDACTOR VA REVIEWERLAR TOMONIDAN BERILADIGAN STATUSLAR--------*/
    I_ACCEPTED,                                 // maqolanin tekshirishga qabul qildim
    I_DID_NOT_ACCEPT,                           // maqolanin tekshirishni rad etim
    CHECK_AND_ACCEPT,                           // maqolani qabul qildim                 rewivirlar tomondan beriladigon statuslar
    CHECK_AND_CANCEL,                           // maqolani qabul qilmadim               rewivirlar tomondan beriladigon statuslar
    CHECK_AND_RECYCLE,                          // maqolani qayta ishlashga berdim       rewivirlar tomondan beriladigon statuslar

    /*********-------------------------------------*******************/
    NULL,
    NONE,
    CONFIRM,
    UN_CONFIRM
}