package beans;

import java.time.LocalTime;

public class WorkingHours {
	
	private LocalTime startHour;
	private LocalTime endHour;
	public WorkingHours() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WorkingHours(LocalTime startHour, LocalTime endHour) {
		super();
		this.startHour = startHour;
		this.endHour = endHour;
	}
	public LocalTime getStartHour() {
		return startHour;
	}
	public void setStartHour(LocalTime startHour) {
		this.startHour = startHour;
	}
	public LocalTime getEndHour() {
		return endHour;
	}
	public void setEndHour(LocalTime endHour) {
		this.endHour = endHour;
	}
	
	

}
