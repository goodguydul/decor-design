package com.KevAndz.decordesign.controller;

public class URLS {
    private static final String URL_ROOT = "http://ddapi.ulasanproduk.com/api/Api.php?action=";
    public static final String URL_REGISTER = URL_ROOT + "signup";
    public static final String URL_LOGIN= URL_ROOT + "login";
    public static final String URL_SEARCH= URL_ROOT + "search";
    public static final String URL_CHATLIST= URL_ROOT + "retrieveMessage";
    public static final String URL_CHATDATA= URL_ROOT + "retrieveFullMessage";
    public static final String URL_SENDCHAT= URL_ROOT + "sendMessage";
    public static final String URL_UPDATE= URL_ROOT + "update";
    public static final String URL_FETCHFEED= URL_ROOT + "fetchFeed";
    public static final String URL_UPLOAD_PROFILE_PIC= "http://ddapi.ulasanproduk.com/api/upload.php";
}
