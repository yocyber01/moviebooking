package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import common.ShowTime;
import graphic.MovieSelector;
import manager.Bookmanager;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;

public class Book_GUI extends JFrame {

	private Bookmanager b;
	private JScrollPane scrollPane;
	private JPanel contentPane;
	private JPanel content;
	private ArrayList<MovieSelector> booking_row;

	public static void main(String[] args) {
		new Book_GUI();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Book_GUI frame = new Book_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Book_GUI() {
		b = new Bookmanager();
		booking_row = new ArrayList<MovieSelector>();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1110, 780);
		
		contentPane = new JPanel();
		contentPane.setBounds(0, 0, 1110, 780);
		contentPane.setVisible(true);
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		
		content = new JPanel();
		content.setBounds(0, 0, 1080, 180*5);
		content.setPreferredSize(new Dimension(1080, 180*5));
		content.setVisible(true);
		content.setLayout(null);
		
		scrollPane = new JScrollPane(content);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0), 2));
		scrollPane.setBounds(0,11,1080,720);
		
		contentPane.add(scrollPane);
		
		addAllRows(b.getShowtimes());

	}

	private void addAllRows(ArrayList<ShowTime> showtimes) {
		ArrayList<ShowTime> buffer = new ArrayList<ShowTime>();
		ShowTime[] showtime_temp = new ShowTime[buffer.size()];
		int element_count = 0;
		for (int i = 0; i < showtimes.size(); ++i) {
			if (i != 0 && showtimes.get(i - 1).getMovieId() == showtimes.get(i).getMovieId()) {
				buffer.add(showtimes.get(i));
			} else {
				if (i != 0) {
					showtime_temp = new ShowTime[buffer.size()];
					for (int n = 0; n < buffer.size(); ++n) {
						showtime_temp[n] = buffer.get(n);
					}
					MovieSelector selector_temp = new MovieSelector(content.getWidth(),content.getHeight() / 5, b.getMovieById(showtime_temp[0].getMovieId()), showtime_temp);
					selector_temp.setBounds(0, selector_temp.getHeight()*element_count, selector_temp.getWidth(), selector_temp.getHeight());
					content.add(selector_temp,BorderLayout.NORTH);
					element_count += 1;
					booking_row.add(selector_temp);

					buffer = new ArrayList<ShowTime>();
					buffer.add(showtimes.get(i));
					

				} else {
					buffer.add(showtimes.get(i));
				}
			}
		}
		showtime_temp = new ShowTime[buffer.size()];
		for (int n = 0; n < buffer.size(); ++n) {
			showtime_temp[n] = buffer.get(n);
		}
		
		MovieSelector selector_temp = new MovieSelector(content.getWidth(),content.getHeight() / 5, b.getMovieById(showtime_temp[0].getMovieId()), showtime_temp);
		selector_temp.setBounds(0, selector_temp.getHeight()*element_count, selector_temp.getWidth(), selector_temp.getHeight());
		content.add(selector_temp);
		element_count += 1;
		System.out.println(element_count);
	}
}
