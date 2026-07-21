package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AdminPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public AdminPanel(CardLayout cardLayout, JPanel cardPanel) {

		setLayout(null);
		setBackground(new Color(248, 249, 251));

		// 상단 바 UI
		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);

		JLabel title = new JLabel("漫画貸出管理システム");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
		title.setBounds(25, 15, 300, 30);

		topBar.add(title);
		add(topBar);

		// 관리자 메뉴 카드 UI
		JPanel card = new JPanel(null);
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createLineBorder(new Color(225, 228, 232)));
		card.setBounds(250, 105, 500, 430);

		JLabel adminTitle = new JLabel("管理者メニュー");
		adminTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 32));
		adminTitle.setForeground(new Color(35, 40, 48));
		adminTitle.setBounds(50, 35, 300, 45);

		card.add(adminTitle);

		JLabel sub = new JLabel("管理する機能を選択してください。");
		sub.setFont(new Font("Yu Gothic UI", Font.PLAIN, 15));
		sub.setForeground(new Color(110, 118, 128));
		sub.setBounds(50, 82, 350, 25);

		card.add(sub);

		// 도서 관리 버튼
		JButton bookManageBtn = new JButton("書籍管理");
		bookManageBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
		bookManageBtn.setBounds(50, 170, 400, 55);

		bookManageBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "bookManage");
		});

		card.add(bookManageBtn);

		// 회원 / 대여 관리 버튼
		JButton memberManageBtn = new JButton("会員・貸出管理");
		memberManageBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 16));
		memberManageBtn.setBounds(50, 260, 400, 55);

		memberManageBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "memberManage");
		});

		card.add(memberManageBtn);
		add(card);

		// 로그아웃 버튼
		JButton logoutBtn = new JButton("ログアウト");
		logoutBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 15));
		logoutBtn.setForeground(new Color(70, 75, 85));
		logoutBtn.setBackground(new Color(248, 249, 251));
		logoutBtn.setBorderPainted(false);
		logoutBtn.setFocusPainted(false);
		logoutBtn.setBounds(800, 555, 130, 35);

		add(logoutBtn);

		// 로그아웃 이벤트
		logoutBtn.addActionListener(e -> {
			JOptionPane.showMessageDialog(this, "ログアウトしました。");
			cardLayout.show(cardPanel, "login");
		});
	}
}