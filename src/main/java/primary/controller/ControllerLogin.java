package primary.controller;

import com.jfoenix.controls.JFXSpinner;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;
import primary.sample.Login;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import primary.conexao.ConexaoMySql;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import primary.sample.Principal;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class ControllerLogin implements Initializable {

    @FXML
    private JFXButton btnEntrar;
    @FXML
    public TextField txtAcesso;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private FontAwesomeIcon close;
    @FXML
    private FontAwesomeIcon minimizar;
    @FXML
    private Label lblBanco;
    @FXML
    private ImageView imgBanco;
    @FXML
    private JFXCheckBox rememberMe;
    @FXML
    private JFXSpinner spinner;
    @FXML
    private Label lblMsg;
    @FXML
    private AnchorPane anchorPane;

    private Preferences preferences;
    int i;

    private ConexaoMySql conn = new ConexaoMySql();
    private Image bancoOn = new Image(String.valueOf(getClass().getResource("/imagens/icons8-aceitar-banco-de-dados-20.jpg")));
    private Image bancoOff = new Image(String.valueOf(getClass().getResource("/imagens/icons8-excluir-banco-de-dados-20.jpg")));


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn.getConnection();
        preferences = Preferences.userNodeForPackage(ControllerLogin.class);
        if (preferences != null) {
            if (preferences.get("txtAcesso", null) != null && !preferences.get("txtSenha", null).isEmpty()) {
                txtAcesso.setText(preferences.get("txtAcesso", null));
                txtSenha.setText(preferences.get("txtSenha", null));
            }
        }
        /*
          Verifica se o banco está conectado ->
          Se estiver conectado: seta uma mensagem que está conectado e uma imagem;
          Se estiver offline: seta uma mensagem que está offline e uma imagem;
         */
        if (conn.conn != null) {
            lblBanco.setText("Conexão estabelecida");
            imgBanco.setImage(bancoOn);
        } else {
            lblBanco.setText("Conexão não estabelecida");
            imgBanco.setImage(bancoOff);
        }
        //Método para clicar no botão e entrar no sistema
        btnEntrar.setOnMouseClicked((MouseEvent e) -> {
            if (conn.conn == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("Banco de dados offline");
                alert.setContentText("Opa! Parece que o banco de dados está offline!");
                alert.show();
            }
            /*
              Instância das classes -> Conexão e a Janela principal
             */
            Principal viewPrincipal = new Principal();
            try {
                //Verifica se o que o usuário digitou está contido no banco de dados
                conn.executeSql("SELECT * FROM tbl_usuarios where cod_acesso='" + txtAcesso.getText() + "'");
                conn.resultSet.first();
                //Se o que o usuário digitou tiver no banco, verifica se a senha que o usuário digitou é válida
                if (conn.resultSet.getString("senha").equals(txtSenha.getText())) {
                    if (rememberMe.isSelected()) {
                        preferences.put("txtAcesso", txtAcesso.getText());
                        preferences.put("txtSenha", txtSenha.getText());
                        rememberMe.checkedColorProperty();
                    } else {
                        preferences.put("txtAcesso", "");
                        preferences.put("txtSenha", "");
                    }

                    /*
                      Se a senha estiver válida, a janela atual é fechada
                     */
                    spinner.setVisible(true);
                    lblMsg.setVisible(true);
                    Task task = new Task() {
                        @Override
                        protected Integer call() throws Exception {
                            int i;
                            for (i = 0; i < 101; i++) {
                                updateProgress(i, 101);
                                Thread.sleep(100);
                            }
                            return i;
                        }

                        @Override
                        protected void succeeded() {
                            Login.getStage().close();
                            conn.closeConnection();

                            try {
                                new Principal().start(new Stage());
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Information");
                                alert.setHeaderText("Login Efetuado");
                                alert.setContentText("Seja bem-vindo, login efetuado com sucesso!");
                                alert.show();
                            } catch (Exception ex) {
                                Logger.getLogger(ControllerLogin.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        @Override
                        protected void failed() {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Erro no sistema");
                            alert.setContentText("Não foi possível carregar o sistema.");
                            alert.show();
                        }
                    };
                    Thread thread = new Thread(task);
                    thread.setDaemon(true);
                    thread.start();
                    //spinner.progressProperty().bind(task.progressProperty());
                } else {
                    AlertError();
                }
                //Se o usuário não estiver presente no banco de dados -> Mensagem de erro
            } catch (SQLException e1) {
                AlertError();
            }
        });

        //Fecha a janela e encerra o sistema
        close.setOnMouseClicked((MouseEvent event) -> System.exit(0));

        //Minimiza a janela
        minimizar.setOnMouseClicked((MouseEvent e) -> Login.getStage().setIconified(true));
    }

    private void AlertError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Usuário/Senha inválidos");
        alert.setContentText("Usuário ou senha inválidos!");
        alert.show();
    }


}
