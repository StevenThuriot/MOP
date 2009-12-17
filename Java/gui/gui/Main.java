package gui;

import exception.TimeException;


public class Main {
	/**
	 * @param args
	 * @throws TimeException 
	 */
	public static void main(String[] args) throws TimeException {
	    MainGUI mainGUI = new MainGUI(System.in,System.out);
		
		Thread t = new Thread(mainGUI);
		
		t.start();
	}

}
