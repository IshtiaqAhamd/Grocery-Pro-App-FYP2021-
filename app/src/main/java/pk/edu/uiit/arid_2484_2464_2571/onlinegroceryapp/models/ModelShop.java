package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.models;

public class ModelShop {
    String uid,Email_Address, Full_Name, Shop_Name, Phone_Number, Deliver_Fee, Country_Name, State_Name, City_Name, Latitude, Longitude, Address, Confirm_Password, Account_Type, Online, Shop_Open, Profile_Image, Time_Stamp;

    public ModelShop() {
    }

    public ModelShop(String uid, String email_Address, String full_Name, String shop_Name, String phone_Number, String deliver_Fee, String country_Name, String state_Name, String city_Name, String latitude, String longitude, String address, String confirm_Password, String account_Type, String online, String shop_Open, String profile_Image, String time_Stamp) {
        this.uid = uid;
        Email_Address = email_Address;
        Full_Name = full_Name;
        Shop_Name = shop_Name;
        Phone_Number = phone_Number;
        Deliver_Fee = deliver_Fee;
        Country_Name = country_Name;
        State_Name = state_Name;
        City_Name = city_Name;
        Latitude = latitude;
        Longitude = longitude;
        Address = address;
        Confirm_Password = confirm_Password;
        Account_Type = account_Type;
        Online = online;
        Shop_Open = shop_Open;
        Profile_Image = profile_Image;
        Time_Stamp = time_Stamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail_Address() {
        return Email_Address;
    }

    public void setEmail_Address(String email_Address) {
        Email_Address = email_Address;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getShop_Name() {
        return Shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        Shop_Name = shop_Name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getDeliver_Fee() {
        return Deliver_Fee;
    }

    public void setDeliver_Fee(String deliver_Fee) {
        Deliver_Fee = deliver_Fee;
    }

    public String getCountry_Name() {
        return Country_Name;
    }

    public void setCountry_Name(String country_Name) {
        Country_Name = country_Name;
    }

    public String getState_Name() {
        return State_Name;
    }

    public void setState_Name(String state_Name) {
        State_Name = state_Name;
    }

    public String getCity_Name() {
        return City_Name;
    }

    public void setCity_Name(String city_Name) {
        City_Name = city_Name;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getConfirm_Password() {
        return Confirm_Password;
    }

    public void setConfirm_Password(String confirm_Password) {
        Confirm_Password = confirm_Password;
    }

    public String getAccount_Type() {
        return Account_Type;
    }

    public void setAccount_Type(String account_Type) {
        Account_Type = account_Type;
    }

    public String getOnline() {
        return Online;
    }

    public void setOnline(String online) {
        Online = online;
    }

    public String getShop_Open() {
        return Shop_Open;
    }

    public void setShop_Open(String shop_Open) {
        Shop_Open = shop_Open;
    }

    public String getProfile_Image() {
        return Profile_Image;
    }

    public void setProfile_Image(String profile_Image) {
        Profile_Image = profile_Image;
    }

    public String getTime_Stamp() {
        return Time_Stamp;
    }

    public void setTime_Stamp(String time_Stamp) {
        Time_Stamp = time_Stamp;
    }
}
