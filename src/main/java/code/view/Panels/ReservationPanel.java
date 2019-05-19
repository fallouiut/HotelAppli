package code.view.Panels;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ReservationPanel extends HotelPanel {
	
	private JScrollPane m_mainPanel;
	private JTable m_tableReservation;
	private boolean m_tableSet = false;
	public ReservationPanel(String type) {
		super(type);
		setLayout(new BorderLayout());
	}
	

	public JTable setTableauReservation(Object[][] donnees, Object[] enTete) {
		if (!m_tableSet)
		{
			m_tableReservation = new JTable (donnees, enTete);
			m_mainPanel = new JScrollPane(m_tableReservation);
			add(m_mainPanel, BorderLayout.CENTER);
			m_tableSet = true;
			return m_tableReservation;
		}
		else
		{
			m_tableReservation = new JTable (donnees, enTete);
			remove(m_mainPanel);
			m_mainPanel = new JScrollPane(m_tableReservation);
			add(m_mainPanel, BorderLayout.CENTER);
			revalidate();
			repaint();
			return m_tableReservation;
		}
	}
	
}
