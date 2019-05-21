package code.view.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import code.view.Vues.Formulaire;

public class SupremePanel extends HotelPanel {

	public enum BOUTONS_SUPREME { AJOUTER, ETAT_HOTEL, COMPTE_RENDU, AJOUTER_ADMIN, CONFIRMER_FORMULAIRE };
	private Formulaire m_formulaire = null;
	private boolean m_formulaireFini = false;
	private JComboBox <String> m_typeChambre;
	private JComboBox <Integer> m_nbrChambres;
	private JTextField m_numEtage;
	private JTextField m_nomUtilisateur;
	private JTextField m_numChambre;
	private ArrayList <JTextField> m_datesTravaux = new ArrayList <JTextField> ();
	private JPasswordField m_motDePasse;
	private ArrayList<JRadioButton> m_droitsAdmins;
	
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
		
		JButton boutonAdmins = new JButton("Ajouter Admins");
		boutonAdmins.setEnabled(true);
		m_boutons.add(boutonAdmins);
		add(boutonAdmins);
	}
	
	public JTable setTableauEtat(Object[][] donnees, Object[] enTete) {
		
		JFrame vueHotels = new JFrame ("Compte Rendu Etat");
		vueHotels.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable table = new JTable(donnees, enTete);
		vueHotels.add(new JScrollPane(table), BorderLayout.CENTER);
		vueHotels.setVisible(true);
		vueHotels.pack();	
		return table;
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
	
	public void setTableauEtatTypeChambre(Object[][] donnees, Object[] enTete) {
		JFrame vueHotels = new JFrame ("Repartition chambres disponibles");
		vueHotels.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable table = new JTable(donnees, enTete);
		vueHotels.add(new JScrollPane(table), BorderLayout.CENTER);
		vueHotels.setVisible(true);
		vueHotels.pack();
	}
	
	public JTable setTableauServicesSupprimer(Object[][] donnees, Object[] enTete)
	{
		JFrame tableauService = new JFrame ("Suppresion Services Hotel");
		tableauService.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable table = new JTable(donnees, enTete);
		tableauService.add(new JScrollPane(table), BorderLayout.CENTER);
		tableauService.setVisible(true);
		tableauService.pack();
		return table;
	}
	
	public JTable setTableauServicesAjouter(Object[][] donnees, Object[] enTete)
	{
		JFrame tableauService = new JFrame ("Ajouts Services Hotel");
		tableauService.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable table = new JTable(donnees, enTete);
		tableauService.add(new JScrollPane(table), BorderLayout.CENTER);
		tableauService.setVisible(true);
		tableauService.pack();
		return table;
	}
	
	public void setFormulaireHotel(List<String> services, List <String> typesChambre) {
		m_formulaire = new Formulaire(services, typesChambre);
		m_boutons.add(m_formulaire.getConfirmerBouton());
	}
	
	public JButton setVueTravaux()
	{
		JFrame vueTravaux = new JFrame("Travaux");
		vueTravaux.setLayout(new BorderLayout());
		JPanel panelPrincipal = new JPanel(new GridLayout(0, 2));
		JLabel LDateDebut, LDateFin, LNumChambre;
		LDateDebut = new JLabel("Date debut :");
		LDateFin = new JLabel ("Date fin : ");
		LNumChambre = new JLabel ("Numero Chambre");
		JTextField TDateDebut, TDateFin;
		TDateDebut = new JTextField();
		TDateDebut.setPreferredSize(new Dimension(100, 15));
		m_datesTravaux.add(TDateDebut);
		TDateFin = new JTextField();
		TDateFin.setPreferredSize(new Dimension(100, 15));
		m_datesTravaux.add(TDateFin);
		m_numChambre = new JTextField();
		m_numChambre.setPreferredSize(new Dimension(100, 15));
		
		panelPrincipal.add(LNumChambre);
		panelPrincipal.add(m_numChambre);
		panelPrincipal.add(LDateDebut);
		panelPrincipal.add(TDateDebut);
		panelPrincipal.add(LDateFin);
		panelPrincipal.add(TDateFin);
		vueTravaux.add(panelPrincipal, BorderLayout.CENTER);
		
		JButton boutonConfirmer = new JButton("Valider");
		vueTravaux.add(boutonConfirmer, BorderLayout.SOUTH);
		
		
		vueTravaux.setVisible(true);
		vueTravaux.pack();
		return boutonConfirmer;
		
	}
	
	public JButton setAjoutChambres(List <String> typesChambre)
	{
		JFrame vueAjoutChambres = new JFrame("Ajout chambres");
		vueAjoutChambres.setLayout(new BorderLayout());
		JPanel panelPrincipal = new JPanel(new GridLayout(0, 2));
		
		JLabel LNumEtage, LTypeChambre, LNbrChambres;
		LNumEtage = new JLabel("Numero Etage");
        LTypeChambre = new JLabel("Type de chambre");
        LNbrChambres = new JLabel("Nombre de chambres");

        
		m_numEtage = new JTextField();
		m_numEtage.setPreferredSize(new Dimension(200, 20));
		panelPrincipal.add(LNumEtage);
		panelPrincipal.add(m_numEtage);
		
		m_typeChambre = new JComboBox <String> ();
		for (String typeChambre : typesChambre)
			m_typeChambre.addItem(typeChambre);
		panelPrincipal.add(LTypeChambre);
		panelPrincipal.add(m_typeChambre);
		
		m_nbrChambres = new JComboBox <Integer> ();
		for (int i = 1; i < 10; i++)
			m_nbrChambres.addItem(new Integer(i));
		panelPrincipal.add(LNbrChambres);
		panelPrincipal.add(m_nbrChambres);
		
		vueAjoutChambres.add(panelPrincipal, BorderLayout.CENTER);
		
		JButton boutonConfirmer = new JButton("Valider");
		boutonConfirmer.addActionListener(e -> vueAjoutChambres.dispose());
		vueAjoutChambres.add(boutonConfirmer, BorderLayout.SOUTH);	
		
		vueAjoutChambres.setVisible(true);
		vueAjoutChambres.pack();
		return boutonConfirmer;
	}
	
	
	public JTable setChoixHotelAdmin(Object[][] donnees, Object[] enTete)
	{
		JFrame vueChoixHotel = new JFrame("Choisir Hotel");
		JTable table = new JTable(donnees, enTete);
		vueChoixHotel.add(new JScrollPane(table));
		vueChoixHotel.setVisible(true);
		vueChoixHotel.pack();
		return table;
	}
	
	
	public JButton setVueAdmin(List <String> droitsAdmins)
	{
		JFrame vueAdmin = new JFrame("Creer admin");
		vueAdmin.setLayout(new BorderLayout());
		JPanel panelPrincipal = new JPanel(new GridLayout(0, 2));
		
		JLabel LIdentifiant, LMotDePasse;
		LIdentifiant = new JLabel("Nom d'utilisateur");
		LMotDePasse = new JLabel("Mot de passe");

        m_nomUtilisateur = new JTextField();
        m_nomUtilisateur.setPreferredSize(new Dimension(200, 10));
		m_motDePasse = new JPasswordField();
		m_motDePasse.setPreferredSize(new Dimension(200, 10));
		panelPrincipal.add(LIdentifiant);
		panelPrincipal.add(m_nomUtilisateur);
		panelPrincipal.add(LMotDePasse);
		panelPrincipal.add(m_motDePasse);
		
		m_droitsAdmins = new ArrayList <JRadioButton> ();
		for (String droit : droitsAdmins)
		{
			JRadioButton radioButton = new JRadioButton(droit);
			panelPrincipal.add(radioButton);
			m_droitsAdmins.add(radioButton);
		}
		
		vueAdmin.add(panelPrincipal, BorderLayout.CENTER);
		
		JButton validerBouton = new JButton("Valider");
		validerBouton.addActionListener(e -> vueAdmin.dispose());
		vueAdmin.add(validerBouton, BorderLayout.SOUTH);
		
		vueAdmin.setVisible(true);
		vueAdmin.pack();
		return validerBouton;
	}
	
	
	public boolean finirFormulaire()
	{
		m_formulaire.validerAjoutChambre();
		int continuer = JOptionPane.showConfirmDialog(
			    m_formulaire,
			    "Voulez-vous ajouter d'autre chambres ?",
			    "Ajout Chambres",
			    JOptionPane.YES_NO_OPTION);
		if (continuer != JOptionPane.YES_OPTION)
		{
			m_formulaireFini = true;
			m_formulaire.dispose();
		}
		return m_formulaireFini;
	}
	
	
	public Formulaire getFormulaire()
	{
		return m_formulaire;
	}
	
	public JTextField getNumChambre()
	{
		return m_numChambre;
	}
	
	public JComboBox <String> getTypeChambre()
	{
		return m_typeChambre;
	}
	
	public JComboBox <Integer> getNbrChambres()
	{
		return m_nbrChambres;
	}
	
	public JTextField getNumEtage()
	{
		return m_numEtage;
	}
	
	public ArrayList <JTextField> getDatesTravaux()
	{
		return m_datesTravaux;
	}

	public ArrayList <JRadioButton> getDroitsAdmin()
	{
		return m_droitsAdmins;
	}
	
	public String getNomUtilisateur()
	{
		return m_nomUtilisateur.getText();
	}
	
	public char[] getMotDePasse()
	{
		return m_motDePasse.getPassword();
	}


}
