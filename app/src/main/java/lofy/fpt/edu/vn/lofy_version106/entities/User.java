package lofy.fpt.edu.vn.lofy_version106.entities;

import android.graphics.Color;

import java.util.ArrayList;

public class User {
    private String userId;
    private String userName;
    private String nickName;
    private IsHost isHost;
    private String phoneNumber;
    private UserLocation userLocation;
    private ArrayList<String> markPlace;
    private IsMember isMember;
    private Notification notification;
    private int uColor;

    public User() {
    }

    public User(String userId, String userName, String nickName, IsHost isHost,
                String phoneNumber, UserLocation userLocation, ArrayList<String> markPlace,
                IsMember isMember, Notification notification, int uColor) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.isHost = isHost;
        this.phoneNumber = phoneNumber;
        this.userLocation = userLocation;
        this.markPlace = markPlace;
        this.isMember = isMember;
        this.notification = notification;
        this.uColor = uColor;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public IsHost getIsHost() {
        return isHost;
    }

    public void setIsHost(IsHost isHost) {
        this.isHost = isHost;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public ArrayList<String> getMarkPlace() {
        return markPlace;
    }

    public void setMarkPlace(ArrayList<String> markPlace) {
        this.markPlace = markPlace;
    }

    public IsMember getIsMember() {
        return isMember;
    }

    public void setIsMember(IsMember isMember) {
        this.isMember = isMember;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public int getuColor() {
        return uColor;
    }

    public void setuColor(int uColor) {
        this.uColor = uColor;
    }
}
