package common;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ShowTime {
	private int id;
	private int movie_id;
	private String date;
	private String time;
	public SeatBook seat;
	private String time_data;

	public ShowTime(int id, int movie_id, String date, String time, String Obj) {
		System.out.println("id : " + id);
		this.id = id;
		this.movie_id = movie_id;
		this.date = date;
		this.time_data = time;
		this.time = "" + time.charAt(0) + time.charAt(1) + ":" + time.charAt(2) + time.charAt(3);
		// System.out.println(this.time);
		seat = new SeatBook(Obj);
	}

	public int getID() {
		return id;
	}

	public int getMovieId() {
		return movie_id;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}
	
	public String getdataTime() {
		return time_data;
	}

	public int seatLeft() {
		return seat.seatleft();
	}

	public int countBookedSeat() {
		return seat.countBooked();
	}

	public void updateBooked(int[] seatLabelId) {
		seat.updateBooked(seatLabelId);
	}

	public String getImgPathFromSeatId(int seat_label_id) {
		return seat.getImgPathFromId(seat_label_id);
	}

	public static class SeatBook {
		private boolean seat_status[][];
		private int count_booked_seat;
		private String[][] img_path;

		public SeatBook(String obj) {
			seat_status = new boolean[15][20];
			img_path = new String[15][20];

			String obj_buffer[] = obj.trim().substring(1, obj.length() - 1).split(",");
			String row[] = new String[obj_buffer.length];
			for (int i = 0; i < row.length; ++i) {
				row[i] = decodingSeatRow(obj_buffer[i]);
				System.out.println(row[i]);
				for (int j = 0; j < row[i].length(); ++j) {
					String buffer = "" + row[i].charAt(j);
					seat_status[i][j] = Integer.parseInt(buffer) == 1 ? true : false;
					if (seat_status[i][j] == true) {
						img_path[i][j] = "/seat_grey.jpg";
						count_booked_seat += 1;
					} else {
						if (i < 7) {
							img_path[i][j] = img_path[i][j] = "/seat_blue.jpg";
						} else if (i < 14) {
							img_path[i][j] = img_path[i][j] = "/seat_red.jpg";
						} else {
							img_path[i][j] = img_path[i][j] = "/seat_yellow.jpg";
						}
					}
				}
			}
		}

		private String decodingHexToStatus(String hex) {
			int decimal = Integer.parseInt(hex, 16);
			String result = "" + decimal;
			if (result.length() < 10) {
				String buffer = "";
				for (int i = 0; i < 10 - result.length(); ++i) {
					buffer = buffer + "0";
				}
				result = buffer + result;
			}
			return result;
		}

		private String decodingSeatRow(String obj) {
			String hex_buffer[] = obj.trim().substring(1, obj.length() - 1).split("#");
			String row_buffer = decodingHexToStatus(hex_buffer[0].trim()) + decodingHexToStatus(hex_buffer[1].trim());
			return row_buffer;
		}
		
		public static String encodingDecToHexRow(String non_encode) {
			String[] decimal = new String[2];
			decimal[0] = non_encode.substring(0, 10);
			decimal[1] = non_encode.substring(10);
			String hex1 = Integer.toHexString(Integer.parseInt(decimal[0]));
			String hex2 = Integer.toHexString(Integer.parseInt(decimal[1]));
			String result = "{" + hex1 + "#" + hex2 + "}";
			return result;
		}

		public int countBooked() {
			return count_booked_seat;
		}

		public int seatleft() {
			return 300 - count_booked_seat;
		}

		public void updateBooked(int[] seat_label_id) {
			for (int seatId : seat_label_id) {
				int i = (int) Math.floor(seatId / 20);
				int j = seatId % 20;
				seat_status[i][j] = true;
				img_path[i][j] = "seat_grey";
			}
		}

		public String getImgPathFromId(int seat_label_id) {
			int i = (int) Math.floor(seat_label_id / 20);
			int j = seat_label_id % 20;
			return img_path[i][j];
		}
		
		public boolean getSeatStatus(int i ,int j) {
			return seat_status[i][j];
		}
	}
}
