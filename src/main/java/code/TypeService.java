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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeService service = (TypeService) o;

        if (Float.compare(service.prix, prix) != 0) return false;
        return nom != null ? nom.equals(service.nom) : service.nom == null;
    }

    @Override
    public int hashCode() {
        int result = nom != null ? nom.hashCode() : 0;
        result = 31 * result + (prix != +0.0f ? Float.floatToIntBits(prix) : 0);
        return result;
    }
}
