package com.example.afl_monitoringandroidapp;

public class Globals {

    public static String serverURL = "https://api.aflmonitoring.com/";
    public static String userTypeURL = serverURL + "api/get-user/";
    public static String urlPost_user = serverURL + "rest-auth/login/";

    public static String map_Unassigned_Admin = serverURL + "api/locations/unassigned";
    public static String map_Assigned_Admin = serverURL + "api/locations/assigned";
    public static String map_Count_Admin = serverURL + "api/count-reports/";
    public static String url_Location_Admin = serverURL + "api/upload/locations/";
    public static String url_Bulk_Admin = serverURL + "api/upload/mail/";

    public static String pendingDatewiseList = serverURL + "api/locations/pending";
    public static String ongoingDatewiseList = serverURL + "api/locations/ongoing";
    public static String completedDatewiseList = serverURL + "api/locations/completed";

    public static String url_Radius = serverURL + "api/Radius/";

    public static String pendingList = serverURL + "api/locationsDatewise/pending";
    public static String smsPending = serverURL + "api/trigger/sms/pending";
    public static String ongoingList = serverURL + "api/locationsDatewise/ongoing";
    public static String completedList = serverURL + "api/locationsDatewise/completed";

    public static String User = serverURL + "api/user/";
    public static String report_ado = serverURL + "api/report-ado/";

    public static String districtUrl = serverURL + "api/district/";
    public static String usersListADO = serverURL + "api/users-list/ado/?search=";
    public static String admin = serverURL + "api/admin/";

    public static String usersList = serverURL + "api/users-list/dda/";
    public static String districtStat = serverURL + "api/countReportBtwDates/";


    //ADO user
    public static String map_Pending_ADO = serverURL + "api/locations/ado/pending";
    public static String adoPending = serverURL + "api/locations/ado/pending";
    public static String adoCompleted = serverURL + "api/locations/ado/completed";
    public static String adoPendingReport = serverURL + "api/report-ado/add/";
    public static String adoPendingReportImageUpload = serverURL + "api/upload/images/";

    //DDA user
    public static String map_Assigned_DDA = serverURL + "api/locations/dda/assigned";
    public static String map_Unassigned_DDA = serverURL + "api/locations/dda/unassigned";
    public static String assignedLocationsDDA = serverURL + "api/locations/dda/assigned";
    public static String unassignedLocationsDDA = serverURL + "api/locations/dda/unassigned";
    public static String ddaOngoing = serverURL + "api/locations/dda/ongoing";
    public static String ddaCompleted = serverURL + "api/locations/dda/completed";
    public static String assignADO = serverURL + "api/ado/";

    public static String villageNameFragment = serverURL + "api/villages-list/district/";
    public static String sendSelectedId = serverURL + "api/user/";

}