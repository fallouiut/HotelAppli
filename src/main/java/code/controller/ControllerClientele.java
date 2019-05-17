package code.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import code.*;
import code.controller.ControllerVue.PANEL;
import code.model.DAOInterfaces.DAOClient;
import code.model.DAOInterfaces.DAOHotel;
import code.model.DAOInterfaces.DAOReservation;
import code.model.DAOJDBC.DAOClientJDBC;
import code.model.DAOJDBC.DAOHotelJDBC;
import code.model.DAOJDBC.DAOReservationJDBC;
import code.view.Panels.ClientelePanel;
import code.view.Panels.ClientelePanel.CHAMPS_CLIENTELE;
import code.view.Vues.Vue;

public class ControllerClientele extends AbstractController {

	private DAOClient daoClient = new DAOClientJDBC();
	private DAOReservation daoReservation = new DAOReservationJDBC();
	private DAOHotel daoHotel = new DAOHotelJDBC();
	private Admin admin;
	
	private ClientelePanel m_panel;
	public ControllerClientele(Vue vue) {
		super(vue);
		admin = SessionUnique.getInstance().getSession();
		initController();
	}

	@Override
	public void initController() {
		m_panel = (ClientelePanel) ControllerVue.getPanel(PANEL.CLIENTELE);
		JButton rechercherBouton = m_panel.getBoutons().get(CHAMPS_CLIENTELE.RECHERCHE.ordinal());
		rechercherBouton.addActionListener(e -> trouverHistoriqueClient());
		JButton ajouterServiceBouton = m_panel.getBoutons().get(CHAMPS_CLIENTELE.SERVICE_CLIENT.ordinal());
		ajouterServiceBouton.addActionListener(e -> ajouterServiceClient());
		JButton afficherPresentsBouton = m_panel.getBoutons().get(CHAMPS_CLIENTELE.HOTEL.ordinal());
		afficherPresentsBouton.addActionListener(e -> afficherClientsPresents());

	}	
	// Recupere la liste des clients pr?sents dans l'hotel/les hotels ?
	private void afficherClientsPresents() {
		/*Object [][] donnees =
		{
				{ "Jean", "Bon", "18/05/2019", "Non Payee" }, 
		   		{ "Marc", "Ise", "25/05/2019", "Non Payee" }, 
		   		{ "Joyce", "Lyne", "22/05/2019", "Non payee"},
		};*/
		Object[][] donnees = {};

		//quel hotel ?
		Map<Client, Reservation> clientsPresents = daoClient.findByHotel(admin.getHotelsGeres().get(0));
		int i = 0;
		for (Map.Entry<Client, Reservation> entry : clientsPresents.entrySet()) {
			donnees[i][0] = entry.getKey().getPrenom();
			donnees[i][1] = entry.getKey().getNom();
			donnees[i][2] = entry.getValue().getDateDepart().toString();
			donnees[i][3] = "Non Payée";
			++i;
		}

		String [] enTete = {"Prenom", "Nom", "Date Depart", "Etat Facture"};
		m_panel.setTableauClients(donnees, enTete); 
		return;
	}

	private void trouverHistoriqueClient() { // verifier ID client ici (quel client ??)
		String text = m_panel.getTextes().get(CHAMPS_CLIENTELE.RECHERCHE.ordinal()).getText();
		int idClient = Integer.parseInt(text);
		Client client = daoClient.getById(idClient);
		if (client != null)
			montrerHistorique(client);
		else
			JOptionPane.showMessageDialog(m_panel, "Cet ID ne fait référence a aucun client. \n\n ID : " + text, "Error", JOptionPane.WARNING_MESSAGE);
	}
	// recuperer historique ici
	private void montrerHistorique(Client client)
	{
		/*Object [][] donnees =
		{
				{ "Formule 2", "02/02/08", "08/02/08", "Payee" }, 
		   		{ "Campanil", "15/12/06", "28/12/06", "Payee" }, 
		   		{ "Hilton", "01/04/2019", "---", "Non payee"},
		};*/

		List<Reservation> reservations = daoReservation.findHistoriqueClient(client.getNum());
		Object [][] donnees ={};

		for (int i = 0 ; i < reservations.size() ; ++i) {
			donnees[i][0] = reservations.get(i).getHotel().getNom();
			donnees[i][1] = reservations.get(i).getDateArrivee().toString();
			donnees[i][2] = reservations.get(i).getDateDepart().toString();
			//On se fait pas trop chier pour savoir s'il a payé ou pas lol
			donnees[i][3] = reservations.get(i).getDateDepart().isAfter(LocalDate.now()) ? "Non payée" : "Payée";
		}

		String [] enTete = {"Hotel", "Date Debut", "Date Fin", "Facture"};
		m_panel.setTableauHistorique(donnees, enTete); 
	}
	
	// afficher les reservations
	private void ajouterServiceClient()
	{
		String text = m_panel.getTextes().get(CHAMPS_CLIENTELE.SERVICE_CLIENT.ordinal()).getText();
        int idClient = Integer.parseInt(text);
        Client client = daoClient.getById(idClient);
		if (client != null)
			montrerReservations(client); // afficher ici les reservations
		else
			JOptionPane.showMessageDialog(m_panel, "Cet ID ne fait r?f?rence a aucun client. \n\n ID : " + text, "Error", JOptionPane.WARNING_MESSAGE);
			
	}

	// recuperer les services d'un hotel ici
	private void montrerReservations(Client client)
	{
		/*Object [][] donnees =
		{
				{ "Formule 2", "02/02/08", "08/02/08", "Payee" }, 
		   		{ "Campanil", "15/12/06", "28/12/06", "Payee" }, 
		   		{ "Hilton", "01/04/2019", "---", "Non payee"},
		};*/

        List<Reservation> reservations = daoReservation.findHistoriqueClient(client.getNum());
        Object [][] donnees ={};

        for (int i = 0 ; i < reservations.size() ; ++i) {
            donnees[i][0] = reservations.get(i).getHotel().getNumHotel();
            donnees[i][1] = reservations.get(i).getHotel().getNom();
            donnees[i][2] = reservations.get(i).getDateArrivee().toString();
            donnees[i][3] = reservations.get(i).getDateDepart().toString();
            //On se fait pas trop chier pour savoir s'il a payé ou pas lol
            donnees[i][4] = reservations.get(i).getDateDepart().isAfter(LocalDate.now()) ? "Non payée" : "Payée";
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
                    // Comment je récupère l'id de la réservation ou il a cliqué ??

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

			private void validerAjoutServices() {
				ArrayList <String> servicesAjoutes = new ArrayList <String> ();
				for (JCheckBox box : m_panel.getBoxes())
				{
					if (box.isSelected())
						servicesAjoutes.add(box.getText());
				}
				System.out.println(servicesAjoutes.toString());
				// Ajouter les services
			}
		});
	}

}
