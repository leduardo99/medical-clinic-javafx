package primary.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import primary.conexao.ConexaoMySql;
import primary.sample.*;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class ControllerPrincipal implements Initializable {
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnAtualizar;
    @FXML
    private FontAwesomeIcon close;
    @FXML
    private FontAwesomeIcon minimizar;
    @FXML
    private Label nPacientes;
    @FXML
    private Label nMedicos;
    @FXML
    private Label nAgenda;

    private ConexaoMySql conn = new ConexaoMySql();

    private int contP = 0;
    private int contM = 0;
    private int contA = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Login login = new Login();

        contarRegistrosPaciente();
        contarRegistrosAgenda();
        contarRegistrosMedico();

        btnAtualizar.setOnMouseClicked((MouseEvent e) -> {
            contP = 0;
            contM = 0;
            contA = 0;
            contarRegistrosPaciente();
            contarRegistrosMedico();
            contarRegistrosAgenda();
        });

        minimizar.setOnMouseClicked((MouseEvent event) -> {
            Principal.getStage().setIconified(true);
        });

        close.setOnMouseClicked((MouseEvent event) -> {
            System.exit(0);
        });

        btnLogout.setOnMouseClicked((MouseEvent event) -> {
            Principal.getStage().close();
            try {
                login.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    private void contarRegistrosPaciente() {
        String sql = "SELECT * FROM tbl_paciente";
        try {
            conn.getConnection();
            PreparedStatement preparedStatement = conn.conn.prepareStatement(sql);
            Task task = new Task() {
                @Override
                protected Integer call() throws Exception {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        contP++;
                    }
                    return contP;
                }

                @Override
                protected void succeeded() {
                    nPacientes.setText(String.valueOf(contP));
                    conn.closeConnection();
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void contarRegistrosMedico() {
        String sql = "SELECT * FROM tbl_medicos";
        try {
            conn.getConnection();
            PreparedStatement preparedStatement = conn.conn.prepareStatement(sql);
            Task task = new Task() {
                @Override
                protected Integer call() throws Exception {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        contM++;
                    }
                    return contM;
                }

                @Override
                protected void succeeded() {
                    nMedicos.setText(String.valueOf(contM));
                    conn.closeConnection();
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void contarRegistrosAgenda() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dtHoje = dateFormat.format(date);
        String sql = "SELECT * FROM tbl_agendamento WHERE data='" + dtHoje + "' AND status='Em aberto'";
        try {
            conn.getConnection();
            PreparedStatement preparedStatement = conn.conn.prepareStatement(sql);
            Task task = new Task() {
                @Override
                protected Integer call() throws Exception {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        contA++;
                    }
                    return contA;
                }

                @Override
                protected void succeeded() {
                    nAgenda.setText(String.valueOf(contA));
                    conn.closeConnection();
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void abrirMedicosCad(ActionEvent actionEvent) {
        Medicos viewMedicos = new Medicos();
        Principal.getStage().setIconified(true);
        try {
            viewMedicos.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirUsuarios(ActionEvent actionEvent) {
        Usuario viewUsuario = new Usuario();
        Principal.getStage().setIconified(true);
        try {
            viewUsuario.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirPaciente(ActionEvent actionEvent) {
        Paciente viewPaciente = new Paciente();
        Principal.getStage().setIconified(true);
        try {
            viewPaciente.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirAgenda(ActionEvent event) {
        Agenda viewAgenda = new Agenda();
        Principal.getStage().setIconified(true);
        try {
            viewAgenda.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirAgendaMed(ActionEvent event) {
        AgendaMedicos agendaMedicos = new AgendaMedicos();
        Principal.getStage().setIconified(true);
        try {
            agendaMedicos.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirAgendamento(ActionEvent event) {
        Agendamento viewAgendamento = new Agendamento();
        Principal.getStage().setIconified(true);
        try {
            viewAgendamento.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
