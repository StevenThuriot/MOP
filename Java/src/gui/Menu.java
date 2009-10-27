package gui;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
//TODO Handle invallid input
public class Menu {
	private InputStream in;
	private PrintStream out;
	private Scanner s;
	private NumberFormat f;
	private SimpleDateFormat df;
	
	public Menu(InputStream i, PrintStream o){
		in = i;
		out = o;
		s = new Scanner(in);
		//System.out.println(s.delimiter().pattern());
		//s.useDelimiter(Pattern.compile("\n+",Pattern.UNIX_LINES));
		s.useDelimiter(System.getProperty("line.separator"));
		df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		f = NumberFormat.getIntegerInstance();
		//f.setMinimumIntegerDigits(2);
	}
	
	public String prompt(String message){
		out.println(message);
		if(s.hasNext()){
			return s.next();
		}
		throw new RuntimeException();
	}
	
	public boolean dialogYesNo(String message){
		int choice = this.menu(message, "yes", "no");
		return choice == 0;
	}
	
	public int menu(String title, List<String> options){
		return menu(title, options.toArray(new String[0]));
	}
	
	public int menu(String title, String... options){
		this.printList(title, options);
		if(s.hasNextInt()){
			return s.nextInt();
		}
		throw new InputMismatchException(s.next());
	}
	
	public GregorianCalendar promptDate(String message){
		String dStr = this.prompt(message+" eg. "+df.format((new GregorianCalendar()).getTime()));
		System.out.println(dStr);
		/*if(dStr.equals(""))
			dStr = df.format((new GregorianCalendar()).getTime());*/
		Date date;
		try {
			date = df.parse(dStr);
		} catch (ParseException e) {
			System.out.println("Date improperly formatted eg."+df.format((new GregorianCalendar()).getTime()));
			return promptDate(message);
		}
		GregorianCalendar grC = new GregorianCalendar();
		grC.setTime(date);
		return grC;
	}
	
	public void printList(String title, String... list){
		out.println(title);
		for(int i = 0; i < list.length;i++){
			out.println(f.format(i)+": "+list[i]);
		}
	}
	
	public void printList(String title, List<String> list){
		printList(title, list.toArray(new String[0]));
	}
	
	public void println(String message){
		out.println(message);
	}
	
	public String format(GregorianCalendar gc){
		return df.format(gc.getTime());
	}

}
