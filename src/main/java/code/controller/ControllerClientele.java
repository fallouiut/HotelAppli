package code.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import code.*;
import code.model.DAOInterfaces.DAOClient;
import code.model.DAOInterfaces.DAOHotel;
import code.model.DAOInterfaces.DAOReservation;
import code.model.DAOInterfaces.DAOTypeService;
import code.model.DAOJDBC.DAOClientJDBC;
import code.model.DAOJDBC.DAOHotelJDBC;
import code.model.DAOJDBC.DAOReservationJDBC;
import code.model.DAOJDBC.DAOTypeServiceJDBC;
import code.view.Panels.ClientelePanel;

public class ControllerClientele extends AbstractController {

	private DAOClient daoClient = new DAOClientJDBC();
	private DAOReservation daoReservation = new DAOReservationJDBC();
	private DAOHotel daoHotel = new DAOHotelJDBC();
	private DAOTypeService daoTypeService = new DAOTypeServiceJDBC();
	private Admin admin;
	
	private ClientelePanel m_panel;
	public ControllerClientele(ClientelePanel panel) {
		m_panel = panel;
		admin = SessionUnique.getInstance().getSession();
		initController();
	}

	@Override
	public void initController() {
		afficherClientsPresents();
	}
	
	private void afficherClientsPresents() {


		//TODO : remplacer plus tard par .get(0), là c'est juste pour test
		Map<Client, Reservation> clientsPresents = daoClient.findByHotel(admin.getHotelsGeres().get(6));
		Object[][] donnees = new Object[clientsPresents.size()][12];
		System.out.println(clientsPresents);
		int i = 0;
		for (Map.Entry<Client, Reservation> entry : clientsPresents.entrySet()) {
			donnees[i][0] =
					entry.getKey().getNum();
			donnees[i][1] = entry.getKey().getPrenom();
			donnees[i][2] = entry.getKey().getNom();
			donnees[i][3] = entry.getKey().getNomEnteprise();
			donnees[i][4] = entry.getValue().getNumReservation();
			donnees[i][5] = entry.getValue().getDateArrivee().toString();
			donnees[i][6] = entry.getValue().getDateDepart().toString();
			donnees[i][7] = entry.getValue().getNbPersonnes();
			++i;
		}

		String [] enTete = {"Num client", "Prenom", "Nom", "Entreprise", "Num reservation", "Date Arrivee", "Date Depart", "Nb personnes"};
		JTable table = m_panel.setTableauClients(donnees, enTete);
		table.addMouseListener(new MouseAdapter() {
            
			Integer numClient = null;
			Integer numReservation = null;
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) 
                {
                	JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    afficherPopUpDecision();
					numClient = (Integer)target.getModel().getValueAt(row, 0);
					numReservation = (Integer)target.getModel().getValueAt(row, 4);
                }
                return;
            }

			private void afficherPopUpDecision() {
				Object[] options = {"Consulter Historique",
	                    "Ajouter un service",
	                    "Annuler"};
				int decision = JOptionPane.showOptionDialog(m_panel,
			    "Que désirez-vous faire ?", "Client selectionné",
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[2]);
				if (decision == 0)
					montrerHistorique(numClient);
				else if (decision == 1)
				{
					// recuperer les services disponibles
					List <TypeService> services = daoTypeService.findAll();
					List<String> nomServices = new ArrayList<>();
					for (TypeService service : services) {
						nomServices.add(service.getNom());
					}
					m_panel.setChoixService(nomServices);
					JButton validerBouton = m_panel.getBoutons().get(0);
					validerBouton.addActionListener(e -> ajouterServiceClient(numReservation));
				}
				
			}
		});
	}
       
	// recuperer historique ici
	private void montrerHistorique(Integer numClient)
	{
		System.out.println(numClient);
		List<Reservation> reservations = daoReservation.findHistoriqueClient(numClient);
		System.out.println(reservations);
		Object [][] donnees = new Object[reservations.size()][16];

		for (int i = 0 ; i < reservations.size() ; ++i) {
			donnees[i][0] = reservations.get(i).getHotel().getNom();
			donnees[i][1] = reservations.get(i).getClient().getPrenom();
			donnees[i][2] = reservations.get(i).getClient().getNom();
			donnees[i][3] = reservations.get(i).getClient().getNomEnteprise();
			donnees[i][4] = reservations.get(i).getNumReservation();
			donnees[i][5] = reservations.get(i).getDateArrivee().toString();
			donnees[i][6] = reservations.get(i).getDateDepart().toString();
			donnees[i][7] = reservations.get(i).getNbPersonnes();
			donnees[i][8] = reservations.get(i).getEtat();
			donnees[i][9] = reservations.get(i).getPrixTotal();
			donnees[i][10] = reservations.get(i).getReduction();
			donnees[i][11] = reservations.get(i).getDateDepart().isAfter(LocalDate.now()) ? "Non payée" : "Payée";
		}

		String [] enTete = {"Hotel", "Nom", "Prenom", "Entreprise", "Num reservation", "Date Debut", "Date Fin", "Nb personnes", "Etat", "Prix total", "Reduction", "Facture"};
		m_panel.setTableauHistorique(donnees, enTete);
		JButton boutonPub = m_panel.getBoutons().get(0);
		boutonPub.addActionListener(e -> envoyerPub());
	}
	// ajouter service ici
	private void ajouterServiceClient(Integer numReservation) {
		List <String> servicesAjoutes = new ArrayList <String> ();
		for (JCheckBox box : m_panel.getBoxes())
		{
			if (box.isSelected())
				servicesAjoutes.add(box.getText());
		}
		System.out.println(servicesAjoutes.toString());
		daoReservation.updateLiensTypeService(numReservation, servicesAjoutes);
	}

	private void envoyerPub() {
		JOptionPane.showMessageDialog(m_panel, "Publicité envoyée !", "Publicité", JOptionPane.INFORMATION_MESSAGE);
	}

	// Je sais plus trop à quoi ça sert tout ça..
