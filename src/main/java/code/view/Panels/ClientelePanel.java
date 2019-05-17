package code.view.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ClientelePanel extends HotelPanel{
	
	private ArrayList <JCheckBox> m_boxes = new ArrayList <JCheckBox> ();
	
	public ClientelePanel(String type) 
	{
		super(type);
		setLayout(new BorderLayout());
		
	}
	public JTable setTableauClients(Object[][] donnees, Object[] enTete) 
	{
		JTable tableClient = new JTable (donnees, enTete);
		JScrollPane pane = new JScrollPane(tableClient);
		add(pane, BorderLayout.CENTER);
		return tableClient;
	}
	
	public JTable setTableauHistorique(Object[][] donnees, Object[] enTete)
	{
		JFrame historiqueClient = new JFrame ("Historique Client");
		historiqueClient.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable table = new JTable(donnees, enTete);
		JButton envoyerPub = new JButton("Pub");
		m_boutons.add(envoyerPub);
		historiqueClient.add(envoyerPub, BorderLayout.SOUTH);
		historiqueClient.add(new JScrollPane(table), BorderLayout.CENTER);
		historiqueClient.setVisible(true);
		historiqueClient.pack();
		return table;
	}
	
	public JTable setTableauReservations(Object[][] donnees, Object[] enTete)
	{
		JFrame historiqueFacture = new JFrame ("Reservations Client");
		JTable table = new JTable(donnees, enTete);
		historiqueFacture.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		historiqueFacture.add(new JScrollPane(table), BorderLayout.CENTER);
		historiqueFacture.setVisible(true);
		historiqueFacture.pack();
		return table;
	}
	
	public JButton setChoixService(List<String> services)
	{
		JFrame choixService = new JFrame ("Choix du service");
		JPanel panel = new JPanel();
		choixService.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	    for (String service : services)
	    {
	    	JCheckBox checkBox = new JCheckBox(service);
	    	checkBox.setSelected(false);
	    	panel.add(checkBox);
	    	m_boxes.add(checkBox);
	    }
	    JButton validerBouton = new JButton("Valider");
	    validerBouton.setPreferredSize(new Dimension(75, 20));
	    m_boutons.add(validerBouton);
	    choixService.add(panel, BorderLayout.CENTER);
	    choixService.add(validerBouton, BorderLayout.SOUTH);
	    
	    choixService.setVisible(true);
	    choixService.pack();
	    return validerBouton;
	}
	
	public ArrayList <JCheckBox> getBoxes()
	{
		return m_boxes;
	}


}
