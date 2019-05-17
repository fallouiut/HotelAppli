package code;

import java.time.LocalDate;

/**
 * Created by Vincent on 07/05/2019.
 */
public class EtatChambre {

    private String nomEtat;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    public EtatChambre() {}

    public EtatChambre(String nomEtat, LocalDate dateDebut, LocalDate dateFin) {
        this.nomEtat = nomEtat;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getNomEtat() {
        return nomEtat;
    }

    public void setNomEtat(String nomEtat) {
        this.nomEtat = nomEtat;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "EtatChambre{" +
                "nomEtat='" + nomEtat + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
