/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.List;

/**
 * @author TimeTraveler
 */
public class Process {
    StackPane Parent = null;

    public void setStack(StackPane Parent) {
        this.Parent = Parent;
    }

    public void StartErrorTypeNoData(List<File> Songs, List<File> Videos, List<File> Directories) {

        DirectoryHandle dh = new DirectoryHandle();
        VBox Layout = new VBox(2);
        JFXDialog dialog = new JFXDialog(Parent, Layout, DialogTransition.CENTER);
        ImageView ErrorImage = new ImageView(new Image((getClass().getResourceAsStream("assets/sad.png"))));
        Label Heading = new Label("It seems to be Lonely Here");
        Heading.setTextFill(Color.WHITE);
        Label Content = new Label("Please Register the Directories which contain media files.");
        Content.setTextFill(Color.WHITE);
        JFXButton Open = new JFXButton("Click Here");
        Heading.setStyle("-fx-font-size: 50;");
        Content.setStyle("-fx-font-size: 20;");
        Layout.getChildren().addAll(ErrorImage, Heading, Content, Open);
        Layout.setAlignment(Pos.CENTER);
        Layout.setStyle("-fx-background-color: #464444;-fx-border-size: 10;-fx-border-color: ORANGE;");
        Layout.setPrefSize(800, 300);
        Open.setOnMouseClicked((e) -> {
            try {
                dh.ChooseDirectory();
                DirectoryHandle.ClearLocation();
                DirectoryHandle.checkDuplicate();
                dh.getStreams(Songs, Videos);
                dh.getDirectories(Directories);
                dialog.close();
            } catch (Exception ex) {
                SetError("Error while Syncing Directories", ex.getMessage());
            }
        });

        dialog.autosize();
        dialog.toFront();
        dialog.show();
    }


    public void SetError(String Head, String Message) {
        JFXDialogLayout Layout = new JFXDialogLayout();
        Label HeadL = new Label(Head);
        HeadL.setTextFill(Color.WHITE);
        Label MessageL = new Label(Message);
        MessageL.setTextFill(Color.WHITE);
        Layout.setHeading(HeadL);
        Layout.setBody(MessageL);
        Layout.setStyle("-fx-background-color: #464444;-fx-border-size: 10;-fx-border-color: ORANGE;");
        JFXDialog dialog = new JFXDialog(Parent, Layout, DialogTransition.CENTER);
        dialog.autosize();
        dialog.toFront();
        dialog.show();
    }

    public void createCoustomDialog(Node n) {
        n.setStyle("-fx-background-color: #464444;-fx-border-size: 10;-fx-border-color: ORANGE;");
        JFXDialog dialog = new JFXDialog(Parent, (Region) n, DialogTransition.CENTER);
        dialog.autosize();
        dialog.toFront();
        dialog.show();
    }
}
