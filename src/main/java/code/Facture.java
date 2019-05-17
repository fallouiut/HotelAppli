package code;

public class Facture {
    private int num;
    private double prix;
    private Reservation reservation;

    public Facture(int num, double prix, Reservation reservation) {
        this.num = num;
        this.prix = prix;
        this.reservation = reservation;
    }

    public int getNum() {
        return num;
    }

    public double getPrix() {
        return prix;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
