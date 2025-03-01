import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private List<User> members;

    public Group(String name, User creator) {
        this.name = name;
        this.members = new ArrayList<>();
        addMember(creator); // Grubu oluşturan kullanıcıyı otomatik olarak üyelere ekle
    }
    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public void removeMember(User user) {
        members.remove(user);
    }
    public String displayMembers() {
        StringBuilder membersList = new StringBuilder("Members of " + name + ":\n");
        for (User member : members) {
            membersList.append(member.getUsername()).append("\n");
        }
        return membersList.toString();
    }
    
}
