package primary.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.concurrent.Task;
import primary.conexao.ConexaoMySql;
import primary.model.ModelUsuario;
import primary.util.Utilidade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import primary.dao.UsuarioDao;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import primary.sample.Usuario;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerUsuarios implements Initializable {

    @FXML
    private JFXComboBox<String> comboTipo;
    @FXML
    private JFXButton btnNovo;
    @FXML
    private JFXButton btnSalvar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXButton btnEditar;
    @FXML
    private JFXButton btnExcluir;
    @FXML
    private JFXTextField txtCod;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtNome;
    @FXML
    private JFXButton btnCod;
    @FXML
    private JFXPasswordField txtCSenha;
    @FXML
    private JFXPasswordField txtSenha;
    @FXML
    private JFXTextField txtPesquisar;
    @FXML
    private JFXButton btnPesquisar;
    @FXML
    private JFXTextField txtID;
    @FXML
    private FontAwesomeIcon close;
    @FXML
    private FontAwesomeIcon minimizar;
    @FXML
    private TableView<ModelUsuario> tblUsers;
    @FXML
    private TableColumn<Integer, ModelUsuario> cID;
    @FXML
    private TableColumn<String, ModelUsuario> cNome;
    @FXML
    private TableColumn<String, ModelUsuario> cEmail;
    @FXML
    private TableColumn<String, ModelUsuario> cCod;
    @FXML
    private TableColumn<String, ModelUsuario> cSenha;

    private Utilidade util = new Utilidade();
    private ModelUsuario modelUsuario = new ModelUsuario();
    private UsuarioDao usuarioDao = new UsuarioDao();
    private ConexaoMySql conex = new ConexaoMySql();
    private int flag = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conex.getConnection();
        preencherComboBox();
        preencherTabela();
        close.setOnMouseClicked((MouseEvent e) -> Usuario.getStage().close());

        minimizar.setOnMouseClicked((MouseEvent e) -> Usuario.getStage().setIconified(true));

        btnNovo.setOnMouseClicked((MouseEvent e) -> {
            flag = 1;
            System.out.println("Flag: " + flag);
            habilitarCampos();
            btnExcluir.setDisable(true);
            btnEditar.setDisable(true);
            txtCSenha.setText("");
            txtEmail.setText("");
            txtNome.setText("");
            txtSenha.setText("");
            txtCod.setText("");
            txtID.setText("");
        });

        btnCancelar.setOnMouseClicked((MouseEvent e) -> {
            desbilitarCampos();
            btnCancelar.setDisable(true);
        });

        btnEditar.setOnMouseClicked((MouseEvent e) -> {
            habilitarCampos();
            btnExcluir.setDisable(true);
            btnNovo.setDisable(true);
            btnEditar.setDisable(true);
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
                    Task task = new Task() {
                        @Override
                        protected Object call() {
                            return null;
                        }

                        @Override
                        protected void succeeded() {
                            modelUsuario.setId_usuario(Integer.parseInt(txtID.getText()));
                            usuarioDao.deleteUsuario(modelUsuario);
                            desbilitarCampos();
                            btnNovo.setDisable(false);
                            preencherTabela();

                        }
                    };
                    Thread thread = new Thread(task);
                    thread.setDaemon(true);
                    thread.start();
                } else if (escolha == btnNao) {
                    System.out.println("Usuário escolheu não");
                }
            });

        });

        btnSalvar.setOnMouseClicked((MouseEvent e) -> {
            if (flag == 1) {
                String cod_acesso = txtCod.getText();
                String senha = txtSenha.getText();
                String nome = txtNome.getText();
                String email = txtEmail.getText();
                /*
                  Verifica se o e-mail que o usuário digitou é válido ->
                  Se for válido ele registra o usuário;
                  Se não for, mostra uma notifição de erro
                 */
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = p.matcher(email);
                boolean matchFound = m.matches();
                if (!matchFound) {
                    Image img = new Image("/imagens/error.png");
                    Notifications not = Notifications.create()
                            .title("Email inválido")
                            .text("Error O e-mail que você forneceu é inválido")
                            .graphic(new ImageView(img))
                            .hideAfter(Duration.seconds(10))
                            .position(Pos.BOTTOM_RIGHT)
                            .onAction(event -> System.out.println("Notificação fechada"));
                    not.hideCloseButton();
                    not.show();
                } else {
                    modelUsuario.setCod_acesso(cod_acesso);
                    modelUsuario.setSenha(senha);
                    modelUsuario.setNome(nome);
                    modelUsuario.setEmail(email);
                    usuarioDao.getUsuario(modelUsuario);
                    desbilitarCampos();
                    btnNovo.setDisable(false);
                    preencherTabela();
                }
            } else {
                String cod_acesso = txtCod.getText();
                String senha = txtSenha.getText();
                String nome = txtNome.getText();
                String email = txtEmail.getText();
                String id = txtID.getText();
                /*
                  Verifica se o e-mail que o usuário digitou é válido ->
                  Se for válido ele edita o usuário;
                  Se não for, mostra uma notifição de erro
                 */
                Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = p.matcher(email);
                boolean matchFound = m.matches();
                if (!matchFound) {
                    Image img = new Image("/imagens/error.png");
                    Notifications not = Notifications.create()
                            .title("Email inválido")
                            .text("Error O e-mail que você forneceu é inválido")
                            .graphic(new ImageView(img))
                            .hideAfter(Duration.seconds(10))
                            .position(Pos.BOTTOM_RIGHT)
                            .onAction(event -> System.out.println("Notificação fechada"));
                    not.hideCloseButton();
                    not.show();
                } else {
                    modelUsuario.setCod_acesso(cod_acesso);
                    modelUsuario.setSenha(senha);
                    modelUsuario.setNome(nome);
                    modelUsuario.setEmail(email);
                    modelUsuario.setId_usuario(Integer.parseInt(id));
                    usuarioDao.editUsuario(modelUsuario);
                    btnNovo.setDisable(false);
                    desbilitarCampos();
                    preencherTabela();
                }
            }
        });

        btnPesquisar.setOnMouseClicked((MouseEvent e) -> {
            btnEditar.setDisable(false);
            btnExcluir.setDisable(false);
            btnNovo.setDisable(false);
            modelUsuario.setPesquisa(txtPesquisar.getText());
            ModelUsuario model = usuarioDao.searchUsuario(modelUsuario);
            txtID.setText(String.valueOf(model.getId_usuario()));
            txtCod.setText(model.getCod_acesso());
            txtSenha.setText(model.getSenha());
            txtCSenha.setText(model.getSenha());
            txtNome.setText(model.getNome());
            txtEmail.setText(model.getEmail());
            btnEditar.setDisable(false);
            btnExcluir.setDisable(false);
        });

        tblUsers.setOnMouseClicked((MouseEvent e) -> {
            btnCancelar.setDisable(false);
            ModelUsuario modelUsuario = tblUsers.getSelectionModel().getSelectedItem();
            String nome = modelUsuario.getNome();
            conex.getConnection();
            conex.executeSql("SELECT * FROM tbl_usuarios where nome='" + nome + "'");
            try {
                conex.resultSet.first();
                txtCod.setText(conex.resultSet.getString("cod_acesso"));
                txtNome.setText(conex.resultSet.getString("nome"));
                txtSenha.setText(conex.resultSet.getString("senha"));
                txtCSenha.setText(conex.resultSet.getString("senha"));
                txtEmail.setText(conex.resultSet.getString("email"));
                txtID.setText(String.valueOf(conex.resultSet.getInt("pk_id_usuario")));
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            btnEditar.setDisable(false);
            btnExcluir.setDisable(false);
            conex.closeConnection();
        });
    }

    @FXML
    void gerarCodigo() {
        String senha = util.gerarSenhaAleatoria();
        txtCod.setText(senha);
    }

    //Habilita os campos de texto e botões
    private void habilitarCampos() {
        btnCancelar.setDisable(false);
        btnSalvar.setDisable(false);
        txtCSenha.setDisable(false);
        txtNome.setDisable(false);
        txtEmail.setDisable(false);
        txtSenha.setDisable(false);
        btnCod.setDisable(false);
    }

    //Desabilita os campos de texto e botões
    private void desbilitarCampos() {
        btnSalvar.setDisable(true);
        btnCancelar.setDisable(true);
        btnEditar.setDisable(true);
        btnExcluir.setDisable(true);
        btnCod.setDisable(true);
        txtCSenha.setDisable(true);
        txtEmail.setDisable(true);
        txtNome.setDisable(true);
        txtSenha.setDisable(true);
        txtCSenha.setText("");
        txtEmail.setText("");
        txtNome.setText("");
        txtSenha.setText("");
        txtCod.setText("");
        txtID.setText("");
    }

    private void preencherTabela() {
        conex.getConnection();
        cID.setCellValueFactory(new PropertyValueFactory("id_usuario"));
        cNome.setCellValueFactory(new PropertyValueFactory("nome"));
        cEmail.setCellValueFactory(new PropertyValueFactory("email"));
        cCod.setCellValueFactory(new PropertyValueFactory("cod_acesso"));
        cSenha.setCellValueFactory(new PropertyValueFactory("senha"));
        tblUsers.setItems(getList());
    }

    private ObservableList<ModelUsuario> getList() {
        UsuarioDao usuarioDao = new UsuarioDao();
        return FXCollections.observableArrayList(usuarioDao.getList());
    }

    private void preencherComboBox() {
        ObservableList<String> observableList = FXCollections.observableArrayList("Administrador", "Médico", "Recepcionista");
        comboTipo.setItems(observableList);
    }
}
