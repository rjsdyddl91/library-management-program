package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.java.library.dao.BookDAOImple;
import edu.java.library.dao.RentalDAOImple;
import edu.java.library.vo.BookVO;
import edu.java.library.vo.MemberVO;
import edu.java.library.vo.RentalJoinVO;
import edu.java.library.vo.RentalVO;

public class UserPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private MemberVO loginUser;
	private JTable bookTable;
	
	private ArrayList<BookVO> currentBookList =
			new ArrayList<BookVO>();

	private int currentPage = 1;

	private final int PAGE_SIZE = 10;
	
	private JButton firstPageBtn;
	private JButton prevPageBtn;
	private JButton nextPageBtn;
	private JButton lastPageBtn;
	private JLabel pageLabel;

	public UserPanel(CardLayout cardLayout, JPanel cardPanel) {

		setLayout(null);
		setBackground(new Color(248, 249, 251));

		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);
		add(topBar);

		JLabel title = new JLabel("만화책 대여 시스템");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		title.setBounds(25, 15, 300, 30);
		topBar.add(title);

		JLabel pageTitle = new JLabel("도서 목록");
		pageTitle.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		pageTitle.setForeground(new Color(35, 40, 48));
		pageTitle.setBounds(60, 85, 250, 45);
		add(pageTitle);

		JLabel guide = new JLabel("대여가능 도서는 더블클릭으로 대여, 본인이 대여한 도서는 더블클릭으로 반납할 수 있습니다.");
		guide.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		guide.setForeground(new Color(110, 118, 128));
		guide.setBounds(60, 130, 700, 30);
		add(guide);

		JLabel searchLabel =
				new JLabel("도서 검색");

		searchLabel.setBounds(
				60,
				165,
				80,
				20);
		
		add(searchLabel);
		
		JTextField searchField =
				new JTextField(
						"도서명을 입력해주세요.");

		searchField.setBounds(
				60,
				190,
				300,
				40);

		searchField.setForeground(
				Color.GRAY);

		add(searchField);


		// 🔥 여기부터 추가
		searchField.addFocusListener(
				new java.awt.event.FocusAdapter() {

			@Override
			public void focusGained(
					java.awt.event.FocusEvent e) {

				if(searchField.getText()
						.equals(
								"도서명을 입력해주세요.")) {

					searchField.setText("");

					searchField.setForeground(
							Color.BLACK);
				}
			}

			@Override
			public void focusLost(
					java.awt.event.FocusEvent e) {

				if(searchField.getText()
						.trim()
						.isEmpty()) {

					searchField.setText(
							"도서명을 입력해주세요.");

					searchField.setForeground(
							Color.GRAY);

					loadAllBooks();
				}
			}
		});

		JButton myRentalBtn = new JButton("내 대여 정보");
		myRentalBtn.setBounds(760, 190, 150, 40);
		add(myRentalBtn);

		JButton logoutBtn =
				new JButton("로그아웃");

		logoutBtn.setFont(
				new Font(
						"맑은 고딕",
						Font.BOLD,
						15));

		logoutBtn.setForeground(
				new Color(
						70,
						75,
						85));

		logoutBtn.setBackground(
				new Color(
						248,
						249,
						251));

		logoutBtn.setBorderPainted(
				false);

		logoutBtn.setFocusPainted(
				false);

		logoutBtn.setBounds(
				800,
				555,
				130,
				35);

		add(logoutBtn);

		bookTable = new JTable();
		bookTable.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
		bookTable.setRowHeight(28);
		bookTable.getTableHeader().setReorderingAllowed(false);

		bookTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if(e.getClickCount() == 2) {

					int row = bookTable.getSelectedRow();

					if(row == -1) {
						return;
					}

					int bookId = Integer.parseInt(
							bookTable.getValueAt(row, 0).toString());

					String bookTitle =
							bookTable.getValueAt(row, 1).toString();

					String status =
							bookTable.getValueAt(row, 4).toString();

					if(status.equals("대여가능")) {

						int confirm =
								JOptionPane.showConfirmDialog(
										UserPanel.this,
										"\""
										+ bookTitle
										+ "\" 을 대여하시겠습니까?",
										"도서 대여 확인",
										JOptionPane.YES_NO_OPTION);

						if(confirm != JOptionPane.YES_OPTION) {
							return;
						}

						RentalVO vo = new RentalVO();
						vo.setMemberId(loginUser.getMemberId());
						vo.setBookId(bookId);

						RentalDAOImple rentalDao =
								new RentalDAOImple();

						int result =
								rentalDao.rentBook(vo);

						if(result == 1) {

							JOptionPane.showMessageDialog(
									UserPanel.this,
									"\""
									+ bookTitle
									+ "\" 대여 완료!");

							loadAllBooks();
							
							

						} else {

							JOptionPane.showMessageDialog(
									UserPanel.this,
									"도서 대여 실패!");
						}

					} else if(status.equals("대여중")) {

						int confirm =
								JOptionPane.showConfirmDialog(
										UserPanel.this,
										"\""
										+ bookTitle
										+ "\" 을 반납하시겠습니까?",
										"도서 반납 확인",
										JOptionPane.YES_NO_OPTION);

						if(confirm != JOptionPane.YES_OPTION) {
							return;
						}

						RentalDAOImple rentalDao =
								new RentalDAOImple();

						int result =
								rentalDao.returnBookByBookId(
										loginUser.getMemberId(),
										bookId);

						if(result == 1) {

							JOptionPane.showMessageDialog(
									UserPanel.this,
									"\""
									+ bookTitle
									+ "\" 반납 완료!");

							loadAllBooks();

						} else {

							JOptionPane.showMessageDialog(
									UserPanel.this,
									"도서 반납 실패!\n본인이 대여한 도서만 반납할 수 있습니다.");
						}
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(bookTable);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setWheelScrollingEnabled(false);
		scrollPane.setBounds(60, 255, 880, 300);
		add(scrollPane);
		
		firstPageBtn = new JButton("<<");
		firstPageBtn.setBounds(325, 565, 55, 30);
		add(firstPageBtn);

		prevPageBtn = new JButton("<");
		prevPageBtn.setBounds(390, 565, 55, 30);
		add(prevPageBtn);

		pageLabel = new JLabel("1 / 1");
		pageLabel.setHorizontalAlignment(JLabel.CENTER);
		pageLabel.setBounds(455, 565, 90, 30);
		add(pageLabel);

		nextPageBtn = new JButton(">");
		nextPageBtn.setBounds(555, 565, 55, 30);
		add(nextPageBtn);

		lastPageBtn = new JButton(">>");
		lastPageBtn.setBounds(620, 565, 55, 30);
		add(lastPageBtn);
		
		firstPageBtn.addActionListener(e -> {

			currentPage = 1;
			showBookPage();
		});

		prevPageBtn.addActionListener(e -> {

			if(currentPage > 1) {
				currentPage--;
				showBookPage();
			}
		});

		nextPageBtn.addActionListener(e -> {

			int totalPage =
					(int) Math.ceil(
							(double) currentBookList.size()
							/ PAGE_SIZE);

			if(currentPage < totalPage) {
				currentPage++;
				showBookPage();
			}
		});

		lastPageBtn.addActionListener(e -> {

			int totalPage =
					(int) Math.ceil(
							(double) currentBookList.size()
							/ PAGE_SIZE);

			if(totalPage == 0) {
				totalPage = 1;
			}

			currentPage = totalPage;
			showBookPage();
		});

		loadAllBooks();

		searchField.getDocument().addDocumentListener(
				new javax.swing.event.DocumentListener() {

			@Override
			public void insertUpdate(
					javax.swing.event.DocumentEvent e) {

				searchBooks(searchField.getText());
			}

			@Override
			public void removeUpdate(
					javax.swing.event.DocumentEvent e) {

				searchBooks(searchField.getText());
			}

			@Override
			public void changedUpdate(
					javax.swing.event.DocumentEvent e) {

				searchBooks(searchField.getText());
			}
		});

		myRentalBtn.addActionListener(e -> {
			showMyRentalInfo();
		});

		logoutBtn.addActionListener(e -> {

			JOptionPane.showMessageDialog(
					this,
					"로그아웃 되었습니다.");

			cardLayout.show(cardPanel, "login");
		});
	}

	public void setLoginUser(MemberVO loginUser) {
		this.loginUser = loginUser;
		loadAllBooks();
	}

	private void loadAllBooks() {

		BookDAOImple dao = new BookDAOImple();

		currentBookList =
				dao.selectAll();

		currentPage = 1;

		showBookPage();
	}

	private void searchBooks(String keyword) {

		keyword = keyword.trim();
		
		if(keyword.equals(
				"도서명을 입력해주세요.")) {

			loadAllBooks();
			return;
		}

		if(keyword.isBlank()) {
			loadAllBooks();
			return;
		}

		BookDAOImple dao = new BookDAOImple();

		ArrayList<BookVO> list =
				dao.selectByTitle(keyword);

		currentBookList = list;
		currentPage = 1;
		showBookPage();
	}

	private void showBookTable(ArrayList<BookVO> list) {

		String[] columnNames = {
				"도서번호",
				"제목",
				"작가",
				"출판사",
				"상태"
		};

		String[][] data =
				new String[list.size()][5];

		for(int i = 0; i < list.size(); i++) {

			BookVO book = list.get(i);

			data[i][0] = String.valueOf(book.getBookId());
			data[i][1] = book.getTitle();
			data[i][2] = book.getAuthor();
			data[i][3] = book.getPublisher();
			data[i][4] = book.getBookStatus();
		}

		bookTable.setModel(
				new javax.swing.table.DefaultTableModel(
						data,
						columnNames) {

					private static final long serialVersionUID = 1L;

					@Override
					public boolean isCellEditable(
							int row,
							int column) {

						return false;
					}
				});
	}

	private void showMyRentalInfo() {

		RentalDAOImple dao =
				new RentalDAOImple();

		ArrayList<RentalJoinVO> list =
				dao.selectMyRental(
						loginUser.getMemberId());

		if(list.isEmpty()) {

			JOptionPane.showMessageDialog(
					this,
					"대여 기록이 없습니다.");

			return;
		}

		String[] columnNames = {
				"대여번호",
				"도서번호",
				"책 제목",
				"대여일",
				"반납예정일",
				"반납일",
				"상태"
		};

		String[][] data =
				new String[list.size()][7];

		for(int i = 0; i < list.size(); i++) {

			RentalJoinVO rental =
					list.get(i);

			data[i][0] =
					String.valueOf(
							rental.getRentalId());

			data[i][1] =
					String.valueOf(
							rental.getBookId());

			data[i][2] =
					rental.getTitle();

			data[i][3] =
					String.valueOf(
							rental.getRentalDate());

			data[i][4] =
					String.valueOf(
							rental.getDueDate());

			if(rental.getReturnDate() == null) {

				data[i][5] = "";

			} else {

				data[i][5] =
						String.valueOf(
								rental.getReturnDate());
			}

			if(rental.getRentalStatus().equals("대여중")
					&& rental.getDueDate().before(
							new java.sql.Date(
									System.currentTimeMillis()))) {

				data[i][6] =
						"연체중";

			} else {

				data[i][6] =
						rental.getRentalStatus();
			}
		}

		JTable table =
				new JTable(
						data,
						columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(
					int row,
					int column) {

				return false;
			}
		};
		
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if(e.getClickCount() == 2) {

					int row = table.getSelectedRow();

					if(row == -1) {
						return;
					}

					int bookId =
							Integer.parseInt(
									table.getValueAt(row, 1).toString());

					String bookTitle =
							table.getValueAt(row, 2).toString();

					String status =
							table.getValueAt(row, 6).toString();

					RentalDAOImple rentalDao =
							new RentalDAOImple();

					if(status.equals("대여중")) {

						int confirm =
								JOptionPane.showConfirmDialog(
										UserPanel.this,
										"\""
										+ bookTitle
										+ "\" 을 반납하시겠습니까?",
										"도서 반납 확인",
										JOptionPane.YES_NO_OPTION);

						if(confirm != JOptionPane.YES_OPTION) {
							return;
						}

						int result =
								rentalDao.returnBookByBookId(
										loginUser.getMemberId(),
										bookId);

						if(result == 1) {

							JOptionPane.showMessageDialog(
									UserPanel.this,
									"\""
									+ bookTitle
									+ "\" 반납 완료!");

							loadAllBooks();
							refreshMyRentalTable(table);

						} else {

							JOptionPane.showMessageDialog(
									UserPanel.this,
									"도서 반납 실패!");
						}

					} else if(status.equals("반납완료")) {

						int confirm =
								JOptionPane.showConfirmDialog(
										UserPanel.this,
										"\""
										+ bookTitle
										+ "\" 을 다시 대여하시겠습니까?",
										"도서 대여 확인",
										JOptionPane.YES_NO_OPTION);

						if(confirm != JOptionPane.YES_OPTION) {
							return;
						}

						RentalVO vo =
								new RentalVO();

						vo.setMemberId(
								loginUser.getMemberId());

						vo.setBookId(bookId);

						int result =
								rentalDao.rentBook(vo);

						if(result == 1) {

							JOptionPane.showMessageDialog(
									UserPanel.this,
									"\""
									+ bookTitle
									+ "\" 대여 완료!");

							loadAllBooks();
							refreshMyRentalTable(table);

						} else {
							JOptionPane.showMessageDialog(
									UserPanel.this,
									"현재 대여가 불가능한 도서입니다.");
						}
					}
				}
			}
		});

		table.getTableHeader()
		.setReorderingAllowed(false);

		JScrollPane scrollPane =
				new JScrollPane(table);

		scrollPane.setPreferredSize(
				new java.awt.Dimension(
						850,
						300));

		JOptionPane.showMessageDialog(
				this,
				scrollPane,
				"내 대여 정보 조회",
				JOptionPane.PLAIN_MESSAGE);
	}
	
	private void refreshMyRentalTable(JTable table) {

		RentalDAOImple dao = new RentalDAOImple();

		ArrayList<RentalJoinVO> list =
				dao.selectMyRental(loginUser.getMemberId());

		String[] columnNames = {
				"대여번호",
				"도서번호",
				"책 제목",
				"대여일",
				"반납예정일",
				"반납일",
				"상태"
		};

		String[][] data =
				new String[list.size()][7];

		for(int i = 0; i < list.size(); i++) {

			RentalJoinVO rental = list.get(i);

			data[i][0] = String.valueOf(rental.getRentalId());
			data[i][1] = String.valueOf(rental.getBookId());
			data[i][2] = rental.getTitle();
			data[i][3] = String.valueOf(rental.getRentalDate());
			data[i][4] = String.valueOf(rental.getDueDate());
			if(rental.getReturnDate() == null) {

				data[i][5] = "";

			} else {

				data[i][5] =
						String.valueOf(
								rental.getReturnDate());
			}
			data[i][6] = rental.getRentalStatus();
		}

		table.setModel(
				new javax.swing.table.DefaultTableModel(
						data,
						columnNames) {

					private static final long serialVersionUID = 1L;

					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				});
	}
	
	private void showBookPage() {

		int start =
				(currentPage - 1)
				* PAGE_SIZE;

		int end =
				Math.min(
						start + PAGE_SIZE,
						currentBookList.size());

		ArrayList<BookVO> pageList =
				new ArrayList<BookVO>();

		for(int i = start; i < end; i++) {

			pageList.add(
					currentBookList.get(i));
		}

		showBookTable(pageList);

		int totalPage =
				(int) Math.ceil(
						(double)
						currentBookList.size()
						/ PAGE_SIZE);

		if(totalPage == 0) {
			totalPage = 1;
		}

		pageLabel.setText(
				currentPage
				+ " / "
				+ totalPage);
	}

}