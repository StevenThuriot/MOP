package model.focus;
import exception.ArrayLengthException;
import model.User;

public class FocusFactory {
		/**
		 * Shows all the tasks according to a certain FocusType strategy.
		  * @param type A type of FocusWork (enum)
		  * @param user The user who's asking for his or her tasks
		  * @param settings 
		  * 	When selecting a DeadlineFocus, this array contains one item which represents the maximum amount of items returned.
		  * 	When selecting a DurationFocus, this array contains two items. The first one represents the minimum duration. The second item represents the maximum duration.
		  * 	The default strategy does not require any items.
		 * @throws ArrayLengthException 
		 */
	public static FocusWork createFocus(FocusType type, User user, Object[] settings) throws ArrayLengthException
	{
		switch(type) {
    		case DeadlineFocus:
   				return new FocusWork(user,new DeadlineFocus(settings));
            case DurationFocus:
           		return new FocusWork(user, new DurationFocus(settings));
            case TaskTypeFocus:
            	return new FocusWork(user, new TaskTypeFocus(settings));
            default:
                return new FocusWork(user, new FocusStrategy());
		}
	}
}
