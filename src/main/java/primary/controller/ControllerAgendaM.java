package primary.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import primary.conexao.ConexaoMySql;
import primary.dao.AgendamentoDao;
import primary.dao.MedicoDao;
import primary.model.ModelAgendamento;

import javax.swing.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class ControllerAgendaM implements Initializable {
    @FXML
    private TableView<ModelAgendamento> tableAgendaM;
    @FXML
    private TableColumn<ModelAgendamento, Integer> cID;
    @FXML
    private TableColumn<ModelAgendamento, String> cPaciente;
    @FXML
    private TableColumn<ModelAgendamento, String> cMotivo;
    @FXML
    private TableColumn<ModelAgendamento, String> cTurno;
    @FXML
    private TableColumn<ModelAgendamento, String> cStatus;
    @FXML
    private JFXComboBox comboMedicos;
    @FXML
    private JFXButton btnAtender;
    @FXML
    private JFXButton btnPesquisar;


    MedicoDao medicoDao = new MedicoDao();
    AgendamentoDao agendamentoDao = new AgendamentoDao();
    ConexaoMySql conex = new ConexaoMySql();

    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String dtHoje = dateFormat.format(date);
    String status;
    int cod;
    String codigo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Task t = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }

            @Override
            protected void succeeded() {
                medicoDao.getListMedicos(comboMedicos);
            }
        };
        Thread th = new Thread(t);
        th.setDaemon(true);
        th.start();

        btnPesquisar.setOnMouseClicked((MouseEvent e) -> {
            Task task = new Task<Integer>() {
                @Override
                protected Integer call() throws Exception {
                    cod = agendamentoDao.searchMedicoID(String.valueOf(comboMedicos.getValue()));
                    return cod;
                }

                @Override
                protected void succeeded() {
                    codigo = String.valueOf(cod);
                    preencherTabela();
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });
    }

    public void preencherTabela() {
        conex.getConnection();
        cID.setCellValueFactory(new PropertyValueFactory("id_agendamento"));
        cPaciente.setCellValueFactory(new PropertyValueFactory("paciente"));
        cMotivo.setCellValueFactory(new PropertyValueFactory("motivo"));
        cTurno.setCellValueFactory(new PropertyValueFactory("turno"));
        cStatus.setCellValueFactory(new PropertyValueFactory("status"));
        tableAgendaM.setItems(getList());
        if (tableAgendaM.getItems() == null) {
            JOptionPane.showMessageDialog(null, "Não há agendamentos para hoje!");
        }
        conex.closeConnection();
    }

    public ObservableList<ModelAgendamento> getList() {
        AgendamentoDao agendamentoDao = new AgendamentoDao();
        status = "Em atendimento";
        return FXCollections.observableArrayList(agendamentoDao.getListMedico(dtHoje, status, codigo));

    }
}
