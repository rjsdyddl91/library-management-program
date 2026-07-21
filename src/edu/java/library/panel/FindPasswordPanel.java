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

		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);

		JLabel title = new JLabel("만화책 대여 시스템");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		title.setBounds(25, 15, 300, 30);
		topBar.add(title);
		add(topBar);

		JPanel card = new JPanel(null);
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createLineBorder(new Color(225, 228, 232)));
		card.setBounds(300, 105, 400, 430);

		JLabel findTitle = new JLabel("비밀번호 찾기");
		findTitle.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		findTitle.setForeground(new Color(35, 40, 48));
		findTitle.setBounds(40, 35, 250, 45);
		card.add(findTitle);

		JLabel sub = new JLabel("가입 시 입력한 정보를 입력하세요.");
		sub.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		sub.setForeground(new Color(110, 118, 128));
		sub.setBounds(40, 80, 300, 25);
		card.add(sub);

		JTextField nameField = new JTextField();
		nameField.setBounds(40, 130, 320, 42);
		nameField.setBorder(BorderFactory.createTitledBorder("이름"));
		card.add(nameField);

		JTextField phoneField = new JTextField();
		phoneField.setBounds(40, 190, 320, 42);
		phoneField.setBorder(BorderFactory.createTitledBorder("전화번호"));
		card.add(phoneField);

		phoneField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String text = phoneField.getText().replaceAll("-", "");
				text = text.replaceAll("[^0-9]", "");

				if(text.length() > 11) {
					text = text.substring(0, 11);
				}

				StringBuilder sb = new StringBuilder();

				if(text.length() < 4) {
					sb.append(text);
				} else if(text.length() < 8) {
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

		JTextField emailField = new JTextField();
		emailField.setBounds(40, 250, 320, 42);
		emailField.setBorder(BorderFactory.createTitledBorder("이메일"));
		card.add(emailField);

		JButton findBtn = new JButton("비밀번호 찾기");
		findBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		findBtn.setForeground(Color.WHITE);
		findBtn.setBackground(new Color(70, 135, 100));
		findBtn.setFocusPainted(false);
		findBtn.setBorderPainted(false);
		findBtn.setBounds(40, 325, 320, 45);
		card.add(findBtn);

		JButton backBtn = new JButton("뒤로가기");
		backBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		backBtn.setBounds(40, 380, 320, 38);
		card.add(backBtn);

		add(card);

		findBtn.addActionListener(e -> {
			String name = nameField.getText();
			String phone = phoneField.getText();
			String email = emailField.getText();
			
			// 이메일 형식 검사
			if(!email.matches(
					"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

				JOptionPane.showMessageDialog(
						this,
						"올바른 이메일 형식을 입력하세요.");

				return;
			}

			if(name.isBlank() || phone.isBlank() || email.isBlank()) {
				JOptionPane.showMessageDialog(
						this,
						"모든 정보를 입력하세요.");
				return;
			}

			if(!phone.matches("^010-\\d{4}-\\d{4}$")) {
				JOptionPane.showMessageDialog(
						this,
						"전화번호를 정확히 입력하세요.");
				return;
			}

			MemberDAOImple dao = new MemberDAOImple();

			String password = dao.findPassword(name, phone, email);

			if(password != null) {
				JOptionPane.showMessageDialog(
						this,
						"비밀번호는 " + password + " 입니다.");
			} else {
				JOptionPane.showMessageDialog(
						this,
						"일치하는 회원 정보가 없습니다.");
			}
		});

		backBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "login");
		});
	}
}