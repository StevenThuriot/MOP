package exception;

@SuppressWarnings("serial")
public class WrongUserForTaskTypeException extends Exception {
	public WrongUserForTaskTypeException()
	{
		super("The user can not create a task of this tasktype");
	}
}
