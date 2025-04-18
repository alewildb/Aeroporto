package model;

public class Volo_Partenza extends Volo{
    Gate gate;
    public Volo_Partenza(String codice_Univoco, String compagnia_Aerea, String aeroporto_Arrivo, int giorno_Partenza, int mese_Partenza, int anno_Partenza, Gate gate) {
       super(codice_Univoco, compagnia_Aerea, "Napoli", aeroporto_Arrivo, giorno_Partenza, mese_Partenza, anno_Partenza);
       this.gate = gate;
    }
    public Gate getGate() {
        return gate;
    }
    public void setGate(Gate gate) {
        this.gate = gate;
    }
}
