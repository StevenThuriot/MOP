package controller;

import model.focus.DeadlineFocus;
import model.focus.DurationFocus;
import model.focus.FocusStrategy;
import model.focus.FocusWork;

public class FocusFactory {
	FocusFactory()
	{
		
	}
	 public enum FocusType{
		 DurationFocus,
		 DeadlineFocus
	 }
	public static FocusWork createFocus(FocusType type)
	{
		switch(type)
		{
		case DurationFocus:
			return new FocusWork(new DurationFocus(0, 0));
		case DeadlineFocus:
			return new FocusWork(new DeadlineFocus(0));
		default:
			return new FocusWork(new FocusStrategy());
		}
	}
}
