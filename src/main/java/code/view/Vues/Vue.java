package code.view.Vues;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import code.view.Panels.AccueilPanel;
import code.view.Panels.ClientelePanel;
import code.view.Panels.ConnectionPanel;
import code.view.Panels.FacturationPanel;
import code.view.Panels.HotelPanel;
import code.view.Panels.ReservationPanel;
import code.view.Panels.SupremePanel;


public class Vue extends JFrame {
	
	public enum MENU_ITEM { DECONNECTION, RETOUR } ;
	private ArrayList <JMenuItem> m_menuItems = new ArrayList <JMenuItem> ();
	private ArrayList <HotelPanel> m_panels = new ArrayList <HotelPanel> ();
	private JMenu m_menu;
	
	
	public Vue ()
	{
		construirePanels();
		construireMenuBarre();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);	
	}

	private void construirePanels ()
	{
		m_panels.add(new ConnectionPanel());
		m_panels.add(new AccueilPanel());
		m_panels.add(new ClientelePanel());
		m_panels.add(new FacturationPanel());
		m_panels.add(new ReservationPanel());
		m_panels.add(new SupremePanel());
	}
	
	private void construireMenuBarre()
	{
		m_menu = new JMenu ("Session");
		JMenuItem deconnectionItem = new JMenuItem("Deconnection");
		m_menuItems.add(deconnectionItem);
		JMenuItem retourItem = new JMenuItem("Retour");
		m_menuItems.add(retourItem);
		JMenuBar barre = new JMenuBar();
		m_menu.add(deconnectionItem);
		m_menu.add(retourItem);
		barre.add(m_menu);
		setJMenuBar(barre);
	}
	
	public ArrayList <HotelPanel> getPanels()
	{
		return m_panels;
	}
	
	public ArrayList <JMenuItem> getMenuItem()
	{
		return m_menuItems;
	}
	
	public JMenu getMenu()
	{
		return m_menu;
	}
	
}
