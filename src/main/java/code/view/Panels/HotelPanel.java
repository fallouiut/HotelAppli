package code.view.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public abstract class HotelPanel extends JPanel {
	
	protected ArrayList <JButton> m_boutons = new ArrayList <JButton> ();
	protected ArrayList <JTextField> m_textes = new ArrayList <JTextField> ();
	protected JPanel m_mainPanel;
	protected boolean m_termine;
	protected final int LONGUEUR = 400;
	protected final int LARGEUR = 300;
	
	
	public HotelPanel ()
	{
		setPreferredSize(new Dimension(LONGUEUR, LARGEUR));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		setLayout(new BorderLayout());
		m_mainPanel = new JPanel();
		add(m_mainPanel, BorderLayout.CENTER);
	}
	
	public abstract boolean fonctionne();
	
	public void setTermine(final boolean termine)
	{
		this.m_termine = termine;
	}
	
	public ArrayList <JButton> getBoutons()
	{
		return m_boutons;
	}
	
	public ArrayList <JTextField> getTextes()
	{
		return m_textes;
	}
}
