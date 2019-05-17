package code.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JTable;

import code.view.Panels.FacturationPanel;

public class ControllerFacturation extends AbstractController {

	private FacturationPanel m_panel;
	public ControllerFacturation(FacturationPanel facturationPanel) {
		m_panel = facturationPanel;
		initController();
	}

	@Override
	public void initController() 
	{
		choisirClient();
	}
	
	public void choisirClient()
	{
		Object [][] donnees = 
			{
					{ "JeanTest", "Test" }, 
			   		{ "BernardTest", "Test"}, 
			   		{ "MarieTest", "Test"},
			};
			
		String [] enTete = {"NomTest", "Type"};
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
	
	// Ici valider la facture
	private Object valider() {
		// TODO Auto-generated method stub
		return null;
	}

}
