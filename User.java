import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

public class User implements IUser, Observer, Subject {
    private String username;
    private String password;
    private List<Post> wall;
    private List<User> friends;
    private List<Group> groups;
    private List<Post> friendsPosts;
    private List<Observer> observers; // Gözlemcileri tutacak liste

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wall = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.friendsPosts = new ArrayList<>();
        this.observers = new ArrayList<>(); // Gözlemcileri başlat
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Post> getWall() {
        return wall;
    }

    public List<User> getFriends() {
        return friends;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Post> getFriendsPosts() {
        return friendsPosts;
    }

    public void post(String content) {
        Post newPost = new Post(this.username, content);
        wall.add(newPost);
        notifyObservers(newPost); // Gözlemcilere bildirim gönder
        for (User friend : friends) {
            friend.getFriendsPosts().add(newPost);
        }
    }

    public boolean isFriend(User user) {
        return friends.contains(user);
    }

    public void addFriend(User user) {
        if (!friends.contains(user)) {
            friends.add(user);
            addObserver(user); // Arkadaşı gözlemci olarak ekle
            user.addFriend(this); // Çift yönlü ekleme
        }
    }

    public void removeFriend(User user) {
        if (friends.contains(user)) {
            friends.remove(user);
            removeObserver(user); // Arkadaşı gözlemciden kaldır
            user.removeFriend(this); // Çift yönlü kaldırma
        }
    }

    public void addGroup(Group group) {
        if (!groups.contains(group)) {
            groups.add(group);
        }
    }

    public Group getGroupByName(String name) {
        for (Group group : groups) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }

    public String displayGroupMembers(String groupName) {
        Group group = getGroupByName(groupName);
        if (group != null) {
            return group.displayMembers();
        } else {
            return "Group not found.";
        }
    }

    @Override
    public void update(Post post) {
        friendsPosts.add(post);
        // Bildirim gönderme
        JOptionPane.showMessageDialog(null, "Yeni gönderi Eklendi!", "Bildirim", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Post post) {
        for (Observer observer : observers) {
            observer.update(post);
        }
    }

    public void loadFriend(User user) {
        if (!friends.contains(user)) {
            friends.add(user);
        }
    }

    // Arkadaşların birbirini gözlemci olarak eklemesi
    public void initializeObservers() {
        for (User friend : friends) {
            addObserver(friend);
        }
    }

    // Iterator pattern implementation
    public Iterator<Post> wallIterator() {
        return new PostIterator(wall);
    }

    public Iterator<Post> friendsPostsIterator() {
        return new PostIterator(friendsPosts);
    }

    // Inner class for Post iterator
    private static class PostIterator implements Iterator<Post> {
        private final List<Post> posts;
        private int index;

        public PostIterator(List<Post> posts) {
            this.posts = posts;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < posts.size();
        }

        @Override
        public Post next() {
            return posts.get(index++);
        }
    }
}
