package code.controller;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import code.view.Panels.ReservationPanel;
import code.view.Vues.Vue.MENU_ITEM;

public class ControllerReservation extends AbstractController {

	private ReservationPanel m_panel;
	private boolean isDemande = true;
	private boolean isConfirmation = false;
	private JMenuItem m_demandee;
	private JMenuItem m_confirmation;
	
	public ControllerReservation(ReservationPanel panel, ArrayList <JMenuItem> itemsReservation) 
	{
		super();
		m_panel = panel;
		m_demandee = itemsReservation.get(MENU_ITEM.RESERVATION_DEMANDE.ordinal());
		m_confirmation = itemsReservation.get(MENU_ITEM.RESERVATION_CONFIRMATION.ordinal());
		initController();
	}

	@Override
	public void initController() 
	{
		m_demandee.addActionListener(e -> changerFiltre());
		m_confirmation.addActionListener(e -> changerFiltre());
		changerFiltre();
	}

	private void changerFiltre() {
		m_confirmation.setEnabled(isConfirmation);
		m_demandee.setEnabled(isDemande);
		isDemande = !isDemande;
		isConfirmation = !isConfirmation;
		afficherTableauReservation();
	}

	private void afficherTableauReservation() {
		JTable tableReservation;
		if (isConfirmation)
		{
			// Recuperer les reservations dans cet etat
			Object [][] donnees = 
				{
						{ "JeanTest2", "Test" }, 
				   		{ "BernardTest", "Test"}, 
				   		{ "MarieTest", "Test"},
				};
				
			String [] enTete = {"Type", "Prix"};
			tableReservation = m_panel.setTableauReservation(donnees, enTete);
			tableReservation.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2)
                    {
                    	int reply = JOptionPane.showConfirmDialog(m_panel, "Voulez-vous passer cette reservation dans l'historique ?", "Gestion de la reservation", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                          // Mettre reservation dans historique ici
                        	System.out.println("Reservation enregistreee dans l'historique.");
                        }
                    }
                }
			});
		}
		else
		{
			// Recuperer les reservations dans cet etat
			Object [][] donnees = 
				{
						{ "JeanTest1", "Test" }, 
				   		{ "BernardTest", "Test"}, 
				   		{ "MarieTest", "Test"},
				};
				
			String [] enTete = {"Type", "Prix"};
			tableReservation = m_panel.setTableauReservation(donnees, enTete);
			// Changer etat reservation dans le MouseAdapter()
			tableReservation.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2)
                    {
                    	int reply = JOptionPane.showConfirmDialog(m_panel, "Voulez-vous passer cette reservation dans l'etat \"En attente de confirmation\" ?", "Gestion de la reservation", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                        	// Changer etat reservation ici
                        	System.out.println("Etat de la reservation modifié.");
                        }
                    }
                }
			});
		}
	}
	
	
}
