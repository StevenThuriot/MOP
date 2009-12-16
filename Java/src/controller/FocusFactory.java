package controller;
import model.User;
import model.focus.*;

public class FocusFactory {
	 public enum FocusType{
		 DurationFocus,
		 DeadlineFocus,
		 Default
	 }
		/**
		 * Shows all the tasks according to a certain FocusType strategy.
		  * @param type A type of FocusWork (enum)
		  * @param user The user who's asking for his or her tasks
		  * @param var1 When selecting DeadlineFocus, the max number, otherwise the min duration with a DurationFocus
		  * @param var2 Maximum duration when selecting a DurationFocus. Ignored when a DeadlineFocus
		 */
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
