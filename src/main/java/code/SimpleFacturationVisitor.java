package code;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleFacturationVisitor implements FacturationVisitor {

    private float prixTotal = 0;
    private Map<String, Float> details = new LinkedHashMap<>();


    public Map<String, Float> getDetails() {
        return this.details;
    }

    public float getPrixTotal() {
        this.details.put("Prix total", this.prixTotal);
        return this.prixTotal;
    }

    @Override
    public void visit(Reservation reservation) {
        this.prixTotal -= reservation.getReduction();
        this.details.put("Reduction", reservation.getReduction());
    }

    @Override
    public void visit(Chambre chambre) {
        this.prixTotal += chambre.getPrix();
        this.details.put("Chambre "+ chambre.getNumChambre(), chambre.getPrix());
    }

    @Override
    public void visit(TypeService typeService) {
        this.prixTotal += typeService.getPrix();
        this.details.put("Service: " + typeService.getNom(), (float)typeService.getPrix());
    }

    @Override
    public void visit(Option option) {
        this.prixTotal += option.getPrix();
        this.details.put("- option chambre: " +option.getNom(), option.getPrix());
    }
}
