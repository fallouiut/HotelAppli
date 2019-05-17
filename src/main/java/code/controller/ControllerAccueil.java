package code.controller;

import java.util.ArrayList;

import javax.swing.JButton;

import code.Admin;
import code.TypeAcces;
import code.controller.ControllerVue.PANEL;
import code.model.DAOInterfaces.DAOAdmin;
import code.model.DAOJDBC.DAOAdminJDBC;
import code.view.Panels.AccueilPanel;
import code.view.Vues.Vue;

public class ControllerAccueil extends AbstractController {
	
	private ArrayList <JButton> m_boutons = new ArrayList <JButton> ();
	private static boolean m_clienteleSet = false;
	private static boolean m_reservationSet = false;
	private static boolean m_facturationSet = false;
	private static boolean m_supremeSet = false;

	private static PANEL m_prochainPanel;
	private AccueilPanel  m_panel;
	private Admin m_Admin;
	private DAOAdmin daoAdmin;
	
	public ControllerAccueil(Vue vue, Admin admin) {
		super(vue);
		m_Admin = admin;
		initController();
		daoAdmin = new DAOAdminJDBC();
	}
	
	public static PANEL getProchainPanel()
	{
		return m_prochainPanel;
	}
	
	public void initPanel()
	{
		ArrayList <String> nomsBoutons = new ArrayList <String> ();
		for (TypeAcces acces : DAOAdminJDBC.getTypesAcces())
			nomsBoutons.add(acces.getTypeAcces());	
		m_panel.construireBoutons(nomsBoutons);
		m_boutons = m_panel.getBoutons();
		for (int i = 0; i < m_boutons.size(); i++)
			m_boutons.get(i).addActionListener(e -> creerProchainControleur((JButton) e.getSource()));
	}
	@Override
	public void initController() 
	{
		m_panel = (AccueilPanel) ControllerVue.getPanel(PANEL.ACCUEIL);
		initPanel();
		verifierDroits();
	}
	
	private void verifierDroits () 
	{
		if (m_Admin.getDroits().get("Supreme"))
		{
			for (JButton bouton : m_boutons)
				bouton.setEnabled(true);
		}
		else
		{
			if (!m_Admin.getDroits().get(FACTURATION))
				m_panel.getBouton(FACTURATION).setEnabled(true);
			if (m_Admin.getDroits().get(RESERVATION))
				m_panel.getBouton(RESERVATION).setEnabled(true);
			if (m_Admin.getDroits().get(CLIENTELE))
				m_panel.getBouton(CLIENTELE).setEnabled(true);
		}
	}
	
	private void creerProchainControleur(JButton bouton)
	{
		String nomPanel = bouton.getText();
		switch (nomPanel)
		{
			case CLIENTELE :
				if (!m_clienteleSet)
				{
					new ControllerClientele(m_vue);
					m_clienteleSet = true;
				}
				m_prochainPanel = PANEL.CLIENTELE;
				break;
			case FACTURATION :
				if (!m_facturationSet)
				{
					new ControllerFacturation(m_vue);
					m_facturationSet = true;
				}
				m_prochainPanel = PANEL.FACTURATION;
				break;
			case SUPREME :
				if (!m_supremeSet)
				{
					new ControllerSupreme(m_vue);
					m_supremeSet = true;
				}
				m_prochainPanel = PANEL.SUPREME;
				break;
			case RESERVATION :
				if (!m_reservationSet)
				{
					new ControllerReservation(m_vue);
					m_reservationSet = true;
				}
				m_prochainPanel = PANEL.RESERVATION;
				break;
			default :
				System.err.println("Error while deciding panel.");
		}
		m_panel.setTermine(true);
	}
	
	

}
