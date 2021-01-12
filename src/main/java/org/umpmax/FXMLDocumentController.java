package org.umpmax;

import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {

    Stage stage = null;
    byte CurrentMode = 0;
    List<File> Directories = new ArrayList();
    List<File> Videos = new ArrayList();
    List<File> Songs = new ArrayList();
    List<String> PlayList = null;
    MusicHandle mh = new MusicHandle();
    PlayListHandle PLH = new PlayListHandle();
    DirectoryHandle dh = new DirectoryHandle();
    Process error = new Process();
    Media CurrentPlayingMedia = null;
    int ReValue = 0;
    int SettingsSelected = -1;
    String KeyOb = null;
    JFXButton open = null;
    JFXButton ok = null;
    JFXListView<String> CollectionList = new JFXListView();
    JFXListView<String> RecentList = new JFXListView();
    JFXListView<String> PlayListSongView = new JFXListView();
    JFXListView<String> QueList = new JFXListView();
    JFXListView SettingsContentList = new JFXListView();
    List<File> list = null;
    @FXML
    private JFXButton MinimizeBt;
    @FXML
    private JFXButton ExitBt;
    @FXML
    private JFXButton MainPlaylistBt;
    @FXML
    private JFXButton MainCollectionBt;
    @FXML
    private JFXButton MainSettingsBt;
    @FXML
    private JFXButton PlayerShuffle;
    @FXML
    private JFXButton PlayerLast;
    @FXML
    private JFXButton PlayerPlay;
    @FXML
    private JFXButton PlayerNext;
    @FXML
    private JFXButton PlayerRepeat;
    @FXML
    private JFXSlider PlayerSeek;
    @FXML
    private Label SongName;
    @FXML
    private Label CurrentModeLb;
    @FXML
    private StackPane AllContentStack;
    @FXML
    private VBox PlayerContainer;
    @FXML
    private HBox VisualContainer;
    @FXML
    private JFXButton BoostBt;
    @FXML
    private HBox TitleBar;
    @FXML
    private HBox ControlBar;
    @FXML
    private HBox DataContainer;
    @FXML
    private VBox FatherContainer;
    @FXML
    private JFXButton SearchBt;
    @FXML
    private Label infoactlb;
    @FXML
    private JFXButton EquaBt;
    @FXML
    private HBox FunctionVBox;
    @FXML
    private JFXListView<String> SubContentList;
    @FXML
    private Label companyLogo;
    @FXML
    private StackPane mainStack;
    @FXML
    private HBox InfoBox;
    @FXML
    private ImageView albumImg;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageView Logo = new ImageView(new Image(getClass().getResourceAsStream("assets/Company_Logo.png")));
        Logo.setFitHeight(35);
        Logo.setFitWidth(35);
        companyLogo.setGraphic(Logo);
        companyLogo.setOnMouseClicked((cla) -> {
            VBox vb = new VBox(15);
            vb.setAlignment(Pos.CENTER);
            ImageView i1 = new ImageView(new Image(getClass().getResourceAsStream("assets/Company_Logo.png")));
            Label lb = new Label("This product has been discontinued.\nDeveloper: Aniket Prajapati (prajapati.ani306@gmail.com)\nGithub: https://github.com/aniketfuryrocks/UmpMax");
            lb.setStyle("-fx-font-size: 18;");
            lb.setTextFill(Color.WHITE);
            vb.getChildren().addAll(i1, lb);
            vb.setPrefSize(800, 540);
            error.createCoustomDialog(vb);
        });

        error.setStack(mainStack);
        SetTitleBarControl();
        Decorate();
        StartHandle();
        SetMediaPlayerData();

        Platform.runLater(() -> {
            Node n = this.MinimizeBt;
            stage = (Stage) n.getScene().getWindow();
            mh.SetScene((Stage) n.getScene().getWindow());
            stage.setOnCloseRequest((e) -> {
                if (mh.getMediaPlayer() != null) {
                    mh.Stop();
                    mh.dispose();
                }
                System.exit(0);
            });
        });

        //Song name label Action end
        MainCollectionBt.setOnAction((e) -> {
            CurrentMode = 1;
            CollectionControl();
        });
        MainPlaylistBt.setOnAction((e) -> {
            CurrentMode = 2;
            PlayListAct();
        });

        MainSettingsBt.setOnAction((e) -> {
            CurrentMode = 3;
            MainSettingsControl();
        });
    }

    private void Decorate() {
        ImageView MainMediaView = new ImageView(new Image(getClass().getResourceAsStream("assets/PlayList.png")));
        MainMediaView.setFitHeight(20);
        MainMediaView.setFitWidth(20);
        MainPlaylistBt.setGraphic(MainMediaView);
        ImageView MainMusicView = new ImageView(new Image(getClass().getResourceAsStream("assets/audio.png")));
        MainMusicView.setFitHeight(20);
        MainMusicView.setFitWidth(20);
        MainCollectionBt.setGraphic(MainMusicView);
        ImageView MainSettingsView = new ImageView(new Image(getClass().getResourceAsStream("assets/settings.png")));
        MainSettingsView.setFitHeight(20);
        MainSettingsView.setFitWidth(20);
        MainSettingsBt.setGraphic(MainSettingsView);
        ImageView ExitView = new ImageView(new Image(getClass().getResourceAsStream("assets/exit.png")));
        ExitView.setFitHeight(20);
        ExitView.setFitWidth(20);
        ExitBt.setGraphic(ExitView);
        ImageView BoostView = new ImageView(new Image(getClass().getResourceAsStream("assets/boost.png")));
        BoostView.setFitHeight(20);
        BoostView.setFitWidth(20);
        BoostBt.setGraphic(BoostView);
        ImageView MinimizeView = new ImageView(new Image(getClass().getResourceAsStream("assets/minimize.png")));
        MinimizeView.setFitHeight(20);
        MinimizeView.setFitWidth(20);
        MinimizeBt.setGraphic(MinimizeView);
        ImageView PlayerPlayView = new ImageView(new Image(getClass().getResourceAsStream("assets/Play.png")));
        PlayerPlayView.setFitHeight(20);
        PlayerPlayView.setFitWidth(20);
        PlayerPlay.setGraphic(PlayerPlayView);
        ImageView NextView = new ImageView(new Image(getClass().getResourceAsStream("assets/next.png")));
        NextView.setFitHeight(20);
        NextView.setFitWidth(20);
        PlayerNext.setGraphic(NextView);
        ImageView BackView = new ImageView(new Image(getClass().getResourceAsStream("assets/back.png")));
        BackView.setFitHeight(20);
        BackView.setFitWidth(20);
        PlayerLast.setGraphic(BackView);
        ImageView RepeatView = new ImageView(new Image(getClass().getResourceAsStream("assets/repeat.png")));
        RepeatView.setFitHeight(20);
        RepeatView.setFitWidth(20);
        PlayerRepeat.setGraphic(RepeatView);
        ImageView ShuffleView = new ImageView(new Image(getClass().getResourceAsStream("assets/shuffle.png")));
        ShuffleView.setFitHeight(20);
        ShuffleView.setFitWidth(20);
        PlayerShuffle.setGraphic(ShuffleView);
        ImageView EquaView = new ImageView(new Image(getClass().getResourceAsStream("assets/equalizerIm.png")));
        EquaView.setFitHeight(20);
        EquaView.setFitWidth(20);

        EquaBt.setGraphic(EquaView);
        EquaBt.setDisable(true);
    }

    private void SetTitleBarControl() {

        ExitBt.setOnAction((e) -> {
            if (mh.getMediaPlayer() != null) {
                mh.Stop();
                mh.dispose();
            }
            System.exit(0);
        });
        MinimizeBt.setOnAction((e) -> stage.setIconified(true));
        BoostBt.setOnAction((e) -> {
            System.gc();
            FadeTransition ft = new FadeTransition();
            ft.setFromValue(0);
            ft.setToValue(100);
            ft.setDuration(Duration.seconds(1));
            ft.setAutoReverse(true);
            ft.setNode(infoactlb);
            infoactlb.setText("Boosted");
            infoactlb.setVisible(true);
            ft.playFromStart();
            ft.setOnFinished((jj) -> infoactlb.setVisible(false));
        });
        PlayerShuffle.setOnAction((e) -> {
            if (mh.isShuffling()) {
                mh.Shuffle(false);
                PlayerShuffle.setStyle("-fx-background-color: TRANSPARENT;");
            } else {
                mh.Shuffle(true);
                mh.Repeat(false);
                PlayerShuffle.setStyle("-fx-background-color: #e79523;");
                PlayerRepeat.setStyle("-fx-background-color: TRANSPARENT;");
            }
        });
        PlayerRepeat.setOnAction((e) -> {
            if (mh.isRepeating()) {
                mh.Repeat(false);
                PlayerRepeat.setStyle("-fx-background-color: TRANSPARENT;");
            } else {
                mh.Repeat(true);
                mh.Shuffle(false);
                PlayerRepeat.setStyle("-fx-background-color: #e79523;");
                PlayerShuffle.setStyle("-fx-background-color: TRANSPARENT;");
            }
        });
        PlayerLast.setOnMouseClicked((e) -> {
            if (RecentList.getItems().size() > 0) {
                if (ReValue == -1)
                    ReValue = RecentList.getItems().size() - 1;

                String ToPlay;
                if (RecentList.getItems().size() == 1) {
                    ToPlay = RecentList.getItems().get(0);
                } else {
                    ToPlay = RecentList.getItems().get(ReValue--);
                }
                for (int i = 0; i < Songs.size(); i++) {
                    if (Songs.get(i).getName().equals(ToPlay)) {
                        list = Songs;
                        break;
                    } else {
                        list = Videos;
                    }
                }
                if (list == Songs) {
                    for (int i = 0; i < Songs.size(); i++) {
                        if (ToPlay.equals(Songs.get(i).getName())) {
                            CurrentPlayingMedia = new Media(Songs.get(i).toURI().toString());
                            try {
                                mh.Stop();
                                mh.dispose();
                                mh.setMedia(CurrentPlayingMedia, ToPlay);
                                mh.StartMusicVisual();


                            } catch (Exception ex) {
                                if (QueList.getItems().contains(ToPlay)) {
                                    mh.RemoveFromQue(Songs.get(i));
                                }
                                Songs.remove(i);
                                error.SetError("Error while Playing Media", ex.getMessage());
                            }
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < Videos.size(); i++) {
                        if (ToPlay.equals(Videos.get(i).getName())) {
                            CurrentPlayingMedia = new Media(Videos.get(i).toURI().toString());
                            try {
                                mh.Stop();
                                mh.dispose();
                                mh.setMedia(CurrentPlayingMedia, ToPlay);
                                mh.StartVideo();
                            } catch (Exception ex) {
                                if (QueList.getItems().contains(ToPlay)) {
                                    mh.RemoveFromQue(Videos.get(i));
                                }
                                Videos.remove(i);
                                error.SetError("Error while Playing Media", ex.getMessage());
                            }
                            break;
                        }
                    }
                }
            }

        });
        PlayerNext.setOnAction((e) -> {
            if (CurrentPlayingMedia != null) {
                Runnable t = mh.getMediaPlayer().getOnEndOfMedia();
                t.run();
                CurrentPlayingMedia = mh.getMediaPlayer().getMedia();
            }
        });
    }

    private void CollectionControl() {
        SearchBt.setVisible(false);
        CurrentModeLb.setText("Collection");
        SubContentList.getItems().clear();
        SubContentList.getItems().addAll("Music Collection", "Video Collection", "Recent Played", "Queue");
        AllContentStack.getChildren().clear();
        open = null;
        ok = null;
        CollectionBtAct();
    }

    private void MainSettingsControl() {
        SearchBt.setVisible(false);
        CurrentModeLb.setText("Settings");
        SettingsContentList.getItems().clear();
        SubContentList.getItems().clear();
        AllContentStack.getChildren().clear();
        AllContentStack.getChildren().add(SettingsContentList);
        SubContentList.getItems().addAll("Update Data", "Manage Directories", "Manage Media", "Manage Visualizer");
        SubContentList.setOnMouseClicked((e) -> {
            SettingsSelected = SubContentList.getSelectionModel().getSelectedIndex();
            if (SettingsSelected != -1) {
                if (SettingsSelected == 0) {
                    SettingsContentList.getItems().clear();
                    SettingsContentList.getItems().addAll("Update Data", "Update PlayList", "Update Queue");
                }
                if (SettingsSelected == 1) {
                    SettingsContentList.getItems().clear();
                    for (int i = 0; i < Directories.size(); i++) {
                        SettingsContentList.getItems().add(Directories.get(i).getAbsolutePath());
                    }
                    SettingsContentList.getItems().add("  Add Directory");
                }
                if (SettingsSelected == 2) {
                    if (CurrentPlayingMedia != null) {
                        JFXToggleButton toggle = new JFXToggleButton();
                        toggle.setText("umpmax.Equalizer");
                        toggle.setTextFill(Color.WHITE);
                        toggle.setMouseTransparent(true);
                        toggle.selectedProperty().set(mh.isEqEnabled());

                        VBox vb = new VBox(4);
                        Label lb = new Label("Balance");
                        JFXSlider balance = new JFXSlider();
                        vb.getChildren().addAll(lb, balance);
                        balance.setValue(mh.getBalance());
                        balance.setOrientation(Orientation.HORIZONTAL);
                        balance.setMin(-1);
                        balance.setMax(1);
                        balance.setOnMouseDragged((ba) -> {
                            mh.setBalance(balance.getValue());
                        });
                        VBox VolumeVb = new VBox(4);
                        Label Volumelb = new Label("Volume");
                        JFXSlider Volume = new JFXSlider();
                        Volume.setValue(mh.getVolume());
                        Volume.setOrientation(Orientation.HORIZONTAL);
                        Volume.setMin(0);
                        Volume.setMax(1);
                        Volume.setOnMouseDragged((ba) -> {
                            mh.setVolume(Volume.getValue());
                        });

                        VolumeVb.getChildren().addAll(Volumelb, Volume);
                        SettingsContentList.getItems().clear();
                        SettingsContentList.getItems().addAll(toggle, vb, VolumeVb, new Label("BufferProgressTime : " + mh.getBufferProgressTime() + " sec"), "Show Metadata", "Show BookMarks");

                    } else {
                        error.SetError("Notification", "These settings are not accessible right now");
                    }

                }
                if (SettingsSelected == 3) {
                    if (CurrentPlayingMedia != null) {
                        JFXToggleButton toggle = new JFXToggleButton();
                        toggle.setText("Detach to newWindows");
                        toggle.setTextFill(Color.WHITE);
                        toggle.setMouseTransparent(true);
                        toggle.selectedProperty().set(mh.isVisualizerDetahed());
                        SettingsContentList.getItems().clear();
                        SettingsContentList.getItems().addAll(toggle);
                    } else {
                        error.SetError("Notification", "These settings are not accessible right now");
                    }
                }
            }
        });
        SettingsContentList.setOnMouseClicked((e) -> {
            int selectedint = SettingsContentList.getSelectionModel().getSelectedIndex();
            if (selectedint != -1) {
                if (SettingsSelected == 0) {
                    if (e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY) {
                        switch (selectedint) {
                            case 0:
                                try {
                                    System.out.println("case 0");
                                    DirectoryHandle.ClearLocation();
                                    DirectoryHandle.CheckData();
                                    DirectoryHandle.checkDuplicate();
                                    Directories.clear();
                                    Songs.clear();
                                    Videos.clear();
                                    dh.getDirectories(Directories);
                                    dh.getStreams(Songs, Videos);
                                } catch (Exception ex) {
                                    error.SetError("Error while UpDating Collection", ex.getMessage());
                                }
                                break;

                            case 1:
                                PlayList.clear();
                                PlayList.addAll(PLH.getPlayLists());
                                break;
                            case 2:
                                List<File> TempQue = mh.getQue();
                                QueList.getItems().clear();
                                for (int i = 0; i < TempQue.size(); i++) {
                                    QueList.getItems().add(TempQue.get(i).getName());
                                }
                                break;
                        }
                    }
                }
                if (SettingsSelected == 1) {
                    if (selectedint == SettingsContentList.getItems().size() - 1 && e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY) {
                        try {
                            dh.ChooseDirectory();
                            DirectoryHandle.ClearLocation();
                            DirectoryHandle.CheckData();
                            DirectoryHandle.checkDuplicate();
                            dh.getDirectories(Directories);
                            Songs.clear();
                            Videos.clear();
                            dh.getStreams(Songs, Videos);
                            SettingsContentList.getItems().clear();
                            for (int i = 0; i < Directories.size(); i++) {
                                SettingsContentList.getItems().add(Directories.get(i).getAbsolutePath());
                            }
                            SettingsContentList.getItems().add("  Add Directory");
                        } catch (Exception ex) {
                            error.SetError("Error while Adding Directory", ex.getMessage());
                        }
                    } else if (e.isPopupTrigger()) {
                        ContextMenu m = new ContextMenu();
                        MenuItem m1 = new MenuItem("Remove Directory");
                        m1.setOnAction((m1a) -> {
                            try {
                                DirectoryHandle.RemoveDirectory((String) SettingsContentList.getItems().get(selectedint));
                                Directories.clear();
                                Songs.clear();
                                Videos.clear();
                                dh.getStreams(Songs, Videos);
                                dh.getDirectories(Directories);
                                SettingsContentList.getItems().clear();
                                for (int i = 0; i < Directories.size(); i++) {
                                    SettingsContentList.getItems().add(Directories.get(i).getAbsolutePath());
                                }
                                SettingsContentList.getItems().add("  Add Directory");
                            } catch (Exception ex) {
                                error.SetError("Error while Removing Directory", ex.getMessage());
                            }

                        });
                        m.getItems().add(m1);
                        m.setX(e.getScreenX());
                        m.setY(e.getScreenY());
                        m.show(((Node) e.getSource()).getScene().getWindow());

                    }

                }
                if (SettingsSelected == 2) {

                    if (e.getClickCount() == 1 && e.getButton() == MouseButton.PRIMARY) {
                        switch (selectedint) {
                            case 0:
                                JFXToggleButton toggle = (JFXToggleButton) SettingsContentList.getItems().get(0);
                                if (toggle.selectedProperty().get()) {
                                    toggle.selectedProperty().set(false);
                                    mh.EnableEq(false);
                                } else {
                                    mh.EnableEq(true);
                                    toggle.selectedProperty().set(true);
                                }
                                break;
                            case 4:
                                ObservableMap<String, Object> metadata = mh.getMetadata();
                                Object[] key = metadata.keySet().toArray();
                                JFXListView TempMetadata = new JFXListView();
                                for (int i = 0; i < key.length; i++) {
                                    if (key[i].equals("image")) {
                                        ImageView TempImage = new ImageView();
                                        TempImage.setImage((Image) metadata.get("image"));
                                        Label lb = new Label();
                                        TempImage.setFitHeight(250);
                                        TempImage.setFitWidth(250);
                                        lb.setGraphic(TempImage);
                                        TempMetadata.getItems().add(lb);
                                    } else
                                        TempMetadata.getItems().add(key[i] + " : " + metadata.get(key[i]));
                                }
                                TempMetadata.setPrefSize(600, 500);
                                error.createCoustomDialog(TempMetadata);
                                break;
                            case 5:
                                ObservableMap<String, Duration> markers = mh.getMarkers();
                                Object[] set = markers.keySet().toArray();
                                JFXListView TempMarker = new JFXListView();
                                for (int i = 0; i < markers.size(); i++) {
                                    TempMarker.getItems().add(set[i].toString() + " : " + markers.get(set[i]));
                                }

                                TempMarker.setPrefSize(600, 500);
                                error.createCoustomDialog(TempMarker);
                                break;
                        }
                    }
                }
                if (SettingsSelected == 3) {
                    switch (selectedint) {
                        case 0:
                            JFXToggleButton toggle = (JFXToggleButton) SettingsContentList.getItems().get(0);
                            if (toggle.selectedProperty().get()) {
                                toggle.selectedProperty().set(false);
                                mh.UnDetachVisualizer();

                            } else {
                                toggle.selectedProperty().set(true);
                                mh.DetachVisualizer();
                            }
                            break;
                    }
                }
            }
        });
    }

    private void StartHandle() {
        try {
            dh.getDirectories(Directories);
            dh.getStreams(Songs, Videos);
            PlayList = PLH.getPlayLists();
            if (Directories.isEmpty()) {
                error.StartErrorTypeNoData(Songs, Videos, Directories);
            } else {
                CurrentMode = 1;
                CollectionControl();
                CurrentMode = 11;
                if (Songs.size() > 0) {
                    CollectionList.getItems().clear();
                    for (int i = 0; i < Songs.size(); i++) {
                        CollectionList.getItems().add(Songs.get(i).getName());
                    }
                    CollectionAct(CollectionList);
                    CurrentModeLb.setText("Music");
                } else {
                    error.StartErrorTypeNoData(Songs, Videos, Directories);
                }
            }
        } catch (Exception ex) {
            try {
                DirectoryHandle.ClearLocation();
                DirectoryHandle.CheckData();
                DirectoryHandle.checkDuplicate();
                PLH.checkDatabase();
            } catch (Exception ex1) {
                error.SetError("Error While Handling Database", ex.getMessage());
            }

        }


    }

    private void CollectionBtAct() {
        SubContentList.setOnMouseClicked((e) -> {
            String selection = SubContentList.getSelectionModel().getSelectedItem();
            if (selection != null) {

                if (selection.equals("Music Collection")) {
                    CurrentMode = 11;
                    if (Songs.size() > 0) {
                        CollectionList.getItems().clear();
                        for (int i = 0; i < Songs.size(); i++) {
                            CollectionList.getItems().add(Songs.get(i).getName());
                        }
                        CollectionAct(CollectionList);
                        CurrentModeLb.setText("Music");
                    } else {
                        error.StartErrorTypeNoData(Songs, Videos, Directories);
                    }
                }
                if (selection.equals("Video Collection")) {
                    CurrentMode = 12;
                    if (Videos.size() > 0) {
                        CollectionList.getItems().clear();
                        for (int i = 0; i < Videos.size(); i++) {
                            CollectionList.getItems().add(Videos.get(i).getName());
                        }
                        CollectionAct(CollectionList);
                        CurrentModeLb.setText("Video");
                    } else {
                        error.StartErrorTypeNoData(Songs, Videos, Directories);
                    }
                }
                if (selection.equals("Recent Played")) {
                    CurrentMode = 13;
                    RecentAct();
                    CurrentModeLb.setText("Recent");
                }
                if (selection.equals("Queue")) {
                    if (e.isPopupTrigger()) {
                        ContextMenu menu = new ContextMenu();
                        MenuItem m1 = new MenuItem("Add MusicCollection");
                        MenuItem m2 = new MenuItem("Add VideoCollection");
                        MenuItem m3 = new MenuItem("Clear Queue");
                        EventHandler<ActionEvent> mAction = ((ActionEvent ma) -> {
                            if (ma.getSource() == m1) {
                                for (int i = 0; i < Songs.size(); i++) {
                                    File f = Songs.get(i);
                                    if (!mh.getQue().contains(f)) {
                                        mh.AddToQue(f);
                                    }
                                }
                            } else if (ma.getSource() == m2) {
                                for (int i = 0; i < Videos.size(); i++) {
                                    File f = Videos.get(i);
                                    if (!mh.getQue().contains(f)) {
                                        mh.AddToQue(f);
                                    }
                                }
                            } else {
                                mh.ClearQue();
                            }
                        });
                        menu.getItems().addAll(m1, m2, m3);
                        m1.setOnAction(mAction);
                        m2.setOnAction(mAction);
                        m3.setOnAction(mAction);
                        menu.setX(e.getScreenX());
                        menu.setY(e.getScreenY());
                        menu.show(stage);
                    } else {
                        CurrentMode = 14;
                        QueAct();
                        CurrentModeLb.setText("Queue");
                    }
                }


            }


        });


    }

    private void QueAct() {
        CollectionAct(QueList);
    }

    private void PlayListAct() {
        CurrentModeLb.setText("PlayList");
        SubContentList.getItems().clear();
        SubContentList.getItems().addAll(PlayList);
        AllContentStack.getChildren().clear();
        AllContentStack.getChildren().add(PlayListSongView);
        PlayListSongView.getItems().clear();
        open = null;
        ok = null;
        SearchBt.setVisible(true);
        SearchBt.setText("+");
        SearchBt.setOnAction((e) -> {


            FunctionVBox.getChildren().clear();
            JFXTextField PlayField = new JFXTextField();
            PlayField.setPromptText("PlayList Name");
            PlayField.setUnFocusColor(Color.WHITE);
            PlayField.setFocusColor(Color.ORANGE);
            PlayField.setStyle("-fx-text-fill: white;");
            PlayField.setLabelFloat(true);
            FunctionVBox.getChildren().add(PlayField);
            PlayField.setOnAction((pa) -> {
                PLH.createNewPlayList(PlayField.getText());
                FunctionVBox.getChildren().clear();
                FunctionVBox.getChildren().addAll(CurrentModeLb, SearchBt);
                PlayList = null;
                PlayList = PLH.getPlayLists();
                SubContentList.getItems().clear();
                SubContentList.getItems().addAll(PlayList);
            });


        });
        SubContentList.setOnMouseClicked((sp) -> {
            String selected = SubContentList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    PlayListSongView.getItems().clear();
                    PlayListSongView.getItems().addAll(PLH.getPlayListSongs(selected));
                } catch (Exception ex) {
                    System.out.println("ex " + ex);
                }
                CollectionAct(PlayListSongView);
                if (sp.isPopupTrigger()) {
                    ContextMenu cb = new ContextMenu();
                    MenuItem m1 = new MenuItem("Delete PlayList");
                    MenuItem m2 = new MenuItem("Add to Que");
                    cb.getItems().addAll(m1, m2);
                    cb.setX(sp.getScreenX());
                    cb.setY(sp.getScreenY());
                    cb.show(((Node) sp.getSource()).getScene().getWindow());
                    m1.setOnAction((m1e) -> {
                        PLH.DeletePlayList(selected);
                        PlayList = null;
                        PlayList = PLH.getPlayLists();
                        SubContentList.getItems().clear();
                        SubContentList.getItems().addAll(PlayList);
                        PlayListSongView.getItems().clear();
                    });
                    m2.setOnAction((m1e) -> {

                        List<String> TempPlay = PLH.getPlayListSongs(selected);
                        for (int i = 0; i < Songs.size(); i++) {
                            for (int y = 0; y < TempPlay.size(); y++) {
                                String comp = TempPlay.get(y);
                                File Son = Songs.get(i);
                                if (Son.getName().equals(comp)) {
                                    if (!mh.getQue().contains(Son)) {
                                        mh.AddToQue(Son);
                                    }
                                }

                            }
                        }
                        for (int i = 0; i < Videos.size(); i++) {
                            for (int y = 0; y < TempPlay.size(); y++) {
                                if (Videos.get(i).getName().equals(TempPlay.get(y))) {
                                    String comp = TempPlay.get(y);
                                    File Vid = Videos.get(i);
                                    if (Vid.getName().equals(comp)) {
                                        if (!mh.getQue().contains(Vid)) {
                                            mh.AddToQue(Vid);
                                        }
                                    }
                                }

                            }
                        }
                    });


                }

            }


        });


    }

    private void RecentAct() {
        AllContentStack.getChildren().clear();
        AllContentStack.getChildren().add(RecentList);
        CollectionAct(RecentList);
    }

    private void CollectionAct(JFXListView<String> ListView) {
        list = null;
        System.out.println("Current Selection " + CurrentMode);
        AllContentStack.getChildren().clear();
        AllContentStack.getChildren().add(ListView);
        ListView.setOnMouseClicked((e) -> {
            String selected = ListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                for (int i = 0; i < Songs.size(); i++) {
                    if (Songs.get(i).getName().equals(selected)) {
                        list = Songs;
                        break;
                    }
                }
                if (list == null) {
                    for (int i = 0; i < Videos.size(); i++) {
                        if (Videos.get(i).getName().equals(selected)) {
                            list = Videos;
                            break;
                        }
                    }
                }

                if (e.getClickCount() == 2 && e.getButton() == MouseButton.PRIMARY) {
                    for (int i = 0; i < list.size(); i++) {
                        if (selected.equals(list.get(i).getName())) {
                            try {
                                CurrentPlayingMedia = new Media(list.get(i).toURI().toString());
                                mh.Stop();
                                mh.dispose();
                                mh.setMedia(CurrentPlayingMedia, selected);

                                mh.getMediaPlayer().setOnError(() -> {
                                    error.SetError("Error while Playing Media", mh.getMediaPlayer().getError().getMessage());
                                });
                                if (list == Songs)
                                    mh.StartMusicVisual();
                                else
                                    mh.StartVideo();


                            } catch (Exception ex) {

                                if (mh.getQue().contains(list.get(i))) {
                                    mh.RemoveFromQue(list.get(i));
                                }
                                list.remove(i);
                                ListView.getItems().remove(i);
                                error.SetError("Error while Playing Media", ex.getMessage());
                                System.out.println(ex);
                                System.out.println(ex.getClass());
                            }
                            break;

                        }
                    }
                }
                if (e.isPopupTrigger()) {

                    System.out.println(CurrentMode + " Popup");
                    ContextMenu m = new ContextMenu();

                    if (CurrentMode == 14) {

                        MenuItem m1 = new MenuItem();
                        m1.setText("Remove from Que");
                        m1.setOnAction((m1e) -> {
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getName().equals(ListView.getSelectionModel().getSelectedItem())) {
                                    mh.RemoveFromQue(list.get(i));
                                    break;
                                }
                            }

                        });
                        m.getItems().add(m1);
                    } else {
                        MenuItem m1 = new MenuItem("Add to Que");
                        MenuItem[] m2 = new MenuItem[PlayList.size()];
                        Menu Pmenu = new Menu("Add to Playlist");
                        EventHandler<ActionEvent> eee = (ActionEvent e1) -> {
                            for (int i = 0; i < m2.length; i++) {
                                if (e1.getSource().equals(m2[i])) {
                                    try {
                                        String sel = ListView.getSelectionModel().getSelectedItem();
                                        if (!PLH.getPlayListSongs(PlayList.get(i)).contains(sel))
                                            PLH.WriteToPlayList(PlayList.get(i), sel);

                                    } catch (IOException ex) {
                                        error.SetError("Error while Writing to PlayList", ex.getMessage());
                                    }

                                }
                            }
                        };


                        for (int i = 0; i < PlayList.size(); i++) {
                            m2[i] = new MenuItem(PlayList.get(i));
                            Pmenu.getItems().add(m2[i]);
                            m2[i].setOnAction(eee);

                        }


                        m1.setOnAction((m1e) -> {
                            if (!QueList.getItems().contains(selected)) {
                                mh.AddToQue(list.get(ListView.getSelectionModel().getSelectedIndex()));
                            }
                        });
                        m.getItems().addAll(m1, Pmenu);
                        if (CurrentMode == 2) {
                            MenuItem m3 = new MenuItem("Remove");
                            m3.setOnAction((m3a) -> {
                                String select = SubContentList.getSelectionModel().getSelectedItem();
                                PLH.DeletePlayListSong(select, selected);
                                PlayListSongView.getItems().clear();
                                try {
                                    PlayListSongView.getItems().addAll(PLH.getPlayListSongs(select));
                                } catch (Exception ex) {
                                    error.SetError("Error while removing PlayList Song", ex.getMessage());
                                }
                            });

                            m.getItems().add(m3);
                        }
                    }
                    m.setX(e.getScreenX());
                    m.setY(e.getScreenY());
                    m.show(stage);
                }
            }

        });

        ListView.setOnKeyPressed((e) -> {
            String selected = ListView.getSelectionModel().getSelectedItem();
            if (e.getCode() == KeyCode.ENTER) {
                if (selected != null) {
                    for (int i = 0; i < Songs.size(); i++) {
                        if (Songs.get(i).getName().equals(selected)) {
                            list = Songs;
                            break;
                        } else {
                            list = Videos;
                        }
                    }

                    for (int i = 0; i < list.size(); i++) {
                        if (selected.equals(list.get(i).getName())) {
                            try {

                                CurrentPlayingMedia = new Media(list.get(i).toURI().toString());
                                mh.Stop();
                                mh.dispose();
                                mh.setMedia(CurrentPlayingMedia, selected);

                                mh.getMediaPlayer().setOnError(() -> {
                                    error.SetError("Error while Playing Media", mh.getMediaPlayer().getError().getMessage());
                                });
                                if (list.equals(Songs))
                                    mh.StartMusicVisual();
                                else
                                    mh.StartVideo();


                                break;
                            } catch (Exception ex) {
                                if (mh.getQue().contains(list.get(i))) {
                                    mh.RemoveFromQue(list.get(i));
                                }
                                list.remove(i);
                                ListView.getItems().remove(i);
                                error.SetError("Error while Playing Media", ex.getMessage());
                            }
                            break;

                        }
                    }
                }
            }
        });


    }

    private void SetMediaPlayerData() {
        mh.setNodes(PlayerSeek, PlayerPlay, SongName, InfoBox, VisualContainer, PlayerContainer, TitleBar, ControlBar, FatherContainer, infoactlb, EquaBt, albumImg, SongName, DataContainer);
        mh.Recent(RecentList);
        mh.QueList(QueList);
        mh.setPane(mainStack);
    }

}
