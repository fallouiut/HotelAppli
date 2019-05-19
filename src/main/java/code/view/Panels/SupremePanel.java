package code.view.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import code.view.Vues.Formulaire;

public class SupremePanel extends HotelPanel {

	public enum BOUTONS_SUPREME { AJOUTER, ETAT_HOTEL, COMPTE_RENDU };
	private Formulaire m_formulaire = null;
	
	public SupremePanel(String type)
	{
		super(type);	
		setLayout(new FlowLayout(FlowLayout.LEADING, 50, 50));
		setPreferredSize(new Dimension(LONGUEUR, LARGEUR));
		construireBoutons();
	}
	
	private void construireBoutons() {

		JButton boutonAjouter = new JButton("Ajouter");
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
	
	public void setFormulaireHotel(List<String> services, List <String> typesChambre) {
		m_formulaire = new Formulaire(services, typesChambre);
	}
	
	public Formulaire getFormulaire()
	{
		return m_formulaire;
	}
	
}
