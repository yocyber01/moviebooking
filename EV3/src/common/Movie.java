package common;

public class Movie {
	private int id;
	private String engname;
	private String thainame;
	private int theater;

	public Movie(int id,String engname,String thainame,int theater) {
		this.id = id;
		this.engname = engname;
		this.thainame = thainame;
		this.theater = theater;
		
	}
	
	public int getId() {
		return id;
	}
	public int getTheater() {
		return theater;
	}
	public String getEngName() {
		return engname;
	}
	public String getThName() {
		return thainame;
	}
}