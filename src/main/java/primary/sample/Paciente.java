package primary.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Paciente extends Application {
    public static Stage stage;
    private double x;
    private double y;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        Paciente.stage = stage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ViewPaciente.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Paciente");
        stage.setScene(new Scene(root));
        stage.show();
        setStage(stage);

        /*
          Ao pressionar a tela ->
          Guarda a posição atual X
          Guarda a posição atual Y
         */
        root.setOnMousePressed((event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        }));

        /*
          Ao arrastar a tela ->
          Pega a posição atual X menos a posição armazenada X
          Pega a posição atual Y menos a posição armazenada Y
          Seta na tela a posição X e Y
         */
        root.setOnMouseDragged((event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        }));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
