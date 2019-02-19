package primary.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import primary.conexao.ConexaoMySql;
import primary.dao.AgendamentoDao;
import primary.model.ModelAgendamento;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ControllerAgenda implements Initializable {
    @FXML
    private FontAwesomeIcon close;
    @FXML
    private FontAwesomeIcon minimizar;
    @FXML
    private TableColumn<ModelAgendamento, Integer> cID;
    @FXML
    private TableColumn<ModelAgendamento, String> cPaciente;
    @FXML
    private TableColumn<ModelAgendamento, String> cMedico;
    @FXML
    private TableColumn<ModelAgendamento, String> cTurno;
    @FXML
    private TableColumn<ModelAgendamento, String> cData;
    @FXML
    private TableColumn<ModelAgendamento, String> cStatus;
    @FXML
    private TableView<ModelAgendamento> tblAgenda;
    @FXML
    private JFXButton btnAtender;

    private ModelAgendamento modelAgendamento = new ModelAgendamento();
    private AgendamentoDao agendamentoDao = new AgendamentoDao();
    private ConexaoMySql conex = new ConexaoMySql();
    private Calendar calendar = Calendar.getInstance();
    private Date date = calendar.getTime();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String dtHoje = dateFormat.format(date);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preencherTabela();
        System.out.println(dtHoje);

        tblAgenda.setOnMouseClicked((MouseEvent e)->{
            String cod_agenda = String.valueOf(tblAgenda.getSelectionModel().getSelectedItem().getId_agendamento());
            conex.getConnection();
            conex.executeSql("SELECT * FROM tbl_agendamento WHERE pk_id_agendamento='"+cod_agenda+"'");
            try {
                conex.resultSet.first();
                modelAgendamento.setStatus("Em atendimento");
                modelAgendamento.setId_agendamento(conex.resultSet.getInt("pk_id_agendamento"));
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        btnAtender.setOnMouseClicked((MouseEvent e)->{
            agendamentoDao.alterAgenda(modelAgendamento);
            preencherTabela();
        });
    }

    public void preencherTabela() {
        conex.getConnection();
        cID.setCellValueFactory(new PropertyValueFactory("id_agendamento"));
        cPaciente.setCellValueFactory(new PropertyValueFactory("paciente"));
        cMedico.setCellValueFactory(new PropertyValueFactory("medico"));
        cTurno.setCellValueFactory(new PropertyValueFactory("turno"));
        cData.setCellValueFactory(new PropertyValueFactory("data"));
        cStatus.setCellValueFactory(new PropertyValueFactory("status"));
        tblAgenda.setItems(getList());
        if (tblAgenda == null) {
            JOptionPane.showMessageDialog(null, "Não há agendamentos para hoje!");
        }
        conex.closeConnection();
    }
    public ObservableList<ModelAgendamento> getList() {
        AgendamentoDao agendamentoDao = new AgendamentoDao();
        String status = "Em aberto";
        return FXCollections.observableArrayList(agendamentoDao.getList(dtHoje, status));
    }
}
