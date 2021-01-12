import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicHandle extends Thread {

    MediaPlayer mp;
    MediaView mv;
    JFXSlider Seeker = new JFXSlider();
    JFXButton PlayBt;
    Label NameLb;
    String SongName;
    HBox VisualContainer;
    Thread t1 = new Thread();
    Thread t2 = new Thread();
    HBox TitleBar;
    HBox ControlBar;
    VBox FatherContainer;
    Stage stage;
    HBox InfoBox;
    VBox PlayerContainer;
    Stage st = null;
    ImageView PlayerPauseView = new ImageView(new Image(getClass().getResourceAsStream("assets/Pause.png")));
    ImageView PlayerPlayView = new ImageView(new Image(getClass().getResourceAsStream("assets/Play.png")));
    Label InfoLb = null;
    Rectangle[] rect = null;
    ScaleTransition[] sct = null;
    Equalizer eq = new Equalizer();
    JFXButton EqBt = null;
    Boolean EqP = false;
    List<File> Que = new ArrayList();
    Boolean rep = false;
    Boolean shuff = false;
    JFXListView<String> recent;
    ListView<String> QueList;
    ImageView AlbumImg = null;
    Stage Vst = null;
    Label SongNameLb;
    double hie = 585.0;
    double wid = 1050.0;
    double stx = 311.0;
    double sty = 83.0;
    int r = 0;
    HBox DataContainer;
    //Media Values
    double balance = 0.0;
    double volume = 200.0;
    double progressTime = 60.0;
    boolean Mute = false;
    //VisualizerValue
    Color RectColor = Color.YELLOWGREEN;
    boolean detached = false;
    boolean videoPlay = false;

    public MusicHandle() {
        Thread.currentThread().setPriority(MAX_PRIORITY);
    }

    public void setMedia(Media media, String SongName) throws Exception {
        this.SongName = SongName;

        if (!recent.getItems().contains(SongName))
            recent.getItems().add(SongName);


        mp = new MediaPlayer(media);
        mp.setOnReady(() -> {
            play();
            StartNodes();
            eq.StartEq(mp);
            if (EqP) {
                mp.getAudioEqualizer().setEnabled(true);
                EqBt.setDisable(false);
            } else {
                mp.getAudioEqualizer().setEnabled(false);
                EqBt.setDisable(true);
            }
            setVolume(volume);
            setBufferProgressTime(progressTime);
            setBalance(balance);
            mp.setMute(Mute);
            videoPlay = false;
        });

        mp.setOnEndOfMedia(() -> {
            if (shuff)
                ShuffleAct();
            else if (rep)
                RepeatAct();
            else if (Que.size() > 0) {
                if (r == Que.size())
                    r = 0;
                else if (r == Que.size() - 1)
                    r = 0;
                else
                    r++;

                Stop();
                dispose();
                try {
                    setMedia(new Media(Que.get(r).toURI().toString()), Que.get(r).getName());
                    String type = Files.probeContentType(Que.get(r).toPath());
                    System.out.println(type);
                    if (type != null) {
                        if (type.substring(0, type.indexOf('/')).equals("audio") || type.equals("application/vnd.apple.mpegurl"))
                            StartMusicVisual();
                        else
                            StartVideo();
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                    Que.remove(r);
                    QueList.getItems().clear();
                    for (int i = 0; i < Que.size(); i++) {
                        QueList.getItems().add(Que.get(i).getName());
                    }
                    Stop();
                    dispose();
                }
            }
        });

    }

    public void play() {
        EqBt.setDisable(!EqP);
        mp.play();
        PlayBt.setGraphic(PlayerPauseView);
    }

    public void pause() {
        mp.pause();
        PlayBt.setGraphic(PlayerPlayView);
    }

    public void Stop() {
        try {
            EqBt.setDisable(true);
            mp.stop();
            PlayBt.setGraphic(PlayerPlayView);
        } catch (NullPointerException ex) {
            System.out.println("Error while Stop" + ex);
        }
    }

    public void dispose() {
        try {
            EqBt.setDisable(true);
            mp.dispose();
        } catch (NullPointerException ex) {
            System.out.println("Error while Dispose " + ex);
        }
    }

    public void seek(double seekValue) {
        mp.seek(Duration.seconds(seekValue));
    }

    public MediaPlayer getMediaPlayer() {
        return mp;
    }

    public void setNodes(JFXSlider Seeker, JFXButton PlayBt, Label NameLb, HBox InfoBox, HBox VisualContainer, VBox PlayerContainer, HBox TitleBar, HBox ControlBar, VBox FatherContainer, Label InfoLb, JFXButton EqBt, ImageView AlbumImg, Label SongName, HBox DataContainer) {

        this.Seeker = Seeker;
        this.PlayBt = PlayBt;
        this.NameLb = NameLb;
        this.VisualContainer = VisualContainer;
        this.InfoBox = InfoBox;
        this.TitleBar = TitleBar;
        this.ControlBar = ControlBar;
        this.FatherContainer = FatherContainer;
        this.PlayerContainer = PlayerContainer;
        this.InfoLb = InfoLb;
        this.EqBt = EqBt;
        this.AlbumImg = AlbumImg;
        this.SongNameLb = SongName;
        this.DataContainer = DataContainer;
        PlayerPlayView.setFitHeight(20);
        PlayerPlayView.setFitWidth(20);
        PlayerPauseView.setFitHeight(20);
        PlayerPauseView.setFitWidth(20);
    }

    public void StartNodes() {
        System.out.println("Nodes Started");
        //Seeker
        double Total = mp.getMedia().getDuration().toSeconds();
        Seeker.setMax(Total);
        Seeker.setMin(0);
        Seeker.setOnMouseDragged((e) -> {
            mp.seek(Duration.seconds(Seeker.getValue()));
        });
        mp.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
            Seeker.setValue(newValue.toSeconds());
        });
        //End

        //PlayBt

        PlayBt.setOnAction((e) -> {
            if (mp.getStatus() == MediaPlayer.Status.PLAYING)
                pause();
            else
                play();
        });
        //End


        //Label
        NameLb.setText("  " + SongName);
        //End


        EqBt.setOnAction((e) -> {
            eq.BtAct();
        });


    }

    public void StartMusicVisual() {
        t1 = new Thread(() -> {
            if (!stage.isFullScreen()) {
                stage.setWidth(1040);
                stage.setHeight(585);
            }
            if (!videoPlay) {
                PlayerContainer.setStyle("-fx-background-color: #464444;");
                PlayerContainer.getChildren().clear();
                if (detached)
                    PlayerContainer.getChildren().addAll(InfoBox);
                else
                    PlayerContainer.getChildren().addAll(VisualContainer, InfoBox);
            }

            //Set Image In AlbumImageCircle
            InfoBox.getChildren().clear();
            Image img = (Image) mp.getMedia().getMetadata().get("image");
            if (img != null) {
                AlbumImg.setFitHeight(175);
                AlbumImg.setFitWidth(175);
                AlbumImg.setImage(img);
                InfoBox.getChildren().add(AlbumImg);
            }
            //End
            //Set Music Data
            Label Title = new Label((String) mp.getMedia().getMetadata().get("title"));
            Title.setStyle("-fx-font-size: 40;");
            Title.setTextFill(Color.WHITE);
            Label Album = new Label((String) mp.getMedia().getMetadata().get("album"));
            Album.setTextFill(Color.WHITE);
            Album.setStyle("-fx-font-size: 20;");
            Label Artist = new Label((String) mp.getMedia().getMetadata().get("artist"));
            Artist.setTextFill(Color.WHITE);
            Artist.setStyle("-fx-font-size: 20;");
            VBox vb = new VBox();
            vb.getChildren().addAll(Title, Album, Artist);
            InfoBox.getChildren().add(vb);

            //End
            VisualContainer.getChildren().clear();
            rect = new Rectangle[mp.getAudioSpectrumNumBands()];
            sct = new ScaleTransition[rect.length];
            for (int i = 0; i < rect.length; i++) {
                rect[i] = new Rectangle();
                rect[i].setFill(RectColor);
                rect[i].setHeight(2);
                rect[i].setWidth(4);
                sct[i] = new ScaleTransition();
                sct[i].setNode(rect[i]);
                VisualContainer.getChildren().add(rect[i]);
            }

            mp.setAudioSpectrumListener(new AudioSpectrumListener() {
                @Override
                public void spectrumDataUpdate(double timestamp, double duration, float[] mag, float[] phases) {

                    for (int i = 0; i < rect.length; i++) {
                        double zero = (mag[i] + 60) * 2;
                        double dur = duration / 2;
                        if (zero > 2) {
                            sct[i].setToY(zero);
                            sct[i].setDuration(Duration.seconds(dur));
                            sct[i].play();
                        }
                        if (zero < 2) {
                            sct[i].setToY(2);
                            sct[i].setDuration(Duration.seconds(dur));
                            sct[i].play();

                        }
                    }

                }
            });

        });

        Platform.runLater(t1);
        t1.setPriority(MAX_PRIORITY);

    }

    public void StartVideo() {
        Thread t2 = new Thread(() -> {
            videoPlay = true;
            mv = new MediaView(mp);
            mv.setFitHeight(VisualContainer.getHeight() - TitleBar.getHeight() - ControlBar.getHeight());
            mv.setFitWidth(VisualContainer.getWidth());
            PlayerContainer.setStyle("-fx-background-color: BLACK;");
            PlayerContainer.getChildren().clear();
            PlayerContainer.getChildren().add(mv);
            PlayerContainer.setPrefSize(mv.getFitWidth(), mv.getFitHeight());
            if (detached)
                StartMusicVisual();

            mv.setOnMouseClicked((e) -> {
                if (e.isPopupTrigger()) {
                    System.out.println("Media View Popup");
                    ContextMenu m = new ContextMenu();
                    MenuItem m1 = new MenuItem();
                    m1.setText("Resize");
                    MenuItem m2 = new MenuItem();
                    if (stage.isAlwaysOnTop())
                        m2.setText("Turn off : Window Always On Top");
                    else
                        m2.setText("Turn on : Window Always On Top");


                    m.getItems().addAll(m1, m2);
                    m1.setOnAction((m1a) -> {
                        Label Resizelb = new Label();
                        Resizelb.setText("Resize & ReLocate this window \n to apply changes to main window");
                        Resizelb.setTextFill(Color.WHITE);
                        Resizelb.setStyle("-fx-font-size: 40;");
                        StackPane root = new StackPane();
                        root.setStyle("-fx-background-color: #464444;");
                        root.getChildren().add(Resizelb);
                        Scene sc = new Scene(root, 500, 500);
                        st = new Stage();
                        st.getIcons().add(stage.getIcons().get(0));
                        st.setTitle("Resize Window");
                        st.setMinHeight(100.0);
                        st.setMinWidth(100.0);
                        st.initStyle(StageStyle.DECORATED);

                        st.setScene(sc);
                        st.setHeight(stage.getHeight());
                        st.setWidth(stage.getWidth());
                        st.setX(stage.getX());
                        st.setY(stage.getY());
                        st.show();
                        stage.close();
                        st.setOnHidden((eee) -> {
                            st.close();
                            stage.setHeight(st.getHeight());
                            stage.setWidth(st.getWidth());
                            stage.setX(st.getX());
                            stage.setY(st.getY());
                            stage.show();
                            mv.setFitHeight(stage.getHeight() - 68.0);
                            mv.setFitWidth(stage.getWidth());
                            mv.setPreserveRatio(true);
                            st = null;

                        });


                    });
                    m2.setOnAction((m2e) -> {
                        stage.setAlwaysOnTop(!stage.isAlwaysOnTop());

                    });
                    m.setX(e.getScreenX());
                    m.setY(e.getScreenY());
                    m.show(stage.getScene().getWindow());

                }
            });
        });
        Platform.runLater(t2);
        t2.setPriority(MAX_PRIORITY);


    }

    public void SetScene(Stage stage) {
        stx = stage.getX();
        sty = stage.getY();
        this.stage = stage;
        FadeTransition ft = new FadeTransition();
        ft.setFromValue(0);
        ft.setToValue(100);
        ft.setDuration(Duration.seconds(1));
        ft.setAutoReverse(true);
        ft.setNode(InfoLb);
        ft.setOnFinished((jj) -> {
            InfoLb.setVisible(false);
        });
        stage.getScene().setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.H) {

                if (!DataContainer.isVisible()) {
                    if (FatherContainer.getChildren().contains(ControlBar)) {
                        FatherContainer.getChildren().remove(ControlBar);
                    } else {
                        FatherContainer.getChildren().add(ControlBar);
                        if (mv != null && mv.getFitHeight() > stage.getScene().getHeight() - 68.0) {
                            mv.setFitHeight(stage.getScene().getHeight() - 68.0);
                            mv.setFitWidth(stage.getScene().getWidth());
                        }
                    }
                }

            }
            if (e.getCode() == KeyCode.F) {
                if (mv != null) {
                    mv.setFitHeight(PlayerContainer.getHeight());
                    mv.setFitWidth(PlayerContainer.getWidth());
                    InfoLb.setVisible(true);
                    InfoLb.setText("M_FullScreen");
                    ft.playFromStart();
                }
            }
            if (e.getCode() == KeyCode.R) {
                if (mv != null) {
                    if (mv.isPreserveRatio()) {

                        mv.setPreserveRatio(false);
                        InfoLb.setVisible(true);
                        InfoLb.setText("Ratio : False");
                        ft.playFromStart();
                    } else {
                        mv.setPreserveRatio(true);
                        InfoLb.setVisible(true);
                        InfoLb.setText("Ratio : True");
                        ft.playFromStart();
                    }

                }

            }
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.P) {
                if (mp != null) {
                    if (mp.getStatus() == MediaPlayer.Status.PLAYING) {
                        pause();
                        InfoLb.setVisible(true);
                        InfoLb.setText("Paused");
                        ft.playFromStart();
                    } else {
                        play();
                        InfoLb.setVisible(true);
                        InfoLb.setText("Playing");
                        ft.playFromStart();
                    }
                }
            }

            if (e.getCode() == KeyCode.S) {
                if (mp != null) {
                    if (mp.getStatus() != MediaPlayer.Status.STOPPED)
                        Stop();
                    InfoLb.setVisible(true);
                    InfoLb.setText("Stopped");
                    ft.playFromStart();
                }
            }
            if (e.getCode() == KeyCode.RIGHT) {
                if (mp != null) {
                    mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds() + 1));
                    InfoLb.setVisible(true);
                    InfoLb.setText("Seeked to " + mp.getCurrentTime().toSeconds() + " sec");
                    ft.playFromStart();
                }
            }
            if (e.getCode() == KeyCode.LEFT) {
                if (mp != null) {
                    mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds() - 1));
                    InfoLb.setVisible(true);
                    InfoLb.setText("Seeked to " + mp.getCurrentTime().toSeconds() + " sec");
                    ft.playFromStart();
                }
            }
            if (e.getCode() == KeyCode.M) {
                if (mp.isMute()) {
                    mp.setMute(false);
                    Mute = false;
                    InfoLb.setVisible(true);
                    InfoLb.setText("UnMute");
                    ft.playFromStart();
                } else {
                    mp.setMute(true);
                    Mute = true;
                    InfoLb.setVisible(true);
                    InfoLb.setText("Mute");
                    ft.playFromStart();
                }
            }


        });
        stage.getScene().setOnMouseClicked((e) -> {
            if (e.getClickCount() == 2) {
                if (!stage.isFullScreen()) {
                    stage.setFullScreen(true);
                    InfoLb.setVisible(true);
                    InfoLb.setText("FullScreeen");
                    ft.playFromStart();
                } else {
                    stage.setFullScreen(false);
                    InfoLb.setVisible(true);
                    InfoLb.setText("!FullScreen");
                    ft.playFromStart();
                }
                if (mv != null && mv.getFitHeight() > stage.getScene().getHeight() - 35.0) {
                    mv.setFitHeight(stage.getScene().getHeight() - 35.0);
                    mv.setFitWidth(stage.getScene().getWidth());
                }
            }
        });

        SongNameLb.setOnMouseClicked((e) -> {
            if (mp != null) {
                if (PlayerContainer.isVisible()) {
                    PlayerContainer.setVisible(false);
                    DataContainer.setVisible(true);
                    FatherContainer.getChildren().add(0, TitleBar);
                    if (!stage.isFullScreen()) {
                        hie = stage.getHeight();
                        wid = stage.getWidth();
                        stx = stage.getX();
                        sty = stage.getY();
                        stage.setHeight(585);
                        stage.setWidth(1040);
                        stage.centerOnScreen();
                    }
                } else {
                    DataContainer.setVisible(false);
                    PlayerContainer.setVisible(true);
                    FatherContainer.getChildren().remove(0);
                    if (!stage.isFullScreen() && videoPlay) {
                        stage.setX(stx);
                        stage.setY(sty);
                        stage.setHeight(hie);
                        stage.setWidth(wid);
                    }
                }
            }
        });

    }

    public void RepeatAct() {
        mp.stop();
        mp.play();
    }

    public void ShuffleAct() {
        if (Que.size() > 0) {
            Random rand = new Random();
            r = rand.nextInt(Que.size()) + 1;
            if (r >= Que.size())
                r = rand.nextInt(Que.size());
            Stop();
            dispose();
            try {
                setMedia(new Media(Que.get(r).toURI().toString()), Que.get(r).getName());
                String type = Files.probeContentType(Que.get(r).toPath());
                if (type.substring(0, type.indexOf('/')).equals("audio") || type.equals("application/vnd.apple.mpegurl"))
                    StartMusicVisual();
                else
                    StartVideo();

            } catch (Exception ex) {
                System.out.println(ex);
                Que.remove(r);
                QueList.getItems().clear();
                for (int i = 0; i < Que.size(); i++) {
                    QueList.getItems().add(Que.get(i).getName());
                }
                Stop();
                dispose();
            }
        }
    }

    public void Shuffle(Boolean shuff) {
        this.shuff = shuff;
    }

    public void Repeat(Boolean rep) {
        this.rep = rep;
    }

    public void Recent(JFXListView<String> recent) {
        this.recent = recent;
    }

    public Boolean isShuffling() {
        return shuff;
    }

    public Boolean isRepeating() {
        return rep;
    }

    public List<File> getQue() {
        return Que;
    }

    public void AddToQue(File f) {
        Que.add(f);
        QueList.getItems().add(f.getName());
    }

    public void RemoveFromQue(File f) {
        Que.remove(f);
        QueList.getItems().remove(f.getName());
    }

    public void ClearQue() {
        Que.clear();
        QueList.getItems().clear();
    }

    public Boolean isEqEnabled() {
        return EqP;
    }

    public void EnableEq(Boolean EqP) {
        this.EqP = EqP;
        mp.getAudioEqualizer().setEnabled(EqP);
        EqBt.setDisable(!EqP);
    }

    public void QueList(ListView<String> QueList) {
        this.QueList = QueList;
    }

    public double getBalance() {
        mp.setBalance(balance);
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
        mp.setBalance(balance);
    }

    public double getBufferProgressTime() {
        return mp.getBufferProgressTime().toSeconds();
    }

    public void setBufferProgressTime(double progressTime) {
        this.progressTime = progressTime;
        mp.getBufferProgressTime().add(Duration.seconds(progressTime));
    }

    public double getVolume() {
        mp.setVolume(volume);
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
        mp.setVolume(volume);
    }

    public ObservableMap<String, Duration> getMarkers() {
        return mp.getMedia().getMarkers();
    }

    public void addMarkerifAbsent(String value, Duration duration) {
        mp.getMedia().getMarkers().clear();
        mp.getMedia().getMarkers().putIfAbsent(value, duration);
    }

    public void addMarker(String value, Duration duration) {
        mp.getMedia().getMarkers().clear();
        mp.getMedia().getMarkers().put(value, duration);
    }

    public ObservableMap<String, Object> getMetadata() {
        return mp.getMedia().getMetadata();
    }

    public void addMetadataIfAbseent(String type, Object ob) throws UnsupportedOperationException {
        mp.getMedia().getMetadata().putIfAbsent(type, ob);
    }

    public void addMetadata(String type, Object ob) {
        mp.getMedia().getMetadata().put(type, ob);
    }

    public void setPane(StackPane pane) {
        eq.setPane(pane);
    }

    public void setVisualizerColor(Color color) {
        this.RectColor = color;
        if (rect != null) {
            for (int i = 0; i < rect.length; i++) {
                rect[i].setFill(color);
            }
        }
    }

    public Color getRectColor() {
        return RectColor;
    }

    public void DetachVisualizer() {
        if (!videoPlay) {
            StartMusicVisual();
        }
        Pane p = new Pane();
        p.getChildren().add(VisualContainer);
        p.setStyle("-fx-background-color: transparent;");
        Scene sc = new Scene(p);
        Vst = new Stage();
        Vst.getIcons().add(stage.getIcons().get(0));
        Vst.setScene(sc);
        Vst.initStyle(StageStyle.TRANSPARENT);
        sc.setFill(Color.TRANSPARENT);
        StageResizer sr = new StageResizer(Vst, sc);
        Vst.show();
        sr = null;
        sc = null;
        detached = true;
        Vst.setOnCloseRequest((e) -> {
            UnDetachVisualizer();
        });
    }

    public void UnDetachVisualizer() {
        Vst.close();
        Vst = null;
        detached = false;
        if (!videoPlay) {
            PlayerContainer.setStyle("-fx-background-color: #464444;");
            PlayerContainer.getChildren().clear();
            PlayerContainer.getChildren().addAll(VisualContainer, InfoBox);
        }
    }

    public Boolean isVisualizerDetahed() {
        return detached;
    }


}