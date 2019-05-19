package code.view.Vues;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.JTextField;

public class Formulaire extends JFrame {
	
	public enum CHAMPS_FORMULAIRE { NOM, ADRESSE, COORD } ;
	public enum CHAMPS_CHAMBRE { NUM_ETAGE, NBR_LITS } ;
	public boolean m_rempli = false;
	private ArrayList <String> m_typesChambre;
	private JButton m_validerBouton;
	private JPanel m_panelCentral;
	
	private ArrayList <JTextField> m_fieldsFormulaire = new ArrayList <JTextField> ();
	private ArrayList <JCheckBox> m_checkBoxesFormulaire = new ArrayList <JCheckBox> ();
	private JComboBox <String> m_typeChambreCombo;
	private JComboBox <Integer> m_nombreChambresCombo;
	private ArrayList <JTextField> m_fieldsChambre = new ArrayList <JTextField> ();
	
	private String m_nom;
	private String m_adresse;
	private String m_coord;
	private ArrayList <String> m_servicesChoisis = new ArrayList <String> ();
	private ArrayList <ArrayList <String>> m_chambresAjoutees = new ArrayList <ArrayList <String>> ();
	
	
	public Formulaire(List<String> services, List <String> typesChambre) 
	{
		super();
		m_typesChambre = new ArrayList <String> (typesChambre);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		m_panelCentral = new JPanel(new GridLayout(0, 2));
		m_validerBouton = new JButton("Continuer");
		m_validerBouton.addActionListener(e -> continuerFormulaire());
		setLayout(new BorderLayout());
		add(m_panelCentral, BorderLayout.CENTER);
		add(m_validerBouton, BorderLayout.SOUTH);
		setChampsHotel();
		setServicesHotel(services);
		setVisible(true);
		pack();
	}
	
	private void continuerFormulaire() {
		if (!estRemplis())
			return;
		for (JCheckBox box : m_checkBoxesFormulaire)
		{
			if (box.isSelected())
				m_servicesChoisis.add(box.getText());
		}		
		m_nom = m_fieldsFormulaire.get(CHAMPS_FORMULAIRE.NOM.ordinal()).getText();
		m_adresse = m_fieldsFormulaire.get(CHAMPS_FORMULAIRE.ADRESSE.ordinal()).getText();
		m_coord = m_fieldsFormulaire.get(CHAMPS_FORMULAIRE.COORD.ordinal()).getText();
		
		setVisible(false);
		remove(m_panelCentral);
		m_panelCentral = new JPanel(new GridLayout(0, 2));
		setAjoutChambres(m_typesChambre);
		add(m_panelCentral, BorderLayout.CENTER);
		remove(m_validerBouton);
		m_validerBouton = new JButton("Valider");
		m_validerBouton.addActionListener(e -> validerAjoutChambre());
		remove(m_validerBouton);
		add(m_validerBouton, BorderLayout.SOUTH);
		m_panelCentral.revalidate();
		m_panelCentral.repaint();
		setVisible(true);
		pack();
	}

	private void validerAjoutChambre() 
	{
		ArrayList <String> chambre;
		for (int i = 0; i < m_nombreChambresCombo.getSelectedIndex(); i++)
		{
			chambre = new ArrayList <String> ();
			chambre.add(m_fieldsChambre.get(CHAMPS_CHAMBRE.NUM_ETAGE.ordinal()).getText());
			chambre.add(Integer.toString(m_typeChambreCombo.getSelectedIndex()));
			chambre.add(m_fieldsChambre.get(CHAMPS_CHAMBRE.NBR_LITS.ordinal()).getText());
			m_chambresAjoutees.add(chambre);
			System.out.println(chambre.toString());
		}
		int continuer = JOptionPane.showConfirmDialog(
			    m_panelCentral,
			    "Voulez-vous ajouter d'autre chambres ?",
			    "Ajout Chambres",
			    JOptionPane.YES_NO_OPTION);
		if (continuer != JOptionPane.YES_OPTION)
		{
			m_rempli = true;
			this.dispose();
		}
	}

