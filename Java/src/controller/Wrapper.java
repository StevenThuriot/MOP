package controller;

import gui.Describable;

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

}
