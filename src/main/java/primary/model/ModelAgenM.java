package primary.model;

public class ModelAgenM {
    private int idAgenM;
    private int fkAgenda;
    private String receita;
    private String diagnostico;

    public int getIdAgenM() {
        return idAgenM;
    }

    public void setIdAgenM(int idAgenM) {
        this.idAgenM = idAgenM;
    }

    public int getFkAgenda() {
        return fkAgenda;
    }

    public void setFkAgenda(int fkAgenda) {
        this.fkAgenda = fkAgenda;
    }

    public String getReceita() {
        return receita;
    }

    public void setReceita(String receita) {
        this.receita = receita;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }
}
