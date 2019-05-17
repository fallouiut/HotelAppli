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
	public SupremePanel()
	{
		super();	
		m_mainPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 50, 50));
		m_mainPanel.setPreferredSize(new Dimension(LONGUEUR, LARGEUR));
		construireBoutons();
		this.add(m_mainPanel);
	}
	private void construireBoutons() {

		JButton boutonAjouter = new JButton("Ajouter..");
		boutonAjouter.setEnabled(true);
		m_boutons.add(boutonAjouter);
		m_mainPanel.add(boutonAjouter);
		
		JButton boutonEtatHotel = new JButton("Consulter Hotels");
		boutonEtatHotel.setEnabled(true);
		m_boutons.add(boutonEtatHotel);
		m_mainPanel.add(boutonEtatHotel);
		
		JButton boutonCompteRendu = new JButton("Compte Rendu");
		boutonCompteRendu.setEnabled(true);
		m_boutons.add(boutonCompteRendu);
		m_mainPanel.add(boutonCompteRendu);
	}
	
	public void setTableauHotels(Object[][] donnees, Object[] enTete) {
		
		JFrame vueHotels = new JFrame ("Hotels");
		vueHotels.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable tableau = new JTable(donnees, enTete);
		vueHotels.add(new JScrollPane(tableau), BorderLayout.CENTER);
		vueHotels.setVisible(true);
		vueHotels.pack();	
	}
	
	@Override
	public boolean fonctionne() {
		return !m_termine;
	}

}
