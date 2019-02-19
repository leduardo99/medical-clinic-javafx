package primary.dao;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import primary.conexao.ConexaoMySql;
import primary.model.ModelMedicos;
import primary.model.ModelPaciente;
import primary.util.Utilidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDao {

    Utilidade util = new Utilidade();
    ConexaoMySql conex = new ConexaoMySql();


    public void getMedicos(ModelMedicos modelMedicos) {
        String sql = "INSERT INTO tbl_medicos(nome_medico, especialidade, crm, salario) VALUES (?,?,?,?)";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setString(1, modelMedicos.getNome());
            preparedStatement.setString(2, modelMedicos.getEspecialidade());
            preparedStatement.setInt(3, modelMedicos.getCrm());
            preparedStatement.setString(4, modelMedicos.getSalario());
            preparedStatement.execute();
            util.showPopUpMedico(modelMedicos.getNome());
        } catch (SQLException e) {
            util.showPopUpErrorMedico(modelMedicos.getNome());
            e.printStackTrace();
        }
        conex.closeConnection();
    }

    public ModelMedicos searchMedicos(ModelMedicos modelMedicos) {
        conex.getConnection();
        try {
            conex.executeSql("SELECT * FROM tbl_medicos where nome_medico like'%" + modelMedicos.getPesquisa() + "%'");
            conex.resultSet.first();
            modelMedicos.setId(conex.resultSet.getInt("pk_id_medico"));
            modelMedicos.setNome(conex.resultSet.getString("nome"));
            modelMedicos.setEspecialidade(conex.resultSet.getString("especialidade"));
            modelMedicos.setCrm(conex.resultSet.getInt("crm"));
            modelMedicos.setSalario(conex.resultSet.getString("salario"));
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelMedicos;
    }

    public void editMedico(ModelMedicos modelMedicos) {
        String sql = "UPDATE tbl_medicos SET nome_medico=?,especialidade=?,crm=?, salario=? WHERE pk_id_medico=?";
        conex.getConnection();
        try {
            PreparedStatement pst = conex.conn.prepareStatement(sql);
            pst.setString(1, modelMedicos.getNome());
            pst.setString(2, modelMedicos.getEspecialidade());
            pst.setInt(3, modelMedicos.getCrm());
            pst.setString(4, modelMedicos.getSalario());
            pst.setInt(5, modelMedicos.getId());
            pst.execute();
            util.showPopUpEdit();
        } catch (SQLException e) {
            util.showPopUpErrorEdit();
            e.printStackTrace();
        }
        conex.closeConnection();
    }

    public void deletMedico(ModelMedicos modelMedicos) {
        conex.getConnection();
        String sql = "DELETE FROM tbl_medicos WHERE pk_id_medico=?";
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setInt(1, modelMedicos.getId());
            preparedStatement.execute();
            util.showPopUpDelet();
        } catch (SQLException e) {
            util.showPopUpErrorDelet();
            e.printStackTrace();
        }

        conex.closeConnection();
    }

    public List<ModelMedicos> getList() {
        List<ModelMedicos> listMedicos = new ArrayList<>();
        String sql = "SELECT * FROM tbl_medicos";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ModelMedicos medicos = new ModelMedicos();
                medicos.setId(resultSet.getInt("pk_id_medico"));
                medicos.setNome(resultSet.getString("nome_medico"));
                medicos.setEspecialidade(resultSet.getString("especialidade"));
                medicos.setCrm(resultSet.getInt("crm"));
                medicos.setSalario(resultSet.getString("salario"));
                listMedicos.add(medicos);
            }
            preparedStatement.close();
            resultSet.close();
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMedicos;
    }

    public List<ModelMedicos> getListMedicos(JFXComboBox comboBox) {
        List<ModelMedicos> list = new ArrayList<>();
        ObservableList<ModelMedicos> observableList;
        String sql = "SELECT * FROM tbl_medicos ORDER BY nome_medico";
        conex.getConnection();
        conex.executeSql(sql);
        try {
            conex.resultSet.first();
            do {
                ModelMedicos modelMedicos = new ModelMedicos();
                modelMedicos.setNome(conex.resultSet.getString("nome_medico"));
                list.add(modelMedicos);
                observableList = FXCollections.observableArrayList(list);
                comboBox.setItems(observableList);
            } while (conex.resultSet.next());
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}
