package code;

/**
 * Created by Vincent on 05/05/2019.
 */
public enum Droits {
    RESERVATION("Reservation"),
    FACTURATION("Facturation"),
    CLIENTELE("Clientele"),
    SUPREME("Supreme");

    private String droit;

    Droits(String droit) {
        this.droit = droit;
    }
}
