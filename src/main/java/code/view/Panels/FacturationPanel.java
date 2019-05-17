package code.view.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class FacturationPanel extends HotelPanel {

	public FacturationPanel(String type) 
	{
		super(type);
		setLayout(new BorderLayout());	
	}
	
	public JTable setTableauClients(Object[][] donnees, Object[] enTete) {

		JTable tableClient = new JTable (donnees, enTete);
		JScrollPane pane = new JScrollPane(tableClient);
		add(pane, BorderLayout.CENTER);
		return tableClient;
	}
	
	public JTable setTableauReservation(Object[][] donnees, Object[] enTete) {
		
		JFrame vueReservation = new JFrame ("Liste Reservations");
		vueReservation.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable table = new JTable(donnees, enTete);
		vueReservation.add(new JScrollPane(table), BorderLayout.CENTER);
		vueReservation.setVisible(true);
		vueReservation.pack();
		return table;
	}

	public void setTableauFacture(Object[][] donnees, Object[] enTete) {
		
		JFrame vueFacture = new JFrame ("Facture Client");
		vueFacture.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable tableau = new JTable(donnees, enTete);
		JButton boutonValiderFacture = new JButton ("Valider");
		m_boutons.add(boutonValiderFacture);
		boutonValiderFacture.setPreferredSize(new Dimension(75, 20));
		vueFacture.add(new JScrollPane(tableau), BorderLayout.CENTER);
		vueFacture.add(boutonValiderFacture, BorderLayout.SOUTH);
		vueFacture.setVisible(true);
		vueFacture.pack();
	}
}
