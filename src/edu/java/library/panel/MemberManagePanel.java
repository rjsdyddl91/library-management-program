package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.java.library.dao.MemberDAOImple;
import edu.java.library.dao.RentalDAOImple;
import edu.java.library.vo.MemberVO;
import edu.java.library.vo.OverdueJoinVO;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MemberManagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable memberTable;
	
	// 페이징 처리용 변수
	private ArrayList<MemberVO> currentMemberList =
			new ArrayList<MemberVO>();

	private int currentPage = 1;

	private final int PAGE_SIZE = 10;

	// 페이지 버튼
	private JButton firstPageBtn;
	private JButton prevPageBtn;
	private JButton nextPageBtn;
	private JButton lastPageBtn;
	private JLabel pageLabel;

	public MemberManagePanel(CardLayout cardLayout, JPanel cardPanel) {

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

		JLabel pageTitle = new JLabel("회원 관리");
		pageTitle.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		pageTitle.setForeground(new Color(35, 40, 48));
		pageTitle.setBounds(60, 85, 250, 45);
		add(pageTitle);

		JLabel guide = new JLabel("회원 더블클릭 시 대여 정보 조회 / 관리자 계정은 수정·삭제할 수 없습니다.");
		guide.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		guide.setForeground(new Color(110, 118, 128));
		guide.setBounds(60, 130, 700, 30);
		add(guide);
		
		JLabel searchLabel = new JLabel("회원 검색");
		searchLabel.setBounds(60, 165, 80, 20);
		add(searchLabel);
		

		String searchPlaceholder = "회원명을 입력해주세요.";

		JTextField searchField = new JTextField(searchPlaceholder);
		searchField.setBounds(60, 190, 300, 40);
		searchField.setForeground(Color.GRAY);
		add(searchField);

		addPlaceholder(searchField, searchPlaceholder);

		JButton overdueBtn = new JButton("연체 회원 보기");
		overdueBtn.setBounds(760, 190, 150, 40);
		add(overdueBtn);
		
		// 연체 회원 보기 버튼 클릭 이벤트
		overdueBtn.addActionListener(e -> {

			// RentalDAO 객체 생성
			RentalDAOImple dao =
					new RentalDAOImple();

			// 연체 회원 목록 조회
			ArrayList<OverdueJoinVO> list =
					dao.selectOverdueList();

			// 연체 회원 없을 경우
			if(list.isEmpty()) {

				JOptionPane.showMessageDialog(
						this,
						"연체중인 회원이 없습니다.");

				return;
			}

			// 연체 회원 조회 창 생성
			javax.swing.JDialog dialog =
					new javax.swing.JDialog();

			dialog.setTitle(
					"연체 회원 목록");

			dialog.setSize(
					900,
					400);

			dialog.setLocationRelativeTo(
					this);

			dialog.setModal(true);

			// JTable 데이터 생성
			String[] columnNames = {
					"회원번호",
					"이름",
					"전화번호",
					"이메일",
					"도서명",
					"대여일",
					"반납예정일",
					"상태"
			};

			String[][] data =
					new String[list.size()][8];

			for(int i = 0; i < list.size(); i++) {

				OverdueJoinVO vo =
						list.get(i);

				data[i][0] =
						String.valueOf(
								vo.getMemberId());

				data[i][1] =
						vo.getName();

				data[i][2] =
						vo.getPhone();

				data[i][3] =
						vo.getEmail();

				data[i][4] =
						vo.getTitle();

				data[i][5] =
						String.valueOf(
								vo.getRentalDate());

				data[i][6] =
						String.valueOf(
								vo.getDueDate());

				data[i][7] =
						vo.getStatus();
			}

			// JTable 생성
			JTable overdueTable =
					new JTable(
							data,
							columnNames);

			overdueTable.setRowHeight(
					28);

			overdueTable.getTableHeader()
					.setReorderingAllowed(
							false);

			JScrollPane scrollPane =
					new JScrollPane(
							overdueTable);

			dialog.add(scrollPane);

			// 창 열기
			dialog.setVisible(true);
		});

		memberTable = new JTable();
		memberTable.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		memberTable.setRowHeight(24);
		memberTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if(e.getClickCount() == 2) {

					int row = memberTable.getSelectedRow();

					if(row == -1) {
						return;
					}

					int memberId =
							Integer.parseInt(
									memberTable.getValueAt(row, 0).toString());

					MemberDAOImple dao = new MemberDAOImple();

					MemberVO member =
							dao.selectByMemberId(memberId);

					if(member == null) {
						JOptionPane.showMessageDialog(
								MemberManagePanel.this,
								"존재하지 않는 회원입니다.");
						return;
					}

					for(java.awt.Component comp : cardPanel.getComponents()) {

						if(comp instanceof MemberRentalInfoPanel) {

							((MemberRentalInfoPanel) comp).setMember(member);
							break;
						}
					}

					cardLayout.show(cardPanel, "memberRentalInfo");
				}
			}
		});
		memberTable.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollPane = new JScrollPane(memberTable);
		scrollPane.setBounds(60, 255, 880, 264);
		add(scrollPane);
		
		firstPageBtn = new JButton("<<");
		firstPageBtn.setBounds(325, 570, 55, 28);
		add(firstPageBtn);

		prevPageBtn = new JButton("<");
		prevPageBtn.setBounds(390, 570, 55, 28);
		add(prevPageBtn);

		pageLabel = new JLabel("1 / 1");
		pageLabel.setHorizontalAlignment(
				JLabel.CENTER);
		pageLabel.setBounds(
				455,
				570,
				90,
				28);

		add(pageLabel);

		nextPageBtn = new JButton(">");
		nextPageBtn.setBounds(
				555,
				570,
				55,
				28);

		add(nextPageBtn);

		lastPageBtn = new JButton(">>");
		lastPageBtn.setBounds(
				620,
				570,
				55,
				28);

		add(lastPageBtn);
		
		firstPageBtn.addActionListener(e -> {

			currentPage = 1;
			showMemberPage();
		});

		prevPageBtn.addActionListener(e -> {

			if(currentPage > 1) {

				currentPage--;
				showMemberPage();
			}
		});

		nextPageBtn.addActionListener(e -> {

			int totalPage =
					(int) Math.ceil(
							(double)
							currentMemberList.size()
							/ PAGE_SIZE);

			if(currentPage < totalPage) {

				currentPage++;
				showMemberPage();
			}
		});

		lastPageBtn.addActionListener(e -> {

			int totalPage =
					(int) Math.ceil(
							(double)
							currentMemberList.size()
							/ PAGE_SIZE);

			if(totalPage == 0) {
				totalPage = 1;
			}

			currentPage = totalPage;
			showMemberPage();
		});

		JButton updateBtn = new JButton("회원 수정");
		updateBtn.setBounds(60, 535, 120, 38);
		add(updateBtn);

		updateBtn.addActionListener(e -> {

			int row =
					memberTable.getSelectedRow();

			if(row == -1) {

				JOptionPane.showMessageDialog(
						this,
						"수정할 회원을 선택하세요.");

				return;
			}

			int memberId =
					Integer.parseInt(
							memberTable.getValueAt(
									row,
									0)
							.toString());

			MemberDAOImple dao =
					new MemberDAOImple();

			MemberVO member =
					dao.selectByMemberId(
							memberId);

			if(member == null) {

				JOptionPane.showMessageDialog(
						this,
						"존재하지 않는 회원입니다.");

				return;
			}

			// 관리자 계정 수정 불가
			if(member.getEmail()
					.equals("admin")) {

				JOptionPane.showMessageDialog(
						this,
						"관리자 계정은 수정할 수 없습니다.");

				return;
			}

			String newName;

			while(true) {

				newName =
						JOptionPane.showInputDialog(
								this,
								"수정할 이름을 입력해주세요.\n\n"
								+ "기존 이름 : \""
								+ member.getName()
								+ "\"");

				if(newName == null) {
					return;
				}

				if(newName.isBlank()) {

					JOptionPane.showMessageDialog(
							this,
							"수정할 이름을 입력해주세요.");

					continue;
				}

				break;
			}

			String newPhone;

			while(true) {

				newPhone =
						JOptionPane.showInputDialog(
								this,
								"수정할 전화번호를 입력해주세요.\n\n"
								+ "기존 전화번호 : \""
								+ member.getPhone()
								+ "\"");

				if(newPhone == null) {
					return;
				}

				if(newPhone.isBlank()) {

					JOptionPane.showMessageDialog(
							this,
							"수정할 전화번호를 입력해주세요.");

					continue;
				}

				newPhone =
						newPhone.replaceAll("[^0-9]", "");

				if(newPhone.length() == 11) {

					newPhone =
							newPhone.substring(0, 3)
							+ "-"
							+ newPhone.substring(3, 7)
							+ "-"
							+ newPhone.substring(7);

				} else {

					JOptionPane.showMessageDialog(
							this,
							"전화번호는 11자리 숫자로 입력해주세요.");

					continue;
				}

				break;
			}

			String newEmail;

			while(true) {

				newEmail =
						JOptionPane.showInputDialog(
								this,
								"수정할 이메일을 입력해주세요.\n\n"
								+ "기존 이메일 : \""
								+ member.getEmail()
								+ "\"");

				if(newEmail == null) {
					return;
				}

				if(newEmail.isBlank()) {

					JOptionPane.showMessageDialog(
							this,
							"수정할 이메일을 입력해주세요.");

					continue;
				}

				break;
			}

			member.setName(newName);
			member.setPhone(newPhone);
			member.setEmail(newEmail);

			int result =
					dao.update(member);

			if(result == 1) {

				JOptionPane.showMessageDialog(
						this,
						"회원 정보 수정 완료!");

				loadAllMembers();

			} else {

				JOptionPane.showMessageDialog(
						this,
						"회원 수정 실패!");
			}
		});

		JButton deleteBtn = new JButton("회원 삭제");
		deleteBtn.setBounds(195, 535, 120, 38);
		add(deleteBtn);
		
		deleteBtn.addActionListener(e -> {

			int row =
					memberTable.getSelectedRow();

			if(row == -1) {

				JOptionPane.showMessageDialog(
						this,
						"삭제할 회원을 선택하세요.");

				return;
			}

			int memberId =
					Integer.parseInt(
							memberTable.getValueAt(row, 0).toString());

			MemberDAOImple dao =
					new MemberDAOImple();

			MemberVO member =
					dao.selectByMemberId(memberId);

			if(member == null) {

				JOptionPane.showMessageDialog(
						this,
						"존재하지 않는 회원입니다.");

				return;
			}

			if(member.getEmail().equals("admin")) {

				JOptionPane.showMessageDialog(
						this,
						"관리자 계정은 삭제할 수 없습니다.");

				return;
			}

			int confirm =
					JOptionPane.showConfirmDialog(
							this,
							"\""
							+ member.getName()
							+ "\" 회원을 삭제하시겠습니까?",
							"회원 삭제 확인",
							JOptionPane.YES_NO_OPTION);

			if(confirm != JOptionPane.YES_OPTION) {
				return;
			}

			int result =
					dao.delete(memberId);

			if(result == 1) {

				JOptionPane.showMessageDialog(
						this,
						"\""
						+ member.getName()
						+ "\" 회원 삭제 완료!");

				loadAllMembers();

			} else {

				JOptionPane.showMessageDialog(
						this,
						"대여 기록이 있는 회원은 삭제할 수 없습니다.");
			}
		});

		JButton backBtn = new JButton("뒤로가기");
		backBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		backBtn.setForeground(new Color(70, 75, 85));
		backBtn.setBackground(new Color(248, 249, 251));
		backBtn.setBorderPainted(false);
		backBtn.setFocusPainted(false);
		backBtn.setBounds(810, 565, 130, 35);
		add(backBtn);

		loadAllMembers();

		searchField.getDocument().addDocumentListener(
				new javax.swing.event.DocumentListener() {

			@Override
			public void insertUpdate(
					javax.swing.event.DocumentEvent e) {

				searchMembers(searchField.getText());
			}

			@Override
			public void removeUpdate(
					javax.swing.event.DocumentEvent e) {

				searchMembers(searchField.getText());
			}

			@Override
			public void changedUpdate(
					javax.swing.event.DocumentEvent e) {

				searchMembers(searchField.getText());
			}
		});

		backBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "admin");
		});
	}

	private void loadAllMembers() {

		MemberDAOImple dao =
				new MemberDAOImple();

		currentMemberList =
				dao.selectAll();

		currentPage = 1;

		showMemberPage();
	}

	private void searchMembers(String keyword) {

		keyword = keyword.trim();

		if(keyword.isBlank()
				|| keyword.equals("회원명을 입력해주세요.")) {

			loadAllMembers();
			return;
		}

		MemberDAOImple dao = new MemberDAOImple();

		currentMemberList =
				dao.selectByName(keyword);

		currentPage = 1;

		showMemberPage();
	}
	
	private void showMemberPage() {

		int start =
				(currentPage - 1)
				* PAGE_SIZE;

		int end =
				Math.min(
						start + PAGE_SIZE,
						currentMemberList.size());

		ArrayList<MemberVO> pageList =
				new ArrayList<MemberVO>();

		for(int i = start; i < end; i++) {

			pageList.add(
					currentMemberList.get(i));
		}

		showMemberTable(pageList);

		int totalPage =
				(int) Math.ceil(
						(double)
						currentMemberList.size()
						/ PAGE_SIZE);

		if(totalPage == 0) {
			totalPage = 1;
		}

		pageLabel.setText(
				currentPage
				+ " / "
				+ totalPage);
	}

	private void showMemberTable(ArrayList<MemberVO> list) {

		String[] columnNames = {
				"회원번호",
				"이름",
				"전화번호",
				"이메일"
		};

		String[][] data =
				new String[list.size()][4];

		for(int i = 0; i < list.size(); i++) {

			MemberVO member =
					list.get(i);

			data[i][0] =
					String.valueOf(
							member.getMemberId());

			data[i][1] =
					member.getName();

			data[i][2] =
					member.getPhone();

			data[i][3] =
					member.getEmail();
		}

		memberTable.setModel(
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

				if(field.getText()
						.equals(placeholder)) {

					field.setText("");
					field.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(
					java.awt.event.FocusEvent e) {

				if(field.getText()
						.trim()
						.isEmpty()) {

					field.setText(placeholder);
					field.setForeground(Color.GRAY);
				}
			}
		});
	}
}