package main;

public interface Subject {
	public void setObserver(Observer o);
	public void notifys(String unit, String state);
	
}
