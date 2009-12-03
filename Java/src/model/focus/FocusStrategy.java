package model.focus;
import java.util.List;
import model.Task;
import model.User;

/**
 * Strategy class
 * @author bart
 *
 */
public abstract class FocusStrategy {
	private User currentUser;
	public abstract List<Task> showtasks();
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
}
