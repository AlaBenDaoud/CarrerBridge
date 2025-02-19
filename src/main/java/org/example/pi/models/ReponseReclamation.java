package org.example.pi.models;

import java.time.LocalDateTime;

public class ReponseReclamation {
    private int id;
    private int idRec;
    private int idUser;
    private int idReceiver;
    private String reponse;
    private String pdfPath;
    private LocalDateTime date;
    private String statueOfReponseReclamation;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdRec() { return idRec; }
    public void setIdRec(int idRec) { this.idRec = idRec; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public int getIdReceiver() { return idReceiver; }
    public void setIdReceiver(int idReceiver) { this.idReceiver = idReceiver; }

    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getStatueOfReponseReclamation() { return statueOfReponseReclamation; }
    public void setStatueOfReponseReclamation(String statueOfReponseReclamation) { this.statueOfReponseReclamation = statueOfReponseReclamation; }
}