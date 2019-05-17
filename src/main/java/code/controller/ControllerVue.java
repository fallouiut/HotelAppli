package code.controller;

import javax.swing.JMenuItem;

import code.view.Panels.HotelPanel;
import code.view.Vues.Vue;
public class ControllerVue extends AbstractController {
	
	public enum PANEL { CONNECTION, ACCUEIL, CLIENTELE, FACTURATION, RESERVATION, SUPREME, RETOUR };
	public enum ETAT_CONNECTION { UNDEFINED, CONNECTED, DISCONNECTED };
	
	public boolean m_clienteleSet = false;
	public boolean m_reservationSet = false;
	public boolean m_facturationSet = false;
	public boolean m_supremeSet = false;
	
	private HotelPanel m_panelCourant;
	private ETAT_CONNECTION m_connecte = ETAT_CONNECTION.UNDEFINED;
	private boolean m_retour = false;
	private ControllerConnection m_connection;
	
	public ControllerVue() {
		super(new Vue());
		initController();
		deroulement();
	}

	public void deroulement()
	{
		m_panelCourant = getPanel(PANEL.CONNECTION);
		m_vue.add(m_panelCourant);
		m_vue.pack();
		while (true)
		{
			while (m_panelCourant.fonctionne())
			{
				if ( m_connecte == ETAT_CONNECTION.DISCONNECTED || m_retour)
					break;
			}
			m_vue.remove(m_panelCourant);
			int retourTransition = transition();
			m_panelCourant = m_vue.getPanels().get(retourTransition);
			m_vue.add(m_panelCourant);
			m_vue.pack();
		}
	}
	
	public int transition()
	{
		int returnValue = -1;
		m_panelCourant.setTermine(false);
		if (m_connecte == ETAT_CONNECTION.DISCONNECTED)
		{
			m_vue.getMenu().setEnabled(false);
			returnValue = PANEL.CONNECTION.ordinal();
		}
		else if (m_panelCourant.equals(getPanel(PANEL.CONNECTION)))
		{
			m_connecte = ETAT_CONNECTION.CONNECTED;	
			returnValue = PANEL.ACCUEIL.ordinal();
			m_vue.getMenu().setEnabled(true);
			new ControllerAccueil(m_vue, m_connection.getAdmin());
		}
		else if (m_retour)
			returnValue = PANEL.ACCUEIL.ordinal();
		else if (m_panelCourant.equals(getPanel(PANEL.ACCUEIL)))
			returnValue =  ControllerAccueil.getProchainPanel().ordinal();
		else if (m_panelCourant.equals(getPanel(PANEL.FACTURATION)))
		{
			if (!m_facturationSet)
			{
				new ControllerFacturation(m_vue);
				m_facturationSet = true;
			}
		}
		else if (m_panelCourant.equals(getPanel(PANEL.SUPREME)))
		{
			if (!m_supremeSet)
			{
				new ControllerSupreme(m_vue);
				m_supremeSet = true;
			}
		}
		else if (m_panelCourant.equals(getPanel(PANEL.RESERVATION)))
		{
			if (m_reservationSet)
			{
				new ControllerReservation(m_vue);
				m_reservationSet = true;
			}
		}
		m_retour = false;
		if (returnValue == -1)
			System.err.println("Error in transition");
		return returnValue;
	}

	public HotelPanel getPanelCourant()
	{
		return m_panelCourant;
	}
	
	public static HotelPanel getPanel(PANEL typePanel)
	{
		return m_vue.getPanels().get(typePanel.ordinal());
	}

	@Override
	public void initController() {
		m_connection = new ControllerConnection(m_vue);
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

	public static void main(String[] args) {
		new ControllerVue();
	}
}
