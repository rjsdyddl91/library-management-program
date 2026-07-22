package edu.java.library.main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.java.library.panel.AdminPanel;
import edu.java.library.panel.BookManagePanel;
import edu.java.library.panel.FindPasswordPanel;
import edu.java.library.panel.JoinPanel;
import edu.java.library.panel.LoginPanel;
import edu.java.library.panel.MemberManagePanel;
import edu.java.library.panel.MemberRentalInfoPanel;
import edu.java.library.panel.UserPanel;

public class LibraryMain {

	public static void main(String[] args) {

		// 프로그램 메인 창 생성
		JFrame frame = new JFrame("만화책 대여 시스템");
		frame.setSize(1000, 650);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		// 화면 전환을 위한 CardLayout 생성
		CardLayout cardLayout = new CardLayout();
		JPanel cardPanel = new JPanel(cardLayout);

		// 각 화면(Panel) 객체 생성
		LoginPanel loginPanel = new LoginPanel(cardLayout, cardPanel);
		UserPanel userPanel = new UserPanel(cardLayout, cardPanel);
		JoinPanel joinPanel = new JoinPanel(cardLayout, cardPanel);
		FindPasswordPanel findPasswordPanel = new FindPasswordPanel(cardLayout, cardPanel);
		AdminPanel adminPanel = new AdminPanel(cardLayout, cardPanel);
		BookManagePanel bookManagePanel = new BookManagePanel(cardLayout, cardPanel);
		MemberManagePanel memberManagePanel = new MemberManagePanel(cardLayout, cardPanel);
		MemberRentalInfoPanel memberRentalInfoPanel = new MemberRentalInfoPanel(cardLayout, cardPanel);

		// CardLayout에 각 화면 등록
		cardPanel.add(loginPanel, "login");
		cardPanel.add(userPanel, "user");
		cardPanel.add(joinPanel, "join");
		cardPanel.add(findPasswordPanel, "findPassword");
		cardPanel.add(adminPanel, "admin");
		cardPanel.add(bookManagePanel, "bookManage");
		cardPanel.add(memberManagePanel, "memberManage");
		cardPanel.add(memberRentalInfoPanel, "memberRentalInfo");

		// 프로그램 실행
		frame.add(cardPanel);
		frame.setVisible(true);
	}

}