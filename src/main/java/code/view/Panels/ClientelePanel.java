package code.view.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ClientelePanel extends HotelPanel{
	public enum CHAMPS_CLIENTELE { RECHERCHE, SERVICE_CLIENT, HOTEL};
	
	private ArrayList <JCheckBox> m_boxes = new ArrayList <JCheckBox> ();
	@Override
	public boolean fonctionne() {
		return !m_termine;
	}
	
	public ClientelePanel() 
	{
		super();
		m_mainPanel.setLayout(new FlowLayout(FlowLayout.LEADING, LONGUEUR / 8, 20));
		m_mainPanel.setPreferredSize(new Dimension(LONGUEUR, LARGEUR));
		
		construireChampsTextes();
		construireReservationsBouton();
		
		m_mainPanel.add(m_textes.get(CHAMPS_CLIENTELE.RECHERCHE.ordinal()));
		m_mainPanel.add(m_boutons.get(CHAMPS_CLIENTELE.RECHERCHE.ordinal()));
		
		m_mainPanel.add(m_textes.get(CHAMPS_CLIENTELE.SERVICE_CLIENT.ordinal()));
		m_mainPanel.add(m_boutons.get(CHAMPS_CLIENTELE.SERVICE_CLIENT.ordinal()));
		
		m_mainPanel.add(m_textes.get(CHAMPS_CLIENTELE.HOTEL.ordinal()));
		m_mainPanel.add(m_boutons.get(CHAMPS_CLIENTELE.HOTEL.ordinal()));
		
	}

	private void construireReservationsBouton() {
		JButton boutonRecherche = new JButton("Afficher");
		boutonRecherche.setPreferredSize(new Dimension(85, 20));
		m_boutons.add(boutonRecherche);
		
		JButton boutonService = new JButton("Ajouter");
		boutonService.setPreferredSize(new Dimension(75, 20));
		m_boutons.add(boutonService);	
		
		JButton boutonPresents = new JButton("Afficher");
		boutonPresents.setPreferredSize(new Dimension(85, 20));
		m_boutons.add(boutonPresents);
	}

	private void construireChampsTextes() {
		m_textes.add(new JTextField("ID Recherche"));
		m_textes.add(new JTextField("ID Service"));
		m_textes.add(new JTextField("ID Hotel"));
		for (JTextField texte : m_textes)
		{
			texte.addMouseListener(new MouseListener() {
				private boolean cliqued = false;
				@Override
				public void mousePressed(MouseEvent arg0) {
					if (!cliqued)
					{
						((JTextField) arg0.getSource()).setText("");
						cliqued = true;
					}
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (!cliqued)
					{
						((JTextField) e.getSource()).setText("");
					}
				}
			} );
			texte.setColumns(18);
		}
	}
	
	public void setTableauHistorique(Object[][] donnees, Object[] enTete)
	{
		JFrame historiqueFacture = new JFrame ("Historique Client");
		historiqueFacture.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		historiqueFacture.add(new JScrollPane(new JTable(donnees, enTete)), BorderLayout.CENTER);
		historiqueFacture.setVisible(true);
		historiqueFacture.pack();	
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
	
	public void setTableauClients(Object[][] donnees, Object[] enTete)
	{
		JFrame clientsPresents = new JFrame ("Clients Presents");
		clientsPresents.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		clientsPresents.add(new JScrollPane(new JTable(donnees, enTete)), BorderLayout.CENTER);
		clientsPresents.setVisible(true);
		clientsPresents.pack();	
	}
	public JButton setChoixService(ArrayList <String> services)
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
