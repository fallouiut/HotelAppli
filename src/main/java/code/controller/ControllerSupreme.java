package code.controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import code.*;
import code.model.DAOInterfaces.DAOChambre;
import code.model.DAOInterfaces.DAOHotel;
import code.model.DAOInterfaces.DAOTypeService;
import code.model.DAOJDBC.DAOChambreJDBC;
import code.model.DAOJDBC.DAOHotelJDBC;
import code.model.DAOJDBC.DAOTypeServiceJDBC;
import code.view.Panels.SupremePanel;
import code.view.Panels.SupremePanel.BOUTONS_SUPREME;
import code.view.Vues.Formulaire;

public class ControllerSupreme extends AbstractController {

	private DAOHotel daoHotel = new DAOHotelJDBC();
	private DAOTypeService daoTypeService = new DAOTypeServiceJDBC();
	private DAOChambre daoChambre = new DAOChambreJDBC();
	SupremePanel m_panel;
	public ControllerSupreme(SupremePanel panel) {
		super();
		m_panel = panel;
		initController();
	}

	@Override
	public void initController() {
		JButton ajouterBouton = m_panel.getBoutons().get(BOUTONS_SUPREME.AJOUTER.ordinal());
		ajouterBouton.addActionListener(e -> afficherFormulaireHotel());
		JButton etatHotelBouton = m_panel.getBoutons().get(BOUTONS_SUPREME.ETAT_HOTEL.ordinal());
		etatHotelBouton.addActionListener(e -> construirefenetreEtat());
		JButton compteRenduBouton = m_panel.getBoutons().get(BOUTONS_SUPREME.COMPTE_RENDU.ordinal());
		compteRenduBouton.addActionListener(e -> fenetreCompteRendu());	
	}

	private Object fenetreCompteRendu() {
		// TODO Auto-generated method stub
		return null;
	}

	private void enregistrerFormulaire() 
	{
		if (!m_panel.finirFormulaire())
			return;
		Formulaire formulaireHotel = m_panel.getFormulaire();
		// Utiliser les getters sur formulaireHotel pour récuperer les infos.
		String nom = formulaireHotel.getNom();
		String adresse = formulaireHotel.getAdresse();
		String coordLongi = formulaireHotel.getLatitude();
		String coordLati = formulaireHotel.getLongitude();
		String ville = formulaireHotel.getVille();

		List <String> servicesChoisis = formulaireHotel.getServicesChoisis();
		List <List <String>> chambres = formulaireHotel.getChambresAjoutees();
		// Ici ajouter hotel et chambres
		Hotel newHotel = new Hotel();
		newHotel.setNom(nom);
		newHotel.setAdresse(adresse);
		newHotel.setLongitude(Float.parseFloat(coordLongi));
		newHotel.setLatitude(Float.parseFloat(coordLati));
		newHotel.setVille(ville);
		newHotel = daoHotel.insert(newHotel);

		List<TypeService> typesServices = new ArrayList<>();
		for (String service : servicesChoisis) {
			typesServices.add(daoTypeService.getById(service));
		}

		daoHotel.insertServices(newHotel.getNumHotel(), typesServices);

		System.out.println(chambres);
		// Une chambre => { Num etage, typeChambre, nombre lits }
		for (List<String> chambre : chambres) {
			Integer numEtage = Integer.parseInt(chambre.get(0));
			System.out.println(newHotel);
			System.out.println(numEtage);
			Integer numNewChambre = daoChambre.getMaxNumChambre(newHotel.getNumHotel(), numEtage) + 1;
			String typeChambre = chambre.get(1);
			Chambre newChambre = new Chambre();
			newChambre.setNumHotel(newHotel.getNumHotel());
			newChambre.setNumChambre(numNewChambre);
			newChambre.setType(typeChambre);
			if (daoChambre.insert(newChambre) == null) {
				//traitement erreur (pop-up ?)
				System.err.println("bug à la chambre " + chambre);
			}
		}
	}

	// Recuperer la liste des services ici pour construire le formulaire d'ajout d'hotel
	// Recuperer la liste des types de chambre ici pour construire le formulaire d'ajout d'hotel
	private void afficherFormulaireHotel() 
	{
		List<TypeService> services = daoTypeService.findAll();
		List<String> nomServices = new ArrayList<>();
		for (TypeService service : services) {
			nomServices.add(service.getNom());
		}
        m_panel.setFormulaireHotel(nomServices, daoChambre.getTypesChambres());
		JButton validerFormulaireBouton = m_panel.getBoutons().get(BOUTONS_SUPREME.CONFIRMER_FORMULAIRE.ordinal());
		validerFormulaireBouton.addActionListener(e -> enregistrerFormulaire());		
	}

