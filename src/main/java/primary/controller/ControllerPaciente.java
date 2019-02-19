package primary.controller;

import com.jfoenix.controls.*;
import primary.conexao.ConexaoMySql;
import primary.dao.PacienteDao;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import primary.model.ModelPaciente;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import primary.sample.Paciente;
import primary.util.MaskFieldUtil;

public class ControllerPaciente implements Initializable {
    @FXML
    private ImageView imgFoto;
    @FXML
    private JFXComboBox<String> comboBoxSexo;
    @FXML
    private JFXComboBox<String> comboBoxTipoS;
    @FXML
    private JFXComboBox<String> comboBoxPlano;
    @FXML
    private JFXTextField txtNome;
    @FXML
    private JFXTextField txtIdade;
    @FXML
    private JFXTextField txtTelefone;
    @FXML
    private JFXTextField txtCelular;
    @FXML
    private JFXTextField txtCpf;
    @FXML
    private JFXTextField txtEnd1;
    @FXML
    private JFXDatePicker txtDate;
    @FXML
    private JFXTextField txtEnd2;
    @FXML
    private JFXTextArea txtNota;
    @FXML
    private JFXTextField txtId;
    @FXML
    private JFXTextField txtPesquisar;
    @FXML
    private JFXButton btnFoto;
    @FXML
    private JFXButton btnSalvar;
    @FXML
    private JFXButton btnNovo;
    @FXML
    private JFXButton btnPesquisar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXButton btnEditar;
    @FXML
    private JFXButton btnExcluir;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView<ModelPaciente> tblPaciente;
    @FXML
    private TableColumn<ModelPaciente, Integer> cId;
    @FXML
    private TableColumn<ModelPaciente, String> cNome;
    @FXML
    private TableColumn<ModelPaciente, String> cPlano;
    @FXML
    private TableColumn<ModelPaciente, String> cIdade;
    @FXML
    private TableColumn<ModelPaciente, String> cSexo;
    @FXML
    private TableColumn<ModelPaciente, String> cTipoS;
    @FXML
    private TableColumn<ModelPaciente, String> cCelular;
    @FXML
    private TableColumn<ModelPaciente, String> cCpf;
    @FXML
    private TableColumn<ModelPaciente, String> cNascimento;
    @FXML
    private TableColumn<ModelPaciente, String> cNotas;
    @FXML
    private FontAwesomeIcon close;
    @FXML
    private FontAwesomeIcon minimize;



    private String caminhoFoto;
    private int flag = 0;

    private ConexaoMySql conex = new ConexaoMySql();
    private ModelPaciente modelPaciente = new ModelPaciente();
    private PacienteDao pacienteDao = new PacienteDao();

    private ObservableList<String> sexo = FXCollections.observableArrayList("Masculino", "Feminino");
    private ObservableList<String> tipoS = FXCollections.observableArrayList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
    private ObservableList<String> plano = FXCollections.observableArrayList("Plano Versátil", "Plano Dinâmico", "Plano Líder");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtDate.setValue(LocalDate.of(2018, 01, 01));
        preencherTabela();
        MaskFieldUtil.cpfField(txtCpf);
        MaskFieldUtil.foneField(txtCelular);
        MaskFieldUtil.fixoField(txtTelefone);
        conex.getConnection();
        comboBoxSexo.setItems(sexo);
        comboBoxTipoS.setItems(tipoS);
        comboBoxPlano.setItems(plano);

        close.setOnMouseClicked((MouseEvent e) -> {
            Paciente.getStage().close();
        });

        minimize.setOnMouseClicked((MouseEvent e) -> {
            Paciente.getStage().setIconified(true);
        });

