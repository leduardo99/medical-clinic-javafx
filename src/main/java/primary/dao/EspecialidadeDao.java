package primary.dao;

import primary.conexao.ConexaoMySql;
import primary.model.ModelEspecialidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadeDao {

    ConexaoMySql conex = new ConexaoMySql();

    public List<ModelEspecialidade> getList() {
        List<ModelEspecialidade> especialidades = new ArrayList<>();
        String sql = "SELECT * FROM tbl_especialidade";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ModelEspecialidade especialidade = new ModelEspecialidade();
                especialidade.setNomeEspecialidade(resultSet.getString("especialidade"));
                especialidades.add(especialidade);
            }
            preparedStatement.close();
            resultSet.close();
            conex.closeConnection();
        } catch (SQLException e) {
            return null;
        }
        return especialidades;
    }

}
