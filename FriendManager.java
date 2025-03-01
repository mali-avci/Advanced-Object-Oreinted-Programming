import java.io.*;
import java.util.*;

public class FriendManager {
    private Map<String, Set<String>> friendsMap;
    private String filename;

    private static FriendManager instance;
    private static final Object lock = new Object();
    private static final String USER_FRIENDS_DELIMITER = ":";
    private static final String FRIENDS_DELIMITER = ",";

    private FriendManager(String filename) {
        this.filename = filename;
        friendsMap = new HashMap<>();
        loadFriendsFromFile();
    }

    public static FriendManager getInstance(String filename) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new FriendManager(filename);
                }
            }
        }
        return instance;
    }

    private void loadFriendsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(USER_FRIENDS_DELIMITER);
                if (parts.length == 2) {
                    String user = parts[0].trim();
                    String[] friends = parts[1].split(FRIENDS_DELIMITER);
                    for (String friend : friends) {
                        addFriend(user, friend.trim()); // Her bir arkadaşlık ilişkisi için addFriend metodunu çağır
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(String user, String friend) {
        friendsMap.computeIfAbsent(user, k -> new HashSet<>()).add(friend);
        friendsMap.computeIfAbsent(friend, k -> new HashSet<>()).add(user);
        saveFriendsToFile();

        
    }

    public void removeFriend(String user, String friend) {
        if (friendsMap.containsKey(user)) {
            friendsMap.get(user).remove(friend);
        }
        if (friendsMap.containsKey(friend)) {
            friendsMap.get(friend).remove(user);
        }
        saveFriendsToFile();
    }

    public Set<String> getFriends(String user) {
        return friendsMap.getOrDefault(user, new HashSet<>());
    }

    private void saveFriendsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Set<String>> entry : friendsMap.entrySet()) {
                bw.write(entry.getKey() + USER_FRIENDS_DELIMITER + String.join(FRIENDS_DELIMITER, entry.getValue()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFriend(String username, String friendUsername) {
        return friendsMap.containsKey(username) && friendsMap.get(username).contains(friendUsername);
    }
}
