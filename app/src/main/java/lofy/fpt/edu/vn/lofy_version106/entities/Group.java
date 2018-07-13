package lofy.fpt.edu.vn.lofy_version106.entities;

public class Group {
    private String groupId;
    private String groupName;
    private int numberMenmber;
    private String groupCode;
    private int numberOfDevice;
    private User user;

    public Group() {
    }

    public Group(String groupId, String groupName, int numberMenmber, String groupCode, int numberOfDevice, User user) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.numberMenmber = numberMenmber;
        this.groupCode = groupCode;
        this.numberOfDevice = numberOfDevice;
        this.user = user;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getNumberMenmber() {
        return numberMenmber;
    }

    public void setNumberMenmber(int numberMenmber) {
        this.numberMenmber = numberMenmber;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public int getNumberOfDevice() {
        return numberOfDevice;
    }

    public void setNumberOfDevice(int numberOfDevice) {
        this.numberOfDevice = numberOfDevice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
