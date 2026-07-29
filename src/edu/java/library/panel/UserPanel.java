package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

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

	private ArrayList<BookVO> currentBookList = new ArrayList<>();

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

		// 상단 바
		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);
		add(topBar);

		JLabel title = new JLabel("漫画貸出管理システム");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
		title.setBounds(25, 15, 300, 30);
		topBar.add(title);

		// 도서 목록 제목
		JLabel pageTitle = new JLabel("書籍一覧");
		pageTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 30));
		pageTitle.setForeground(new Color(35, 40, 48));
		pageTitle.setBounds(60, 85, 250, 45);
		add(pageTitle);

		JLabel guide = new JLabel(
				"貸出可能な書籍はダブルクリックで借りられます。自分が借りた書籍はダブルクリックで返却できます。");

		guide.setFont(new Font("Yu Gothic UI", Font.PLAIN, 14));
		guide.setForeground(new Color(110, 118, 128));
		guide.setBounds(60, 130, 850, 30);
		add(guide);

		// 도서 검색
		JLabel searchLabel = new JLabel("書籍検索");
		searchLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		searchLabel.setBounds(60, 165, 80, 20);
		add(searchLabel);

		String searchPlaceholder = "書籍名を入力してください。";

		JTextField searchField = new JTextField(searchPlaceholder);
		searchField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		searchField.setBounds(60, 190, 300, 40);
		searchField.setForeground(Color.GRAY);
		add(searchField);

		searchField.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {

				if (searchField.getText().equals(searchPlaceholder)) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				if (searchField.getText().trim().isEmpty()) {
					searchField.setText(searchPlaceholder);
					searchField.setForeground(Color.GRAY);
					loadAllBooks();
				}
			}
		});

		// 내 대여 정보 버튼
		JButton myRentalBtn = new JButton("自分の貸出情報");
		myRentalBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
		myRentalBtn.setBounds(760, 190, 150, 40);
		add(myRentalBtn);

		// 로그아웃 버튼
		JButton logoutBtn = new JButton("ログアウト");
		logoutBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 15));
		logoutBtn.setForeground(new Color(70, 75, 85));
		logoutBtn.setBackground(new Color(248, 249, 251));
		logoutBtn.setBorderPainted(false);
		logoutBtn.setFocusPainted(false);
		logoutBtn.setBounds(800, 555, 130, 35);
		add(logoutBtn);

		// 도서 목록
		bookTable = new JTable();
		bookTable.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		bookTable.setRowHeight(28);
		bookTable.getTableHeader().setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
		bookTable.getTableHeader().setReorderingAllowed(false);

		bookTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() != 2) {
					return;
				}

				int row = bookTable.getSelectedRow();

				if (row == -1) {
					return;
				}

				if (loginUser == null) {
					JOptionPane.showMessageDialog(UserPanel.this, "ログイン情報を確認できません。");
					return;
				}

				int bookId = Integer.parseInt(bookTable.getValueAt(row, 0).toString());
				String bookTitle = bookTable.getValueAt(row, 1).toString();

				BookDAOImple bookDao = new BookDAOImple();
				BookVO selectedBook = bookDao.selectByBookId(bookId);

				if (selectedBook == null) {
					JOptionPane.showMessageDialog(UserPanel.this, "該当する書籍が存在しません。");
					loadAllBooks();
					return;
				}

				// DB에 저장된 상태값이므로 한국어로 비교
				String status = selectedBook.getBookStatus();

				if (status.equals("貸出可能")) {

					int confirm = JOptionPane.showConfirmDialog(
							UserPanel.this,
							"「" + bookTitle + "」を借りますか？",
							"書籍貸出の確認",
							JOptionPane.YES_NO_OPTION);

					if (confirm != JOptionPane.YES_OPTION) {
						return;
					}

					RentalVO vo = new RentalVO();
					vo.setMemberId(loginUser.getMemberId());
					vo.setBookId(bookId);

					RentalDAOImple rentalDao = new RentalDAOImple();
					int result = rentalDao.rentBook(vo);

					if (result == 1) {
						JOptionPane.showMessageDialog(UserPanel.this, "「" + bookTitle + "」を貸し出しました。");
						loadAllBooks();
					} else {
						JOptionPane.showMessageDialog(UserPanel.this, "書籍の貸出に失敗しました。");
					}

				} else if (status.equals("貸出中")) {

					int confirm = JOptionPane.showConfirmDialog(
							UserPanel.this,
							"「" + bookTitle + "」を返却しますか？",
							"書籍返却の確認",
							JOptionPane.YES_NO_OPTION);

					if (confirm != JOptionPane.YES_OPTION) {
						return;
					}

					RentalDAOImple rentalDao = new RentalDAOImple();
					int result = rentalDao.returnBookByBookId(loginUser.getMemberId(), bookId);

					if (result == 1) {
						JOptionPane.showMessageDialog(UserPanel.this, "「" + bookTitle + "」を返却しました。");
						loadAllBooks();
					} else {
						JOptionPane.showMessageDialog(
								UserPanel.this,
								"書籍の返却に失敗しました。\n自分が借りた書籍のみ返却できます。");
					}

				} else {
					JOptionPane.showMessageDialog(UserPanel.this, "現在、この書籍を操作することはできません。");
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(bookTable);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setWheelScrollingEnabled(false);
		scrollPane.setBounds(60, 255, 880, 300);
		add(scrollPane);

		// 페이지 이동 버튼
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

			if (currentPage > 1) {
				currentPage--;
				showBookPage();
			}
		});

		nextPageBtn.addActionListener(e -> {

			int totalPage = (int) Math.ceil((double) currentBookList.size() / PAGE_SIZE);

			if (currentPage < totalPage) {
				currentPage++;
				showBookPage();
			}
		});

		lastPageBtn.addActionListener(e -> {

			int totalPage = (int) Math.ceil((double) currentBookList.size() / PAGE_SIZE);

			if (totalPage == 0) {
				totalPage = 1;
			}

			currentPage = totalPage;
			showBookPage();
		});

		loadAllBooks();

		// 도서 검색 이벤트
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				searchBooks(searchField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				searchBooks(searchField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				searchBooks(searchField.getText());
			}
		});

		myRentalBtn.addActionListener(e -> {
			showMyRentalInfo();
		});

		logoutBtn.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "ログアウトしました。");
			cardLayout.show(cardPanel, "login");
		});
	}

	public void setLoginUser(MemberVO loginUser) {
		this.loginUser = loginUser;
		loadAllBooks();
	}

	private void loadAllBooks() {

		BookDAOImple dao = new BookDAOImple();
		currentBookList = dao.selectAll();

		currentPage = 1;
		showBookPage();
	}

	private void searchBooks(String keyword) {

		keyword = keyword.trim();

		if (keyword.equals("書籍名を入力してください。") || keyword.isBlank()) {
			loadAllBooks();
			return;
		}

		BookDAOImple dao = new BookDAOImple();
		currentBookList = dao.selectByTitle(keyword);

		currentPage = 1;
		showBookPage();
	}

	private void showBookTable(ArrayList<BookVO> list) {

		String[] columnNames = {
				"書籍番号",
				"書名",
				"著者",
				"出版社",
				"状態"
		};

		String[][] data = new String[list.size()][5];

		for (int i = 0; i < list.size(); i++) {

			BookVO book = list.get(i);

			data[i][0] = String.valueOf(book.getBookId());
			data[i][1] = book.getTitle();
			data[i][2] = book.getAuthor();
			data[i][3] = book.getPublisher();
			data[i][4] = book.getBookStatus();
		}

		bookTable.setModel(new DefaultTableModel(data, columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}

	private void showMyRentalInfo() {

		if (loginUser == null) {
			JOptionPane.showMessageDialog(this, "ログイン情報を確認できません。");
			return;
		}

		RentalDAOImple dao = new RentalDAOImple();
		ArrayList<RentalJoinVO> list = dao.selectMyRental(loginUser.getMemberId());

		if (list == null || list.isEmpty()) {
			JOptionPane.showMessageDialog(this, "貸出履歴がありません。");
			return;
		}

		String[] columnNames = {
				"貸出番号",
				"書籍番号",
				"書名",
				"貸出日",
				"返却期限",
				"返却日",
				"状態"
		};

		String[][] data = createRentalTableData(list);

		JTable table = new JTable(new DefaultTableModel(data, columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		table.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		table.setRowHeight(28);
		table.getTableHeader().setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
		table.getTableHeader().setReorderingAllowed(false);

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() != 2) {
					return;
				}

				int row = table.getSelectedRow();

				if (row == -1) {
					return;
				}

				int bookId = Integer.parseInt(table.getValueAt(row, 1).toString());
				String bookTitle = table.getValueAt(row, 2).toString();
				String status = table.getValueAt(row, 6).toString();

				RentalDAOImple rentalDao = new RentalDAOImple();

				if (status.equals("貸出中") || status.equals("延滞中")) {

					int confirm = JOptionPane.showConfirmDialog(
							UserPanel.this,
							"「" + bookTitle + "」を返却しますか？",
							"書籍返却の確認",
							JOptionPane.YES_NO_OPTION);

					if (confirm != JOptionPane.YES_OPTION) {
						return;
					}

					int result = rentalDao.returnBookByBookId(loginUser.getMemberId(), bookId);

					if (result == 1) {
						JOptionPane.showMessageDialog(UserPanel.this, "「" + bookTitle + "」を返却しました。");
						loadAllBooks();
						refreshMyRentalTable(table);
					} else {
						JOptionPane.showMessageDialog(UserPanel.this, "書籍の返却に失敗しました。");
					}

				} else if (status.equals("返却済み")) {

					int confirm = JOptionPane.showConfirmDialog(
							UserPanel.this,
							"「" + bookTitle + "」をもう一度借りますか？",
							"書籍貸出の確認",
							JOptionPane.YES_NO_OPTION);

					if (confirm != JOptionPane.YES_OPTION) {
						return;
					}

					RentalVO vo = new RentalVO();
					vo.setMemberId(loginUser.getMemberId());
					vo.setBookId(bookId);

					int result = rentalDao.rentBook(vo);

					if (result == 1) {
						JOptionPane.showMessageDialog(UserPanel.this, "「" + bookTitle + "」を貸し出しました。");
						loadAllBooks();
						refreshMyRentalTable(table);
					} else {
						JOptionPane.showMessageDialog(UserPanel.this, "現在、この書籍は貸出できません。");
					}
				}
			}
		});

		JScrollPane rentalScrollPane = new JScrollPane(table);
		rentalScrollPane.setPreferredSize(new Dimension(850, 300));

		JOptionPane.showMessageDialog(
				this,
				rentalScrollPane,
				"自分の貸出情報",
				JOptionPane.PLAIN_MESSAGE);
	}

	private void refreshMyRentalTable(JTable table) {

		RentalDAOImple dao = new RentalDAOImple();
		ArrayList<RentalJoinVO> list = dao.selectMyRental(loginUser.getMemberId());

		String[] columnNames = {
				"貸出番号",
				"書籍番号",
				"書名",
				"貸出日",
				"返却期限",
				"返却日",
				"状態"
		};

		String[][] data = createRentalTableData(list);

		table.setModel(new DefaultTableModel(data, columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}

	private String[][] createRentalTableData(ArrayList<RentalJoinVO> list) {

		String[][] data = new String[list.size()][7];

		for (int i = 0; i < list.size(); i++) {

			RentalJoinVO rental = list.get(i);

			data[i][0] = String.valueOf(rental.getRentalId());
			data[i][1] = String.valueOf(rental.getBookId());
			data[i][2] = rental.getTitle();
			data[i][3] = String.valueOf(rental.getRentalDate());
			data[i][4] = String.valueOf(rental.getDueDate());

			if (rental.getReturnDate() == null) {
				data[i][5] = "";
			} else {
				data[i][5] = String.valueOf(rental.getReturnDate());
			}

			data[i][6] =
					rental.getRentalStatus();
		}

		return data;
	}

	private void showBookPage() {

		int start = (currentPage - 1) * PAGE_SIZE;
		int end = Math.min(start + PAGE_SIZE, currentBookList.size());

		ArrayList<BookVO> pageList = new ArrayList<>();

		for (int i = start; i < end; i++) {
			pageList.add(currentBookList.get(i));
		}

		showBookTable(pageList);

		int totalPage = (int) Math.ceil((double) currentBookList.size() / PAGE_SIZE);

		if (totalPage == 0) {
			totalPage = 1;
		}

		pageLabel.setText(currentPage + " / " + totalPage);
	}

}