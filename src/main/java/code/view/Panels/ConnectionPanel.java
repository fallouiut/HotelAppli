package code.view.Panels;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConnectionPanel extends HotelPanel {
	
	public enum CHAMPS_CONNECTION { NOM_UTILISATEUR, MOT_DE_PASSE } ;

	public ConnectionPanel(String type) {
		super(type);
		setLayout(new FlowLayout(FlowLayout.LEADING, LONGUEUR / 4, 10));
		setPreferredSize(new Dimension(LONGUEUR, LARGEUR));
		
		construireChampsTextes();
		construireConnectionBouton();
	}

	
	public void construireConnectionBouton ()
	{
		JButton validerButton = new JButton("Valider");
		validerButton.setPreferredSize(new Dimension(75, 20));
		m_boutons.add(validerButton);
		add(validerButton);
	}
	
	public void construireChampsTextes()
	{
		JTextField nomUtilisateur = new JTextField("Nom d'utilisateur");
		nomUtilisateur.setColumns(18);
		m_textes.add(nomUtilisateur);
		add(nomUtilisateur);
		
		JPasswordField motDePasse = new JPasswordField("Mot de passe");
		motDePasse.setColumns(18);
		m_textes.add(motDePasse);
		add(motDePasse);
		
		for (JTextField textField : m_textes)
			textField.addMouseListener(new MouseListener() {
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
	}
	
	// INTERARGIT AVEC DATABASE
	public boolean verifierChamps(final String nomUtilisateur, final String motDePasse)
	{
		if (nomUtilisateur == "" || motDePasse == "")
			return false;
		return true;
	}
}
