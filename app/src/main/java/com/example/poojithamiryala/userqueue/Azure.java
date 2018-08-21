package com.example.poojithamiryala.userqueue;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.List;

/**
 * Created by poojitha miryala on 10-03-2018.
 */

public class Azure
{
    public static MobileServiceClient mClient;
    public static MobileServiceUser user;
    public static MobileServiceTable<Organization_details> mOrgan;
    public static MobileServiceTable<BookUser> mbook;
    public static MobileServiceTable<AdminQ> madmin;
    public static List<Organization_details> college;
    public static List<Organization_details> others;
    public static List<Organization_details> Hospital;
    public static List<Organization_details> Diag;
    public static String category="";
    public static String organname="";
    public static String location="";
    public static String service="";
    public static String branch="";
    public static String city="";
    public static String contact="";
    public static List<Organization_details> restu;
}
