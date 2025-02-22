package com.example.budgettracker.dto.response;

import com.example.budgettracker.model.TransaktionAusgabe;
import com.example.budgettracker.model.TransaktionEinnahme;
import com.example.budgettracker.model.TransaktionNotiz;

import java.util.List;

public class TransaktionUebersichtResponseDTO {
    private List<TransaktionEinnahmeResponseDTO> einnahmenResponseDTO;
    private List<TransaktionAusgabeResponseDTO> ausgabenResponseDTO;
    private List<TransaktionNotizResponseDTO> notizenResponseDTO;

    public TransaktionUebersichtResponseDTO() {}

    public List<TransaktionEinnahmeResponseDTO> getEinnahmenResponseDTO() {
        return einnahmenResponseDTO;
    }
    public List<TransaktionAusgabeResponseDTO> getAusgabenResponseDTO() {
        return ausgabenResponseDTO;
    }
    public List<TransaktionNotizResponseDTO> getNotizenResponseDTO() {
        return notizenResponseDTO;
    }

    public void setEinnahmenResponseDTO(List<TransaktionEinnahmeResponseDTO> einnahmenResponseDTO) {
        this.einnahmenResponseDTO = einnahmenResponseDTO;
    }
    public void setAusgabenResponseDTO(List<TransaktionAusgabeResponseDTO> ausgabenResponseDTO) {
        this.ausgabenResponseDTO = ausgabenResponseDTO;
    }
    public void setNotizenResponseDTO(List<TransaktionNotizResponseDTO> notizenResponseDTO) {
        this.notizenResponseDTO = notizenResponseDTO;
    }
}
