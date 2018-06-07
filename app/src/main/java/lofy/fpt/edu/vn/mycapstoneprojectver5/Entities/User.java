package lofy.fpt.edu.vn.mycapstoneprojectver5.Entities;

public class User {
    private String name;
    private String keyId;
    private String lati;
    private String longti;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
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

    public User(String name, String keyId, String lati, String longti) {

        this.name = name;
        this.keyId = keyId;
        this.lati = lati;
        this.longti = longti;
    }
}
