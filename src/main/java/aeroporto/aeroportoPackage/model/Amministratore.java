package aeroporto.aeroportoPackage.model;

import aeroporto.aeroportoPackage.controller.AppController;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Amministratore extends Utente {
    public Amministratore(String login, String password) {
        super(login, password);
    }

    public boolean inserisci_Volo(AppController appController, String codiceUnivoco, String compagniaAerea,
                                  String aeroportoPartenza, String aeroportoArrivo,
                                  int giorno, int mese, int anno,
                                  String orarioPrevisto, int minutiRitardo,
                                  Stato_Volo stato, String tipoVolo, Integer numeroGateSePartenza){
        if (appController.internalFindVoloByCodice(codiceUnivoco).isPresent()) {
            System.err.println("Amministratore_Model: Impossibile inserire, codice volo '" + codiceUnivoco + "' già esistente.");
            return false;
        }
        if (orarioPrevisto == null || !orarioPrevisto.matches("\\d{2}:\\d{2}")) {
            System.err.println("Amministratore_Model: Orario previsto non valido. Usare formato HH:MM.");
            return false;
        }

        Volo nuovoVolo;
        if ("Partenza".equalsIgnoreCase(tipoVolo)) {
            if (numeroGateSePartenza == null) {
                System.err.println("Amministratore_Model: Per un Volo di Partenza, il gate è obbligatorio.");
                return false;
            }
            Optional<Gate> gateOpt = appController.internalFindGateByNumero(numeroGateSePartenza);
            if (gateOpt.isEmpty()) {
                System.err.println("Amministratore_Model: Gate " + numeroGateSePartenza + " non trovato.");
                return false;
            }
            nuovoVolo = new Volo_Partenza(codiceUnivoco, compagniaAerea, aeroportoArrivo, giorno, mese, anno, orarioPrevisto, minutiRitardo, gateOpt.get());
        } else if ("Arrivo".equalsIgnoreCase(tipoVolo)) {
            nuovoVolo = new Volo_Arrivo(codiceUnivoco, compagniaAerea, aeroportoPartenza, giorno, mese, anno, orarioPrevisto, minutiRitardo);
        } else {
            System.err.println("Amministratore_Model: Tipo di volo non riconosciuto: " + tipoVolo);
            return false;
        }
        nuovoVolo.setStato_Volo(stato);
        appController.internalAddVolo(nuovoVolo);
        System.out.println("Amministratore_Model: Volo '" + codiceUnivoco + "' inserito.");
        return true;
    }

    public boolean aggiorna_Volo(AppController appController, Volo voloConNuoviDati){
        if (voloConNuoviDati.getOrario_Previsto() == null || !voloConNuoviDati.getOrario_Previsto().matches("\\d{2}:\\d{2}")) {
            System.err.println("Amministratore_Model: Orario previsto non valido per l'aggiornamento. Usare formato HH:MM.");
            return false;
        }
        boolean success = appController.internalUpdateVolo(voloConNuoviDati); 
        if (success) {
            System.out.println("Amministratore_Model: Volo '" + voloConNuoviDati.getCodice_Univoco() + "' aggiornato.");
        } else {
            System.err.println("Amministratore_Model: Impossibile aggiornare, volo '" + voloConNuoviDati.getCodice_Univoco() + "' non trovato.");
        }
        return success;
    }

    public boolean assegna_Gate(AppController appController, String numero_Gate_str, String codice_Volo){
        Optional<Volo> voloOpt = appController.internalFindVoloByCodice(codice_Volo);
        if (voloOpt.isEmpty() || !(voloOpt.get() instanceof Volo_Partenza)) {
            System.err.println("Amministratore_Model: Volo di partenza " + codice_Volo + " non trovato per assegnazione gate.");
            return false;
        }
        Volo_Partenza voloDiPartenza = (Volo_Partenza) voloOpt.get();
        try {
            int numGate = Integer.parseInt(numero_Gate_str);
            Optional<Gate> gateOpt = appController.internalFindGateByNumero(numGate);
            if (gateOpt.isEmpty()) {
                System.err.println("Amministratore_Model: Gate " + numero_Gate_str + " non trovato.");
                return false;
            }
            voloDiPartenza.setGate(gateOpt.get());
            boolean success = appController.internalUpdateVolo(voloDiPartenza); 
            if (success) {
                System.out.println("Amministratore_Model: Gate " + numero_Gate_str + " assegnato al volo " + codice_Volo);
            }
            return success;
        } catch (NumberFormatException e) {
            System.err.println("Amministratore_Model: Numero gate non valido: " + numero_Gate_str);
            return false;
        }
    }

    public List<Prenotazione> visualizza_Passeggeri(AppController appController){
        return appController.internalGetAllPrenotazioni(); 
    }

    public List<Bagaglio> visualizza_BagagliTutti(AppController appController){
        return appController.internalGetAllBagagli(); 
    }

    public boolean aggiorna_Bagaglio(AppController appController, int codice_Bagaglio, Stato_Bagaglio nuovoStato){
        Optional<Bagaglio> bagaglioOpt = appController.internalFindBagaglioByCodice(codice_Bagaglio);
        if (bagaglioOpt.isPresent()) {
            Bagaglio bagaglio = bagaglioOpt.get();
            bagaglio.setStato_Bagaglio(nuovoStato);
            boolean success = appController.internalUpdateBagaglio(bagaglio); 
            if (success) {
                System.out.println("Amministratore_Model: Bagaglio codice '" + codice_Bagaglio + "' aggiornato a " + nuovoStato);
                if (nuovoStato == Stato_Bagaglio.SMARRITO) {
                    appController.internalSegnalaBagaglioSmarrito(bagaglio); 
                } else {
                    appController.internalRisolviBagaglioSmarrito(bagaglio); 
                }
            } else {
                System.err.println("Amministratore_Model: Impossibile aggiornare (AppController.internalUpdateBagaglio), bagaglio '" + codice_Bagaglio + "'.");
            }
            return success;
        }
        System.err.println("Amministratore_Model: Impossibile aggiornare, bagaglio '" + codice_Bagaglio + "' non trovato.");
        return false;
    }

    public boolean elimina_Volo(AppController appController, String codiceVolo) {
        List<Prenotazione> prenotazioniDaInvalidare = appController.internalGetAllPrenotazioni().stream()
                .filter(p -> {
                    String codiceVoloPrenotato = AppController.extractCodiceVoloFromNumeroBiglietto(p.getNumero_Biglietto()); // Static call
                    return codiceVolo != null && codiceVolo.equalsIgnoreCase(codiceVoloPrenotato);
                })
                .collect(Collectors.toList());

        for (Prenotazione p : prenotazioniDaInvalidare) {
            p.setStato_Prenotazione(Stato_Prenotazione.CANCELLATA);
            appController.internalUpdatePrenotazione(p);
            System.out.println("Amministratore_Model: Prenotazione " + p.getNumero_Biglietto() + " cancellata causa eliminazione volo " + codiceVolo);
        }

        boolean rimosso = appController.internalRemoveVolo(codiceVolo);
        if (rimosso) {
            System.out.println("Amministratore_Model: Volo '" + codiceVolo + "' eliminato.");
        } else {
            System.err.println("Amministratore_Model: Impossibile eliminare, volo '" + codiceVolo + "' non trovato.");
        }
        return rimosso;
    }

    public List<Bagaglio> visualizza_Smarrimenti(AppController appController){
        return appController.internalGetBagagliSmarriti();
    }
}