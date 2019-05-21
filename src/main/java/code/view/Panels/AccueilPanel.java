package code.view.Panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;

public class AccueilPanel extends HotelPanel {
	
	public AccueilPanel(String type) {
		super(type);	
		setLayout(new GridLayout(0, 2));
		setPreferredSize(new Dimension(LONGUEUR, LARGEUR));
	}
	
	public void construireBoutons(ArrayList<String> nomBoutons)
	{
		for (String nomBouton : nomBoutons)
		{
			JButton bouton = new JButton(nomBouton);
			bouton.setEnabled(false);
			m_boutons.add(bouton);
			add(bouton);
		}
	}
	
	public JButton getBouton(String nom)
	{
		for (JButton bouton : m_boutons)
		{
			if (bouton.getText().equals(nom))
				return bouton;
		}
		return null;
	}

	@Override
	public boolean fonctionne() {
		return !m_termine;
	}
}
