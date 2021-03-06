package dk.gettodone.pro.data;

public class Context {
	private long id;
	private String name;
	
	public Context() {
	}
	
	public Context(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() { return id; }
	public void setId(long value) { id = value; }
	
	public String getName() { return name; }
	public void setName(String value) { name = value; }
	
	@Override
	public String toString() {
		return name;
	}
}
