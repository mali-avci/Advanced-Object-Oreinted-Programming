
public abstract class ProfileDecorator extends User {
    protected User decoratedUser;

    public ProfileDecorator(User user) {
        super(user.getUsername(), user.getPassword());
        this.decoratedUser = user;
    }


    public abstract boolean isCanBeSearched();
}
