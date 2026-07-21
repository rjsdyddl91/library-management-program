package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.java.library.dao.MemberDAOImple;

public class FindPasswordPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public FindPasswordPanel(CardLayout cardLayout, JPanel cardPanel) {

		setLayout(null);
		setBackground(new Color(248, 249, 251));

		// 상단 바
		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);

		JLabel title = new JLabel("漫画貸出管理システム");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
		title.setBounds(25, 15, 300, 30);

		topBar.add(title);
		add(topBar);

		// 비밀번호 찾기 카드
		JPanel card = new JPanel(null);
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createLineBorder(new Color(225, 228, 232)));
		card.setBounds(300, 105, 400, 430);

		JLabel findTitle = new JLabel("パスワード確認");
		findTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 30));
		findTitle.setForeground(new Color(35, 40, 48));
		findTitle.setBounds(40, 35, 250, 45);

		card.add(findTitle);

		JLabel sub = new JLabel("会員登録時に入力した情報を入力してください。");
		sub.setFont(new Font("Yu Gothic UI", Font.PLAIN, 14));
		sub.setForeground(new Color(110, 118, 128));
		sub.setBounds(40, 80, 320, 25);

		card.add(sub);

		// 이름 입력
		JTextField nameField = new JTextField();
		nameField.setBounds(40, 130, 320, 42);
		nameField.setBorder(BorderFactory.createTitledBorder("氏名"));

		card.add(nameField);

		// 전화번호 입력
		JTextField phoneField = new JTextField();
		phoneField.setBounds(40, 190, 320, 42);
		phoneField.setBorder(BorderFactory.createTitledBorder("電話番号"));

		card.add(phoneField);

		phoneField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				String text = phoneField.getText().replaceAll("-", "");
				text = text.replaceAll("[^0-9]", "");

				if (text.length() > 11) {
					text = text.substring(0, 11);
				}

				StringBuilder sb = new StringBuilder();

				if (text.length() < 4) {
					sb.append(text);
				} else if (text.length() < 8) {
					sb.append(text.substring(0, 3))
							.append("-")
							.append(text.substring(3));
				} else {
					sb.append(text.substring(0, 3))
							.append("-")
							.append(text.substring(3, 7))
							.append("-")
							.append(text.substring(7));
				}

				phoneField.setText(sb.toString());
			}
		});

		// 이메일 입력
		JTextField emailField = new JTextField();
		emailField.setBounds(40, 250, 320, 42);
		emailField.setBorder(BorderFactory.createTitledBorder("メールアドレス"));

		card.add(emailField);

		// 비밀번호 확인 버튼
		JButton findBtn = new JButton("パスワードを確認");
		findBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 15));
		findBtn.setForeground(Color.WHITE);
		findBtn.setBackground(new Color(70, 135, 100));
		findBtn.setFocusPainted(false);
		findBtn.setBorderPainted(false);
		findBtn.setBounds(40, 325, 320, 45);

		card.add(findBtn);

		// 뒤로가기 버튼
		JButton backBtn = new JButton("戻る");
		backBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 14));
		backBtn.setBounds(40, 380, 320, 38);

		card.add(backBtn);
		add(card);

		// 비밀번호 찾기 버튼 클릭 이벤트
		findBtn.addActionListener(e -> {

			String name = nameField.getText();
			String phone = phoneField.getText();
			String email = emailField.getText();

			// 빈칸 검사
			if (name.isBlank() || phone.isBlank() || email.isBlank()) {
				JOptionPane.showMessageDialog(this, "すべての項目を入力してください。");
				return;
			}

			// 이메일 형식 검사
			if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
				JOptionPane.showMessageDialog(this, "正しいメールアドレスを入力してください。");
				return;
			}

			// 전화번호 형식 검사
			if (!phone.matches("^(070|080|090)-\\d{4}-\\d{4}$")) {
				JOptionPane.showMessageDialog(this, "電話番号を正しく入力してください。");
				return;
			}

			MemberDAOImple dao = new MemberDAOImple();
			String password = dao.findPassword(name, phone, email);

			if (password != null) {
				JOptionPane.showMessageDialog(this, "パスワードは「" + password + "」です。");
			} else {
				JOptionPane.showMessageDialog(this, "一致する会員情報が見つかりません。");
			}
		});

		backBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "login");
		});
	}
}