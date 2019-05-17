package code.view.Panels;

import java.awt.BorderLayout;
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
	
	
	public HotelPanel (String type)
	{
		setName(type);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
	
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
	
	public boolean fonctionne()
	{
		return !m_termine;
	}
}
