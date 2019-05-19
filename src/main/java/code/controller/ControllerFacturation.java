package code.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import code.*;
import code.model.DAOInterfaces.DAOClient;
import code.model.DAOInterfaces.DAOHotel;
import code.model.DAOInterfaces.DAOReservation;
import code.model.DAOInterfaces.DAOTypeService;
import code.model.DAOJDBC.DAOClientJDBC;
import code.model.DAOJDBC.DAOHotelJDBC;
import code.model.DAOJDBC.DAOReservationJDBC;
import code.model.DAOJDBC.DAOTypeServiceJDBC;
import code.view.Panels.FacturationPanel;

public class ControllerFacturation extends AbstractController {

	private DAOClient daoClient = new DAOClientJDBC();
	private DAOReservation daoReservation = new DAOReservationJDBC();
	private DAOHotel daoHotel = new DAOHotelJDBC();
	private DAOTypeService daoTypeService = new DAOTypeServiceJDBC();
	private Admin admin = SessionUnique.getInstance().getSession();

	private FacturationPanel m_panel;
	public ControllerFacturation(FacturationPanel facturationPanel) {
		m_panel = facturationPanel;
		initController();
	}

	@Override
	public void initController() 
	{
		afficherClientsPresents();
	}
	
	public void choisirClient()
	{
		Object [][] donnees = 
			{
					{ "JeanTest", "Test" }, 
			   		{ "BernardTest", "Test"}, 
			   		{ "MarieTest", "Test"},
			};
			
		String [] enTete = {"Type", "Prix"};
		// Ici recuperer les clients presents
		JTable table = m_panel.setTableauClients(donnees, enTete);
		table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) 
                {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    // Ici recuperer les infos reservations
            		Object [][] donnees = 
            			{
            					{ "ReservationATest", "Test" }, 
            			   		{ "ReservationBTest", "Test"}, 
            			   		{ "ReservationCTest", "Test"},
            			};
            			
            			String [] enTete = {"ReservationTest", "Type"};
                   choisirReservation(m_panel.setTableauReservation(donnees, enTete));
                }
            }

			private void choisirReservation(JTable tableauReservation) {
				tableauReservation.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (e.getClickCount() == 2) 
                        {
                            JTable target = (JTable)e.getSource();
                            int row = target.getSelectedRow();
                            int column = target.getSelectedColumn();
                            // Ici recuperer les details de la facture
                    		Object [][] donnees = 
                    			{
                    					{ "FactureATest", "Test" }, 
                    			   		{ "FactureBTest", "Test"}, 
                    			   		{ "FactureCTest", "Test"},
                    			};
                    			
                    			String [] enTete = {"NomTest", "Type"};
                            m_panel.setTableauFacture(donnees, enTete);
                    		JButton bouton = m_panel.getBoutons().get(0);
                    		bouton.addActionListener(e1 -> valider());
                        }
                    }	
                });
			}
		});
	}

	private void afficherClientsPresents() {

		Map<Client, Reservation> clientsPresents = daoClient.findByHotel(admin.getHotelsGeres().get(0));
		Object[][] donnees = new Object[clientsPresents.size()][12];
		System.out.println(clientsPresents);
		int i = 0;
		for (Map.Entry<Client, Reservation> entry : clientsPresents.entrySet()) {
			donnees[i][0] = entry.getKey().getNum();
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
					numClient = (Integer)target.getModel().getValueAt(row, 0);
					numReservation = (Integer)target.getModel().getValueAt(row, 4);

				}
				return;
			}
		});
	}
	
	// Ici valider la facture
	private Object valider() {
		// TODO Auto-generated method stub
		return null;
	}

}
