package lofy.fpt.edu.vn.lofy_version106.entities;

import android.media.Image;

public class Notification {
    private int id;
    private String message;
    private Image imageNoti;

    public Notification() {
    }

    public Notification(int id, String message, Image imageNoti) {
        this.id = id;
        this.message = message;
        this.imageNoti = imageNoti;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Image getImageNoti() {
        return imageNoti;
    }

    public void setImageNoti(Image imageNoti) {
        this.imageNoti = imageNoti;
    }
}
