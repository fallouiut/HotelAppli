package code;

/**
 * Created by Vincent on 03/05/2019.
 */
public abstract class Option {

    private String nom;
    private float prix;

    public Option(String nom, float prix) {
        this.nom = nom;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public float getPrix() {
        return prix;
    }

}