	// Requete de recuperation des hotels à faire
	// donnees Objet[][]
	// enTete Objet[][]
	private void construirefenetreEtat() {

		List<Hotel> hotels = daoHotel.findAllLight();
		Object[][] donnees = new Object[hotels.size()][16];
		int i = 0;
		for (Hotel hotel : hotels) {
			donnees[i][0] = hotel.getNumHotel();
			donnees[i][1] = hotel.getNom();
			donnees[i][2] = hotel.getVille();
			donnees[i][3] = hotel.getAdresse();
			++i;
		}
		String [] enTete = {"Num hotel", "Nom", "Ville", "Adresse"};
		JTable table = m_panel.setTableauHotels(donnees, enTete); 
		table.addMouseListener(new MouseAdapter() {
            
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) 
                {
                	JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
					Integer numHotel = (Integer)target.getModel().getValueAt(row, 0);
                    afficherPopUpDecision(numHotel);
                    // Ici recuperer les infos sur l'hotel selectionne
					Hotel hotel = daoHotel.getById(numHotel);
                }
                return;
            }
			
			private void afficherPopUpDecision(Integer numHotel) {
				Object[] options = {"Ajouter Service",
	                    "Supprimer Service",
	                    "Ajouter travaux", "Ajouter Chambres"};
				
				int decision = JOptionPane.showOptionDialog(m_panel,
					    "Que désirez-vous faire ?", "Action sur hôtel",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[2]);
						if (decision == 0)
							afficherVueAjouterService(numHotel);
						else if (decision == 1)
							afficherVueSupprimerService(numHotel);
						else if (decision == 2)
							afficherVueTravaux(numHotel);
						else
							afficherVueAjoutChambres(numHotel);
				
			}
		});
	}
	
	// A adapter par rapport à la fonction afficherVueAjouterService
	private void afficherVueAjouterService(Integer numHotel) {
		List<TypeService> services = daoTypeService.findAll(); // Changer cette requete par requete pour recuperer tous les services existants
		String[] enTete = {"Service", "Prix"};
		Object[][] donnees = new Object[services.size()][16];
		int i = 0;
		for (TypeService service : services) {
			donnees[i][0] = service.getNom();
			donnees[i][1] = service.getPrix();
			++i;
		}
		Hotel hotelUpdated = new Hotel();
		hotelUpdated.setNumHotel(numHotel);
		hotelUpdated.setServices(daoHotel.getServicesById(numHotel));

		JTable table = m_panel.setTableauServicesAjouter(donnees, enTete);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) 
                {
                	JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    String serviceAAjouter = (String)target.getModel().getValueAt(row, 0);
                    TypeService serviceToAdd = daoTypeService.getById(serviceAAjouter);
                    if (!hotelUpdated.getServices().contains(serviceToAdd)) {
                    	hotelUpdated.getServices().add(serviceToAdd);
					}

                    // Ici recuperer le service à ajouter
                    afficherPopUpConfirmationAjout(hotelUpdated);
                }
                return;	
		}
			
			private void afficherPopUpConfirmationAjout(Hotel hotelUpdated) {
				System.out.println(hotelUpdated);
				Object[] options = {"Oui", "Non"};
				
				int decision = JOptionPane.showOptionDialog(m_panel,
					    "Etes-vous sûr(e) de vouloir ajouter ce service ?", "Ajout service",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[1]);
						if (decision == 0)
							daoHotel.updateServices(hotelUpdated);
			}
		});
	}
		
	// recuperer les services

	private void afficherVueSupprimerService(Integer numHotel) 
	{
		List<TypeService> servicesHotel = daoHotel.getServicesById(numHotel);
		System.out.println(servicesHotel);
		String[] enTete = {"Service", "Prix"};
		Object[][] donnees = new Object[servicesHotel.size()][16];

		int i = 0;
		for (TypeService service : servicesHotel) {
			donnees[i][0] = service.getNom();
			donnees[i][1] = service.getPrix();
			++i;
		}

		Hotel hotelUpdated = new Hotel();
		hotelUpdated.setNumHotel(numHotel);
		hotelUpdated.setServices(servicesHotel);

		JTable table = m_panel.setTableauServicesSupprimer(donnees, enTete);
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) 
                {
                	JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    String serviceASuppr = (String)target.getModel().getValueAt(row, 0);
                    TypeService serviceToRemove = null;
                    for (TypeService service : hotelUpdated.getServices()) {
                    	if (service.getNom().equals(serviceASuppr)) {
							serviceToRemove = service;
							break;
						}
					}
					if (serviceToRemove != null) {
						hotelUpdated.getServices().remove(serviceToRemove);
					}

                    // Ici recuperer le service à supprimer
                    afficherPopUpConfirmationSupression(hotelUpdated);
                }
                return;
            }

			private void afficherPopUpConfirmationSupression(Hotel hotelUpdated) {
				Object[] options = {"Oui", "Non"};
				
				int decision = JOptionPane.showOptionDialog(m_panel,
					    "Etes-vous sûr(e) de vouloir supprimer ce service ?", "Suppresion service",
					    JOptionPane.YES_NO_CANCEL_OPTION,
					    JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[1]);
						if (decision == 0)
							daoHotel.updateServices(hotelUpdated);
			}
		
		});
	}

	private void afficherVueTravaux(Integer numHotel) {
		JButton boutonValider = m_panel.setVueTravaux();
		boutonValider.addActionListener(e -> enregistrerTravaux(numHotel));
	}

	// Enregistrer ici les dates de travaux
	private void enregistrerTravaux(Integer numHotel) {
		String dateDebut = m_panel.getDatesTravaux().get(0).getText();
		String dateFin = m_panel.getDatesTravaux().get(1).getText();
		//toutes les chambres de l'hotel (liste déroulante ?)
		Set<Chambre> allChambres = daoHotel.getChambresById(numHotel);
		//la chambre sur laquelle il a cliqué
		int numChambreChoisie = 101;
		if (verifierDate(numHotel, numChambreChoisie, dateDebut, dateFin))
		{
			Chambre chambre = new Chambre();
			chambre.setNumHotel(numHotel);
			chambre.setNumChambre(numChambreChoisie);
			chambre.setEtat(new EtatChambre("TRAVAUX", LocalDate.parse(dateDebut), LocalDate.parse(dateFin)));
			daoChambre.insertEntreeHistorique(chambre);
			return;
		}
	}

	private boolean verifierDate(Integer numHotel, Integer numChambre, String dateDebut, String dateFin) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
		    Date dateDebutParsed = sdf.parse(dateDebut);
		    Date dateFinParsed = sdf.parse(dateFin);
		    if (!dateDebutParsed.before(dateFinParsed))
		    {
		    	JOptionPane.showMessageDialog(m_panel, "La date de fin doit être postérieure à la date de début.", "Error", JOptionPane.WARNING_MESSAGE);
		    	return false;
		    }
			if (!daoChambre.verifDatesTravaux(numHotel, numChambre, dateDebut, dateFin)) {
				JOptionPane.showMessageDialog(m_panel, "Cette chambre est réservée ou en travaux dans cet intervalle de temps.", "Error", JOptionPane.WARNING_MESSAGE);
				return false;
			}

		} catch (ParseException pe) {
			JOptionPane.showMessageDialog(m_panel, "Veuillez insérer des dates dans le format yyyy-MM-dd", "Error", JOptionPane.WARNING_MESSAGE);
		   return false;
		}
		return true;
	}

	private void afficherVueAjoutChambres(Integer numHotel) {
		JButton boutonValider = m_panel.setAjoutChambres(daoChambre.getTypesChambres());
		boutonValider.addActionListener(e -> ajouterChambre(numHotel));
	}

	// Ajouter ici une chambre
	private void ajouterChambre(Integer numHotel) {
		String typeChambre = (String) m_panel.getTypeChambre().getSelectedItem();
		Integer numEtage = Integer.parseInt(m_panel.getNumEtage().getText());
		Integer nbrChambres = (Integer) m_panel.getNbrChambres().getSelectedItem();
		if (typeChambre == null || typeChambre.isEmpty())
			return;
		if (numEtage == null || nbrChambres == null)
			return;

		for (int i = 0 ; i < nbrChambres ; ++i) {
			Chambre newChambre = new Chambre();
			newChambre.setNumChambre(daoChambre.getMaxNumChambre(numHotel, numEtage) + 1);
			newChambre.setNumHotel(numHotel);
			newChambre.setType(typeChambre);

			if (daoChambre.insert(newChambre) == null) {
				//traitement erreur (pop-up ?)
				System.err.println("bug à la chambre " + newChambre);
			}
			else
				System.out.println("Chambre ajoutée");
		}

		return;
	}
}
