package lofy.fpt.edu.vn.lofy_version106.entities;

public class UserLocation {
    private String userId;
    private String lati;
    private String longti;

    public UserLocation() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongti() {
        return longti;
    }

    public void setLongti(String longti) {
        this.longti = longti;
    }

    public UserLocation(String userId, String lati, String longti) {

        this.userId = userId;
        this.lati = lati;
        this.longti = longti;
    }
}
