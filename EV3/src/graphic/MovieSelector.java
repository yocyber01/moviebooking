package graphic;

import javax.swing.JPanel;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

import common.Movie;
import common.ShowTime;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MovieSelector extends JPanel {
	private Image image;
	private JLabel image_label;
	private JLabel eng_name;
	private JLabel th_name;
	private JPanel grey_panel;
	private JLabel cinema_label;
	private JLabel theater_num;
	public JButton[] allButton;
	public static ShowTime[] arr_showtimes;

	public MovieSelector(int width, int hieght, Movie moivie, ShowTime[] showtimes) {
		arr_showtimes = showtimes;
		setSize(width - 20, hieght);

		image_label = new JLabel("");
		image_label.setBounds(0, 0, 150, 180);
		setLayout(null);
		add(image_label);

		eng_name = new JLabel("Engname");
		eng_name.setBackground(Color.BLACK);
		eng_name.setBounds(160, 11, 870, 28);
		eng_name.setFont(new Font("Tahoma", Font.BOLD, 24));
		add(eng_name);

		th_name = new JLabel("Thname");
		th_name.setBounds(160, 42, 870, 39);
		th_name.setFont(new Font("Cordia New", Font.PLAIN, 24));
		add(th_name);

		grey_panel = new JPanel();
		grey_panel.setBackground(Color.GRAY);
		grey_panel.setBounds(145, -2, 935, 83);
		add(grey_panel);

		cinema_label = new JLabel("CINEMA");
		cinema_label.setFont(new Font("Tahoma", Font.BOLD, 24));
		cinema_label.setBounds(160, 115, 105, 28);
		add(cinema_label);

		theater_num = new JLabel("XXX");
		theater_num.setFont(new Font("Tahoma", Font.BOLD, 24));
		theater_num.setBounds(266, 115, 48, 28);
		add(theater_num);

		if (moivie != null)
			dataLoad(moivie);
		else
			System.out.println("moivie is null");
		if (showtimes != null)
			addShowtimeBtn(showtimes, moivie);
		else {
			System.out.println("showtime is null");

			allButton[0] = new JButton("XX:XX");
			allButton[0].setHorizontalAlignment(SwingConstants.LEFT);
			allButton[0].setHorizontalTextPosition(SwingConstants.LEFT);
			allButton[0].setFont(new Font("Tahoma", Font.PLAIN, 18));
			allButton[0].setBounds(324, 120, 79, 23);
			add(allButton[0]);
		}
	}

	private void dataLoad(Movie moivie) {
		image = new ImageIcon(this.getClass().getResource(String.format("/%d.jpg", moivie.getId()))).getImage();
		image = image.getScaledInstance(image_label.getWidth(), image_label.getHeight(), java.awt.Image.SCALE_SMOOTH);
		image_label.setIcon(new ImageIcon(image));

		eng_name.setText(moivie.getEngName());
		th_name.setText(moivie.getThName());
		theater_num.setText("" + moivie.getTheater());
	}

	private void addShowtimeBtn(ShowTime[] showtimes, Movie movie) {
		if (showtimes != null)
			allButton = new JButton[showtimes.length];
		else
			allButton = new JButton[1];
		
		for (int i = 0; i < showtimes.length; ++i) {
			allButton[i] = new JButton(showtimes[i].getTime());
			allButton[i].setHorizontalAlignment(SwingConstants.LEFT);
			allButton[i].setHorizontalTextPosition(SwingConstants.LEFT);
			allButton[i].setFont(new Font("Tahoma", Font.PLAIN, 18));
			allButton[i].setBounds(324 + (i * 110), 120, 80, 25);
			int n = i;
			allButton[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					System.out.println();
					Seat_GUI seat_window = new Seat_GUI(showtimes[n], movie);
					seat_window.setVisible(true);
				}
			});
			add(allButton[i]);
		}
	}
}
