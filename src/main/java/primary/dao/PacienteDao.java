package primary.dao;

import primary.conexao.ConexaoMySql;
import primary.model.ModelPaciente;
import primary.util.Utilidade;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {

    Utilidade util = new Utilidade();
    ConexaoMySql conex = new ConexaoMySql();

    public void getPaciente(ModelPaciente modelPaciente) {
        conex.getConnection();
        String sql = "INSERT INTO tbl_paciente(nome,idade, sexo,endereco1, endereco2, foto, telefone, celular, tiposangue, cpf, " +
                "datanascimento, recomendacoes, plano) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setString(1, modelPaciente.getNome());
            preparedStatement.setString(2, modelPaciente.getIdade());
            preparedStatement.setString(3, modelPaciente.getSexo());
            preparedStatement.setString(4, modelPaciente.getEndereco1());
            preparedStatement.setString(5, modelPaciente.getEndereco2());
            preparedStatement.setString(6, modelPaciente.getFoto());
            preparedStatement.setString(7, modelPaciente.getTelefone());
            preparedStatement.setString(8, modelPaciente.getCelular());
            preparedStatement.setString(9, modelPaciente.getTiposangue());
            preparedStatement.setString(10, modelPaciente.getCpf());
            preparedStatement.setString(11, modelPaciente.getDatanascimento());
            preparedStatement.setString(12, modelPaciente.getRecomendacoes());
            preparedStatement.setString(13, modelPaciente.getPlano());
            preparedStatement.execute();
            util.showPopUpPaciente(modelPaciente.getNome());
        } catch (SQLException e) {
            util.showPopUpErrorPaciente(modelPaciente.getNome());
            e.printStackTrace();
        }
        conex.closeConnection();
    }

    public ModelPaciente searchPaciente(ModelPaciente modelPaciente) {
        conex.getConnection();
        String sql = "SELECT * FROM tbl_paciente where nome like'%" + modelPaciente.getPesquisa() + "%'";
        try {
            conex.executeSql(sql);
            conex.resultSet.first();
            modelPaciente.setId(conex.resultSet.getInt("pk_id_paciente"));
            modelPaciente.setNome(conex.resultSet.getString("nome"));
            modelPaciente.setIdade(conex.resultSet.getString("idade"));
            modelPaciente.setSexo(conex.resultSet.getString("sexo"));
            modelPaciente.setEndereco1(conex.resultSet.getString("endereco1"));
            modelPaciente.setEndereco2(conex.resultSet.getString("endereco2"));
            modelPaciente.setFoto(conex.resultSet.getString("foto"));
            modelPaciente.setTelefone(conex.resultSet.getString("telefone"));
            modelPaciente.setCelular(conex.resultSet.getString("celular"));
            modelPaciente.setTiposangue(conex.resultSet.getString("tiposangue"));
            modelPaciente.setCpf(conex.resultSet.getString("cpf"));
            modelPaciente.setDatanascimento(conex.resultSet.getString("datanascimento"));
            modelPaciente.setRecomendacoes(conex.resultSet.getString("recomendacoes"));
            modelPaciente.setPlano(conex.resultSet.getString("plano"));
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelPaciente;
    }

    public void deletPaciente(ModelPaciente modelPaciente) {
        conex.getConnection();
        String sql = "DELETE FROM tbl_paciente WHERE pk_id_paciente=?";
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setInt(1, modelPaciente.getId());
            preparedStatement.execute();
            util.showPopUpDelet();
        } catch (SQLException e) {
            util.showPopUpErrorDelet();
            e.printStackTrace();
        }
        conex.closeConnection();
    }

    public void editPaciente(ModelPaciente modelPaciente) {
        conex.getConnection();
        String sql = "UPDATE tbl_paciente SET nome=?, idade=?, sexo=?, endereco1=?, endereco2=?, foto=?, telefone=?, celular=?, tiposangue=?, cpf=?, datanascimento=?, recomendacoes=?, plano=? WHERE pk_id_paciente=?";
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setString(1, modelPaciente.getNome());
            preparedStatement.setString(2, modelPaciente.getIdade());
            preparedStatement.setString(3, modelPaciente.getSexo());
            preparedStatement.setString(4, modelPaciente.getEndereco1());
            preparedStatement.setString(5, modelPaciente.getEndereco2());
            preparedStatement.setString(6, modelPaciente.getFoto());
            preparedStatement.setString(7, modelPaciente.getTelefone());
            preparedStatement.setString(8, modelPaciente.getCelular());
            preparedStatement.setString(9, modelPaciente.getTiposangue());
            preparedStatement.setString(10, modelPaciente.getCpf());
            preparedStatement.setString(11, modelPaciente.getDatanascimento());
            preparedStatement.setString(12, modelPaciente.getRecomendacoes());
            preparedStatement.setString(13, modelPaciente.getPlano());
            preparedStatement.setInt(14, modelPaciente.getId());
            preparedStatement.execute();
            util.showPopUpEdit();
        } catch (SQLException e) {
            util.showPopUpErrorEdit();
            e.printStackTrace();
        }
        conex.closeConnection();
    }

    public List<ModelPaciente> getList() {
        List<ModelPaciente> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_paciente";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ModelPaciente modelPaciente = new ModelPaciente();
                modelPaciente.setId(resultSet.getInt("pk_id_paciente"));
                modelPaciente.setNome(resultSet.getString("nome"));
                modelPaciente.setIdade(resultSet.getString("idade"));
                modelPaciente.setSexo(resultSet.getString("sexo"));
                modelPaciente.setEndereco1(resultSet.getString("endereco1"));
                modelPaciente.setEndereco2(resultSet.getString("endereco2"));
                modelPaciente.setFoto(resultSet.getString("foto"));
                modelPaciente.setTelefone(resultSet.getString("telefone"));
                modelPaciente.setCelular(resultSet.getString("celular"));
                modelPaciente.setTiposangue(resultSet.getString("tiposangue"));
                modelPaciente.setCpf(resultSet.getString("cpf"));
                modelPaciente.setDatanascimento(resultSet.getString("datanascimento"));
                modelPaciente.setRecomendacoes(resultSet.getString("recomendacoes"));
                modelPaciente.setPlano(resultSet.getString("plano"));
                list.add(modelPaciente);
            }
            preparedStatement.close();
            resultSet.close();
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ModelPaciente> getListAgendamento(ModelPaciente modelPaciente) {
        List<ModelPaciente> list = new ArrayList<>();
        String sql = "SELECT pk_id_paciente, nome, endereco1, celular, cpf FROM tbl_paciente WHERE nome LIKE'%"+modelPaciente.getPesquisa()+"%'";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                modelPaciente.setId(resultSet.getInt("pk_id_paciente"));
                modelPaciente.setNome(resultSet.getString("nome"));
                modelPaciente.setEndereco1(resultSet.getString("endereco1"));
                modelPaciente.setCelular(resultSet.getString("celular"));
                modelPaciente.setCpf(resultSet.getString("cpf"));
                list.add(modelPaciente);
            }
            preparedStatement.close();
            resultSet.close();
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ModelPaciente> getListAgendamento2() {
        List<ModelPaciente> list = new ArrayList<>();
        String sql = "SELECT pk_id_paciente, nome, endereco1, celular, cpf FROM tbl_paciente";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ModelPaciente modelPaciente = new ModelPaciente();
                modelPaciente.setId(resultSet.getInt("pk_id_paciente"));
                modelPaciente.setNome(resultSet.getString("nome"));
                modelPaciente.setEndereco1(resultSet.getString("endereco1"));
                modelPaciente.setCelular(resultSet.getString("celular"));
                modelPaciente.setCpf(resultSet.getString("cpf"));
                list.add(modelPaciente);
            }
            preparedStatement.close();
            resultSet.close();
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
