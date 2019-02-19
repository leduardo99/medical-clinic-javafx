package primary.conexao;


import java.sql.*;

public class ConexaoMySql {

    public Statement statement;
    public ResultSet resultSet;
    public Connection conn;

    public void getConnection() {
        try {
            String drive = "com.mysql.jdbc.Driver";
            System.setProperty("jdbc.Drivers", drive);
            String url = "jdbc:mysql://35.199.73.177/clinicamedica";
            String user = "root";
            String senha = "913700";
            conn = DriverManager.getConnection(url, user, senha);
            System.out.println("Conexão efetuada");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            conn.close();
            System.out.println("Conexão finalizada");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void executeSql(String sql) {
        try {
            statement = conn.createStatement(resultSet.TYPE_SCROLL_INSENSITIVE, resultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
