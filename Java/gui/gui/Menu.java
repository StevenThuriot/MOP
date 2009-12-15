package gui;

import java.io.InputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * @author koen
 *
 */
public class Menu {
	private InputStream in;
	private PrintStream out;
	private Scanner s;
	private NumberFormat f;
	private SimpleDateFormat df;
	
	/**
	 * @param i
	 * @param o
	 */
	public Menu(InputStream i, PrintStream o){
		in = i;
		out = o;
		s = new Scanner(in);
		s.useDelimiter(System.getProperty("line.separator"));
		df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		f = NumberFormat.getIntegerInstance();
	}
	
	/**
	 * @param message
	 * @return
	 */
	public String prompt(String message){
		out.println(message);
			return s.next().trim();
	}
	
	/**
	 * @param message
	 * @return
	 */
	public boolean dialogYesNo(String message){
		int choice = this.menu(message, "yes", "no");
		return choice == 0;
	}
	
	/**
	 * @param title
	 * @param options
	 * @return
	 */
	public int menu(String title, List<String> options){
		return menu(title, options.toArray(new String[0]));
	}
	
	/**
	 * @param title
	 * @param options
	 * @return
	 */
	public <D extends Describable> D menuGen(String title, List<D> options){
		this.printListGen(title, options);
		int choice=0;
		while (true) {
			while (!s.hasNextInt()) {
				out.println("Incorrect input, try again");
				s.next();
			}
			choice = s.nextInt();
			if(choice < 0 || choice >= options.size()){
				out.println("Incorrect input, try again");
				continue;
			}else
				break;
		}
		return options.get(choice);
	}
	
	/**
	 * @param title
	 * @param options
	 * @return
	 */
	public <D extends Describable> D menuGenOpt(String title, List<D> options, String quit){
		this.printListGen(title, options, quit);
		int choice=0;
		while (true) {
			while (!s.hasNextInt()) {
				out.println("Incorrect input, try again");
				s.next();
			}
			choice = s.nextInt();
			if(choice < 0){
				out.println("Incorrect input, try again");
				continue;
			}
			if(choice >= options.size()){
				return null;
			}
			break;
		}
		return options.get(choice);
	}
	
	/**
	 * @param title
	 * @param options
	 * @return
	 */
	public <D extends Describable> ArrayList<D> menuGenMulti(String title, List<D> options){
		ArrayList<D> o = new ArrayList<D>(options);
		ArrayList<D> l = new ArrayList<D>();
		int choice = 0;
		while (true) {
			this.printListGen(title, o,"No more");
			while (!s.hasNextInt()) {
				out.println("Incorrect input, try again");
				s.next();
			}
			choice = s.nextInt();
			if(choice < 0){
				out.println("Incorrect input, try again");
				continue;
			}
			if(choice >= o.size())
				break;
			l.add(o.remove(choice));
		}
		return l;
	}
	
	/**
	 * @param title
	 * @param options
	 * @return
	 */
	public int menu(String title, String... options){
		this.printList(title, options);
		int choice = 0;
		while (true) {
			while (!s.hasNextInt()) {
				out.println("Incorrect input, try again");
				s.next();
			}
			choice = s.nextInt();
			if(choice < 0 || choice >= options.length){
				out.println("Incorrect input, try again");
				continue;
			}else
				break;
		}
		return choice;
	}
	
	/**
	 * @param message
	 * @return
	 */
	public GregorianCalendar promptDate(String message){
		String dStr = this.prompt(message+" eg. "+df.format((new GregorianCalendar()).getTime()));
		Date date;
		try {
			date = df.parse(dStr);
		} catch (ParseException e) {
			out.println("Date improperly formatted eg."+df.format((new GregorianCalendar()).getTime()));
			return promptDate(message);
		}
		GregorianCalendar grC = new GregorianCalendar();
		grC.setTime(date);
		return grC;
	}
	
	/**
	 * @param title
	 * @param list
	 */
	public void printList(String title, String... list){
		out.println(title);
		for(int i = 0; i < list.length;i++){
			out.println(f.format(i)+": "+list[i]);
		}
	}
	
	/**
	 * @param title
	 * @param list
	 */
	public void printList(String title, List<String> list){
		printList(title, list.toArray(new String[0]));
	}
	
	/**
	 * @param title
	 * @param list
	 */
	public <D extends Describable> void printListGen(String title, List<D> list, String... extra){
		out.println(title);
		for(int i = 0; i < list.size();i++){
			out.println(f.format(i)+": "+list.get(i).getDescription());
		}
		for(int i = 0; i < extra.length;i++){
			out.println(f.format(i+list.size())+": "+extra[i]);
		}
	}
	
	/**
	 * @param message
	 */
	public void println(String message){
		out.println(message);
	}
	
	/**
	 * @param gc
	 * @return
	 */
	public String format(GregorianCalendar gc){
		return df.format(gc.getTime());
	}

}
