package lofy.fpt.edu.vn.lofy_version106.entities;

public class IsMember {
    private int id;
    private String content;
    private String userId;

    public IsMember() {
    }

    public IsMember(int id, String content, String userId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
