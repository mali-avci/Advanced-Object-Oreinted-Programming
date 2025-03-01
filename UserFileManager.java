import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserFileManager {
    private String filePath;

    public UserFileManager(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, User> readUsers() {
        Map<String, User> users = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String password = parts[1];
                    User registeredUser = UserFactory.createUser(username, password);
                    users.put(username, registeredUser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void saveUser(User user) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(user.getUsername() + "," + user.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserInFile(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