        btnSalvar.setOnMouseClicked((MouseEvent e) -> {
            if (flag == 1) {
                modelPaciente.setNome(txtNome.getText());
                modelPaciente.setSexo(comboBoxSexo.getValue());
                modelPaciente.setTiposangue(comboBoxTipoS.getValue());
                modelPaciente.setPlano(comboBoxPlano.getValue());
                modelPaciente.setIdade((txtIdade.getText()));
                modelPaciente.setCpf(txtCpf.getText());
                modelPaciente.setTelefone(txtTelefone.getText());
                modelPaciente.setCelular(txtCelular.getText());
                modelPaciente.setEndereco1(txtEnd1.getText());
                modelPaciente.setEndereco2(txtEnd2.getText());
                modelPaciente.setRecomendacoes(txtNota.getText());
                modelPaciente.setDatanascimento(String.valueOf(txtDate.getValue()));
                modelPaciente.setFoto(caminhoFoto);
                pacienteDao.getPaciente(modelPaciente);
                preencherTabela();
                desabilitarCampos();
            } else {
                modelPaciente.setNome(txtNome.getText());
                modelPaciente.setSexo(comboBoxSexo.getValue());
                modelPaciente.setTiposangue(comboBoxTipoS.getValue());
                modelPaciente.setPlano(comboBoxPlano.getValue());
                modelPaciente.setIdade((txtIdade.getText()));
                modelPaciente.setCpf(txtCpf.getText());
                modelPaciente.setTelefone(txtTelefone.getText());
                modelPaciente.setCelular(txtCelular.getText());
                modelPaciente.setEndereco1(txtEnd1.getText());
                modelPaciente.setEndereco2(txtEnd2.getText());
                modelPaciente.setRecomendacoes(txtNota.getText());
                modelPaciente.setDatanascimento(String.valueOf(txtDate.getValue()));
                modelPaciente.setFoto(caminhoFoto);
                modelPaciente.setId(Integer.parseInt(txtId.getText()));
                pacienteDao.editPaciente(modelPaciente);
                preencherTabela();
                desabilitarCampos();
            }
        });

        btnFoto.setOnMouseClicked((MouseEvent e) -> {
            selectionPicture();
        });

        btnNovo.setOnMouseClicked((MouseEvent e) ->
        {
            flag = 1;
            System.out.println("Flag alterada: " + flag);
            habilitarCampos();
        });

        btnCancelar.setOnMouseClicked((MouseEvent e) -> {
            desabilitarCampos();
        });

        btnPesquisar.setOnMouseClicked((MouseEvent e) -> {
            btnNovo.setDisable(true);
            btnExcluir.setDisable(false);
            btnEditar.setDisable(false);
            btnCancelar.setDisable(false);
            modelPaciente.setPesquisa(txtPesquisar.getText());
            ModelPaciente model = pacienteDao.searchPaciente(modelPaciente);
            txtNome.setText(model.getNome());
            comboBoxSexo.setValue(String.valueOf(model.getSexo()));
            comboBoxTipoS.setValue(String.valueOf(model.getTiposangue()));
            comboBoxPlano.setValue(String.valueOf(model.getPlano()));
            txtIdade.setText(model.getIdade());
            txtCpf.setText(model.getCpf());
            txtTelefone.setText(model.getTelefone());
            txtCelular.setText(model.getCelular());
            txtEnd1.setText(model.getEndereco1());
            txtEnd2.setText(model.getEndereco2());
            txtNota.setText(model.getRecomendacoes());
            txtDate.setValue(LocalDate.parse(model.getDatanascimento()));
            txtId.setText(String.valueOf(model.getId()));
            imgFoto.setImage(new Image("file:///" + model.getFoto()));
            tabPane.getSelectionModel().select(0);
        });

        btnExcluir.setOnMouseClicked((MouseEvent e) -> {
            Alert dialogoExe = new Alert(Alert.AlertType.WARNING);
            dialogoExe.setTitle("Excluindo dados");
            dialogoExe.setHeaderText("Você deseja realmente excluir os dados selecionados?");
            ButtonType btnSim = new ButtonType("Sim");
            ButtonType btnNao = new ButtonType("Não");

            dialogoExe.getButtonTypes().setAll(btnSim, btnNao);
            dialogoExe.showAndWait().ifPresent(escolha -> {
                if (escolha == btnSim) {
                    modelPaciente.setId(Integer.parseInt(txtId.getText()));
                    pacienteDao.deletPaciente(modelPaciente);
                    desabilitarCampos();
                    preencherTabela();
                } else if (escolha == btnNao) {
                    System.out.println("Usuário escolheu não");
                }
            });
        });

        btnEditar.setOnMouseClicked((MouseEvent e) ->
        {
            flag = 2;
            System.out.println("Flag alterada: " + flag);
            btnEditar.setDisable(true);
            btnCancelar.setDisable(false);
            btnExcluir.setDisable(true);
            btnNovo.setDisable(true);
            habilitarCampos();
        });

