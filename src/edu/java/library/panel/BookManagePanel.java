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
import edu.java.library.vo.BookVO;

public class BookManagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable bookTable;
	
	// 페이징 처리용 변수
	private ArrayList<BookVO> currentBookList =
			new ArrayList<BookVO>();

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

		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);
		add(topBar);

		JLabel topTitle = new JLabel("만화책 대여 시스템");
		topTitle.setForeground(Color.WHITE);
		topTitle.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		topTitle.setBounds(25, 15, 300, 30);
		topBar.add(topTitle);

		JLabel pageTitle = new JLabel("도서 관리");
		pageTitle.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		pageTitle.setForeground(new Color(35, 40, 48));
		pageTitle.setBounds(60, 85, 250, 45);
		add(pageTitle);

		JLabel guide = new JLabel("도서 등록 후 목록에서 더블클릭하면 수정할 수 있습니다. 대여중인 도서는 수정/삭제할 수 없습니다.");
		guide.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		guide.setForeground(new Color(110, 118, 128));
		guide.setBounds(60, 125, 800, 30);
		add(guide);

		JLabel titleLabel = new JLabel("제목");
		titleLabel.setBounds(60, 165, 80, 20);
		add(titleLabel);

		String titlePlaceholder = "제목을 입력해주세요.";

		JTextField titleField = new JTextField(titlePlaceholder);
		titleField.setBounds(60, 190, 230, 40);
		titleField.setForeground(Color.GRAY);
		add(titleField);

		addPlaceholder(titleField, titlePlaceholder);

		JLabel authorLabel = new JLabel("작가");
		authorLabel.setBounds(310, 165, 80, 20);
		add(authorLabel);

		String authorPlaceholder = "작가를 입력해주세요.";

		JTextField authorField = new JTextField(authorPlaceholder);
		authorField.setBounds(310, 190, 200, 40);
		authorField.setForeground(Color.GRAY);
		add(authorField);

		addPlaceholder(authorField, authorPlaceholder);

		JLabel publisherLabel = new JLabel("출판사");
		publisherLabel.setBounds(530, 165, 80, 20);
		add(publisherLabel);

		String publisherPlaceholder = "출판사를 입력해주세요.";

		JTextField publisherField = new JTextField(publisherPlaceholder);
		publisherField.setBounds(530, 190, 200, 40);
		publisherField.setForeground(Color.GRAY);
		add(publisherField);

		addPlaceholder(publisherField, publisherPlaceholder);

		JButton insertBtn = new JButton("등록");
		insertBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		insertBtn.setForeground(Color.WHITE);
		insertBtn.setBackground(new Color(70, 135, 100));
		insertBtn.setFocusPainted(false);
		insertBtn.setBorderPainted(false);
		insertBtn.setBounds(750, 190, 90, 40);
		add(insertBtn);
		
		JLabel searchLabel =
				new JLabel("도서 검색");

		searchLabel.setBounds(
				60,
				230,
				80,
				20);

		add(searchLabel);

		String searchPlaceholder =
				"도서명을 입력해주세요.";

		JTextField searchField =
				new JTextField(
						searchPlaceholder);

		searchField.setBounds(
				60,
				250,
				300,
				38);

		searchField.setForeground(
				Color.GRAY);

		add(searchField);

		searchField.addFocusListener(
				new java.awt.event.FocusAdapter() {

			@Override
			public void focusGained(
					java.awt.event.FocusEvent e) {

				if(searchField.getText()
						.equals(
								searchPlaceholder)) {

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
							searchPlaceholder);

					searchField.setForeground(
							Color.GRAY);

					loadAllBooks();
				}
			}
		});

		JButton deleteBtn = new JButton("도서 삭제");
		deleteBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		deleteBtn.setBounds(800, 250, 120, 38);
		add(deleteBtn);

		bookTable = new JTable();
		bookTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		bookTable.setRowHeight(24);
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

					BookDAOImple dao = new BookDAOImple();

					BookVO book = dao.selectByBookId(bookId);

					if(book == null) {
						JOptionPane.showMessageDialog(
								BookManagePanel.this,
								"존재하지 않는 도서입니다.");
						return;
					}

					if(book.getBookStatus().equals("대여중")) {
						JOptionPane.showMessageDialog(
								BookManagePanel.this,
								"대여중인 도서는 수정할 수 없습니다.");
						return;
					}

					String newTitle;

					while(true) {
						newTitle =
								JOptionPane.showInputDialog(
										BookManagePanel.this,
										"수정할 제목을 입력해주십시오.\n\n"
										+ "기존 제목 : \""
										+ book.getTitle()
										+ "\"");

						if(newTitle == null) {
							return;
						}

						if(newTitle.isBlank()) {
							JOptionPane.showMessageDialog(
									BookManagePanel.this,
									"수정할 제목을 입력해주세요.");
							continue;
						}

						break;
					}

					String newAuthor;

					while(true) {
						newAuthor =
								JOptionPane.showInputDialog(
										BookManagePanel.this,
										"수정할 작가를 입력해주십시오.\n\n"
										+ "기존 작가 : \""
										+ book.getAuthor()
										+ "\"");

						if(newAuthor == null) {
							return;
						}

						if(newAuthor.isBlank()) {
							JOptionPane.showMessageDialog(
									BookManagePanel.this,
									"수정할 작가를 입력해주세요.");
							continue;
						}

						break;
					}

					String newPublisher;

					while(true) {
						newPublisher =
								JOptionPane.showInputDialog(
										BookManagePanel.this,
										"수정할 출판사를 입력해주십시오.\n\n"
										+ "기존 출판사 : \""
										+ book.getPublisher()
										+ "\"");

						if(newPublisher == null) {
							return;
						}

						if(newPublisher.isBlank()) {
							JOptionPane.showMessageDialog(
									BookManagePanel.this,
									"수정할 출판사를 입력해주세요.");
							continue;
						}

						break;
					}

					BookVO updateVo =
							new BookVO(
									bookId,
									newTitle,
									newAuthor,
									newPublisher,
									book.getBookStatus());

					int result =
							dao.update(updateVo);

					if(result == 1) {
						JOptionPane.showMessageDialog(
								BookManagePanel.this,
								"\""
								+ newTitle
								+ "\" 수정 완료!");

						loadAllBooks();

					} else {
						JOptionPane.showMessageDialog(
								BookManagePanel.this,
								"도서 수정 실패!");
					}
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(bookTable);
		scrollPane.setBounds(60, 300, 880, 264);
		add(scrollPane);
		
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
		

		JButton backBtn = new JButton("뒤로가기");
		backBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		backBtn.setForeground(new Color(70, 75, 85));
		backBtn.setBackground(new Color(248, 249, 251));
		backBtn.setBorderPainted(false);
		backBtn.setFocusPainted(false);
		backBtn.setBounds(810, 575, 130, 35);
		add(backBtn);

		loadAllBooks();

		insertBtn.addActionListener(e -> {

			String bookTitle = titleField.getText().trim();
			String author = authorField.getText().trim();
			String publisher = publisherField.getText().trim();

			if(bookTitle.isBlank()
					|| author.isBlank()
					|| publisher.isBlank()
					|| bookTitle.equals(titlePlaceholder)
					|| author.equals(authorPlaceholder)
					|| publisher.equals(publisherPlaceholder)) {

				JOptionPane.showMessageDialog(
						this,
						"제목, 작가, 출판사를 모두 입력하세요.");

				return;
			}

			BookVO vo = new BookVO();
			vo.setTitle(bookTitle);
			vo.setAuthor(author);
			vo.setPublisher(publisher);
			vo.setBookStatus("대여가능");

			BookDAOImple dao = new BookDAOImple();

			int result = dao.insert(vo);

			if(result == 1) {

				JOptionPane.showMessageDialog(
						this,
						"\""
						+ bookTitle
						+ "\" 등록 완료!");

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
						"도서 등록 실패!");
			}
		});

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

		deleteBtn.addActionListener(e -> {

			int row = bookTable.getSelectedRow();

			if(row == -1) {
				JOptionPane.showMessageDialog(
						this,
						"삭제할 도서를 선택하세요.");
				return;
			}

			int bookId =
					Integer.parseInt(
							bookTable.getValueAt(row, 0).toString());

			BookDAOImple dao = new BookDAOImple();

			BookVO book =
					dao.selectByBookId(bookId);

			if(book == null) {
				JOptionPane.showMessageDialog(
						this,
						"존재하지 않는 도서입니다.");
				return;
			}

			if(book.getBookStatus().equals("대여중")) {
				JOptionPane.showMessageDialog(
						this,
						"대여중인 도서는 삭제할 수 없습니다.");
				return;
			}

			int confirm =
					JOptionPane.showConfirmDialog(
							this,
							"\""
							+ book.getTitle()
							+ "\" 을 삭제하시겠습니까?",
							"도서 삭제 확인",
							JOptionPane.YES_NO_OPTION);

			if(confirm != JOptionPane.YES_OPTION) {
				return;
			}

			int result =
					dao.delete(bookId);

			if(result == 1) {
				JOptionPane.showMessageDialog(
						this,
						"\""
						+ book.getTitle()
						+ "\" 삭제 완료!");

				loadAllBooks();

			} else {
				JOptionPane.showMessageDialog(
						this,
						"도서 삭제 실패!");
			}
		});

		backBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "admin");
		});
	}

	private void loadAllBooks() {

		BookDAOImple dao =
				new BookDAOImple();

		currentBookList =
				dao.selectAll();

		currentPage = 1;

		showBookPage();
	}

	private void searchBooks(String keyword) {

		keyword = keyword.trim();

		if(keyword.isBlank()
				|| keyword.equals(
						"도서명을 입력해주세요.")) {

			loadAllBooks();
			return;
		}

		BookDAOImple dao = new BookDAOImple();

		currentBookList =
				dao.selectByTitle(keyword);

		currentPage = 1;

		showBookPage();
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

			BookVO book =
					list.get(i);

			data[i][0] =
					String.valueOf(
							book.getBookId());

			data[i][1] =
					book.getTitle();

			data[i][2] =
					book.getAuthor();

			data[i][3] =
					book.getPublisher();

			data[i][4] =
					book.getBookStatus();
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
	private void addPlaceholder(
			JTextField field,
			String placeholder) {

		field.addFocusListener(
				new java.awt.event.FocusAdapter() {

			@Override
			public void focusGained(
					java.awt.event.FocusEvent e) {

				if(field.getText().equals(placeholder)) {
					field.setText("");
					field.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(
					java.awt.event.FocusEvent e) {

				if(field.getText().trim().isEmpty()) {
					field.setText(placeholder);
					field.setForeground(Color.GRAY);
				}
			}
		});
	}
}