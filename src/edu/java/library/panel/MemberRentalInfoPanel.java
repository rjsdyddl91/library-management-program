package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.java.library.dao.RentalDAOImple;
import edu.java.library.vo.MemberVO;
import edu.java.library.vo.RentalJoinVO;

import java.util.ArrayList;

public class MemberRentalInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel nameLabel;
	private JLabel phoneLabel;
	private JLabel emailLabel;

	private JTable rentalTable;

	private MemberVO selectedMember;

	public MemberRentalInfoPanel(
			CardLayout cardLayout,
			JPanel cardPanel) {

		setLayout(null);
		setBackground(new Color(248, 249, 251));

		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);
		add(topBar);

		JLabel topTitle =
				new JLabel(
						"만화책 대여 시스템");

		topTitle.setForeground(
				Color.WHITE);

		topTitle.setFont(
				new Font(
						"맑은 고딕",
						Font.BOLD,
						18));

		topTitle.setBounds(
				25,
				15,
				300,
				30);

		topBar.add(topTitle);

		JLabel pageTitle =
				new JLabel(
						"회원 대여 정보");

		pageTitle.setFont(
				new Font(
						"맑은 고딕",
						Font.BOLD,
						30));

		pageTitle.setForeground(
				new Color(
						35,
						40,
						48));

		pageTitle.setBounds(
				60,
				85,
				250,
				45);

		add(pageTitle);

		nameLabel =
				new JLabel(
						"회원명 : ");

		nameLabel.setFont(
				new Font(
						"맑은 고딕",
						Font.PLAIN,
						16));

		nameLabel.setBounds(
				60,
				150,
				300,
				30);

		add(nameLabel);

		phoneLabel =
				new JLabel(
						"전화번호 : ");

		phoneLabel.setFont(
				new Font(
						"맑은 고딕",
						Font.PLAIN,
						16));

		phoneLabel.setBounds(
				60,
				185,
				300,
				30);

		add(phoneLabel);

		emailLabel =
				new JLabel(
						"이메일 : ");

		emailLabel.setFont(
				new Font(
						"맑은 고딕",
						Font.PLAIN,
						16));

		emailLabel.setBounds(
				60,
				220,
				400,
				30);

		add(emailLabel);

		JLabel guide =
				new JLabel(
						"※ 연체 도서는 '연체중'으로 표시됩니다.");

		guide.setForeground(
				new Color(
						110,
						118,
						128));

		guide.setBounds(
				60,
				260,
				350,
				25);

		add(guide);

		rentalTable =
				new JTable();

		rentalTable.setRowHeight(
				28);

		rentalTable.getTableHeader()
				.setReorderingAllowed(
						false);

		JScrollPane scrollPane =
				new JScrollPane(
						rentalTable);

		scrollPane.setBounds(
				60,
				300,
				880,
				220);

		add(scrollPane);

		JButton backBtn =
				new JButton(
						"뒤로가기");

		backBtn.setFont(
				new Font(
						"맑은 고딕",
						Font.BOLD,
						15));

		backBtn.setForeground(
				new Color(
						70,
						75,
						85));

		backBtn.setBackground(
				new Color(
						248,
						249,
						251));

		backBtn.setBorderPainted(
				false);

		backBtn.setFocusPainted(
				false);

		backBtn.setBounds(
				810,
				565,
				130,
				35);

		add(backBtn);

		backBtn.addActionListener(e -> {
			cardLayout.show(
					cardPanel,
					"memberManage");
		});
	}

	public void setMember(
			MemberVO member) {

		selectedMember = member;

		nameLabel.setText(
				"회원명 : "
				+ member.getName());

		phoneLabel.setText(
				"전화번호 : "
				+ member.getPhone());

		emailLabel.setText(
				"이메일 : "
				+ member.getEmail());
		loadRentalInfo();
	}
	private void loadRentalInfo() {

		RentalDAOImple dao =
				new RentalDAOImple();

		ArrayList<RentalJoinVO> list =
				dao.selectMyRental(
						selectedMember.getMemberId());

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

		rentalTable.setModel(
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
	
}