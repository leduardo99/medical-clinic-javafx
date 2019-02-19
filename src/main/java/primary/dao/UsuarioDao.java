package primary.dao;

import primary.conexao.ConexaoMySql;
import primary.model.ModelUsuario;
import primary.util.Utilidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    Utilidade util = new Utilidade();
    ConexaoMySql conex = new ConexaoMySql();

    public void getUsuario(ModelUsuario modelUsuario) {
        String sql = "INSERT INTO tbl_usuarios(cod_acesso, senha, nome, email, tipo_conta) VALUES (?,?,?,?,?)";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setString(1, modelUsuario.getCod_acesso());
            preparedStatement.setString(2, modelUsuario.getSenha());
            preparedStatement.setString(3, modelUsuario.getNome());
            preparedStatement.setString(4, modelUsuario.getEmail());
            preparedStatement.setString(5, modelUsuario.getTipo_conta());
            preparedStatement.execute();
            util.showPopUpUsuario(modelUsuario.getNome());
        } catch (SQLException ex) {
            ex.printStackTrace();
            util.showPopUpErrorUsuario(modelUsuario.getNome());
        }
        conex.closeConnection();
    }

    public ModelUsuario searchUsuario(ModelUsuario modelUsuario) {
        conex.getConnection();
        try {
            conex.executeSql("SELECT * FROM tbl_usuarios where nome like'%" + modelUsuario.getPesquisa() + "%'");
            conex.resultSet.first();
            modelUsuario.setId_usuario(conex.resultSet.getInt("pk_id_usuario"));
            modelUsuario.setCod_acesso(conex.resultSet.getString("cod_acesso"));
            modelUsuario.setSenha(conex.resultSet.getString("senha"));
            modelUsuario.setNome(conex.resultSet.getString("nome"));
            modelUsuario.setEmail(conex.resultSet.getString("email"));
            modelUsuario.setTipo_conta(conex.resultSet.getString("tipo_conta"));
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modelUsuario;
    }

    public void editUsuario(ModelUsuario modelUsuario) {
        conex.getConnection();
        String sql = "UPDATE tbl_usuarios SET cod_acesso=?, senha=?, nome=?, email=?, tipo_conta=? where pk_id_usuario=?";
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setString(1, modelUsuario.getCod_acesso());
            preparedStatement.setString(2, modelUsuario.getSenha());
            preparedStatement.setString(3, modelUsuario.getNome());
            preparedStatement.setString(4, modelUsuario.getEmail());
            preparedStatement.setInt(5, modelUsuario.getId_usuario());
            preparedStatement.setString(6, modelUsuario.getTipo_conta());
            preparedStatement.execute();
            conex.closeConnection();
            util.showPopUpEdit();
        } catch (SQLException e) {
            util.showPopUpErrorEdit();
            e.printStackTrace();
        }
        conex.closeConnection();
    }

    public void deleteUsuario(ModelUsuario modelUsuario) {
        conex.getConnection();
        String sql = "DELETE FROM tbl_usuarios WHERE pk_id_usuario=?";
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setInt(1, modelUsuario.getId_usuario());
            preparedStatement.execute();
            util.showPopUpDelet();
        } catch (SQLException e) {
            util.showPopUpErrorDelet();
            e.printStackTrace();
        }
        conex.closeConnection();
    }

    public List<ModelUsuario> getList() {
        List<ModelUsuario> listLogin = new ArrayList<>();
        String sql = "SELECT * FROM tbl_usuarios";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ModelUsuario modelUsuario = new ModelUsuario();
                modelUsuario.setId_usuario(resultSet.getInt("pk_id_usuario"));
                modelUsuario.setCod_acesso(resultSet.getString("cod_acesso"));
                modelUsuario.setSenha(resultSet.getString("senha"));
                modelUsuario.setNome(resultSet.getString("nome"));
                modelUsuario.setEmail(resultSet.getString("email"));
                //modelUsuario.setTipo_conta(resultSet.getString("tipo_conta"));
                listLogin.add(modelUsuario);
            }
            preparedStatement.close();
            resultSet.close();
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listLogin;
    }
}
