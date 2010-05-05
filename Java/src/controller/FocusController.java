package controller;

import java.util.List;

import exception.ArrayLengthException;
import model.Task;
import model.User;
import model.focus.FocusFactory;
import model.focus.FocusType;
import model.focus.FocusWork;

/**
 * Controller for the Focus Factory
 */
public class FocusController {
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
	public FocusWork createFocus(FocusType type, User user, int[] settings) throws ArrayLengthException
	{
		return FocusFactory.createFocus(type, user, settings);
	}
}
