package code.controller;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import code.controller.ControllerVue.PANEL;
import code.view.Panels.SupremePanel;
import code.view.Panels.SupremePanel.BOUTONS_SUPREME;
import code.view.Vues.Vue;

public class ControllerSupreme extends AbstractController {

	SupremePanel m_panel;
	public ControllerSupreme(Vue vue) {
		super(vue);
		initController();
	}

	@Override
	public void initController() {
		
		m_panel = (SupremePanel) ControllerVue.getPanel(PANEL.SUPREME);
		JButton ajouterBouton = m_panel.getBoutons().get(BOUTONS_SUPREME.AJOUTER.ordinal());
		ajouterBouton.addActionListener(e -> fenetreAjouter());
		JButton etatHotelBouton = m_panel.getBoutons().get(BOUTONS_SUPREME.ETAT_HOTEL.ordinal());
		etatHotelBouton.addActionListener(e -> fenetreEtat());
		JButton compteRenduBouton = m_panel.getBoutons().get(BOUTONS_SUPREME.COMPTE_RENDU.ordinal());
		compteRenduBouton.addActionListener(e -> fenetreCompteRendu());
		
	}
	
	private void fenetreAjouter() {
		Object[] options = {"Ville",
		                    "Hotel",
		                    "Annuler"};
		String decision = Integer.toString(JOptionPane.showOptionDialog(m_panel,
		    "Voulez-vous ajouter une ville ou un hotel ?", "Ajouter dans la base",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[2]));
		if (decision.equals(options[0]))
			ajouterVille();
		else if (decision.equals(options[1]))
			ajouterHotel();
	}

	// Requete d'ajout Hotel a faire
	private void ajouterHotel() {
			
	}

	// Requete d'ajout Ville a faire
	private void ajouterVille() {
		// TODO Auto-generated method stub
		
	}

	// Requete de recuperation des hotels à faire
	// donnees Objet[][]
	// enTete Objet[][]
	private Object fenetreEtat() {
		//m_panel.setTableauHotels(donnees, enTete);
		return null;
	}

	private Object fenetreCompteRendu() {
		// TODO Auto-generated method stub
		return null;
	}

}
