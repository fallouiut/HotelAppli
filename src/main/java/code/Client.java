package code;

public class Client {
    private  int num;
    private  String nom;
    private  String prenom;
    private  String nomEnteprise;
    private  String telephone;
    private  String mail;
    private  String pseudo;
    private  String motDePasse;

    public Client(int num, String nom, String prenom, String nomEnteprise, String telephone, String mail, String pseudo, String motDePasse) {
        this.num = num;
        this.nom = nom;
        this.prenom = prenom;
        this.nomEnteprise = nomEnteprise;
        this.telephone = telephone;
        this.mail = mail;
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
    }

    public Client() {

    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNomEnteprise() {
        return nomEnteprise;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMail() {
        return mail;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNomEnteprise(String nomEnteprise) {
        this.nomEnteprise = nomEnteprise;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    @Override
    public String toString() {
        return "Client{" +
                "num=" + num +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nomEnteprise='" + nomEnteprise + '\'' +
                ", telephone=" + telephone +
                ", mail='" + mail + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                '}';
    }
}
