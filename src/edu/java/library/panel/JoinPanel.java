package edu.java.library.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.java.library.dao.MemberDAOImple;
import edu.java.library.vo.MemberVO;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JoinPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public JoinPanel(CardLayout cardLayout, JPanel cardPanel) {

		setLayout(null);
		setBackground(new Color(248, 249, 251));

		// 상단 바
		JPanel topBar = new JPanel(null);
		topBar.setBackground(new Color(33, 40, 48));
		topBar.setBounds(0, 0, 1000, 60);

		JLabel title = new JLabel("만화책 대여 시스템");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		title.setBounds(25, 15, 300, 30);
		topBar.add(title);

		add(topBar);

		// 회원가입 카드
		JPanel card = new JPanel(null);
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createLineBorder(new Color(225, 228, 232)));
		card.setBounds(300, 90, 400, 470);

		JLabel joinTitle = new JLabel("회원가입");
		joinTitle.setFont(new Font("맑은 고딕", Font.BOLD, 32));
		joinTitle.setForeground(new Color(35, 40, 48));
		joinTitle.setBounds(40, 30, 250, 45);
		card.add(joinTitle);

		JLabel sub = new JLabel("회원 정보를 입력하세요.");
		sub.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		sub.setForeground(new Color(110, 118, 128));
		sub.setBounds(40, 75, 250, 25);
		card.add(sub);

		JTextField nameField = new JTextField();
		nameField.setBounds(40, 120, 320, 42);
		nameField.setBorder(BorderFactory.createTitledBorder("이름"));
		card.add(nameField);

		JTextField phoneField = new JTextField();
		phoneField.setBounds(40, 175, 320, 42);
		phoneField.setBorder(BorderFactory.createTitledBorder("전화번호"));
		card.add(phoneField);
		
		phoneField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {

				String text =
						phoneField.getText()
						.replaceAll("-", "");

				// 숫자만 허용
				text =
						text.replaceAll(
								"[^0-9]",
								"");

				if(text.length() > 11) {
					text =
							text.substring(
									0,
									11);
				}

				StringBuilder sb =
						new StringBuilder();

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
		emailField.setBounds(40, 230, 320, 42);
		emailField.setBorder(BorderFactory.createTitledBorder("이메일"));
		card.add(emailField);

		JPasswordField passwordField = new JPasswordField();
		passwordField.setBounds(40, 285, 320, 42);
		passwordField.setBorder(BorderFactory.createTitledBorder("비밀번호"));
		card.add(passwordField);

		JButton joinBtn = new JButton("가입하기");
		joinBtn.addActionListener(e -> {

			String name = nameField.getText();
			String phone = phoneField.getText();
			String email = emailField.getText();
			String password =
					new String(
							passwordField.getPassword());

			// 빈칸 검사
			if(name.isBlank()
					|| phone.isBlank()
					|| email.isBlank()
					|| password.isBlank()) {

				JOptionPane.showMessageDialog(
						this,
						"모든 정보를 입력하세요.");

				return;
			}
			
			// 전화번호 형식 검사
			if(!phone.matches("^010-\\d{4}-\\d{4}$")) {

				JOptionPane.showMessageDialog(
						this,
						"전화번호를 정확히 입력하세요.");

				return;
			}
			
			// 이메일 형식 검사
			if(!email.matches(
					"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

				JOptionPane.showMessageDialog(
						this,
						"올바른 이메일 형식을 입력하세요.");

				return;
			}

			MemberVO vo =
					new MemberVO(
							0,
							name,
							phone,
							email,
							password);

			MemberDAOImple dao =
					new MemberDAOImple();

			int result =
					dao.insert(vo);

			if(result == 1) {

				JOptionPane.showMessageDialog(
						this,
						"회원가입 성공!");

				cardLayout.show(
						cardPanel,
						"login");

			} else {

				JOptionPane.showMessageDialog(
						this,
						"이미 사용 중인 이메일 또는 전화번호입니다.");
			}
		});
		joinBtn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		joinBtn.setForeground(Color.WHITE);
		joinBtn.setBackground(new Color(70, 135, 100));
		joinBtn.setFocusPainted(false);
		joinBtn.setBorderPainted(false);
		joinBtn.setBounds(40, 355, 320, 45);
		card.add(joinBtn);

		JButton backBtn = new JButton("뒤로가기");
		backBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		backBtn.setBounds(40, 410, 320, 38);
		card.add(backBtn);

		add(card);

		backBtn.addActionListener(e -> {
			cardLayout.show(cardPanel, "login");
		});
	}
}