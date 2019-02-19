package primary.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Utilidade {

    //O método mostra uma notificação que o médico foi cadastrado
    public void showPopUpMedico(String medico) {
        Image img = new Image("/imagens/confirm.png");
        Notifications not = Notifications.create()
                .title("Médico cadastrado")
                .text("O médico " + medico + " foi cadastrado com sucesso!")
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

    //O método mostra uma notificação que o médico não foi cadastrado
    public void showPopUpErrorMedico(String medico) {
        Image img = new Image("/imagens/error.png");
        Notifications not = Notifications.create()
                .title("Médico não cadastrado")
                .text("Error O médico " + medico + " não foi cadastrado no sistema!")
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

    //O método mostra uma notificação que o usuario foi cadastrado
    public void showPopUpUsuario(String usuario) {
        Image img = new Image("/imagens/confirm.png");
        Notifications not = Notifications.create()
                .title("Usuário cadastrado")
                .text("O usuário " + usuario + " foi cadastrado com sucesso!")
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

    //O método mostra uma notificação que o usuario não foi cadastrado
    public void showPopUpErrorUsuario(String usuario) {
        Image img = new Image("/imagens/error.png");
        Notifications not = Notifications.create()
                .title("Usuário não cadastrado")
                .text("Error O Usuário " + usuario + " não foi cadastrado no sistema!")
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

    //Gera uma sequência de números de 1-9 aleatório
    public String gerarSenhaAleatoria() {
        int qtdeMaximaCaracteres = 4;
        String[] caracteres = {"1", "2", "3", "4", "10", "6", "7", "8", "9"};

        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < qtdeMaximaCaracteres; i++) {
            int posicao = (int) (Math.random() * caracteres.length);
            senha.append(caracteres[posicao]);
        }
        return senha.toString();
    }

    public void showPopUpEdit() {
        Image img = new Image("/imagens/confirm.png");
        Notifications not = Notifications.create()
                .title("Dados editados")
                .text("Os dados foram alterados com sucesso!")
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

    public void showPopUpErrorEdit() {
        Image img = new Image("/imagens/error.png");
        Notifications not = Notifications.create()
                .title("Dados não foram editados")
                .text("Error Não foi possível alterar os dados!")
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

    public void showPopUpDelet() {
        Image img = new Image("/imagens/confirm.png");
        Notifications not = Notifications.create()
                .title("Dados deletado")
                .text("Os dados foram deletados com sucesso!")
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

    public void showPopUpErrorDelet() {
        Image img = new Image("/imagens/error.png");
        Notifications not = Notifications.create()
                .title("Dados não foram deletados")
                .text("Error Não foi possível deletar os dados!")
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

    //O método mostra uma notificação que o médico foi cadastrado
    public void showPopUpPaciente(String paciente) {
        Image img = new Image("/imagens/confirm.png");
        Notifications not = Notifications.create()
                .title("Paciente cadastrado")
                .text("O paciente " + paciente + " foi cadastrado com sucesso!")
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

    //O método mostra uma notificação que o médico não foi cadastrado
    public void showPopUpErrorPaciente(String paciente) {
        Image img = new Image("/imagens/error.png");
        Notifications not = Notifications.create()
                .title("Paciente não cadastrado")
                .text("Error O paciente " + paciente + " não foi cadastrado no sistema!")
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
