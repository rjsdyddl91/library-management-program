package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
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
import edu.java.library.vo.BookVO;

public class BookManagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable bookTable;

	// 페이징 처리용 변수
	private ArrayList<BookVO> currentBookList = new ArrayList<>();

	private int currentPage = 1;
	private final int PAGE_SIZE = 10;

	// 페이지 버튼
	private JButton firstPageBtn;
	private JButton prevPageBtn;
	private JButton nextPageBtn;
	private JButton lastPageBtn;
	private JLabel pageLabel;

	public BookManagePanel(CardLayout cardLayout, JPanel cardPanel) {

		setLayout(null);
		setBackground(new Color(248, 249, 251));

		// 상단 바
		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);
		add(topBar);

		JLabel topTitle = new JLabel("漫画貸出管理システム");
		topTitle.setForeground(Color.WHITE);
		topTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
		topTitle.setBounds(25, 15, 300, 30);
		topBar.add(topTitle);

		// 도서 관리 제목
		JLabel pageTitle = new JLabel("書籍管理");
		pageTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 30));
		pageTitle.setForeground(new Color(35, 40, 48));
		pageTitle.setBounds(60, 85, 250, 45);
		add(pageTitle);

		JLabel guide = new JLabel(
				"書籍を登録後、一覧をダブルクリックすると編集できます。貸出中の書籍は編集・削除できません。");

		guide.setFont(new Font("Yu Gothic UI", Font.PLAIN, 14));
		guide.setForeground(new Color(110, 118, 128));
		guide.setBounds(60, 125, 850, 30);
		add(guide);

		// 도서 등록 입력 영역
		JLabel titleLabel = new JLabel("書名");
		titleLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		titleLabel.setBounds(60, 165, 80, 20);
		add(titleLabel);

		String titlePlaceholder = "書名を入力してください。";

		JTextField titleField = new JTextField(titlePlaceholder);
		titleField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		titleField.setBounds(60, 190, 230, 40);
		titleField.setForeground(Color.GRAY);
		add(titleField);

		addPlaceholder(titleField, titlePlaceholder);

		JLabel authorLabel = new JLabel("著者");
		authorLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		authorLabel.setBounds(310, 165, 80, 20);
		add(authorLabel);

		String authorPlaceholder = "著者名を入力してください。";

		JTextField authorField = new JTextField(authorPlaceholder);
		authorField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		authorField.setBounds(310, 190, 200, 40);
		authorField.setForeground(Color.GRAY);
		add(authorField);

		addPlaceholder(authorField, authorPlaceholder);

		JLabel publisherLabel = new JLabel("出版社");
		publisherLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		publisherLabel.setBounds(530, 165, 80, 20);
		add(publisherLabel);

		String publisherPlaceholder = "出版社を入力してください。";

		JTextField publisherField = new JTextField(publisherPlaceholder);
		publisherField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		publisherField.setBounds(530, 190, 200, 40);
		publisherField.setForeground(Color.GRAY);
		add(publisherField);

		addPlaceholder(publisherField, publisherPlaceholder);

		JButton insertBtn = new JButton("登録");
		insertBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
		insertBtn.setForeground(Color.WHITE);
		insertBtn.setBackground(new Color(70, 135, 100));
		insertBtn.setFocusPainted(false);
		insertBtn.setBorderPainted(false);
		insertBtn.setBounds(750, 190, 90, 40);
		add(insertBtn);

		// 도서 검색 영역
		JLabel searchLabel = new JLabel("書籍検索");
		searchLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		searchLabel.setBounds(60, 230, 80, 20);
		add(searchLabel);

		String searchPlaceholder = "書籍名を入力してください。";

		JTextField searchField = new JTextField(searchPlaceholder);
		searchField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		searchField.setBounds(60, 250, 300, 38);
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

		JButton deleteBtn = new JButton("書籍削除");
		deleteBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
		deleteBtn.setBounds(800, 250, 120, 38);
		add(deleteBtn);

		// 도서 목록
		bookTable = new JTable();
		bookTable.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		bookTable.setRowHeight(24);
		bookTable.getTableHeader().setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
		bookTable.getTableHeader().setReorderingAllowed(false);

		// 도서 더블클릭 수정 이벤트
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

				int bookId = Integer.parseInt(
						bookTable.getValueAt(row, 0).toString());

				BookDAOImple dao = new BookDAOImple();
				BookVO book = dao.selectByBookId(bookId);

				if (book == null) {
					JOptionPane.showMessageDialog(
							BookManagePanel.this,
							"該当する書籍が存在しません。");

					return;
				}

				// DB에 저장된 상태값이므로 현재는 한국어 유지
				if (book.getBookStatus().equals("대여중")) {
					JOptionPane.showMessageDialog(
							BookManagePanel.this,
							"貸出中の書籍は編集できません。");

					return;
				}

				String newTitle;

				while (true) {

					newTitle = JOptionPane.showInputDialog(
							BookManagePanel.this,
							"変更後の書名を入力してください。\n\n"
							+ "現在の書名：「"
							+ book.getTitle()
							+ "」");

					if (newTitle == null) {
						return;
					}

					if (newTitle.isBlank()) {
						JOptionPane.showMessageDialog(
								BookManagePanel.this,
								"変更後の書名を入力してください。");

						continue;
					}

					break;
				}

				String newAuthor;

				while (true) {

					newAuthor = JOptionPane.showInputDialog(
							BookManagePanel.this,
							"変更後の著者名を入力してください。\n\n"
							+ "現在の著者：「"
							+ book.getAuthor()
							+ "」");

					if (newAuthor == null) {
						return;
					}

					if (newAuthor.isBlank()) {
						JOptionPane.showMessageDialog(
								BookManagePanel.this,
								"変更後の著者名を入力してください。");

						continue;
					}

					break;
				}

				String newPublisher;

				while (true) {

					newPublisher = JOptionPane.showInputDialog(
							BookManagePanel.this,
							"変更後の出版社を入力してください。\n\n"
							+ "現在の出版社：「"
							+ book.getPublisher()
							+ "」");

					if (newPublisher == null) {
						return;
					}

					if (newPublisher.isBlank()) {
						JOptionPane.showMessageDialog(
								BookManagePanel.this,
								"変更後の出版社を入力してください。");

						continue;
					}

					break;
				}

				BookVO updateVo = new BookVO(
						bookId,
						newTitle,
						newAuthor,
						newPublisher,
						book.getBookStatus());

				int result = dao.update(updateVo);

				if (result == 1) {
					JOptionPane.showMessageDialog(
							BookManagePanel.this,
							"「" + newTitle + "」の更新が完了しました。");

					loadAllBooks();

				} else {
					JOptionPane.showMessageDialog(
							BookManagePanel.this,
							"書籍の更新に失敗しました。");
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(bookTable);
		scrollPane.setBounds(60, 300, 880, 264);
		add(scrollPane);

		// 페이지 이동 버튼
		firstPageBtn = new JButton("<<");
		firstPageBtn.setBounds(325, 570, 55, 28);
		add(firstPageBtn);

		prevPageBtn = new JButton("<");
		prevPageBtn.setBounds(390, 570, 55, 28);
		add(prevPageBtn);

		pageLabel = new JLabel("1 / 1");
		pageLabel.setHorizontalAlignment(JLabel.CENTER);
		pageLabel.setBounds(455, 570, 90, 28);
		add(pageLabel);

		nextPageBtn = new JButton(">");
		nextPageBtn.setBounds(555, 570, 55, 28);
		add(nextPageBtn);

		lastPageBtn = new JButton(">>");
		lastPageBtn.setBounds(620, 570, 55, 28);
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

			int totalPage = (int) Math.ceil(
					(double) currentBookList.size() / PAGE_SIZE);

			if (currentPage < totalPage) {
				currentPage++;
				showBookPage();
			}
		});

		lastPageBtn.addActionListener(e -> {

			int totalPage = (int) Math.ceil(
					(double) currentBookList.size() / PAGE_SIZE);

			if (totalPage == 0) {
				totalPage = 1;
			}

			currentPage = totalPage;
			showBookPage();
		});

		JButton backBtn = new JButton("戻る");
		backBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 15));
		backBtn.setForeground(new Color(70, 75, 85));
		backBtn.setBackground(new Color(248, 249, 251));
		backBtn.setBorderPainted(false);
		backBtn.setFocusPainted(false);
		backBtn.setBounds(810, 575, 130, 35);
		add(backBtn);

		loadAllBooks();

		// 도서 등록 이벤트
		insertBtn.addActionListener(e -> {

			String bookTitle = titleField.getText().trim();
			String author = authorField.getText().trim();
			String publisher = publisherField.getText().trim();

			if (bookTitle.isBlank()
					|| author.isBlank()
					|| publisher.isBlank()
					|| bookTitle.equals(titlePlaceholder)
					|| author.equals(authorPlaceholder)
					|| publisher.equals(publisherPlaceholder)) {

				JOptionPane.showMessageDialog(
						this,
						"書名、著者、出版社をすべて入力してください。");

				return;
			}

			BookVO vo = new BookVO();
			vo.setTitle(bookTitle);
			vo.setAuthor(author);
			vo.setPublisher(publisher);

			// DB에 저장되는 상태값이므로 현재는 한국어 유지
			vo.setBookStatus("대여가능");

			BookDAOImple dao = new BookDAOImple();
			int result = dao.insert(vo);

			if (result == 1) {

				JOptionPane.showMessageDialog(
						this,
						"「" + bookTitle + "」を登録しました。");

				titleField.setText(titlePlaceholder);
				authorField.setText(authorPlaceholder);
				publisherField.setText(publisherPlaceholder);

				titleField.setForeground(Color.GRAY);
				authorField.setForeground(Color.GRAY);
				publisherField.setForeground(Color.GRAY);

				loadAllBooks();

			} else {
				JOptionPane.showMessageDialog(
						this,
						"書籍の登録に失敗しました。");
			}
		});

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

		// 도서 삭제 이벤트
		deleteBtn.addActionListener(e -> {

			int row = bookTable.getSelectedRow();

			if (row == -1) {
				JOptionPane.showMessageDialog(
						this,
						"削除する書籍を選択してください。");

				return;
			}

			int bookId = Integer.parseInt(
					bookTable.getValueAt(row, 0).toString());

			BookDAOImple dao = new BookDAOImple();
			BookVO book = dao.selectByBookId(bookId);

			if (book == null) {
				JOptionPane.showMessageDialog(
						this,
						"該当する書籍が存在しません。");

				return;
			}

			// DB에 저장된 상태값이므로 현재는 한국어 유지
			if (book.getBookStatus().equals("대여중")) {
				JOptionPane.showMessageDialog(
						this,
						"貸出中の書籍は削除できません。");

				return;
			}

			int confirm = JOptionPane.showConfirmDialog(
					this,
					"「" + book.getTitle() + "」を削除しますか？",
					"書籍削除の確認",
					JOptionPane.YES_NO_OPTION);

			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

			int result = dao.delete(bookId);

			if (result == 1) {
				JOptionPane.showMessageDialog(
						this,
						"「" + book.getTitle() + "」を削除しました。");

				loadAllBooks();

			} else {
				JOptionPane.showMessageDialog(
						this,
						"書籍の削除に失敗しました。");
			}
		});

		backBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "admin");
		});
	}

	private void loadAllBooks() {

		BookDAOImple dao = new BookDAOImple();
		currentBookList = dao.selectAll();

		currentPage = 1;
		showBookPage();
	}

	private void searchBooks(String keyword) {

		keyword = keyword.trim();

		if (keyword.isBlank()
				|| keyword.equals("書籍名を入力してください。")) {

			loadAllBooks();
			return;
		}

		BookDAOImple dao = new BookDAOImple();
		currentBookList = dao.selectByTitle(keyword);

		currentPage = 1;
		showBookPage();
	}

	private void showBookPage() {

		int start = (currentPage - 1) * PAGE_SIZE;
		int end = Math.min(start + PAGE_SIZE, currentBookList.size());

		ArrayList<BookVO> pageList = new ArrayList<>();

		for (int i = start; i < end; i++) {
			pageList.add(currentBookList.get(i));
		}

		showBookTable(pageList);

		int totalPage = (int) Math.ceil(
				(double) currentBookList.size() / PAGE_SIZE);

		if (totalPage == 0) {
			totalPage = 1;
		}

		pageLabel.setText(currentPage + " / " + totalPage);
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
			data[i][4] = convertBookStatus(book.getBookStatus());
		}

		bookTable.setModel(new DefaultTableModel(data, columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}

	// DB의 한국어 상태값을 화면에만 일본어로 표시
	private String convertBookStatus(String status) {

		if (status == null) {
			return "";
		}

		switch (status) {

		case "대여가능":
			return "貸出可能";

		case "대여중":
			return "貸出中";

		case "반납완료":
			return "返却済み";

		case "연체중":
			return "延滞中";

		default:
			return status;
		}
	}

	private void addPlaceholder(JTextField field, String placeholder) {

		field.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {

				if (field.getText().equals(placeholder)) {
					field.setText("");
					field.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				if (field.getText().trim().isEmpty()) {
					field.setText(placeholder);
					field.setForeground(Color.GRAY);
				}
			}
		});
	}
}