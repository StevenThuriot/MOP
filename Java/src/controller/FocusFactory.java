package controller;
import exception.ArrayLengthException;
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
		  * @param settings 
		  * 	When selecting a DeadlineFocus, this array contains one item which represents the maximum amount of items returned.
		  * 	When selecting a DurationFocus, this array contains two items. The first one represents the minimum duration. The second item represents the maximum duration.
		  * 	The default strategy does not require any items.
		 * @throws ArrayLengthException 
		 */
	public static FocusWork createFocus(FocusType type, User user, int[] settings) throws ArrayLengthException
	{
		switch(type) {
    		case DeadlineFocus:
    			if (settings.length == 1)
    				return new FocusWork(user,new DeadlineFocus(settings[0]));
    			else
    				throw new ArrayLengthException();
            case DurationFocus:
            	if (settings.length == 2)
            		return new FocusWork(user, new DurationFocus(settings[0], settings[1]));
            default:
                return new FocusWork(user, new FocusStrategy());
		}
	}
}
