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
    private Reservation currentReservation;

    private FacturationPanel m_panel;

    public ControllerFacturation(FacturationPanel facturationPanel) {
        m_panel = facturationPanel;
        initController();
    }

    @Override
    public void initController() {
        choisirClient();
    }

    public void choisirClient() {
        Map<Client, Reservation> clientsPresents = daoClient.findByHotel(admin.getHotelsGeres().get(0));

        Object[][] donnees = new Object[clientsPresents.size()][12];
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

        String[] enTete = {"Num client", "Prenom", "Nom", "Entreprise", "Num reservation", "Date Arrivee", "Date Depart", "Nb personnes"};
        // Ici recuperer les clients presents
        JTable table = m_panel.setTableauClients(donnees, enTete);

        table.addMouseListener(new MouseAdapter() {

            Integer numClient = null;
            Integer numReservation = null;

            String[] enteteFacure = null;
            String[][] donneesFacture = null;

            // quand je clique sur un client
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    numClient = (Integer) target.getModel().getValueAt(row, 0);
                    numReservation = (Integer) target.getModel().getValueAt(row, 4);
                    detailFacture();

                    m_panel.setTableauFacture(donneesFacture, enteteFacure);
                    JButton bouton = m_panel.getBoutons().get(0);
                    bouton.addActionListener(e1 -> valider(numClient));

                    //choisirReservation(m_panel.setTableauReservation(donneesFacture, enteteFacure));
                }
                return;
            }

            public void detailFacture() {
                currentReservation = daoReservation.getById(numReservation);
                currentReservation.getDetailFacture(new SimpleFacturationVisitor(), new ReductionAnciennete());

                enteteFacure = new String[]{"Type", "Prix"};
                donneesFacture = new String[currentReservation.getFacture().size() + 2][enteteFacure.length];

                donneesFacture[0][0] = "Client";
                donneesFacture[0][1] = String.valueOf(numClient);

                donneesFacture[1][0] = "Reservation";
                donneesFacture[1][1] = String.valueOf(numReservation);

                int i = 2;

                for (Map.Entry<String, Float> entry : currentReservation.getFacture().entrySet()) {
                    donneesFacture[i][0] = entry.getKey();
                    donneesFacture[i][1] = String.valueOf(entry.getValue());
                    i += 1;
                }
            }
        });
    }


    // Ici valider la facture
    private void valider(Integer numClient) {

        Object[] options = {"Envoyer la facture", "Annuler"};
        int decision = JOptionPane.showOptionDialog(m_panel,
                "Voulez-vous vraiment envoyer la facure ?",
                "Facturation",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (decision == 0) {
            Client client = daoClient.getById(numClient);

            try {
                Email email = new Email("Facturation séjour", client);
                email.facture(currentReservation);
                JOptionPane.showMessageDialog(m_panel, "Facture envoyée", "Facturation", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(m_panel, "Envoi de la facure raté :/", "Facturation", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}
