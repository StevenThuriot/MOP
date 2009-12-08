package controller;
import model.User;
import model.focus.*;

public class FocusFactory {
	FocusFactory()
	{
		
	}
	 public enum FocusType{
		 DurationFocus,
		 DeadlineFocus
	 }
	public static FocusWork createFocus(FocusType type, User user,int var1,int var2)
	{
		switch(type) {
    		case DeadlineFocus:
                return new FocusWork(user,new DeadlineFocus(var1));
            case DurationFocus:
                return new FocusWork(user, new DurationFocus(var1, var2));
            default:
                return new FocusWork(user, new FocusStrategy());
		}
		
	}
}
