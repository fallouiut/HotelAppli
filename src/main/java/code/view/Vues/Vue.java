package code.view.Vues;

import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import code.view.Panels.AccueilPanel;
import code.view.Panels.ClientelePanel;
import code.view.Panels.ConnectionPanel;
import code.view.Panels.FacturationPanel;
import code.view.Panels.ReservationPanel;
import code.view.Panels.SupremePanel;


public class Vue extends JFrame {
	
	public enum MENU_ITEM { DECONNECTION, RETOUR, RESERVATION_DEMANDE, RESERVATION_CONFIRMATION } ;
	private ArrayList <JMenuItem> m_menuItems = new ArrayList <JMenuItem> ();
	private JMenu m_menu;
	private JMenu m_menuReservation;
	private JMenuBar m_menuBar;
	
	public Vue ()
	{
		setLayout(new CardLayout());
		construirePanels();
		construireMenuBarre();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);	
	}

	private void construirePanels ()
	{
		add(new ConnectionPanel("Connection"), "Connection");
		add(new AccueilPanel("Accueil"), "Accueil");
		add(new ClientelePanel("Clientele"), "Clientele");
		add(new FacturationPanel("Facturation"), "Facturation");
		add(new ReservationPanel("Reservation"), "Reservation");
		add(new SupremePanel("Supreme"), "Supreme");
	}
	
	private void construireMenuBarre()
	{
		m_menu = new JMenu ("Session");
		JMenuItem deconnectionItem = new JMenuItem("Deconnection");
		m_menuItems.add(deconnectionItem);
		JMenuItem retourItem = new JMenuItem("Retour");
		m_menuItems.add(retourItem);
		
		m_menuReservation = new JMenu ("Reservation");
		JMenuItem itemDemandee = new JMenuItem("Demandee");
		m_menuItems.add(itemDemandee);
		JMenuItem itemConfirmation = new JMenuItem("Confirmation");
		m_menuItems.add(itemConfirmation);
		m_menuReservation.setEnabled(true);
		
		m_menuBar = new JMenuBar();
		m_menu.add(deconnectionItem);
		m_menu.add(retourItem);
		m_menuReservation.add(itemConfirmation);
		m_menuReservation.add(itemDemandee);
		m_menuBar.add(m_menu);
		setJMenuBar(m_menuBar);
	}
	
	public ArrayList <JMenuItem> getMenuItem()
	{
		return m_menuItems;
	}
	
	public JMenu getMenuReservation()
	{
		return m_menuReservation;
	}
	
	public JMenuBar getBar()
	{
		return m_menuBar;
	}
	
	public JMenu getMenu()
	{
		return m_menu;
	}
	
}
