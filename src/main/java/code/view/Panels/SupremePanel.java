package code.view.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SupremePanel extends HotelPanel {

	public enum BOUTONS_SUPREME { AJOUTER, ETAT_HOTEL, COMPTE_RENDU };
	public SupremePanel(String type)
	{
		super(type);	
		setLayout(new FlowLayout(FlowLayout.LEADING, 50, 50));
		setPreferredSize(new Dimension(LONGUEUR, LARGEUR));
		construireBoutons();
	}
	private void construireBoutons() {

		JButton boutonAjouter = new JButton("Ajouter..");
		boutonAjouter.setEnabled(true);
		m_boutons.add(boutonAjouter);
		add(boutonAjouter);
		
		JButton boutonEtatHotel = new JButton("Consulter Hotels");
		boutonEtatHotel.setEnabled(true);
		m_boutons.add(boutonEtatHotel);
		add(boutonEtatHotel);
		
		JButton boutonCompteRendu = new JButton("Compte Rendu");
		boutonCompteRendu.setEnabled(true);
		m_boutons.add(boutonCompteRendu);
		add(boutonCompteRendu);
	}
	
	public JTable setTableauHotels(Object[][] donnees, Object[] enTete) {
		
		JFrame vueHotels = new JFrame ("Hotels");
		vueHotels.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable table = new JTable(donnees, enTete);
		vueHotels.add(new JScrollPane(table), BorderLayout.CENTER);
		vueHotels.setVisible(true);
		vueHotels.pack();	
		return table;
	}
	
	public JTable setTableauServices(Object[][] donnees, Object[] enTete)
	{
		JFrame historiqueClient = new JFrame ("Services Client");
		historiqueClient.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable table = new JTable(donnees, enTete);
		historiqueClient.add(new JScrollPane(table), BorderLayout.CENTER);
		historiqueClient.setVisible(true);
		historiqueClient.pack();
		return table;
	}


}
