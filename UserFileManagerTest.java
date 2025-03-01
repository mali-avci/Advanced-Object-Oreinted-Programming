import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class UserFileManagerTest {

    private static final String TEST_FILE_PATH = "D:\\Eclipse\\OOPProjectMain\\OOPProjectDemo1\\src\\test_users.txt";
    private UserFileManager userFileManager;

    @Before
    public void setUp() {
        userFileManager = new UserFileManager(TEST_FILE_PATH);
    }

    @After
    public void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSaveUser() {
        User user = new User("testUser", "testPassword");
        userFileManager.saveUser(user);

        assertTrue(userFileManager.isUserInFile("testUser", "testPassword"));
    }

    @Test
    public void testReadUsers() {
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");

        userFileManager.saveUser(user1);
        userFileManager.saveUser(user2);

        Map<String, User> users = userFileManager.readUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.containsKey("user1"));
        assertTrue(users.containsKey("user2"));
    }

    @Test
    public void testIsUserInFile() {
        User user = new User("testUser", "testPassword");
        userFileManager.saveUser(user);

        assertTrue(userFileManager.isUserInFile("testUser", "testPassword"));
        assertFalse(userFileManager.isUserInFile("nonExistentUser", "testPassword"));
        assertFalse(userFileManager.isUserInFile("testUser", "wrongPassword"));
    }
}
