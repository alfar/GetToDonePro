package dk.gettodone.pro.data;

public class Task {
	private long id;
	private String title;
	private long contextId = 0;
	
	public long getId() { return id; }
	public void setId(long value) { id = value; }
	
	public String getTitle() { return title; }
	public void setTitle(String value) { title = value; }
	
	public long getContextId() { return contextId; }
	public void setContextId(long value) { contextId = value; }
	
	@Override
	public String toString() {
		return title;
	}
}
