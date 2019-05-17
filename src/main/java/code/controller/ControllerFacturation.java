package code.controller;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import code.controller.ControllerVue.PANEL;
import code.view.Panels.FacturationPanel;
import code.view.Vues.Vue;

public class ControllerFacturation extends AbstractController {

	private FacturationPanel m_panel;
	public ControllerFacturation(Vue vue) {
		super(vue);
		m_panel = (FacturationPanel) ControllerVue.getPanel(PANEL.FACTURATION);
		initController();
	}

	@Override
	public void initController() 
	{
		JButton validerBouton = m_panel.getBoutons().get(0);
		validerBouton.addActionListener(e -> rechercherClient());
	}
	
	// id client a verifier
	public void rechercherClient()
	{
			String texte = m_panel.getTextes().get(0).getText();
			
			// Verifier id client
			if (true)
				faireFacture();
			else
				JOptionPane.showMessageDialog(m_panel, "Cet ID ne fait r�f�rence a aucun client. \n\n ID : " + texte, "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	// recuperer Details Factures
	public void faireFacture()
	{
		Object [][] donnees = 
		{
				{ "Reservation chambre", "150�", "3 jours et 2 personnes" }, 
		   		{ "Repas restaurant", "68�", "" }, 
		   		{ "Menage chambre", "15�", "Une fois"},
		};
		
		String [] enTete = {"Type Depense", "Prix", "Details"};
		m_panel.setTableauFacture(donnees, enTete);
	}

}
