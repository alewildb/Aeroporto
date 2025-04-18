package model;

public class Volo_Arrivo extends Volo{
    public Volo_Arrivo(String codice_Univoco, String compagnia_Aerea, String aeroporto_Partenza, int giorno_Partenza, int mese_Partenza, int anno_Partenza) {
        super(codice_Univoco, compagnia_Aerea, aeroporto_Partenza, "Napoli", giorno_Partenza, mese_Partenza, anno_Partenza);
    }
}
