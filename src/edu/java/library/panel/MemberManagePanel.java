package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import edu.java.library.dao.MemberDAOImple;
import edu.java.library.dao.RentalDAOImple;
import edu.java.library.vo.MemberVO;
import edu.java.library.vo.OverdueJoinVO;

public class MemberManagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable memberTable;

	// 페이징 처리용 변수
	private ArrayList<MemberVO> currentMemberList = new ArrayList<>();

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

		// 회원 관리 제목
		JLabel pageTitle = new JLabel("会員管理");
		pageTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 30));
		pageTitle.setForeground(new Color(35, 40, 48));
		pageTitle.setBounds(60, 85, 250, 45);
		add(pageTitle);

		JLabel guide = new JLabel(
				"会員をダブルクリックすると貸出情報を確認できます。管理者アカウントは編集・削除できません。");

		guide.setFont(new Font("Yu Gothic UI", Font.PLAIN, 14));
		guide.setForeground(new Color(110, 118, 128));
		guide.setBounds(60, 130, 800, 30);
		add(guide);

		// 회원 검색
		JLabel searchLabel = new JLabel("会員検索");
		searchLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		searchLabel.setBounds(60, 165, 80, 20);
		add(searchLabel);

		String searchPlaceholder = "会員名を入力してください。";

		JTextField searchField = new JTextField(searchPlaceholder);
		searchField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		searchField.setBounds(60, 190, 300, 40);
		searchField.setForeground(Color.GRAY);
		add(searchField);

		addPlaceholder(searchField, searchPlaceholder);

		// 연체 회원 보기
		JButton overdueBtn = new JButton("延滞会員一覧");
		overdueBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
		overdueBtn.setBounds(760, 190, 150, 40);
		add(overdueBtn);

		// 연체 회원 보기 버튼 클릭 이벤트
		overdueBtn.addActionListener(e -> {

			// RentalDAO 객체 생성
			RentalDAOImple dao = new RentalDAOImple();

			// 연체 회원 목록 조회
			ArrayList<OverdueJoinVO> list = dao.selectOverdueList();

			// 연체 회원 없을 경우
			if (list == null || list.isEmpty()) {
				JOptionPane.showMessageDialog(this, "延滞中の会員はいません。");
				return;
			}

			// 연체 회원 조회 창 생성
			JDialog dialog = new JDialog();
			dialog.setTitle("延滞会員一覧");
			dialog.setSize(900, 400);
			dialog.setLocationRelativeTo(this);
			dialog.setModal(true);

			// JTable 데이터 생성
			String[] columnNames = {
					"会員番号",
					"氏名",
					"電話番号",
					"メールアドレス",
					"書名",
					"貸出日",
					"返却期限",
					"状態"
			};

			String[][] data = new String[list.size()][8];

			for (int i = 0; i < list.size(); i++) {

				OverdueJoinVO vo = list.get(i);

				data[i][0] = String.valueOf(vo.getMemberId());
				data[i][1] = vo.getName();
				data[i][2] = vo.getPhone();
				data[i][3] = vo.getEmail();
				data[i][4] = vo.getTitle();
				data[i][5] = String.valueOf(vo.getRentalDate());
				data[i][6] = String.valueOf(vo.getDueDate());

				// DB 상태값은 그대로 두고 화면에만 일본어로 표시
				data[i][7] = convertRentalStatus(vo.getStatus());
			}

			// JTable 생성
			JTable overdueTable = new JTable(data, columnNames);
			overdueTable.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
			overdueTable.setRowHeight(28);
			overdueTable.getTableHeader().setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
			overdueTable.getTableHeader().setReorderingAllowed(false);

			JScrollPane overdueScrollPane = new JScrollPane(overdueTable);
			dialog.add(overdueScrollPane);

			// 창 열기
			dialog.setVisible(true);
		});

		// 회원 목록
		memberTable = new JTable();
		memberTable.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		memberTable.setRowHeight(24);
		memberTable.getTableHeader().setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
		memberTable.getTableHeader().setReorderingAllowed(false);

		// 회원 더블클릭 이벤트
		memberTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() != 2) {
					return;
				}

				int row = memberTable.getSelectedRow();

				if (row == -1) {
					return;
				}

				int memberId = Integer.parseInt(
						memberTable.getValueAt(row, 0).toString());

				MemberDAOImple dao = new MemberDAOImple();
				MemberVO member = dao.selectByMemberId(memberId);

				if (member == null) {
					JOptionPane.showMessageDialog(
							MemberManagePanel.this,
							"該当する会員が存在しません。");

					return;
				}

				for (Component comp : cardPanel.getComponents()) {

					if (comp instanceof MemberRentalInfoPanel) {
						((MemberRentalInfoPanel) comp).setMember(member);
						break;
					}
				}

				cardLayout.show(cardPanel, "memberRentalInfo");
			}
		});

		JScrollPane scrollPane = new JScrollPane(memberTable);
		scrollPane.setBounds(60, 255, 880, 264);
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
			showMemberPage();
		});

		prevPageBtn.addActionListener(e -> {

			if (currentPage > 1) {
				currentPage--;
				showMemberPage();
			}
		});

		nextPageBtn.addActionListener(e -> {

			int totalPage = (int) Math.ceil(
					(double) currentMemberList.size() / PAGE_SIZE);

			if (currentPage < totalPage) {
				currentPage++;
				showMemberPage();
			}
		});

		lastPageBtn.addActionListener(e -> {

			int totalPage = (int) Math.ceil(
					(double) currentMemberList.size() / PAGE_SIZE);

			if (totalPage == 0) {
				totalPage = 1;
			}

			currentPage = totalPage;
			showMemberPage();
		});

		// 회원 수정 버튼
		JButton updateBtn = new JButton("会員情報編集");
		updateBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
		updateBtn.setBounds(60, 535, 130, 38);
		add(updateBtn);

		updateBtn.addActionListener(e -> {

			int row = memberTable.getSelectedRow();

			if (row == -1) {
				JOptionPane.showMessageDialog(this, "編集する会員を選択してください。");
				return;
			}

			int memberId = Integer.parseInt(
					memberTable.getValueAt(row, 0).toString());

			MemberDAOImple dao = new MemberDAOImple();
			MemberVO member = dao.selectByMemberId(memberId);

			if (member == null) {
				JOptionPane.showMessageDialog(this, "該当する会員が存在しません。");
				return;
			}

			// 관리자 계정 수정 불가
			if (member.getEmail().equals("admin")) {
				JOptionPane.showMessageDialog(this, "管理者アカウントは編集できません。");
				return;
			}

			String newName;

			while (true) {

				newName = JOptionPane.showInputDialog(
						this,
						"変更後の氏名を入力してください。\n\n"
						+ "現在の氏名：「"
						+ member.getName()
						+ "」");

				if (newName == null) {
					return;
				}

				if (newName.isBlank()) {
					JOptionPane.showMessageDialog(this, "変更後の氏名を入力してください。");
					continue;
				}

				break;
			}

			String newPhone;

			while (true) {

				newPhone = JOptionPane.showInputDialog(
						this,
						"変更後の電話番号を入力してください。\n\n"
						+ "現在の電話番号：「"
						+ member.getPhone()
						+ "」");

				if (newPhone == null) {
					return;
				}

				if (newPhone.isBlank()) {
					JOptionPane.showMessageDialog(this, "変更後の電話番号を入力してください。");
					continue;
				}

				newPhone = newPhone.replaceAll("[^0-9]", "");

				if (!newPhone.matches("^(070|080|090)\\d{8}$")) {
					JOptionPane.showMessageDialog(
							this,
							"電話番号は070・080・090から始まる11桁で入力してください。");

					continue;
				}

				newPhone = newPhone.substring(0, 3)
						+ "-"
						+ newPhone.substring(3, 7)
						+ "-"
						+ newPhone.substring(7);

				break;
			}

			String newEmail;

			while (true) {

				newEmail = JOptionPane.showInputDialog(
						this,
						"変更後のメールアドレスを入力してください。\n\n"
						+ "現在のメールアドレス：「"
						+ member.getEmail()
						+ "」");

				if (newEmail == null) {
					return;
				}

				if (newEmail.isBlank()) {
					JOptionPane.showMessageDialog(
							this,
							"変更後のメールアドレスを入力してください。");

					continue;
				}

				if (!newEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
					JOptionPane.showMessageDialog(
							this,
							"正しいメールアドレスを入力してください。");

					continue;
				}

				break;
			}

			member.setName(newName);
			member.setPhone(newPhone);
			member.setEmail(newEmail);

			int result = dao.update(member);

			if (result == 1) {
				JOptionPane.showMessageDialog(this, "会員情報を更新しました。");
				loadAllMembers();
			} else {
				JOptionPane.showMessageDialog(
						this,
						"会員情報の更新に失敗しました。\nメールアドレスまたは電話番号が既に使用されている可能性があります。");
			}
		});

		// 회원 삭제 버튼
		JButton deleteBtn = new JButton("会員削除");
		deleteBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
		deleteBtn.setBounds(205, 535, 120, 38);
		add(deleteBtn);

		deleteBtn.addActionListener(e -> {

			int row = memberTable.getSelectedRow();

			if (row == -1) {
				JOptionPane.showMessageDialog(this, "削除する会員を選択してください。");
				return;
			}

			int memberId = Integer.parseInt(
					memberTable.getValueAt(row, 0).toString());

			MemberDAOImple dao = new MemberDAOImple();
			MemberVO member = dao.selectByMemberId(memberId);

			if (member == null) {
				JOptionPane.showMessageDialog(this, "該当する会員が存在しません。");
				return;
			}

			// 관리자 계정 삭제 불가
			if (member.getEmail().equals("admin")) {
				JOptionPane.showMessageDialog(this, "管理者アカウントは削除できません。");
				return;
			}

			int confirm = JOptionPane.showConfirmDialog(
					this,
					"「" + member.getName() + "」を削除しますか？",
					"会員削除の確認",
					JOptionPane.YES_NO_OPTION);

			if (confirm != JOptionPane.YES_OPTION) {
				return;
			}

			int result = dao.delete(memberId);

			if (result == 1) {
				JOptionPane.showMessageDialog(
						this,
						"「" + member.getName() + "」を削除しました。");

				loadAllMembers();
			} else {
				JOptionPane.showMessageDialog(
						this,
						"貸出履歴がある会員は削除できません。");
			}
		});

		// 뒤로가기 버튼
		JButton backBtn = new JButton("戻る");
		backBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 15));
		backBtn.setForeground(new Color(70, 75, 85));
		backBtn.setBackground(new Color(248, 249, 251));
		backBtn.setBorderPainted(false);
		backBtn.setFocusPainted(false);
		backBtn.setBounds(810, 565, 130, 35);
		add(backBtn);

		loadAllMembers();

		// 회원 검색 이벤트
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				searchMembers(searchField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				searchMembers(searchField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				searchMembers(searchField.getText());
			}
		});

		backBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "admin");
		});
	}

	private void loadAllMembers() {

		MemberDAOImple dao = new MemberDAOImple();
		currentMemberList = dao.selectAll();

		currentPage = 1;
		showMemberPage();
	}

	private void searchMembers(String keyword) {

		keyword = keyword.trim();

		if (keyword.isBlank() || keyword.equals("会員名を入力してください。")) {
			loadAllMembers();
			return;
		}

		MemberDAOImple dao = new MemberDAOImple();
		currentMemberList = dao.selectByName(keyword);

		currentPage = 1;
		showMemberPage();
	}

	private void showMemberPage() {

		int start = (currentPage - 1) * PAGE_SIZE;
		int end = Math.min(start + PAGE_SIZE, currentMemberList.size());

		ArrayList<MemberVO> pageList = new ArrayList<>();

		for (int i = start; i < end; i++) {
			pageList.add(currentMemberList.get(i));
		}

		showMemberTable(pageList);

		int totalPage = (int) Math.ceil(
				(double) currentMemberList.size() / PAGE_SIZE);

		if (totalPage == 0) {
			totalPage = 1;
		}

		pageLabel.setText(currentPage + " / " + totalPage);
	}

	private void showMemberTable(ArrayList<MemberVO> list) {

		String[] columnNames = {
				"会員番号",
				"氏名",
				"電話番号",
				"メールアドレス"
		};

		String[][] data = new String[list.size()][4];

		for (int i = 0; i < list.size(); i++) {

			MemberVO member = list.get(i);

			data[i][0] = String.valueOf(member.getMemberId());
			data[i][1] = member.getName();
			data[i][2] = member.getPhone();
			data[i][3] = member.getEmail();
		}

		memberTable.setModel(new DefaultTableModel(data, columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}

	// DB의 한국어 상태값을 화면에만 일본어로 표시
	private String convertRentalStatus(String status) {

		if (status == null) {
			return "";
		}

		switch (status) {

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