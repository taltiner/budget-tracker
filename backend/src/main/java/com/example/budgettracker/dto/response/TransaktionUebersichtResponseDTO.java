package com.example.budgettracker.dto.response;

import java.util.List;

public class TransaktionUebersichtResponseDTO {
    private List<TransaktionEinnahmeResponseDTO> einnahmen;
    private List<TransaktionAusgabeResponseDTO> ausgaben;
    private List<TransaktionNotizResponseDTO> notizen;

    public TransaktionUebersichtResponseDTO() {}

    public List<TransaktionEinnahmeResponseDTO> getEinnahmen() {
        return einnahmen;
    }
    public List<TransaktionAusgabeResponseDTO> getAusgaben() {
        return ausgaben;
    }
    public List<TransaktionNotizResponseDTO> getNotizen() {
        return notizen;
    }

    public void setEinnahmen(List<TransaktionEinnahmeResponseDTO> einnahmen) {
        this.einnahmen = einnahmen;
    }
    public void setAusgaben(List<TransaktionAusgabeResponseDTO> ausgaben) {
        this.ausgaben = ausgaben;
    }
    public void setNotizen(List<TransaktionNotizResponseDTO> notizen) {
        this.notizen = notizen;
    }
}
