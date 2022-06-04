package manager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import common.Movie;
import common.ShowTime;

public class Bookmanager {
	private ArrayList<Movie> movies;
	private ArrayList<ShowTime> showtimes;
	public static ArrayList<String> showtime_file_text;

	public Bookmanager() {
		movies = new ArrayList<Movie>();
		showtimes = new ArrayList<ShowTime>();
		showtime_file_text = new ArrayList<String>();
		moviesLoad();
		showtimeLoad();
	}

	private void moviesLoad() {
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream("./data/movie.txt");
			isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
			reader = new BufferedReader(isr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(reader);
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String array_buffer[] = line.split("/");
			movies.add(new Movie(Integer.parseInt(array_buffer[0]), array_buffer[1], array_buffer[2],
					Integer.parseInt(array_buffer[3])));
			//System.out.println(line);
		}
		s.close();
	}

	private void showtimeLoad() {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream("./data/showtimes.txt");
			isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
			reader = new BufferedReader(isr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(reader);
		while (s.hasNextLine()) {
			String line = s.nextLine();
			showtime_file_text.add(line);
			String array_buffer[] = line.split("/");
			ShowTime showtime_buffer = new ShowTime(Integer.parseInt(array_buffer[0]),
					Integer.parseInt(array_buffer[1]),array_buffer[2], array_buffer[3], array_buffer[4]);
			
			showtimes.add(showtime_buffer);
		}
		s.close();
	}
	
	public ArrayList<Movie> getMovies(){
		return movies;
	}
	
	public ArrayList<ShowTime> getShowtimes(){
		return showtimes;
	}
	
	public Movie getMovieById(int movie_id) {
		for(int i = 0 ; i < movies.size() ; ++i) {
			if(movie_id == movies.get(i).getId()) {
				return movies.get(i);
			}
		}
		System.out.println("NO Movie");
		return null;
	}
	
	public ShowTime getShowtimeById(int showtime_id) {
		for(int i = 0 ; i < movies.size() ; ++i) {
			if(showtime_id == showtimes.get(i).getID()) {
				return showtimes.get(i);
			}
		}
		System.out.println("NO Showtime");
		return null;
	}
}
