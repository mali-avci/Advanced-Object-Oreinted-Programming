import java.util.ArrayList;
import java.util.List;

public class Wall {
    private List<Post> posts;

    public Wall() {
        this.posts = new ArrayList<>();
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public List<Post> getPosts() {
        return posts;
    }
}
