package primary.dao;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import primary.conexao.ConexaoMySql;
import primary.model.ModelAgendamento;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDao {

    private ConexaoMySql conex = new ConexaoMySql();
    private ConexaoMySql conexPaciente = new ConexaoMySql();
    private ConexaoMySql conexMedico = new ConexaoMySql();

    private int codMedico;
    private int codPaciente;

    public void getAgenda(ModelAgendamento modelAgendamento) {
        searchMedico(modelAgendamento.getMedico());
        searchPaciente(modelAgendamento.getPaciente());
        String sql = "INSERT INTO tbl_agendamento(fk_id_paciente, fk_id_medico, turno, data, motivo, status) VALUES (?,?,?,?,?,?)";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            preparedStatement.setInt(1, codPaciente);
            preparedStatement.setInt(2, codMedico);
            preparedStatement.setString(3, modelAgendamento.getTurno());
            preparedStatement.setString(4, modelAgendamento.getData());
            preparedStatement.setString(5, modelAgendamento.getMotivo());
            preparedStatement.setString(6, modelAgendamento.getStatus());
            preparedStatement.execute();
            showPopUp();
        } catch (SQLException e) {
            showPopUpError();
            e.printStackTrace();
        }
        conex.closeConnection();
    }

    private void searchMedico(String nomeMedico) {
        conexMedico.getConnection();
        conexMedico.executeSql("SELECT * FROM tbl_medicos WHERE nome_medico='"+nomeMedico+"'");
        try {
            conexMedico.resultSet.first();
            codMedico = conexMedico.resultSet.getInt("pk_id_medico");

        } catch (SQLException e) {
            showPopUpErrorMedico(nomeMedico);
            e.printStackTrace();
        }
        conexMedico.closeConnection();
    }

    public int searchMedicoID(String nomeMedico) {
        conexMedico.getConnection();
        conexMedico.executeSql("SELECT * FROM tbl_medicos WHERE nome_medico='"+nomeMedico+"'");
        try {
            conexMedico.resultSet.first();
            codMedico = conexMedico.resultSet.getInt("pk_id_medico");

        } catch (SQLException e) {
            showPopUpErrorMedico(nomeMedico);
            e.printStackTrace();
        }
        conexMedico.closeConnection();
        return codMedico;
    }

    private void searchPaciente(String nomePaciente) {
        conexPaciente.getConnection();
        conexPaciente.executeSql("SELECT * FROM tbl_paciente WHERE nome='"+nomePaciente+"'");
        try {
            conexPaciente.resultSet.first();
            codPaciente = conexPaciente.resultSet.getInt("pk_id_paciente");

        } catch (SQLException e) {
            showPopUpErrorPaciente(nomePaciente);
            e.printStackTrace();
        }
        conexPaciente.closeConnection();
    }

    public List<ModelAgendamento> getList(String data, String turno) {
        List<ModelAgendamento> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_agendamento INNER JOIN tbl_paciente ON fk_id_paciente=pk_id_paciente INNER JOIN tbl_medicos ON fk_id_medico=pk_id_medico WHERE data='"+data+"' AND status='"+turno+"' ORDER BY turno";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                ModelAgendamento modelAgendamento = new ModelAgendamento();
                modelAgendamento.setId_agendamento(resultSet.getInt("pk_id_agendamento"));
                modelAgendamento.setPaciente(resultSet.getString("nome"));
                modelAgendamento.setMedico(resultSet.getString("nome_medico"));
                modelAgendamento.setTurno(resultSet.getString("turno"));
                modelAgendamento.setData(resultSet.getString("data"));
                modelAgendamento.setStatus(resultSet.getString("status"));
                list.add(modelAgendamento);
            }
            preparedStatement.close();
            resultSet.close();
            conex.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ModelAgendamento> getListMedico(String data, String status, String cod) {
        List<ModelAgendamento> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_agendamento INNER JOIN tbl_paciente ON fk_id_paciente=pk_id_paciente INNER JOIN tbl_medicos ON fk_id_medico=pk_id_medico where fk_id_medico='"+cod+"' AND data='"+data+"' AND status='"+status+"'";
        conex.getConnection();
        try {
            PreparedStatement preparedStatement = conex.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                ModelAgendamento modelAgendamento = new ModelAgendamento();
                modelAgendamento.setId_agendamento(resultSet.getInt("pk_id_agendamento"));
                modelAgendamento.setPaciente(resultSet.getString("nome"));
                modelAgendamento.setMedico(resultSet.getString("nome_medico"));
                modelAgendamento.setMotivo(resultSet.getString("motivo"));
                modelAgendamento.setTurno(resultSet.getString("turno"));
                modelAgendamento.setData(resultSet.getString("data"));
                modelAgendamento.setStatus(resultSet.getString("status"));
                list.add(modelAgendamento);
            }
            preparedStatement.close();
            resultSet.close();
            conex.closeConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não há agendamentos para hoje!");
            e.printStackTrace();
        }
        return list;
    }

    public void alterAgenda(ModelAgendamento modelAgendamento) {
        conex.getConnection();
        String sql = "UPDATE tbl_agendamento SET status=? WHERE pk_id_agendamento=?";
        try {
            PreparedStatement preparedStatement =   conex.conn.prepareStatement(sql);
            preparedStatement.setString(1, modelAgendamento.getStatus());
            preparedStatement.setInt(2, modelAgendamento.getId_agendamento());
            preparedStatement.execute();
            JOptionPane.showMessageDialog(null, "Agendamento em atendimento");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atender o agendamento");
            e.printStackTrace();
        }
        conex.closeConnection();
    }


    private void showPopUpErrorMedico(String medico) {
        Image img = new Image("/imagens/error.png");
        Notifications not = Notifications.create()
                .title("Médico não encontrado")
                .text("Error O médico " + medico + " não foi encontrado no sistema!")
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(10))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Notificação fechada");
                    }
                });
        not.hideCloseButton();
        not.show();
    }
    private void showPopUpErrorPaciente(String paciente) {
        Image img = new Image("/imagens/error.png");
        Notifications not = Notifications.create()
                .title("Paciente não encontrado")
                .text("Error O paciente " + paciente + " não foi encontrado no sistema!")
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(10))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Notificação fechada");
                    }
                });
        not.hideCloseButton();
        not.show();
    }
    private void showPopUp() {
        Image img = new Image("/imagens/confirm.png");
        Notifications not = Notifications.create()
                .title("Agendamento feito")
                .text("Agendamento finalizado com sucesso!")
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(10))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Notificação fechada");
                    }
                });
        not.hideCloseButton();
        not.show();
    }
    private void showPopUpError() {
        Image img = new Image("/imagens/error.png");
        Notifications not = Notifications.create()
                .title("Agendamento falhou")
                .text("Não foi possível realizar o agendamento!")
                .graphic(new ImageView(img))
                .hideAfter(Duration.seconds(10))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("Notificação fechada");
                    }
                });
        not.hideCloseButton();
        not.show();
    }

}
