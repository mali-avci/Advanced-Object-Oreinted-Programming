import java.time.LocalDateTime;

public class Post {
    private String username;
    private String content;
    private LocalDateTime timestamp;

    public Post(String username, String content) {
        this.username = username;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
