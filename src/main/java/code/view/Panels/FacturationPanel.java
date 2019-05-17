package code.view.Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class FacturationPanel extends HotelPanel {

	public FacturationPanel() 
	{
		super();
		m_mainPanel.setLayout(new FlowLayout(FlowLayout.LEADING, LONGUEUR / 4, 10));
		m_mainPanel.setPreferredSize(new Dimension(LONGUEUR, LARGEUR));
		
		construireChampsTextes();
		construireFacturationBouton();
	}

	public void setTableauFacture(Object[][] donnees, Object[] enTete) {
		
		JFrame vueFacture = new JFrame ("Facture Client");
		vueFacture.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		JTable tableau = new JTable(donnees, enTete);
		vueFacture.add(new JScrollPane(tableau), BorderLayout.CENTER);
		vueFacture.setVisible(true);
		vueFacture.pack();
			
	}

	private void construireChampsTextes() {
		JTextField IDClient = new JTextField("ID Client");
		IDClient.setColumns(18);
		IDClient.addMouseListener(new MouseListener() {
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
					cliqued = true;
				}
			}
		} );
		
		m_textes.add(IDClient);
		m_mainPanel.add(IDClient);
	}

	private void construireFacturationBouton() {
		JButton validerButton = new JButton("Valider");
		validerButton.setPreferredSize(new Dimension(75, 20));
		m_boutons.add(validerButton);
		m_mainPanel.add(validerButton);
		
	}
	@Override
	public boolean fonctionne() {
		return !m_termine;
	}
	
}
