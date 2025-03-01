public interface IUser {
	void post(String content);
	boolean isFriend(User user);
	public void removeFriend(User user);
	public void addFriend(User user);
}
