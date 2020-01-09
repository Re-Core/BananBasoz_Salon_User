package com.recore.bananbasozsalon.Common;

import android.text.format.DateFormat;

import com.recore.bananbasozsalon.Model.User;

import java.util.Calendar;
import java.util.Locale;

public class Common {

    public static final int PICK_IMAGE_REQUEST = 9999;
    public static final String ProductRef = "Product";
    public static final String CategorieRef = "Categories";
    public static final String ServiceRef = "Services";
    public static final String CommentsRef = "Comments";
    public static final String OrderRef = "Order";
    public static String UserInformation = "UserInformation";
    public static User currentUser;

    public static String timeStampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd \n hh:mm:ss a", calendar).toString();
        return date;
    }
}
