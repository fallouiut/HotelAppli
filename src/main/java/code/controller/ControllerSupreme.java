package code.controller;

import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;

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
		etatHotelBouton.addActionListener(e -> fenetreEtat());
		JButton compteRenduBouton = m_panel.getBoutons().get(BOUTONS_SUPREME.COMPTE_RENDU.ordinal());
		compteRenduBouton.addActionListener(e -> fenetreCompteRendu());
		
	}

	// Recuperer la liste des services ici pour construire le formulaire d'ajout d'hotel
	// Recuperer la liste des types de chambre ici pour construire le formulaire d'ajout d'hotel
	private void afficherFormulaireHotel() {
		List<TypeService> services = daoTypeService.findAll();
		List<String> nomServices = new ArrayList<>();
		for (TypeService service : services) {
			nomServices.add(service.getNom());
		}
		m_panel.setFormulaireHotel(nomServices, daoChambre.getTypesChambres());
		Formulaire formulaireHotel = m_panel.getFormulaire();
		while (formulaireHotel == null || !formulaireHotel.m_rempli);
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

		// Une chambre => { Num etage, typeChambre, nombre lits }
		List<Chambre> newChambres = new ArrayList<>();
		for (List<String> chambre : chambres) {
			Integer numEtage = Integer.parseInt(chambre.get(0));
			Integer numNewChambre = daoChambre.getMaxNumChambre(newHotel.getNumHotel(), numEtage) + 1;
			String typeChambre = chambre.get(1);
			Chambre newChambre = new Chambre();
			newChambre.setNumHotel(newHotel.getNumHotel());
			newChambre.setNumChambre(numNewChambre);
			newChambre.setType(typeChambre);
			daoChambre.insert(newChambre);
		}
	}

	// Requete de recuperation des hotels à faire
	// donnees Objet[][]
	// enTete Objet[][]
	private void fenetreEtat() {

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
							;//afficherVueAjouterService(client);
						else if (decision == 1)
							afficherVueSupprimerService(numHotel);
						else if (decision == 2)
							;//afficherVueTravaux();
						else
							;//afficherVueAjoutChambres();
				
			}
			// recuperer les services
			private void afficherVueSupprimerService(Integer numHotel) {
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

				JTable table = m_panel.setTableauServices(donnees, enTete);
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
		});
	}

	private Object fenetreCompteRendu() {
		// TODO Auto-generated method stub
		return null;
	}

}
