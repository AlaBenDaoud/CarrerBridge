package org.example.auth.models;

public class Reply {
    private int id;         // Unique ID for the reply
    private int postId;     // The ID of the post this reply belongs to
    private int userId;     // The ID of the user who created the reply
    private String content; // The content of the reply

    // Default constructor
    public Reply() {}

    // Parameterized constructor
    public Reply(int id, int postId, int userId, String content) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for postId
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    // Getter and Setter for userId
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getter and Setter for content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                '}';
    }
}