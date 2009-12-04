package controller;

import gui.Describable;
import java.util.ArrayList;

public class Wrapper<T extends Describable> implements Describable{
	
	T t;
	
	public Wrapper(T t){
		this.t = t;
	}
	
	T getContent(){
		return t;
	}
	
	public String getDescription(){
		return t.getDescription();
	}
	
	public static <D extends Describable> ArrayList<Wrapper<D>> wrapList(ArrayList<D> list){
		ArrayList<Wrapper<D>> a = new ArrayList<Wrapper<D>>();
		for(D d:list)
			a.add(new Wrapper<D>(d));
		return a;
	}

}
