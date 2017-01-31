import java.beans.PropertyChangeSupport;

import javafx.beans.property.StringProperty;

public class Track {

	private PropertyChangeSupport propertyChangeSupport;
	private String name;
	private String interpret;
	private String album;
	private long length;
	private String absolutePath;
	
	public Track(String name, String interpret, String album, long length){
		this.name = name;
		this.interpret = interpret;
		this.album = album;
		this.length = length;	
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	protected String getName(){
		return name;
	}
	protected String getInterpret(){
		return interpret;
	}
	protected String getAlbum(){
		return album;
	}
	protected long getLength(){
		return length;
	}
	public String toString(){
		return "Title: " + getName() + " Interpret: " + getInterpret() + " length: " + getLength();
	}
	public void absolutePath(String absolutePath){
		this.absolutePath = absolutePath;
	}
	public String getAbsolutePath(){
		return absolutePath;
	}
	public void setTitle(String name){
		String oldTitle = this.name;
		this.name = name;
		propertyChangeSupport.firePropertyChange(
		"name", oldTitle, name);
	}
	public void setInterpret(String interpret){
		String oldInterpret = this.interpret;
		this.interpret = interpret;
		propertyChangeSupport.firePropertyChange(
		"interpret", oldInterpret, interpret);
	}

}
