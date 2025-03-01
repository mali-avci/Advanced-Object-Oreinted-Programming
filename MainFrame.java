import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class MainFrame extends JFrame {
    private Map<String, User> users;
    private User currentUser;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField registerUsernameField;
    private JTextField registerNameField;

    private JTextArea wallTextArea;
    private JTextField postTextField;
    private DefaultListModel<String> friendsListModel;
    private DefaultListModel<String> groupsListModel;
    private FriendManager friendManager;
    private UserFileManager userFileManager;
    public MainFrame() {
    	userFileManager = new UserFileManager("D:\\Eclipse\\OOPProjectMain\\OOPProjectDemo1\\src\\Users.txt");
        users = new HashMap<>();
        AddToMapRegisteredUsers();
        friendManager = FriendManager.getInstance("D:\\Eclipse\\OOPProjectMain\\OOPProjectDemo1\\src\\Friends.txt");
        setTitle("BBCM Social Network");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new CardLayout());

        JPanel loginPanel = createLoginPanel();
        JPanel mainPanel = createMainPanel();

        getContentPane().add(loginPanel, "Login");
        getContentPane().add(mainPanel, "Main");

        showLoginPanel();
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(102, 0, 153));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(102, 0, 153));
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        usernameLabel.setBackground(new Color(204, 204, 255));
        usernameLabel.setOpaque(true);
        usernameLabel.setBounds(25, 87, 328, 49);
        usernameField = new JTextField();
        usernameField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        usernameField.setBounds(363, 87, 382, 49);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(new Color(102, 51, 153));
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        passwordLabel.setOpaque(true);
        passwordLabel.setBackground(new Color(204, 204, 255));
        passwordLabel.setBounds(25, 165, 328, 49);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
        passwordField.setBounds(363, 165, 382, 49);

        JButton loginButton = new JButton("Login");
        loginButton.setForeground(new Color(255, 255, 255));
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        loginButton.setBackground(new Color(204, 204, 255));
        loginButton.setBounds(47, 256, 251, 68);
        JButton registerButton = new JButton("Register");
        registerButton.setForeground(new Color(255, 255, 255));
        registerButton.setBackground(new Color(204, 204, 255));
        registerButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        registerButton.setBounds(412, 256, 260, 68);
        loginPanel.setLayout(null);

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        
        JLabel welcomeMessage = new JLabel("BBCM Social Network");
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeMessage.setOpaque(true);
        welcomeMessage.setDisplayedMnemonic(KeyEvent.VK_CONVERT);
        welcomeMessage.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        welcomeMessage.setForeground(new Color(255, 255, 255));
        welcomeMessage.setFont(new Font("Tahoma", Font.PLAIN, 30));
        welcomeMessage.setBackground(new Color(204, 204, 255));
        welcomeMessage.setBounds(47, 10, 693, 49);
        loginPanel.add(welcomeMessage);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });

        return loginPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Left Panel (Friends and Groups)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        friendsListModel = new DefaultListModel<>();
        groupsListModel = new DefaultListModel<>();

        JList<String> friendsList = new JList<>(friendsListModel);
        JList<String> groupsList = new JList<>(groupsListModel);

        leftPanel.add(new JLabel("Friends"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(friendsList), BorderLayout.CENTER);

        JPanel groupsPanel = new JPanel(new BorderLayout());
        groupsPanel.add(new JLabel("Groups"), BorderLayout.NORTH);
        
        groupsPanel.add(new JScrollPane(groupsList), BorderLayout.CENTER);

        JPanel groupControlPanel = new JPanel(new GridLayout(2, 1));
        JButton addFriendToGroupButton = new JButton("Add Friend to Group");
        JButton removeFriendFromGroupButton = new JButton("Remove Friend from Group");
        JButton viewGroupButton = new JButton("View Group"); // New button added

        groupControlPanel.add(addFriendToGroupButton);
        groupControlPanel.add(removeFriendFromGroupButton);
        groupControlPanel.add(viewGroupButton); // Added to the panel

        groupsPanel.add(groupControlPanel, BorderLayout.SOUTH);

        leftPanel.add(groupsPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Center Panel (Wall)
        wallTextArea = new JTextArea();
        wallTextArea.setEditable(false);
        mainPanel.add(new JScrollPane(wallTextArea), BorderLayout.CENTER);

        // Bottom Panel (Post and Control)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        postTextField = new JTextField();
        JButton postButton = new JButton("Post");

        JPanel postPanel = new JPanel(new BorderLayout());
        postPanel.add(postTextField, BorderLayout.CENTER);
        postPanel.add(postButton, BorderLayout.EAST);

        JPanel controlPanel = new JPanel();
        JButton addFriendButton = new JButton("Add Friend");
        JButton removeFriendButton = new JButton("Remove Friend");
        JButton createGroupButton = new JButton("Create Group");
        JButton logoutButton = new JButton("Logout");

        controlPanel.add(addFriendButton);
        controlPanel.add(removeFriendButton);
        controlPanel.add(createGroupButton);
        controlPanel.add(logoutButton);

        bottomPanel.add(postPanel, BorderLayout.NORTH);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePost();
            }
        });

        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddFriend();
            }
        });

        removeFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveFriend();
            }
        });

        createGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreateGroup();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogout();
            }
        });

        addFriendToGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddFriendToGroup();
            }
        });

        removeFriendFromGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveFriendFromGroup();
            }
        });

        viewGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewGroup();
            }
        }); // Listener added for the new button

        return mainPanel;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            currentUser = users.get(username);
            showMainPanel();
        } else if (userFileManager.isUserInFile(username, password)) {
            currentUser = UserFactory.createUser(username, password);
            showMainPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegisterDialog() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(3, 2));

        JLabel registerUsernameLabel = new JLabel("Username:");
        registerUsernameField = new JTextField();
        JLabel registerNameLabel = new JLabel("Password:");
        registerNameField = new JTextField();
        registerPanel.add(registerUsernameLabel);
        registerPanel.add(registerUsernameField);
        registerPanel.add(registerNameLabel);
        registerPanel.add(registerNameField);

        int result = JOptionPane.showConfirmDialog(this, registerPanel, "Register", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            handleRegister();
        }
    }
    
    private void handleRegister() {
        String username = registerUsernameField.getText();
        String password = registerNameField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!users.containsKey(username)) {
            User newUser = UserFactory.createUser(username, password);
            users.put(username, newUser);
            userFileManager.saveUser(newUser);

            if (userFileManager.isUserInFile(username, password)) {
                JOptionPane.showMessageDialog(this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register the user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username already taken", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    private void AddToMapRegisteredUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\Eclipse\\OOPProjectMain\\OOPProjectDemo1\\src\\Users.txt"))) {
            String line;
            boolean isEmpty = true;
            while ((line = br.readLine()) != null) {
                isEmpty = false;
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String password = parts[1];
                    User registeredUser = UserFactory.createUser(username, password);
                    users.put(username, registeredUser);
                }
            }
            if (isEmpty) {
                JOptionPane.showMessageDialog(this, "No registered users found. Please register a new user.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user file", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handlePost() {
        String content = postTextField.getText();
        if (!content.isEmpty()) {
            currentUser.post(content);
            updateWall();
            postTextField.setText("");
        }
    }
    

    private void handleAddFriend() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String friendUsername = JOptionPane.showInputDialog(this, "Enter friend's username:");
        if (friendUsername != null && !friendUsername.isEmpty()) {
            if (users.containsKey(friendUsername)) {
                String currentUserUsername = currentUser.getUsername();
                if (currentUserUsername.equals(friendUsername)) {
                    JOptionPane.showMessageDialog(this, "You cannot add yourself as a friend", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                User friend = users.get(friendUsername);
                if (friendManager == null) {
                    friendManager = FriendManager.getInstance("D:\\Eclipse\\OOPProjectMain\\OOPProjectDemo1\\src\\Users.txt");
                }
                
                if (currentUser.isFriend(friend)) {
                    JOptionPane.showMessageDialog(this, "User is already your friend", "Error", JOptionPane.ERROR_MESSAGE);
                } 
                
                else {
                    currentUser.addFriend(friend);
                    friendManager.addFriend(currentUserUsername, friendUsername); // Yeni arkadaşlık ekleniyor
                    updateFriendsList();
                    JOptionPane.showMessageDialog(this, "Friend added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } }else {
                JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }


    private void handleRemoveFriend() {
        String friendUsername = JOptionPane.showInputDialog(this, "Enter friend's username to remove:");
        if (friendUsername != null && !friendUsername.isEmpty()) {
            if (users.containsKey(friendUsername)) {
                User friend = users.get(friendUsername);
                currentUser.removeFriend(friend);
                friendManager.removeFriend(currentUser.getUsername(), friendUsername); // Remove friendship from the file
                updateFriendsList();
                JOptionPane.showMessageDialog(this, "Friend removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void handleCreateGroup() {
        String groupName = JOptionPane.showInputDialog(this, "Enter group name:");
        if (groupName != null && !groupName.isEmpty()) {
            Group group = new Group(groupName, currentUser); // Grubu oluştururken currentUser'ı geçir
            currentUser.addGroup(group);
            groupsListModel.addElement(groupName);
            JOptionPane.showMessageDialog(this, "Group '" + groupName + "' created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleAddFriendToGroup() {
        String groupName = JOptionPane.showInputDialog(this, "Enter group name:");
        String friendUsername = JOptionPane.showInputDialog(this, "Enter friend's username to add to the group:");
        if (groupName != null && !groupName.isEmpty() && friendUsername != null && !friendUsername.isEmpty()) {
            Group group = currentUser.getGroupByName(groupName);
            if (group != null && users.containsKey(friendUsername)) {
                User friend = users.get(friendUsername);
                group.addMember(friend);
                friend.addGroup(group);  // Arkadaşın gruplarını güncelle

                JOptionPane.showMessageDialog(this, "Friend added to group successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Group or user not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemoveFriendFromGroup() {
        String groupName = JOptionPane.showInputDialog(this, "Enter group name:");
        String friendUsername = JOptionPane.showInputDialog(this, "Enter friend's username to remove from the group:");
        if (groupName != null && !groupName.isEmpty() && friendUsername != null && !friendUsername.isEmpty()) {
            Group group = currentUser.getGroupByName(groupName);
            if (group != null && users.containsKey(friendUsername)) {
                User friend = users.get(friendUsername);
                group.removeMember(friend);
                JOptionPane.showMessageDialog(this, "Friend removed from group successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Group or user not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleViewGroup() {
        String groupName = JOptionPane.showInputDialog(this, "Enter group name:");
        if (groupName != null && !groupName.isEmpty()) {
            Group group = currentUser.getGroupByName(groupName);
            if (group != null) {
                showGroupMembers(group);
            } else {
                JOptionPane.showMessageDialog(this, "Group not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showGroupMembers(Group group) {
        GroupAdapter groupAdapter = new GroupAdapter(group); // Group sınıfını adaptör ile sarın

        JFrame groupFrame = new JFrame("Group Members: " + group.getName());
        groupFrame.setSize(400, 300);
        groupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Grup üyelerinin görüntülendiği bölüm
        JPanel membersPanel = new JPanel(new GridLayout(group.getMembers().size(), 1));
        for (User member : group.getMembers()) {
            JLabel nameLabel = new JLabel(member.getUsername());
            membersPanel.add(nameLabel);
        }

        JScrollPane membersScrollPane = new JScrollPane(membersPanel);
        mainPanel.add(membersScrollPane, BorderLayout.CENTER);

        // Chat box bölümü
        JPanel chatPanel = new JPanel(new BorderLayout());
        JTextArea chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        JTextField chatTextField = new JTextField();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatTextField.getText();
                if (!message.isEmpty()) {
                    chatTextArea.append("You: " + message + "\n");
                    groupAdapter.sendMessage(message); // Mesaj gönderme
                    chatTextField.setText("");
                }
            }
        });
        chatPanel.add(chatTextField, BorderLayout.SOUTH);
        chatPanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(chatPanel, BorderLayout.SOUTH);

        groupFrame.getContentPane().add(mainPanel);
        groupFrame.setVisible(true);
    }

    /*
    private void updateGroupMembersList(Group group) {
        wallTextArea.setText("");
        for (User member : group.getMembers()) {
            wallTextArea.append(member.getUsername() + "\n");
        }
    } */

    private void updateWall() {
        wallTextArea.setText("");
        
        // Kullanıcının kendi postlarını ekleyin
        appendPosts(currentUser);
        
        // Arkadaşların postlarını ekleyin
        for (User friend : currentUser.getFriends()) {
            appendPosts(friend);
        }
    }
    //**ITERATOR**
    private void appendPosts(User user) {
        Iterator<Post> wallIterator = user.wallIterator();
        while (wallIterator.hasNext()) {
            Post post = wallIterator.next();
            wallTextArea.append(user.getUsername() + ": " + post.getTimestamp() + " - " + post.getContent() + "\n");
        }
    }
    //**ITERATOR**
    private void updateFriendsList() {
        friendsListModel.clear();
        Set<String> friends = friendManager.getFriends(currentUser.getUsername());
        for (String friend : friends) {
            friendsListModel.addElement(friend);
        }
    }


    private void showLoginPanel() {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "Login");
    }

    private void showMainPanel() {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "Main");
        updateWall();
        updateFriendsList();
    }

    private void handleLogout() {
        currentUser = null;
        showLoginPanel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}



