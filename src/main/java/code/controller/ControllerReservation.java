package code.controller;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import code.Admin;
import code.Reservation;
import code.SessionUnique;
import code.model.DAOInterfaces.DAOReservation;
import code.model.DAOJDBC.DAOReservationJDBC;
import code.view.Panels.ReservationPanel;
import code.view.Vues.Vue.MENU_ITEM;

public class ControllerReservation extends AbstractController {

	private ReservationPanel m_panel;
	private boolean isDemande = true;
	private boolean isConfirmation = false;
	private JMenuItem m_demandee;
	private JMenuItem m_confirmation;

	private Admin admin = SessionUnique.getInstance().getSession();
	private DAOReservation daoReservation = new DAOReservationJDBC();
	
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
			List<Reservation> reservationsConfirmees = daoReservation.findByEtat(admin.getHotelsGeres().get(0), "CONFIRMATION");
			Object [][] donnees = new Object[reservationsConfirmees.size()][16];

			for (int i = 0 ; i < reservationsConfirmees.size() ; ++i) {
				donnees[i][0] = reservationsConfirmees.get(i).getHotel().getNumHotel();
				donnees[i][1] = reservationsConfirmees.get(i).getClient().getNom();
				donnees[i][2] = reservationsConfirmees.get(i).getClient().getPrenom();
				donnees[i][3] = reservationsConfirmees.get(i).getClient().getNomEnteprise();
				donnees[i][4] = reservationsConfirmees.get(i).getNumReservation();
				donnees[i][5] = reservationsConfirmees.get(i).getDateArrivee();
				donnees[i][6] = reservationsConfirmees.get(i).getDateDepart();
				donnees[i][7] = reservationsConfirmees.get(i).getNbPersonnes();
			}
				
			String [] enTete = {"Num hotel", "Nom Client", "Prenom Client", "Entreprise", "Num reservation", "Date arrivee", "Date depart", "Nb personnes"};
			tableReservation = m_panel.setTableauReservation(donnees, enTete);
			tableReservation.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2)
                    {
                    	int reply = JOptionPane.showConfirmDialog(m_panel, "Voulez-vous passer cette reservation dans l'historique ?", "Gestion de la reservation", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                          // Mettre reservation dans historique ici
                        	System.out.println("Reservation enregistree dans l'historique.");
                        }
                    }
                }
			});
		}
		else
		{
			List<Reservation> reservationsConfirmees = daoReservation.findByEtat(admin.getHotelsGeres().get(0), "DEMANDEE");
			Object [][] donnees = new Object[reservationsConfirmees.size()][16];

			for (int i = 0 ; i < reservationsConfirmees.size() ; ++i) {
				donnees[i][0] = reservationsConfirmees.get(i).getHotel().getNumHotel();
				donnees[i][1] = reservationsConfirmees.get(i).getClient().getNom();
				donnees[i][2] = reservationsConfirmees.get(i).getClient().getPrenom();
				donnees[i][3] = reservationsConfirmees.get(i).getClient().getNomEnteprise();
				donnees[i][4] = reservationsConfirmees.get(i).getNumReservation();
				donnees[i][5] = reservationsConfirmees.get(i).getDateArrivee();
				donnees[i][6] = reservationsConfirmees.get(i).getDateDepart();
				donnees[i][7] = reservationsConfirmees.get(i).getNbPersonnes();
			}

			String [] enTete = {"Num hotel", "Nom Client", "Prenom Client", "Entreprise", "Num reservation", "Date arrivee", "Date depart", "Nb personnes"};
			tableReservation = m_panel.setTableauReservation(donnees, enTete);
			// Changer etat reservation dans le MouseAdapter()
			tableReservation.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2)
                    {
						JTable target = (JTable)e.getSource();
						int row = target.getSelectedRow();
						int numReservation = (Integer)target.getModel().getValueAt(row, 4);
                    	int reply = JOptionPane.showConfirmDialog(m_panel, "Voulez-vous passer cette reservation dans l'etat \"En attente de confirmation\" ?", "Gestion de la reservation", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                        	daoReservation.updateEtatReservation(numReservation, "ATTENTE_CONFIRMATION");
                        	System.out.println("Etat de la reservation modifié.");
                        }
                    }
                }
			});
		}
	}
	
	
}
