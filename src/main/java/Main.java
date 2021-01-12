import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        if (Runtime.version().version().get(0) >= 9) {
            try {
                DirectoryHandle.ClearLocation();
                DirectoryHandle.CheckData();
                DirectoryHandle.checkDuplicate();
                PlayListHandle PLH = new PlayListHandle();
                PLH.checkDatabase();
            } catch (Exception ex) {
                ErrorGUI("Error while Talking to DataBase", ex.getMessage());
            }
            try {
                Parent root = FXMLLoader.load(getClass().getResource("fxml/scene.fxml"));
                stage.getIcons().clear();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("assets/Company_Logo.png")));
                stage.setTitle("UmpMax");
                stage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(root);
                new StageResizer(stage, scene);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
            } catch (IOException ex) {
                System.out.println(ex);
                ErrorGUI("Error while loading GUI File", ex.getMessage());
            }
        } else {
            ErrorGUI("Error in JavaRuntime", "Needed JavaRuntime 9.0.0 or above ,got " + Runtime.version().version().get(0));
        }

    }

    private void ErrorGUI(String Head, String Body) {
        VBox LayOut = new VBox(2);
        Label HeadL = new Label(Head);
        Label MessageL = new Label(Body);
        Button bt = new Button("Ok");
        bt.setTextFill(Color.WHITE);
        bt.setStyle("-fx-background-color: transparent;");
        HeadL.setTextFill(Color.WHITE);
        HeadL.setStyle("-fx-font-size: 50;");
        MessageL.setTextFill(Color.WHITE);
        MessageL.setStyle("-fx-font-size: 20;");
        LayOut.getChildren().addAll(new ImageView(new Image(getClass().getResourceAsStream("assets/sad.png"))), HeadL, MessageL, bt);
        LayOut.setAlignment(Pos.CENTER);
        LayOut.setStyle("-fx-background-color: #464444;-fx-border-size: 10;-fx-border-color: ORANGE;");
        LayOut.setPrefSize(800, 300);
        Scene sc = new Scene(LayOut, 800, 300);
        Stage st = new Stage();
        st.setScene(sc);
        st.initStyle(StageStyle.UNDECORATED);
        st.centerOnScreen();
        new StageResizer(st, sc);
        bt.setOnAction((e) -> st.close());
        st.showAndWait();
    }

}
