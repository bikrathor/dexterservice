package com.ccec.dexterservice.managers;

import android.location.Location;

import com.ccec.dexterservice.entities.Requests;

public class AppData {
    //updated from profile
    public static String serviceType = "Car";

    public static String selectedLoc = "";
    public static Location selectedCordLoc = null;
    public static String currentStatus = "";

    public static int selectedItem = 0;
    public static int selectedTab = 0;

    public static boolean setOne = true;

    public static Object currentVeh = null;
    public static Object currentVehCust = null;

    public static String currentPath = "";
    public static String currentSelectedUser = "";
    public static String currentDate = "";

    public static String currentImagePath = "";

    public static Requests currentReq = null;

    public static int getSelectedItem() {
        return selectedItem;
    }

    public static void setSelectedItem(int selectedItem) {
        AppData.selectedItem = selectedItem;
    }
}
