package exception;

@SuppressWarnings("serial")
public class NoReservationOverlapException extends Exception {
	
	public NoReservationOverlapException(){
		super("There is no overlapping timespan between the current Reservation and past ones");
	}
	
	public NoReservationOverlapException(String message){
		super(message);
	}

}
