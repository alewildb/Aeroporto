package model;

public class Prenotazione {
    private enum Stato_Prenotazione{Confermata, in_Attesa, Cancellata}
    private String nome_Passeggero;
    private String cognome_Passeggero;
    private String numero_Documento_Passeggero;
    private Stato_Prenotazione stato_Prenotazione = Stato_Prenotazione.in_Attesa;
    public Prenotazione(String nome_Passeggero, String cognome_Passeggero, String numero_Documento_Passeggero) {
        this.nome_Passeggero = nome_Passeggero;
        this.cognome_Passeggero = cognome_Passeggero;
        this.numero_Documento_Passeggero = numero_Documento_Passeggero;

    }

    public String getNome_Passeggero() {
        return nome_Passeggero;
    }
    public String getCognome_Passeggero() {
        return cognome_Passeggero;
    }
    public String getNumero_Documento_Passeggero() {
        return numero_Documento_Passeggero;
    }
    public Stato_Prenotazione getStato_Prenotazione() {
        return stato_Prenotazione;
    }

    
}
