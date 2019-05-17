package code.controller;

import java.util.ArrayList;

import javax.swing.JButton;

import code.Admin;
import code.TypeAcces;
import code.model.DAOInterfaces.DAOAdmin;
import code.model.DAOJDBC.DAOAdminJDBC;
import code.view.Panels.AccueilPanel;
import code.view.Vues.Vue;

public class ControllerAccueil extends AbstractController {
	
	private ArrayList <JButton> m_boutons = new ArrayList <JButton> ();

	private static PANEL m_prochainPanel;
	private AccueilPanel  m_panel;
	private Admin m_Admin;
	private DAOAdmin daoAdmin;
	
	public ControllerAccueil(AccueilPanel panel, Admin admin) {
		m_panel = panel;
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
			m_boutons.get(i).addActionListener(e -> deciderProchainPanel((JButton) e.getSource()));
	}
	@Override
	public void initController() 
	{
		initPanel();
		verifierDroits();
	}
	
	private void verifierDroits () 
	{
		if (m_Admin.getDroits().get(SUPREME))
			m_panel.getBouton(SUPREME).setEnabled(true);
		if (!m_Admin.getDroits().get(FACTURATION))
			m_panel.getBouton(FACTURATION).setEnabled(true);
		if (m_Admin.getDroits().get(RESERVATION))
			m_panel.getBouton(RESERVATION).setEnabled(true);
		if (m_Admin.getDroits().get(CLIENTELE))
			m_panel.getBouton(CLIENTELE).setEnabled(true);
		m_panel.getBouton(SUPREME).setEnabled(true); // A ENLEVER
	}
	
	private void deciderProchainPanel(JButton bouton)
	{
		String nomPanel = bouton.getText();
		switch (nomPanel)
		{
			case CLIENTELE :
				m_prochainPanel = PANEL.CLIENTELE;
				break;
			case FACTURATION :
				m_prochainPanel = PANEL.FACTURATION;
				break;
			case SUPREME :
				m_prochainPanel = PANEL.SUPREME;
				break;
			case RESERVATION :
				m_prochainPanel = PANEL.RESERVATION;
				break;
			default :
				System.err.println("Error while deciding panel.");
		}
		m_panel.setTermine(true);
	}
	
	

}
