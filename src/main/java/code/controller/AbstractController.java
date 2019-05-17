package code.controller;
import code.view.Vues.Vue;

public abstract class AbstractController {
	protected static Vue m_vue;
	protected String m_type;
	
	protected final static String CLIENTELE = "Clientele";
	protected final static String RESERVATION = "Reservation";
	protected final static String FACTURATION = "Facturation";
	protected final static String SUPREME = "Supreme";
	
	public AbstractController (Vue vue)
	{
		m_vue = vue;
	}
	public abstract void initController();
	
	public String getType()
	{
		return m_type;
	}
}