	private boolean estRemplis() {
		for (JTextField field : m_fieldsFormulaire)
		{
			if (field.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(this, "Veuillez renseigner tous les champs.", "Error", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		
		int i = 0;
		for (JCheckBox box : m_checkBoxesFormulaire)
		{
			if (box.isSelected())
				i++;
		}
		if (i == 0)
		{
			JOptionPane.showMessageDialog(this, "Veuillez choisir au moins un service.", "Error", JOptionPane.WARNING_MESSAGE);
			return false;	
		}
		
		return true;
	}

	private void setChampsHotel()
	{
		JLabel LNom, LAdresse, LCoord;
        LNom = new JLabel("Nom");
        LAdresse = new JLabel("Adresse");
        LCoord = new JLabel("Coordonnées");
        
        JTextField TNom, TAdresse, TCoord;
        TNom = new JTextField();
        TNom.setPreferredSize(new Dimension(200, 15));
        m_fieldsFormulaire.add(TNom);
        TAdresse = new JTextField();
        TAdresse.setPreferredSize(new Dimension(200, 15));
        m_fieldsFormulaire.add(TAdresse);
        TCoord = new JTextField();
        TCoord.setPreferredSize(new Dimension(200, 15));
        m_fieldsFormulaire.add(TCoord);
        
        m_panelCentral.add(LNom);
        m_panelCentral.add(TNom);
        m_panelCentral.add(LAdresse);
        m_panelCentral.add(TAdresse);
        m_panelCentral.add(LCoord);
        m_panelCentral.add(TCoord);
        
	}

	private void setServicesHotel(List<String> services)
	{
		int i = 0;
		JPanel panelCourant = new JPanel();
		for (String service : services)
	    {
	    	JCheckBox checkBox = new JCheckBox(service);
	    	checkBox.setPreferredSize(new Dimension(100, 13));
	    	checkBox.setSelected(false);
	    	panelCourant.add(checkBox);
	    	m_checkBoxesFormulaire.add(checkBox);
	    	i++;
	    	if (i%2 == 0) // 3 check box par panel
	    	{
	    		m_panelCentral.add(panelCourant);
	    		panelCourant = new JPanel();
	    	}
	    }
		if (i%2 == 1) // Si nombre impair
			m_panelCentral.add(new JPanel()); // On equilibre
	}
	
	private void setAjoutChambres(List <String> typesChambre)
	{
		JLabel LNumEtage, LTypeChambre, LNbrLits, LNbrChambres;
		LNumEtage = new JLabel("Numero Etage");
        LTypeChambre = new JLabel("Type de chambre");
        LNbrLits = new JLabel("Nombre de lits");
        LNbrChambres = new JLabel("Nombre de chambres");

        
		JTextField numEtageField = new JTextField();
		numEtageField.setPreferredSize(new Dimension(200, 20));
		m_fieldsChambre.add(numEtageField);
		m_panelCentral.add(LNumEtage);
		m_panelCentral.add(numEtageField);
		
		m_typeChambreCombo = new JComboBox <String> ();
		for (String typeChambre : typesChambre)
			m_typeChambreCombo.addItem(typeChambre);
		m_panelCentral.add(LTypeChambre);
		m_panelCentral.add(m_typeChambreCombo);
		
		JTextField nombreLitField = new JTextField();
		nombreLitField.setPreferredSize(new Dimension(50, 20));
		m_fieldsChambre.add(nombreLitField);
		m_panelCentral.add(LNbrLits);
		m_panelCentral.add(nombreLitField);
		
		m_nombreChambresCombo = new JComboBox <Integer> ();
		for (int i = 1; i < 10; i++)
			m_nombreChambresCombo.addItem(new Integer(i));
		m_panelCentral.add(LNbrChambres);
		m_panelCentral.add(m_nombreChambresCombo);
	}

	public ArrayList<JTextField> getFieldsFormulaire() {
		return m_fieldsFormulaire;
	}
	
	public String getNom() {
		return m_nom;
	}

	public String getAdresse() {
		return m_adresse;
	}

	public String getCoord() {
		return m_coord;
	}

	public ArrayList<String> getServicesChoisis() {
		return m_servicesChoisis;
	}

	public ArrayList<ArrayList<String>> getChambresAjoutees() {
		return m_chambresAjoutees;
	}


	
}
