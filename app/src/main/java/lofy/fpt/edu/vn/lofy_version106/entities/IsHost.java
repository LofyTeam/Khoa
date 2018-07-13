package lofy.fpt.edu.vn.lofy_version106.entities;

import java.util.ArrayList;

public class IsHost {
    private int radius;
    private String startLocation;
    private String endLocation;
    private ArrayList<String> additionPosition;
    private String message;

    public IsHost() {
    }

    public IsHost(int radius, String startLocation, String endLocation, ArrayList<String> additionPosition, String message) {
        this.radius = radius;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.additionPosition = additionPosition;
        this.message = message;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public ArrayList<String> getAdditionPosition() {
        return additionPosition;
    }

    public void setAdditionPosition(ArrayList<String> additionPosition) {
        this.additionPosition = additionPosition;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
