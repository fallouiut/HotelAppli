package code.controller;

import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import code.Client;
import code.Reservation;
import code.view.Panels.SupremePanel;
import code.view.Panels.SupremePanel.BOUTONS_SUPREME;

public class ControllerSupreme extends AbstractController {

	SupremePanel m_panel;
	public ControllerSupreme(SupremePanel panel) {
		super();
		m_panel = panel;
		initController();
	}

	@Override
	public void initController() {
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
		// Deprecated
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
	private void fenetreEtat() {
		Object[][] donnees = {};
		String [] enTete = {"Prenom", "Nom", "Date Depart", "Etat Facture"};
		JTable table = m_panel.setTableauHotels(donnees, enTete); 
		table.addMouseListener(new MouseAdapter() {
            
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) 
                {
                	JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    afficherPopUpDecision();
                    // Ici recuperer les infos sur l'hotel selectionne
                }
                return;
            }
			private void afficherPopUpDecision() {
				Object[] options = {"Ajouter Service",
	                    "Supprimer Service",
	                    "Ajouter travaux", "Ajouter Chambres"};
				
				int decision = JOptionPane.showOptionDialog(m_panel,
					    "Que désirez-vous faire ?", "Action sur hôtel",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[2]);
						if (decision == 0)
							;//afficherVueAjouterService(client);
						else if (decision == 1)
							afficherVueSupprimerService();
						else if (decision == 2)
							;//afficherVueTravaux();
						else
							;//afficherVueAjoutChambres();
				
			}
			// recuperer les services
			private void afficherVueSupprimerService() {
				JTable table = m_panel.setTableauServices(donnees, enTete);
				table.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
		                if (e.getClickCount() == 2) 
		                {
		                	JTable target = (JTable)e.getSource();
		                    int row = target.getSelectedRow();
		                    int column = target.getSelectedColumn();
		                    // Ici recuperer le service à supprimer
		                    afficherPopUpConfirmationSupression();
		                }
		                return;
		            }

					private void afficherPopUpConfirmationSupression() {
						Object[] options = {"Oui", "Non"};
						
						int decision = JOptionPane.showOptionDialog(m_panel,
							    "Etes-vous sûr(e) de vouloir supprimer ce service ?", "Suppresion service",
							    JOptionPane.YES_NO_CANCEL_OPTION,
							    JOptionPane.QUESTION_MESSAGE,
							    null,
							    options,
							    options[1]);
								if (decision == 0)
									; // Supprimer le service selectionné 			
					}
				
				});
			}
		});
	}

	private Object fenetreCompteRendu() {
		// TODO Auto-generated method stub
		return null;
	}

}
