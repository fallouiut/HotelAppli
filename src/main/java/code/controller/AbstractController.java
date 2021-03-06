package code.controller;

public abstract class AbstractController {
	
	public enum PANEL { CONNECTION, ACCUEIL, CLIENTELE, FACTURATION, RESERVATION, SUPREME, RETOUR };
	public enum ETAT_CONNECTION { UNDEFINED, CONNECTED, DISCONNECTED };
	
	protected final static String CONNECTION = "Connection";
	protected final static String CLIENTELE = "Clientele";
	protected final static String RESERVATION = "Reservation";
	protected final static String FACTURATION = "Facturation";
	protected final static String SUPREME = "Supreme";
	protected final static String ACCUEIL = "Accueil";
	
	public abstract void initController();
}
