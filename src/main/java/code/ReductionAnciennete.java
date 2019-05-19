package code;

import code.model.DAOJDBC.DAOReservationJDBC;

public class ReductionAnciennete implements Reduction {
    @Override
    public void calculer(Reservation reservation) {
        int nbReservation = new DAOReservationJDBC().count(reservation.getClient().getNum());
        if (nbReservation >= 5 && nbReservation < 50) {
            reservation.setReduction((float)(reservation.getPrixTotal() * 0.05));
        }
        if (nbReservation >= 50) {
            reservation.setReduction((float)(reservation.getPrixTotal() * 0.1));
        }
    }
}