        tblPaciente.setOnMouseClicked((MouseEvent e) -> {
            btnNovo.setDisable(true);
            btnExcluir.setDisable(false);
            btnEditar.setDisable(false);
            btnCancelar.setDisable(false);
            ModelPaciente model = tblPaciente.getSelectionModel().getSelectedItem();
            String nome = model.getNome();
            conex.getConnection();
            conex.executeSql("SELECT * FROM tbl_paciente where nome='" + nome + "'");
            try {
                conex.resultSet.first();
                txtNome.setText(model.getNome());
                comboBoxSexo.setValue(String.valueOf(model.getSexo()));
                comboBoxTipoS.setValue(String.valueOf(model.getTiposangue()));
                txtIdade.setText(model.getIdade());
                txtCpf.setText(model.getCpf());
                txtTelefone.setText(model.getTelefone());
                txtCelular.setText(model.getCelular());
                comboBoxPlano.setValue(String.valueOf(model.getPlano()));
                txtEnd1.setText(model.getEndereco1());
                txtEnd2.setText(model.getEndereco2());
                txtNota.setText(model.getRecomendacoes());
                txtDate.setValue(LocalDate.parse(model.getDatanascimento()));
                txtId.setText(String.valueOf(model.getId()));
                imgFoto.setImage(new Image("file:///" + model.getFoto()));
                tabPane.getSelectionModel().select(0);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        conex.closeConnection();
    }

    private void selectionPicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Imagens", "*.jpg", "*.png", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            imgFoto.setImage(new Image("file:///" + file.getAbsolutePath()));
            caminhoFoto = file.getAbsolutePath();
        }
    }


    private void habilitarCampos() {
        txtNome.setDisable(false);
        txtIdade.setDisable(false);
        txtTelefone.setDisable(false);
        txtCelular.setDisable(false);
        txtCpf.setDisable(false);
        txtEnd1.setDisable(false);
        txtDate.setDisable(false);
        txtEnd2.setDisable(false);
        txtNota.setDisable(false);
        comboBoxTipoS.setDisable(false);
        comboBoxSexo.setDisable(false);
        comboBoxPlano.setDisable(false);
        btnFoto.setDisable(false);
        btnCancelar.setDisable(false);
        btnSalvar.setDisable(false);
    }

    private void desabilitarCampos() {
        btnNovo.setDisable(false);
        txtId.setText("");
        txtNome.setText("");
        txtIdade.setText("");
        txtTelefone.setText("");
        txtCelular.setText("");
        txtCpf.setText("");
        txtEnd1.setText("");
        txtDate.setValue(LocalDate.of(2018, 01, 01));
        txtEnd2.setText("");
        txtNota.setText("");
        txtId.setDisable(true);
        txtNome.setDisable(true);
        txtIdade.setDisable(true);
        txtTelefone.setDisable(true);
        txtCelular.setDisable(true);
        txtCpf.setDisable(true);
        txtEnd1.setDisable(true);
        txtDate.setDisable(true);
        txtEnd2.setDisable(true);
        txtNota.setDisable(true);
        comboBoxTipoS.setDisable(true);
        comboBoxSexo.setDisable(true);
        comboBoxPlano.setDisable(true);
        btnFoto.setDisable(true);
        btnSalvar.setDisable(true);
        btnCancelar.setDisable(true);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
        imgFoto.setImage(new Image(String.valueOf((getClass().getResource("/imagens/download.png")))));
    }


    public void preencherTabela() {
        conex.getConnection();
        cId.setCellValueFactory(new PropertyValueFactory("id"));
        cNome.setCellValueFactory(new PropertyValueFactory("nome"));
        cPlano.setCellValueFactory(new PropertyValueFactory("plano"));
        cIdade.setCellValueFactory(new PropertyValueFactory("idade"));
        cSexo.setCellValueFactory(new PropertyValueFactory("sexo"));
        cTipoS.setCellValueFactory(new PropertyValueFactory("tiposangue"));
        cCelular.setCellValueFactory(new PropertyValueFactory("celular"));
        cCpf.setCellValueFactory(new PropertyValueFactory("cpf"));
        cNascimento.setCellValueFactory(new PropertyValueFactory("datanascimento"));
        cNotas.setCellValueFactory(new PropertyValueFactory("recomendacoes"));
        tblPaciente.setItems(getList());
        conex.closeConnection();
    }

    public ObservableList<ModelPaciente> getList() {
        PacienteDao pacienteDao = new PacienteDao();
        return FXCollections.observableArrayList(pacienteDao.getList());
    }
}
