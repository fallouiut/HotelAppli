package code.controller;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import code.Admin;
import code.SessionUnique;
import code.model.DAOInterfaces.DAOAdmin;
import code.model.DAOJDBC.DAOAdminJDBC;
import code.view.Panels.ConnectionPanel;
import code.view.Panels.ConnectionPanel.CHAMPS_CONNECTION;

public class ControllerConnection extends AbstractController {

	private ConnectionPanel m_panel; 	
	private String m_nomUtilisateur;
	private String m_motDePasse;
	private DAOAdmin daoAdmin;
	private Admin m_admin;
	
	public ControllerConnection(ConnectionPanel panel) {
		m_panel = panel;
		daoAdmin = new DAOAdminJDBC();
		initController();
	}

	@Override
	public void initController() {
		JButton validerBouton = m_panel.getBoutons().get(0);
		validerBouton.addActionListener(e -> verifierIdentifiants());
	}
	
	private void verifierIdentifiants()
	{
		m_nomUtilisateur = m_panel.getTextes().get(CHAMPS_CONNECTION.NOM_UTILISATEUR.ordinal()).getText();
		m_motDePasse = m_panel.getTextes().get(CHAMPS_CONNECTION.MOT_DE_PASSE.ordinal()).getText();
		// Enlever
		m_nomUtilisateur = "AdminTest";
		m_motDePasse = "administrator";
		/*m_admin = daoAdmin.findByUsernameAndPassword(m_nomUtilisateur, m_motDePasse);*/
		SessionUnique.username = m_nomUtilisateur;
		SessionUnique.password = m_motDePasse;
		m_admin = null;
		if (!m_nomUtilisateur.equals("Nom d'utilisateur") && !m_motDePasse.equals("Mot de passe") && !m_nomUtilisateur.isEmpty() && !m_motDePasse.isEmpty()) {
			m_admin = SessionUnique.getInstance().getSession();
		}
		if (m_admin != null) // si les identifiants sont corrects
		{
			System.out.println(m_admin);
			m_panel.setTermine(true);
		}
		else
		{
			JOptionPane.showMessageDialog(m_panel, "Nom d'utilisateur ou mot de passe incorrect.", "Error", JOptionPane.WARNING_MESSAGE);
			m_nomUtilisateur = "";
			m_motDePasse = "";
		}

	}
	
	public Admin getAdmin()
	{
		return m_admin;
	}
}
