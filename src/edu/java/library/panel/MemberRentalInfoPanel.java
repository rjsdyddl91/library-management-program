package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import edu.java.library.dao.RentalDAOImple;
import edu.java.library.vo.MemberVO;
import edu.java.library.vo.RentalJoinVO;

public class MemberRentalInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel nameLabel;
	private JLabel phoneLabel;
	private JLabel emailLabel;

	private JTable rentalTable;

	private MemberVO selectedMember;

	public MemberRentalInfoPanel(CardLayout cardLayout, JPanel cardPanel) {

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

		// 회원 대여 정보 제목
		JLabel pageTitle = new JLabel("会員貸出情報");
		pageTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 30));
		pageTitle.setForeground(new Color(35, 40, 48));
		pageTitle.setBounds(60, 85, 250, 45);
		add(pageTitle);

		// 회원 정보
		nameLabel = new JLabel("会員名：");
		nameLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		nameLabel.setBounds(60, 150, 400, 30);
		add(nameLabel);

		phoneLabel = new JLabel("電話番号：");
		phoneLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		phoneLabel.setBounds(60, 185, 400, 30);
		add(phoneLabel);

		emailLabel = new JLabel("メールアドレス：");
		emailLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		emailLabel.setBounds(60, 220, 500, 30);
		add(emailLabel);

		JLabel guide = new JLabel("※ 返却期限を過ぎた書籍は「延滞中」と表示されます。");
		guide.setFont(new Font("Yu Gothic UI", Font.PLAIN, 13));
		guide.setForeground(new Color(110, 118, 128));
		guide.setBounds(60, 260, 450, 25);
		add(guide);

		// 대여 정보 테이블
		rentalTable = new JTable();
		rentalTable.setFont(new Font("Yu Gothic UI", Font.PLAIN, 12));
		rentalTable.setRowHeight(28);
		rentalTable.getTableHeader().setFont(new Font("Yu Gothic UI", Font.BOLD, 12));
		rentalTable.getTableHeader().setReorderingAllowed(false);

		JScrollPane scrollPane = new JScrollPane(rentalTable);
		scrollPane.setBounds(60, 300, 880, 220);
		add(scrollPane);

		// 뒤로가기 버튼
		JButton backBtn = new JButton("戻る");
		backBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 15));
		backBtn.setForeground(new Color(70, 75, 85));
		backBtn.setBackground(new Color(248, 249, 251));
		backBtn.setBorderPainted(false);
		backBtn.setFocusPainted(false);
		backBtn.setBounds(810, 565, 130, 35);
		add(backBtn);

		backBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "memberManage");
		});
	}

	public void setMember(MemberVO member) {

		selectedMember = member;

		nameLabel.setText("会員名：" + member.getName());
		phoneLabel.setText("電話番号：" + member.getPhone());
		emailLabel.setText("メールアドレス：" + member.getEmail());

		loadRentalInfo();
	}

	private void loadRentalInfo() {

		RentalDAOImple dao = new RentalDAOImple();
		ArrayList<RentalJoinVO> list = dao.selectMyRental(selectedMember.getMemberId());

		String[] columnNames = {
				"貸出番号",
				"書籍番号",
				"書名",
				"貸出日",
				"返却期限",
				"返却日",
				"状態"
		};

		String[][] data = new String[list.size()][7];
		Date today = new Date(System.currentTimeMillis());

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

			// DB에 저장된 상태값이므로 비교값은 한국어 유지
			if (rental.getRentalStatus().equals("대여중")
					&& rental.getDueDate().before(today)) {

				data[i][6] = "延滞中";

			} else {
				data[i][6] = convertRentalStatus(rental.getRentalStatus());
			}
		}

		rentalTable.setModel(new DefaultTableModel(data, columnNames) {

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
}