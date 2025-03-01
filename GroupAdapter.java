import java.util.ArrayList;
import java.util.List;

class GroupAdapter implements Chat {
    private Group group;
    private List<String> messages;

    public GroupAdapter(Group group) {
        this.group = group;
        this.messages = new ArrayList<>();
    }

    @Override
    public void sendMessage(String message) {
        messages.add(message);
        // İsteğe bağlı: Mesajları tüm üyelere gönderme işlemi
        for (User member : group.getMembers()) {
            // Mesaj gönderme mantığı
        }
    }

    @Override
    public void displayMessages() {
        for (String message : messages) {
            System.out.println(message);
        }
    }
}