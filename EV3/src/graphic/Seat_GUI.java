package graphic;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import common.Movie;
import common.ShowTime;
import common.ShowTime.SeatBook;
import manager.Bookmanager;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;

public class Seat_GUI extends JFrame implements KeyListener {

	private JPanel contentPane;
	private JLabel row_label[];
	private JLabel column_label[];
	private SeatLabel[][] all_seat_label;
	private ArrayList<Integer> selected_seat_id;
	private boolean isShiftdown;
	private int showtime_id;
	private int selected_count;

	public Seat_GUI(ShowTime showtime, Movie movie) {
		showtime_id = showtime.getID();
		row_label = new JLabel[15];
		column_label = new JLabel[20];
		all_seat_label = new SeatLabel[15][20];
		selected_seat_id = new ArrayList<Integer>();
		selected_count = 0;
		isShiftdown = false;

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle(movie.getEngName() + " " + showtime.getTime());
		setBounds(100, 100, 776, 760);
		addKeyListener(this);
		setFocusable(true);
		requestFocusInWindow();
		JFrame frame = this;
		frame.addKeyListener(this);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				hideWindow(showtime);
			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel screen_label = new JLabel("Screen");
		screen_label.setVerticalAlignment(SwingConstants.CENTER);
		screen_label.setHorizontalAlignment(SwingConstants.CENTER);
		screen_label.setFont(new Font("Tahoma", Font.PLAIN, 60));
		screen_label.setForeground(Color.BLACK);
		screen_label.setBackground(Color.GRAY);
		screen_label.setOpaque(true);
		screen_label.setBounds(22, 42, 718, 81);
		contentPane.add(screen_label);

		JLabel engname_label = new JLabel(movie.getEngName());
		engname_label.setForeground(Color.BLACK);
		engname_label.setBackground(Color.ORANGE);
		engname_label.setOpaque(true);
		engname_label.setFont(new Font("Tahoma", Font.BOLD, 18));
		engname_label.setBounds(screen_label.getX(), 0, 230, 40);
		contentPane.add(engname_label);

		JLabel thname_label = new JLabel(movie.getThName());
		thname_label.setForeground(Color.BLACK);
		thname_label.setBackground(Color.YELLOW);
		thname_label.setOpaque(true);
		thname_label.setFont(new Font("Cordia New", Font.BOLD, 28));
		thname_label.setBounds(engname_label.getX() + engname_label.getWidth(), engname_label.getY(),
				engname_label.getWidth(), engname_label.getHeight());
		contentPane.add(thname_label);

		JLabel time_label = new JLabel("Time " + showtime.getTime());
		time_label.setBackground(Color.ORANGE);
		time_label.setOpaque(true);
		time_label.setFont(new Font("Tahoma", Font.BOLD, 20));
		time_label.setVerticalAlignment(SwingConstants.CENTER);
		time_label.setHorizontalAlignment(SwingConstants.CENTER);
		time_label.setBounds(thname_label.getX() + thname_label.getWidth(), thname_label.getY(), 150,
				engname_label.getHeight());
		contentPane.add(time_label);

		JLabel cinema_label = new JLabel("Theater " + movie.getTheater());
		cinema_label.setBackground(Color.YELLOW);
		cinema_label.setOpaque(true);
		cinema_label.setFont(new Font("Tahoma", Font.BOLD, 20));
		cinema_label.setVerticalAlignment(SwingConstants.CENTER);
		cinema_label.setHorizontalAlignment(SwingConstants.CENTER);
		cinema_label.setBounds(time_label.getX() + time_label.getWidth(), time_label.getY(),
				(screen_label.getX() + screen_label.getWidth()) - (time_label.getX() + time_label.getWidth()),
				engname_label.getHeight());
		contentPane.add(cinema_label);

		JButton confirm_button = new JButton("Confirm");
		confirm_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String current_showtime_line = Bookmanager.showtime_file_text.get(showtime_id - 1);
				String[] current_edit_showtime = current_showtime_line.split("/");
				String new_seat_data = "{";
				for (int i = 0; i < all_seat_label.length; ++i) {
					String status_buffer = "";
					for (int j = 0; j < all_seat_label[i].length; ++j) {
						if (all_seat_label[i][j].isHold)
							status_buffer += "1";
						else
							status_buffer += "0";
					}
					System.out.println("status is : " + status_buffer);
					new_seat_data += SeatBook.encodingDecToHexRow(status_buffer);
					if (i != all_seat_label.length - 1)
						new_seat_data += ",";
				}
				new_seat_data += "}";
				current_edit_showtime[4] = new_seat_data;
				new_seat_data = "";
				for (int i = 0; i < current_edit_showtime.length; ++i) {
					new_seat_data += current_edit_showtime[i];
					if (i != current_edit_showtime.length - 1)
						new_seat_data += "/";
				}

				Bookmanager.showtime_file_text.set(showtime_id - 1, new_seat_data);
				new_seat_data = "";
				for (int i = 0; i < Bookmanager.showtime_file_text.size(); ++i) {
					new_seat_data += Bookmanager.showtime_file_text.get(i);
					if (i != Bookmanager.showtime_file_text.size() - 1)
						new_seat_data += "\n";
				}
				System.out.println(new_seat_data);
				OutputStreamWriter writer = null;
				try {
					writer = new OutputStreamWriter(new FileOutputStream("./data/showtimes.txt"),
							StandardCharsets.UTF_8);
					writer.write(new_seat_data);
					writer.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(confirm_button, "Booking Complete");
				MovieSelector.arr_showtimes[showtime_id - 1] = new ShowTime(showtime_id - 1, movie.getId(),
						showtime.getDate(), showtime.getdataTime(), current_edit_showtime[4]);
				hideWindowConfirm(showtime);
				System.exit(0);
			}
		});
		confirm_button.setBounds(77, 669, 134, 41);
		contentPane.add(confirm_button);

		JButton clear_button = new JButton("Clear");
		clear_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Collections.sort(selected_seat_id);
				for (int i = 0; i < selected_seat_id.size(); ++i) {
					Image icon_image = new ImageIcon(
							this.getClass().getResource(showtime.getImgPathFromSeatId(selected_seat_id.get(i))))
									.getImage();
					icon_image = icon_image.getScaledInstance(SeatLabel.width, SeatLabel.hieght,
							java.awt.Image.SCALE_SMOOTH);
					int y = (int) (selected_seat_id.get(i) / 20);
					int x = (int) (selected_seat_id.get(i) % 20);
					all_seat_label[y][x].setIcon(new ImageIcon(icon_image));
					all_seat_label[y][x].isHold = false;
				}
				selected_count = 0;
			}
		});
		clear_button.setBounds(304, 669, 134, 41);
		contentPane.add(clear_button);

		JButton cancel_button = new JButton("Cancel");
		cancel_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hideWindow(showtime);
			}
		});
		cancel_button.setBounds(528, 669, 134, 41);
		contentPane.add(cancel_button);

		for (int i = 0; i < row_label.length; ++i) {
			if (i == row_label.length - 1) {
				row_label[i] = new JLabel("S");
			} else {
				char label_char = (char) (78 - i);
				row_label[i] = new JLabel("" + label_char);
			}
			row_label[i].setSize(25, 25);
			row_label[i].setLocation(70, 148 + ((row_label[i].getHeight() + 10) * i));
			contentPane.add(row_label[i]);
		}

		for (int i = 0; i < column_label.length; ++i) {
			column_label[i] = new JLabel("" + (i + 1));
			column_label[i].setSize(25, 25);
			column_label[i].setLocation(95 + ((column_label[i].getWidth() + 5) * i), 120);
			contentPane.add(column_label[i]);
		}

		int set_id_num = 0;

		for (int i = 0; i < all_seat_label.length; ++i) {
			for (int j = 0; j < all_seat_label[i].length; ++j) {
				all_seat_label[i][j] = new SeatLabel(set_id_num, showtime.seat.getSeatStatus(i, j));
				if (all_seat_label[i][j].isHold)
					selected_seat_id.add(set_id_num);
				all_seat_label[i][j].setSize(SeatLabel.width, SeatLabel.hieght);
				all_seat_label[i][j].setLocation(90 + ((all_seat_label[i][j].getWidth() + 5) * j),
						148 + ((all_seat_label[i][j].getHeight() + 10) * i));
				Image icon_image = new ImageIcon(
						this.getClass().getResource(showtime.getImgPathFromSeatId(all_seat_label[i][j].seat_label_id)))
								.getImage();
				icon_image = icon_image.getScaledInstance(all_seat_label[i][j].getWidth(),
						all_seat_label[i][j].getHeight(), java.awt.Image.SCALE_SMOOTH);
				all_seat_label[i][j].setIcon(new ImageIcon(icon_image));
				/////////////////////////////// label click
				/////////////////////////////// event////////////////////////////////////////
				int y = i;
				int x = j;
				all_seat_label[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (!isShiftdown || selected_seat_id.size() == 0) {
							if (!all_seat_label[y][x].isHold) {
								all_seat_label[y][x].isHold = true;
								Image icon_image = new ImageIcon(this.getClass().getResource("/seat_green.jpg"))
										.getImage();
								icon_image = icon_image.getScaledInstance(SeatLabel.width, SeatLabel.hieght,
										java.awt.Image.SCALE_SMOOTH);
								all_seat_label[y][x].setIcon(new ImageIcon(icon_image));
								selected_seat_id.add(all_seat_label[y][x].seat_label_id);
								selected_count += 1;
							} else {
								all_seat_label[y][x].isHold = false;
								Image icon_image = new ImageIcon(this.getClass()
										.getResource(showtime.getImgPathFromSeatId(all_seat_label[y][x].seat_label_id)))
												.getImage();
								icon_image = icon_image.getScaledInstance(SeatLabel.width, SeatLabel.hieght,
										java.awt.Image.SCALE_SMOOTH);
								all_seat_label[y][x].setIcon(new ImageIcon(icon_image));
								selected_seat_id.remove(selected_seat_id.indexOf(all_seat_label[y][x].seat_label_id));
								selected_count -= 1;
							}
						} else {
							int current_seat_i = y;
							int current_seat_j = x;
							int previous_seat_i = (int) (selected_seat_id.get(selected_seat_id.size() - 1) / 20);
							int previous_seat_j = (int) (selected_seat_id.get(selected_seat_id.size() - 1) % 20);

							if (!all_seat_label[current_seat_i][current_seat_j].isHold) {
								while (true) {
									if (current_seat_j > previous_seat_j) {
										for (int n = previous_seat_j; n <= current_seat_j; ++n) {
											if (!selected_seat_id
													.contains(all_seat_label[previous_seat_i][n].seat_label_id)) {
												all_seat_label[previous_seat_i][n].isHold = true;
												Image icon_image = new ImageIcon(
														this.getClass().getResource("/seat_green.jpg")).getImage();
												icon_image = icon_image.getScaledInstance(SeatLabel.width,
														SeatLabel.hieght, java.awt.Image.SCALE_SMOOTH);
												all_seat_label[previous_seat_i][n].setIcon(new ImageIcon(icon_image));
												selected_seat_id.add(all_seat_label[previous_seat_i][n].seat_label_id);
												selected_count += 1;
												repaint();
											}
										}
									} else if (current_seat_j < previous_seat_j) {
										for (int n = previous_seat_j; n >= current_seat_j; --n) {
											if (!selected_seat_id
													.contains(all_seat_label[previous_seat_i][n].seat_label_id)) {
												all_seat_label[previous_seat_i][n].isHold = true;
												Image icon_image = new ImageIcon(
														this.getClass().getResource("/seat_green.jpg")).getImage();
												icon_image = icon_image.getScaledInstance(SeatLabel.width,
														SeatLabel.hieght, java.awt.Image.SCALE_SMOOTH);
												all_seat_label[previous_seat_i][n].setIcon(new ImageIcon(icon_image));
												selected_seat_id.add(all_seat_label[previous_seat_i][n].seat_label_id);
												selected_count += 1;
												repaint();
											}
										}
									}
									if (current_seat_i != previous_seat_i) {
										if (current_seat_i > previous_seat_i)
											++previous_seat_i;
										else
											--previous_seat_i;
									} else {
										break;
									}
								}
							} else {
								all_seat_label[current_seat_i][current_seat_j].isHold = false;
								Image icon_image = new ImageIcon(this.getClass()
										.getResource(showtime.getImgPathFromSeatId(
												all_seat_label[current_seat_i][current_seat_j].seat_label_id)))
														.getImage();
								icon_image = icon_image.getScaledInstance(SeatLabel.width, SeatLabel.hieght,
										java.awt.Image.SCALE_SMOOTH);
								all_seat_label[current_seat_i][current_seat_j].setIcon(new ImageIcon(icon_image));
								selected_seat_id.remove(selected_seat_id
										.indexOf(all_seat_label[current_seat_i][current_seat_j].seat_label_id));
								selected_count -= 1;
							}
						}
					}
				});
				contentPane.add(all_seat_label[i][j]);
				++set_id_num;
			}
		}
	}

	private void hideWindow(ShowTime showtime) {
		if (selected_count != 0) {
			int a = JOptionPane.showConfirmDialog(this, "Are you sure to close witout save?", "Confirm close",
					JOptionPane.YES_NO_OPTION);
			if (a == JOptionPane.YES_OPTION) {
				setVisible(false);
				for (int i = 0; i < selected_seat_id.size(); ++i) {
					System.out.println("Size is : " + selected_seat_id.size());
					Image icon_image = new ImageIcon(
							this.getClass().getResource(showtime.getImgPathFromSeatId(selected_seat_id.get(i))))
									.getImage();
					icon_image = icon_image.getScaledInstance(SeatLabel.width, SeatLabel.hieght,
							java.awt.Image.SCALE_SMOOTH);
					int y = (int) (selected_seat_id.get(i) / 20);
					int x = (int) (selected_seat_id.get(i) % 20);
					System.out.println(all_seat_label[y][x]);
					System.out.println(selected_seat_id.toString());
					all_seat_label[y][x].setIcon(new ImageIcon(icon_image));
					all_seat_label[y][x].isHold = false;
				}
				selected_seat_id.clear();
				selected_count = 0;
			} else {
				return;
			}
		} else {
			setVisible(false);
		}
	}

	private void hideWindowConfirm(ShowTime showtime) {
		setVisible(false);
		for (int i = 0; i < selected_seat_id.size(); ++i) {
			System.out.println("Size is : " + selected_seat_id.size());
			Image icon_image = new ImageIcon(
					this.getClass().getResource(showtime.getImgPathFromSeatId(selected_seat_id.get(i)))).getImage();
			icon_image = icon_image.getScaledInstance(SeatLabel.width, SeatLabel.hieght, java.awt.Image.SCALE_SMOOTH);
			int y = (int) (selected_seat_id.get(i) / 20);
			int x = (int) (selected_seat_id.get(i) % 20);
			System.out.println(all_seat_label[y][x]);
			System.out.println(selected_seat_id.toString());
			all_seat_label[y][x].setIcon(new ImageIcon(icon_image));
			all_seat_label[y][x].isHold = false;
		}
		selected_seat_id.clear();
		selected_count = 0;
	}

	public int getShowTimeId() {
		return showtime_id;
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 16) {
			isShiftdown = true;
			System.out.println(isShiftdown);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 16) {
			isShiftdown = false;
			System.out.println(isShiftdown);
		}
	}

	private class SeatLabel extends JLabel {
		public static final int width = 25;
		public static final int hieght = 25;
		public int seat_label_id;
		public boolean isHold;

		SeatLabel(int set_id_num, boolean status) {
			seat_label_id = set_id_num;
			isHold = status;
		}

		public String toString() {
			int i = (int) (seat_label_id / 20);
			int j = (int) (seat_label_id % 20);
			return String.format("i : %d \nj : %d\nid : %d", i, j, seat_label_id);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
