package model;

public class Amministratore extends Utente {
    /*Per ora non viene tradotta l'associazione con la classe VOLO, dato che sarebbe non conveniente e serve il database per gestirla
    dato che l'amministratore ha bisogno della lista di tutti i voli esistenti per poter agire.*/
    public Amministratore(String login, String password) {
        super(login, password);
    }

    //prototipi
    public void inserisci_Volo(){};
    public void aggiorna_Volo(String codice_Volo){}
    public void assegna_Gate(String numero_Gate, String codice_Volo){};
    public void visualizza_Passeggeri(){};
    public void visualizza_BagagliTutti(){};
    public void aggiorna_Bagaglio(String codice_Bagaglio){};
    public void visualizza_Smarrimenti(){};
}
