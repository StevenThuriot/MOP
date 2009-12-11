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
	 /**
	  * Statische methode om een FocusWork aan te maken
	  * @param type Het type FocusWork. Dit is een enumeratie v/h type FocusType
	  * @param user De user van wie de tasks worden meegegevn
	  * @param var1 Aantal tasks bij een DeadlineFocus. Minimum duration bij een DurationFocus
	  * @param var2 Maximum duration bij een DurationFocus. Wordt genegeerd bij een DeadlineFocus
	  * @return
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
