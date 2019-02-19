package primary.controller;

import primary.conexao.ConexaoMySql;
import primary.model.ModelEspecialidade;
import primary.model.ModelMedicos;
import primary.sample.Principal;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import primary.dao.EspecialidadeDao;
import primary.dao.MedicoDao;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import primary.sample.Medicos;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;
import primary.util.MaskFieldUtil;

public class ControllerMedicos implements Initializable {

    //Istância da classe ModelMedicos e MedicoDao
    ModelMedicos modelMedicos = new ModelMedicos();
    MedicoDao medicoDao = new MedicoDao();

    /**
     * Instancia a classe EspecialidadeDao, cria uma lista de modelos(especialidade) e setta em uma coleção
     */
    EspecialidadeDao dao = new EspecialidadeDao();
    List<ModelEspecialidade> especialidades = dao.getList();
    ObservableList<ModelEspecialidade> list = FXCollections.observableArrayList(especialidades);

    ConexaoMySql conecta = new ConexaoMySql();

    int flag = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Faz conexão com o banco de dados
        conecta.getConnection();
        //Chama um método para preenchimento da tabela
        preencherTabela();

        //Adiciona as especialidades no comboBox
        comboBox.setItems(list);


        /*Seleciona o primeiro item do comboBox e inicia o mesmo com o nome do item.*/
        /*Fiz a utilização porque quando se fazia uma pesquisa o comboBox ficava vazio.*/
        comboBox.getSelectionModel().selectFirst();

        close.setOnMouseClicked((MouseEvent e) -> {
            /**
             * Fecha a janela atual a e abre a janela principal novamente.
             * Finaliza a conexão com o banco de dados
             */
            Medicos.getStage().close();
            Principal.getStage().show();
            conecta.closeConnection();
        });

        minimizar.setOnMouseClicked((MouseEvent e) -> {
            //Minimza a tela atual
            Medicos.getStage().setIconified(true);
        });

        btnSalvar.setOnMouseClicked((MouseEvent event) -> {
            /**
             * Método de salvar: O método recebe um flag de verificação.
             * Se essa flag for '1' o método executa o if salvando os dados no banco de dados.
             * Se a flag for diferente de '1' o método executa uma edição nos dados e salva os mesmos.
             */
            if (flag == 1) {
                String nome = txtNome.getText();
                String crm = txtCrm.getText();
                String salario = txtSalario.getText();
                String especialidade = String.valueOf(comboBox.getValue());
                if (nome.equals("") || crm.equals("") || salario.equals("") || especialidade.equals("")) {
                    validateFields();
                } else {
                modelMedicos.setNome(nome);
                modelMedicos.setEspecialidade(especialidade);
                modelMedicos.setCrm(Integer.parseInt(crm));
                modelMedicos.setSalario(salario);
                medicoDao.getMedicos(modelMedicos);
                desbilitarCampos();
                btnNovo.setDisable(false);
                txtSalario.setDisable(true);
                txtSalario.setText("R$");
                preencherTabela();
                }
            } else {
                modelMedicos.setId(Integer.parseInt(txtId.getText()));
                modelMedicos.setNome(txtNome.getText());
                modelMedicos.setEspecialidade(String.valueOf(comboBox.getValue()));
                modelMedicos.setCrm(Integer.parseInt(txtCrm.getText()));
                modelMedicos.setSalario(txtSalario.getText());
                medicoDao.editMedico(modelMedicos);
                desbilitarCampos();
                btnNovo.setDisable(false);
                txtSalario.setDisable(true);
                txtSalario.setText("R$");
                preencherTabela();
            }
        });

        btnNovo.setOnMouseClicked((MouseEvent e) -> {
            /**
             * Ao clicar no botão de NOVO, se recebe uma flag com o valor '1' e habilita os compos de texto.
             */
            flag = 1;
            System.out.println("Flag alterada:"+flag);
            habilitarCampos();
            btnEditar.setDisable(true);
            txtNome.setText("");
            txtCrm.setText("");
            txtPesquisar.setText("");
            txtSalario.setText("R$");
            txtSalario.setDisable(false);
            btnExcluir.setDisable(true);
        });

        btnCancelar.setOnMouseClicked((MouseEvent e) -> {
            //Desabilita os campos
            desbilitarCampos();
            btnExcluir.setDisable(true);
            btnNovo.setDisable(false);
            txtPesquisar.setText("");
            txtSalario.setText("");
            txtSalario.setDisable(true);
        });

        btnEditar.setOnMouseClicked((MouseEvent e) -> {
            /**
             * Recebe a flag 2 após pressionar o botão editar e habilita os campos para edição
             */
            flag = 2;
            System.out.println("Flag alterada:"+flag);
            habilitarCampos();
            btnNovo.setDisable(true);
            btnEditar.setDisable(true);
            btnExcluir.setDisable(true);
            txtSalario.setDisable(false);
            txtPesquisar.setText("");
        });

        btnPesquisar.setOnMouseClicked((MouseEvent e) -> {
            /**
             * Desabilita alguns campos e faz uma pesquisa no banco de de dados e seta os
             * valores nos campos de texto e no comboBox.
             */
            btnEditar.setDisable(false);
            btnExcluir.setDisable(false);
            btnNovo.setDisable(false);
            modelMedicos.setPesquisa(txtPesquisar.getText());
            ModelMedicos model = medicoDao.searchMedicos(modelMedicos);
            txtId.setText(String.valueOf(model.getId()));
            txtNome.setText(model.getNome());
            txtCrm.setText(String.valueOf(model.getCrm()));
            txtSalario.setText(model.getSalario());
            comboBox.setValue(model.getEspecialidade());
        });

