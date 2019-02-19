package primary.model;

public class ModelEspecialidade {
    private String nomeEspecialidade;
    private int idEspecialidade;


    public String getNomeEspecialidade() {
        return nomeEspecialidade;
    }

    public void setNomeEspecialidade(String nomeEspecialidade) {
        this.nomeEspecialidade = nomeEspecialidade;
    }

    public int getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(int idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    @Override
    public String toString() {
        return nomeEspecialidade;
    }
}
