package dk.gettodone.pro.data;

public class Calendar {
	private long id;
	private String name;
	private String timezone;
	
	public Calendar() {
	}
	
	public Calendar(long id, String name, String timezone) {
		this.id = id;
		this.name = name;
		this.timezone = timezone;
	}
	
	public long getId() { return id; }
	public void setId(long value) { id = value; }
	
	public String getName() { return name; }
	public void setName(String value) { name = value; }
	
	public String getTimezone() { return timezone; }
	public void setTimezone(String value) { timezone = value; }
	
	@Override
	public String toString() {
		return name;
	}
}
