package primary.controller;

import com.jfoenix.controls.*;
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
import primary.dao.MedicoDao;
import primary.dao.PacienteDao;
import primary.model.ModelAgendamento;
import primary.model.ModelPaciente;
import primary.sample.Agendamento;
import primary.sample.Principal;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ControllerAgendamento implements Initializable {
    @FXML
    private FontAwesomeIcon close;
    @FXML
    private FontAwesomeIcon maximize;
    @FXML
    private FontAwesomeIcon minimize;
    @FXML
    private JFXTextField txtPaciente;
    @FXML
    private JFXButton btnBuscar;
    @FXML
    private JFXComboBox<String> comboBoxT;
    @FXML
    private TableView<ModelPaciente> tblPaciente;
    @FXML
    private TableColumn<ModelPaciente, Integer> cID;
    @FXML
    private TableColumn<ModelPaciente, String> cNome;
    @FXML
    private TableColumn<ModelPaciente, String> cTele;
    @FXML
    private TableColumn<ModelPaciente, String> cCpf;
    @FXML
    private TableColumn<ModelPaciente, String> cEnde;
    @FXML
    private JFXComboBox comboBoxM;
    @FXML
    private JFXDatePicker txtData;
    @FXML
    private JFXTextArea txtMotivo;
    @FXML
    private JFXButton btnAgendar;
    @FXML
    private JFXButton btnCancelar;

    private ObservableList<String> turnos = FXCollections.observableArrayList("Vespertino", "Matutino", "Noturno");

    MedicoDao medicoDao = new MedicoDao();
    ModelAgendamento modelAgendamento = new ModelAgendamento();
    AgendamentoDao agendamentoDao = new AgendamentoDao();

    ConexaoMySql conex = new ConexaoMySql();
    ModelPaciente modelPaciente = new ModelPaciente();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBoxT.setItems(turnos);
        medicoDao.getListMedicos(comboBoxM);
        txtData.setValue(LocalDate.now());

        close.setOnMouseClicked((MouseEvent e)->{
            Agendamento.getStage().close();
        });

        minimize.setOnMouseClicked((MouseEvent e)->{
            Agendamento.getStage().setIconified(true);
        });

        btnBuscar.setOnMouseClicked((MouseEvent e) -> {
            txtData.setDisable(false);
            txtMotivo.setDisable(false);
            comboBoxT.setDisable(false);
            comboBoxM.setDisable(false);
            btnAgendar.setDisable(false);
            btnCancelar.setDisable(false);
            if (txtPaciente.getText().equals("")) {
                tblPaciente.getItems().clear();
                preencherTabela(getList2());
            } else {
                tblPaciente.getItems().clear();
                preencherTabela(getList());
            }
        });

        tblPaciente.setOnMouseClicked((MouseEvent e)->{
            ModelPaciente model = tblPaciente.getSelectionModel().getSelectedItem();
            String nome = model.getNome();
            conex.getConnection();
            conex.executeSql("SELECT * FROM tbl_paciente where nome='" + nome + "'");
            try {
                conex.resultSet.first();
                txtPaciente.setText(model.getNome());
                conex.closeConnection();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        btnAgendar.setOnMouseClicked((MouseEvent e)-> {
            modelAgendamento.setPaciente(txtPaciente.getText());
            modelAgendamento.setMedico(String.valueOf(comboBoxM.getValue()));
            modelAgendamento.setTurno(String.valueOf(comboBoxT.getValue()));
            modelAgendamento.setMotivo(String.valueOf(txtMotivo.getText()));
            modelAgendamento.setData(String.valueOf(txtData.getValue()));
            modelAgendamento.setStatus("Em aberto");
            agendamentoDao.getAgenda(modelAgendamento);
            txtData.setDisable(true);
            txtMotivo.setDisable(true);
            comboBoxT.setDisable(true);
            comboBoxM.setDisable(true);
            btnAgendar.setDisable(true);
            btnCancelar.setDisable(true);
            txtData.setValue(LocalDate.now());
            comboBoxT.setValue("");
            comboBoxM.setValue("");
            txtPaciente.setText("");
            txtMotivo.setText("");
            tblPaciente.getItems().clear();
        });

        btnCancelar.setOnMouseClicked((MouseEvent e)->{
            txtData.setDisable(true);
            txtMotivo.setDisable(true);
            comboBoxT.setDisable(true);
            comboBoxM.setDisable(true);
            btnAgendar.setDisable(true);
            btnCancelar.setDisable(true);
            txtData.setValue(LocalDate.now());
            comboBoxT.setValue("");
            comboBoxM.setValue("");
            txtPaciente.setText("");
            txtMotivo.setText("");
            tblPaciente.getItems().clear();
        });
    }

    public void preencherTabela(ObservableList<ModelPaciente> metodo) {
        conex.getConnection();
        cID.setCellValueFactory(new PropertyValueFactory("id"));
        cNome.setCellValueFactory(new PropertyValueFactory("nome"));
        cTele.setCellValueFactory(new PropertyValueFactory("celular"));
        cCpf.setCellValueFactory(new PropertyValueFactory("cpf"));
        cEnde.setCellValueFactory(new PropertyValueFactory("endereco1"));
        tblPaciente.setItems(metodo);
        conex.closeConnection();
    }

    public ObservableList<ModelPaciente> getList() {
        PacienteDao pacienteDao = new PacienteDao();
        modelPaciente.setPesquisa(txtPaciente.getText());
        ModelPaciente model = pacienteDao.searchPaciente(modelPaciente);
        return FXCollections.observableArrayList(pacienteDao.getListAgendamento(model));
    }

    public ObservableList<ModelPaciente> getList2() {
        PacienteDao pacienteDao = new PacienteDao();
        return FXCollections.observableArrayList(pacienteDao.getListAgendamento2());
    }


}