//	private void montrerReservations(Client client)
	//{
		/*Object [][] donnees =
		{
				{ "Formule 2", "02/02/08", "08/02/08", "Payee" }, 
		   		{ "Campanil", "15/12/06", "28/12/06", "Payee" }, 
		   		{ "Hilton", "01/04/2019", "---", "Non payee"},
		};*/

   /*     List<Reservation> reservations = daoReservation.findHistoriqueClient(client.getNum());
        Object [][] donnees ={};

        for (int i = 0 ; i < reservations.size() ; ++i) {
            donnees[i][0] = reservations.get(i).getHotel().getNumHotel();
            donnees[i][1] = reservations.get(i).getHotel().getNom();
            donnees[i][2] = reservations.get(i).getDateArrivee().toString();
            donnees[i][3] = reservations.get(i).getDateDepart().toString();
            //On se fait pas trop chier pour savoir s'il a payÃ© ou pas lol
            donnees[i][4] = reservations.get(i).getDateDepart().isAfter(LocalDate.now()) ? "Non payÃ©e" : "PayÃ©e";
        }

        //On peut rajouter plus d'informations (a peu pres tout ce qu'il y a dans l'objet Reservation
		String [] enTete = {"Id Hotel", "Nom Hotel", "Date Debut", "Date Fin", "Facture"};
		JTable table = m_panel.setTableauReservations(donnees, enTete);
		table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();

                    // Recuperer l'id reservation, l'hotel et les services propos?s dans cet hotel.
                    // Comment je rÃ©cupÃ¨re l'id de la rÃ©servation ou il a cliquÃ© ??

                    //TODO : VERIFIER SI CA MARCHE ! A LA COLONNE 0 DE LA TABLE IL Y A L'ID RESERVATION
                    int idReservation = (Integer)target.getModel().getValueAt(row, 0);
                    for (Reservation reservation : reservations) {
                        if (reservation.getNumReservation() == idReservation) {
                            Set<TypeService> servicesProposes = daoHotel.getServicesById(reservation.getHotel().getNumHotel());
                            ArrayList<String> nomsServices = new ArrayList<>();
                            for (TypeService service : servicesProposes) {
                                nomsServices.add(service.getNom());
                            }
                            JButton boutonValider = m_panel.setChoixService(nomsServices);
                            boutonValider.addActionListener(e1 -> validerAjoutServices());
                        }
                    }
                }
            }


		});
	} */

}