        btnExcluir.setOnMouseClicked((MouseEvent e)->{
            /**
             * Ao apertar no botão excluir é gerado um alert de confirmação.
             * Se o usuário escolher a opção 'sim' é executado a exclusão dos dados e é carregada a tabela novamente.
             * Senão, apenas sai da janela de confirmação.
             */
            Alert dialogoExe = new Alert(Alert.AlertType.WARNING);
            dialogoExe.setTitle("Excluindo dados");
            dialogoExe.setHeaderText("Você deseja realmente excluir os dados selecionados?");
            ButtonType btnSim = new ButtonType("Sim");
            ButtonType btnNao = new ButtonType("Não");

            dialogoExe.getButtonTypes().setAll(btnSim, btnNao);
            dialogoExe.showAndWait().ifPresent(escolha -> {
                if (escolha == btnSim) {
                    modelMedicos.setId(Integer.parseInt(txtId.getText()));
                    medicoDao.deletMedico(modelMedicos);
                    desbilitarCampos();
                    btnExcluir.setDisable(true);
                    txtSalario.setText("");
                    txtSalario.setDisable(true);
                    preencherTabela();
                } else if (escolha == btnNao) {
                    System.out.println("Usuário escolheu não");
                }
            });
        });

        tableMedicos.setOnMouseClicked((MouseEvent e)->{
            btnCancelar.setDisable(false);
            ModelMedicos model = tableMedicos.getSelectionModel().getSelectedItem();
            String nome = model.getNome();
            conecta.getConnection();
            conecta.executeSql("SELECT * FROM tbl_medicos where nome_medico='"+nome+"'");
            try {
                conecta.resultSet.first();
                txtNome.setText(conecta.resultSet.getString("nome_medico"));
                comboBox.setValue(conecta.resultSet.getString("especialidade"));
                txtCrm.setText(String.valueOf(conecta.resultSet.getInt("crm")));
                txtSalario.setText(conecta.resultSet.getString("salario"));
                txtId.setText(String.valueOf(conecta.resultSet.getInt("pk_id_medico")));

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            btnEditar.setDisable(false);
            btnExcluir.setDisable(false);
            conecta.closeConnection();
        });
    }


    //Habilita os campos de texto e botões
    public void habilitarCampos() {
        btnCancelar.setDisable(false);
        btnSalvar.setDisable(false);
        txtCrm.setDisable(false);
        txtNome.setDisable(false);
        comboBox.setDisable(false);
    }

    //Desabilita os campos de texto e botões
    public void desbilitarCampos() {
        btnCancelar.setDisable(true);
        btnSalvar.setDisable(true);
        txtPesquisar.setText("");
        txtNome.setText("");
        txtCrm.setText("");
        txtCrm.setDisable(true);
        txtNome.setDisable(true);
        comboBox.setDisable(true);
        btnEditar.setDisable(true);
        txtId.setText("");
    }

    //Preeche tabela
    public void preencherTabela(){
        /**
         * Faz uma conexão com o banco de dados e seta valores nas colunas,
         * de acordo com os atributos declados na classe de modelo, após isso
         * seta os valores em uma tabela a partir de uma lista.
         * Fecha conexão.
         */
        conecta.getConnection();
        cID.setCellValueFactory(new PropertyValueFactory("id"));
        cNome.setCellValueFactory(new PropertyValueFactory("nome"));
        cEspec.setCellValueFactory(new PropertyValueFactory("especialidade"));
        cCrm.setCellValueFactory(new PropertyValueFactory("crm"));
        cSalario.setCellValueFactory(new PropertyValueFactory("salario"));

        tableMedicos.setItems(getList());
        conecta.closeConnection();
    }

    //Cria uma lista de modelMedicos
    public ObservableList<ModelMedicos> getList() {
        /**
         * Cria um método de lista observavel que só recebe dados do tipo ModelMedicos.
         * Retorna uma coleção de arraylist pegando uma lista do MedicoDao.
         */
        MedicoDao medicoDao = new MedicoDao();
        return FXCollections.observableArrayList(medicoDao.getList());
    }

    //Validação de campos
    public void validateFields(){
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(txtCrm, Validator.createEmptyValidator("Campo Obrigatório"));
        validationSupport.registerValidator(txtNome, Validator.createEmptyValidator("Campo Obrigatório"));
        validationSupport.registerValidator(txtSalario, Validator.createEmptyValidator("Campo Obrigatório"));
        validationSupport.registerValidator(comboBox, Validator.createEmptyValidator("Campo Obrigatório"));
    }



    /******************************************************** INSTANÂNCIAS DE CONTÉUDO ********************************************************/
    @FXML private JFXButton btnNovo;
    @FXML private JFXButton btnSalvar;
    @FXML private JFXButton btnCancelar;
    @FXML private JFXButton btnEditar;
    @FXML private JFXButton btnExcluir;
    @FXML private JFXTextField txtNome;
    @FXML private JFXTextField txtSalario;
    @FXML private JFXTextField txtCrm;
    @FXML private JFXTextField txtPesquisar;
    @FXML private JFXButton btnPesquisar;
    @FXML private JFXComboBox comboBox;
    @FXML private FontAwesomeIcon close;
    @FXML private FontAwesomeIcon minimizar;
    @FXML private JFXTextField txtId;
    @FXML private TableView<ModelMedicos> tableMedicos;
    @FXML private TableColumn<ModelMedicos, Integer> cID;
    @FXML private TableColumn<ModelMedicos, String> cNome;
    @FXML private TableColumn<ModelMedicos, String> cEspec;
    @FXML private TableColumn<ModelMedicos, String> cSalario;
    @FXML private TableColumn<ModelMedicos, Integer> cCrm;

    /******************************************************************************************************************************************/
}
