package code;

import java.util.Map;

public interface FacturationVisitor {
    void visit(Reservation reservation);
    void visit(Chambre chambre);
    void visit(TypeService typeService);
    void visit(Option option);

    Map<String, Float> getDetails();
    float getPrixTotal();
}
