package code.controller;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JMenuItem;

import code.view.Panels.AccueilPanel;
import code.view.Panels.ClientelePanel;
import code.view.Panels.ConnectionPanel;
import code.view.Panels.FacturationPanel;
import code.view.Panels.HotelPanel;
import code.view.Panels.ReservationPanel;
import code.view.Panels.SupremePanel;
import code.view.Vues.Vue;
public class ControllerVue extends AbstractController {
	
	public boolean m_clienteleSet = false;
	public boolean m_reservationSet = false;
	public boolean m_facturationSet = false;
	public boolean m_supremeSet = false;
	private ETAT_CONNECTION m_connecte = ETAT_CONNECTION.UNDEFINED;
	private boolean m_retour = false;
	private ControllerConnection m_connection;
	private Vue m_vue = new Vue();
	private PANEL m_panelCourant;
	
	public ControllerVue() {
		super();
		initController();
		deroulement();
	}

	public void deroulement()
	{
		CardLayout c = (CardLayout)(m_vue.getContentPane().getLayout());
		c.show(m_vue.getContentPane(), getPanel(PANEL.CONNECTION));
		m_panelCourant = PANEL.CONNECTION;
		m_vue.pack();
		HotelPanel panelCourant;
		while (true)
		{
			panelCourant = getControlledPanel(getPanel(m_panelCourant));
			while (panelCourant.fonctionne())
			{
				if ( m_connecte == ETAT_CONNECTION.DISCONNECTED || m_retour)
					break;
			}
			m_panelCourant = transition();
			c.show(m_vue.getContentPane(), getPanel(m_panelCourant));
			m_vue.pack();
		}
	}
	
	public PANEL transition()
	{
		PANEL returnValue = null;
		getControlledPanel(getPanel(m_panelCourant)).setTermine(false);
		if (m_connecte == ETAT_CONNECTION.DISCONNECTED)
		{
			m_vue.getMenu().setEnabled(false);
			returnValue = PANEL.CONNECTION;
		}
		else if (m_panelCourant == PANEL.CONNECTION)
		{
			m_connecte = ETAT_CONNECTION.CONNECTED;	
			returnValue = PANEL.ACCUEIL;
			m_vue.getMenu().setEnabled(true);
			new ControllerAccueil((AccueilPanel) getControlledPanel(getPanel(PANEL.ACCUEIL)), m_connection.getAdmin());
		}
		else if (m_retour)
		{
			returnValue = PANEL.ACCUEIL;
			m_vue.getBar().removeAll();
			m_vue.getBar().add(m_vue.getMenu());
		}
		else if (m_panelCourant == PANEL.ACCUEIL)
		{
			m_panelCourant =  ControllerAccueil.getProchainPanel();
			transition();
			return m_panelCourant;
		}
		else if (m_panelCourant == PANEL.FACTURATION)
		{
			if (!m_facturationSet)
			{
				new ControllerFacturation((FacturationPanel) getControlledPanel(getPanel(PANEL.FACTURATION)));
				m_facturationSet = true;
			}
		}
		else if (m_panelCourant == PANEL.SUPREME)
		{
			if (!m_supremeSet)
			{
				new ControllerSupreme((SupremePanel) getControlledPanel(getPanel(PANEL.SUPREME)));
				m_supremeSet = true;
			}
		}
		else if (m_panelCourant == PANEL.RESERVATION)
		{
			if (!m_reservationSet)
			{
				new ControllerReservation((ReservationPanel) getControlledPanel(getPanel(PANEL.RESERVATION)), m_vue.getMenuItem());
				m_reservationSet = true;
			}
			m_vue.getBar().add(m_vue.getMenuReservation());
		}
		else if (m_panelCourant == PANEL.CLIENTELE)
		{
			if (!m_clienteleSet)
			{
				new ControllerClientele((ClientelePanel) getControlledPanel(getPanel(PANEL.CLIENTELE)));
				m_clienteleSet = true;
			}
		}
		m_retour = false;
		return returnValue;
	}
	
	public static String getPanel(PANEL typePanel)
	{
		if (typePanel == PANEL.CONNECTION)
				return CONNECTION;
		else if (typePanel == PANEL.CLIENTELE)
			return CLIENTELE;
		else if (typePanel == PANEL.FACTURATION)
			return FACTURATION;
		else if (typePanel == PANEL.SUPREME)
			return SUPREME;
		else if(typePanel == PANEL.RESERVATION)
			return RESERVATION;
		else if (typePanel == PANEL.ACCUEIL)
			return ACCUEIL;
		System.err.println("Error in getPanel()");
		return null;
	}

	@Override
	public void initController() {
		m_connection = new ControllerConnection((ConnectionPanel) getControlledPanel(getPanel(PANEL.CONNECTION)));
		JMenuItem deconnection = m_vue.getMenuItem().get(Vue.MENU_ITEM.DECONNECTION.ordinal());
		deconnection.addActionListener(e -> deconnecter());
		JMenuItem retour = m_vue.getMenuItem().get(Vue.MENU_ITEM.RETOUR.ordinal());
		retour.addActionListener(e -> retour());
	}

	private void retour() {
		m_retour = true;
	}
	
	private void deconnecter() {
		m_connecte = ETAT_CONNECTION.DISCONNECTED;
	}
	
	public HotelPanel getControlledPanel(String s)
	{
		for (Component component : m_vue.getContentPane().getComponents())
		{
			HotelPanel panel = (HotelPanel) component;
			if (panel.getName().equals(s))
				return (HotelPanel) component;
		}
		return null;
	}
	
	public static void main(String[] args) {
		new ControllerVue();
	}
}
