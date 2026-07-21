package edu.java.library.panel;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.*;

import edu.java.library.dao.MemberDAOImple;
import edu.java.library.main.LibraryMain;
import edu.java.library.vo.MemberVO;

// 로그인 화면 Panel
public class LoginPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// 로그인 화면 UI 생성
	public LoginPanel(CardLayout cardLayout, JPanel cardPanel) {

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

		// 캐릭터 이미지
		ImageIcon characterIcon =
				new ImageIcon(LibraryMain.class.getResource("login_character.png"));

		Image characterImage = characterIcon.getImage()
				.getScaledInstance(370, 420, Image.SCALE_SMOOTH);

		JLabel imageLabel = new JLabel(new ImageIcon(characterImage));
		imageLabel.setBounds(35, 125, 390, 440);

		add(imageLabel);

		// 로그인 카드
		JPanel card = new JPanel(null);
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createLineBorder(new Color(225, 228, 232)));
		card.setBounds(420, 105, 520, 430);

		JLabel loginTitle = new JLabel("ログイン");
		loginTitle.setFont(new Font("Yu Gothic UI", Font.BOLD, 34));
		loginTitle.setForeground(new Color(35, 40, 48));
		loginTitle.setBounds(55, 45, 200, 45);

		card.add(loginTitle);

		JLabel sub = new JLabel("サービスをご利用いただくにはログインしてください。");
		sub.setFont(new Font("Yu Gothic UI", Font.PLAIN, 15));
		sub.setForeground(new Color(110, 118, 128));
		sub.setBounds(55, 95, 410, 25);

		card.add(sub);

		// 이메일 입력
		JTextField emailField = new JTextField("メールアドレスを入力してください");
		emailField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 15));
		emailField.setForeground(new Color(150, 155, 165));
		emailField.setBounds(55, 145, 410, 50);
		emailField.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(new Color(215, 220, 228)),
						BorderFactory.createEmptyBorder(0, 15, 0, 15)
				)
		);

		addPlaceholder(emailField, "メールアドレスを入力してください");
		card.add(emailField);

		// 비밀번호 입력
		JPasswordField pwField = new JPasswordField();
		pwField.setText("パスワードを入力してください");
		pwField.setEchoChar((char) 0);
		pwField.setFont(new Font("Yu Gothic UI", Font.PLAIN, 15));
		pwField.setForeground(new Color(150, 155, 165));
		pwField.setBounds(55, 215, 410, 50);
		pwField.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(new Color(215, 220, 228)),
						BorderFactory.createEmptyBorder(0, 15, 0, 15)
				)
		);

		addPasswordPlaceholder(pwField, "パスワードを入力してください");
		card.add(pwField);

		// 로그인 버튼
		JButton loginBtn = new JButton("ログイン");
		loginBtn.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
		loginBtn.setForeground(Color.WHITE);
		loginBtn.setBackground(new Color(70, 135, 100));
		loginBtn.setFocusPainted(false);
		loginBtn.setBorderPainted(false);
		loginBtn.setBounds(55, 295, 410, 55);

		card.add(loginBtn);

		// 회원가입
		JButton joinBtn = new JButton("新規会員登録");
		joinBtn.setBounds(55, 375, 185, 45);

		joinBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "join");
		});

		card.add(joinBtn);

		// 비밀번호 찾기
		JButton findBtn = new JButton("パスワードを忘れた方");
		findBtn.setBounds(280, 375, 185, 45);

		findBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "findPassword");
		});

		card.add(findBtn);
		add(card);

		// 관리자 메뉴
		JButton adminBtn = new JButton("管理者メニュー");
		adminBtn.setBounds(790, 555, 150, 35);

		adminBtn.addActionListener(e -> {

			String adminPassword =
					JOptionPane.showInputDialog(this, "管理者パスワードを入力してください。");

			if (adminPassword == null) {
				return;
			}

			if (adminPassword.equals("3808")) {
				cardLayout.show(cardPanel, "admin");
			} else {
				JOptionPane.showMessageDialog(
						this,
						"管理者パスワードが正しくありません。"
				);
			}
		});

		add(adminBtn);

		// 로그인 버튼 클릭 이벤트
		loginBtn.addActionListener(e -> {

			String email = emailField.getText();
			String password = new String(pwField.getPassword());

			if (email.equals("メールアドレスを入力してください")
					|| email.isBlank()
					|| password.equals("パスワードを入力してください")
					|| password.isBlank()) {

				JOptionPane.showMessageDialog(
						this,
						"メールアドレスとパスワードを入力してください。"
				);

				return;
			}

			MemberDAOImple dao = new MemberDAOImple();
			MemberVO loginUser = dao.login(email, password);

			if (loginUser != null) {

				for (Component comp : cardPanel.getComponents()) {

					if (comp instanceof UserPanel) {
						((UserPanel) comp).setLoginUser(loginUser);
						break;
					}
				}

				JOptionPane.showMessageDialog(
						this,
						loginUser.getName() + "さん、ようこそ。"
				);

				cardLayout.show(cardPanel, "user");

			} else {

				JOptionPane.showMessageDialog(
						this,
						"メールアドレスまたはパスワードが正しくありません。"
				);
			}
		});

		emailField.addActionListener(e -> {
			pwField.requestFocus();
		});

		pwField.addActionListener(e -> {
			loginBtn.doClick();
		});
	}

	private void addPlaceholder(JTextField field, String placeholder) {

		field.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {

				if (field.getText().equals(placeholder)) {
					field.setText("");
					field.setForeground(new Color(40, 45, 55));
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				if (field.getText().isEmpty()) {
					field.setText(placeholder);
					field.setForeground(new Color(150, 155, 165));
				}
			}
		});
	}

	private void addPasswordPlaceholder(JPasswordField field, String placeholder) {

		field.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {

				String text = new String(field.getPassword());

				if (text.equals(placeholder)) {
					field.setText("");
					field.setEchoChar('●');
				}
			}

			@Override
			public void focusLost(FocusEvent e) {

				String text = new String(field.getPassword());

				if (text.isEmpty()) {
					field.setEchoChar((char) 0);
					field.setText(placeholder);
				}
			}
		});
	}
}