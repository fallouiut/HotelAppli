package code;

public class TypeService {
    private String nom;
    private float prix;

    public TypeService(String nom, float prix) {
        this.nom = nom;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    @Override
    public String toString() {
        return "TypeService{" +
                "nom='" + nom + '\'' +
                ", prix=" + prix +
                '}';
    }
}
