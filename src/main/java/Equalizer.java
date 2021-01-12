import com.jfoenix.controls.JFXSlider;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioEqualizer;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class Equalizer extends Thread {
    Process process = null;
    StackPane pane = null;
    VBox vb = null;
    MediaPlayer mp = null;
    JFXSlider[] sd = null;
    HBox slidC = null;
    HBox LbH = null;
    double[] HBg = {8, 6, 3, 0, -12, -14, -12, -10, -8, -4};
    double[] currentEq = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    AudioEqualizer eq = null;

    public void StartEq(MediaPlayer mp) {
        this.mp = mp;
        eq = mp.getAudioEqualizer();
        for (int i = 0; i < eq.getBands().size(); i++) {
            eq.getBands().get(i).setGain(currentEq[i]);
        }
    }

    public void showEq() {
        if (mp.getAudioEqualizer().isEnabled()) {
            double min = EqualizerBand.MIN_GAIN;
            double max = EqualizerBand.MAX_GAIN;

            LbH = new HBox(20);
            LbH.setAlignment(Pos.CENTER);
            Label[] lb = new Label[eq.getBands().size()];

            vb = new VBox();

            slidC = new HBox(40);
            vb.getChildren().addAll(LbH, slidC);
            slidC.setAlignment(Pos.CENTER);

            vb.setStyle("-fx-background-color: #464444; -fx-border-color: #e79523;");
            sd = new JFXSlider[eq.getBands().size()];
            EventHandler<MouseEvent> sda = (MouseEvent e) -> {
                for (int i = 0; i < sd.length; i++) {
                    System.out.println(i);
                    eq.getBands().get(i).setGain(sd[i].getValue());
                    currentEq[i] = eq.getBands().get(i).getGain();
                }

            };
            for (int i = 0; i < sd.length; i++) {
                sd[i] = new JFXSlider();
                sd[i].setMax(max);
                sd[i].setMin(min);
                sd[i].setOrientation(Orientation.VERTICAL);
                sd[i].setValue(eq.getBands().get(i).getGain());
                sd[i].setIndicatorPosition(JFXSlider.IndicatorPosition.RIGHT);
                sd[i].setPrefHeight(450);
                sd[i].setOnMouseDragged(sda);
                sd[i].setOnMouseClicked(sda);
                lb[i] = new Label();
                if (i < 2)
                    lb[i].setText(" " + eq.getBands().get(i).getCenterFrequency() + " Hz");
                else if (i < 5)
                    lb[i].setText((int) eq.getBands().get(i).getCenterFrequency() + " Hz");
                if (eq.getBands().get(i).getCenterFrequency() > 999) {
                    lb[i].setText("   " + (eq.getBands().get(i).getCenterFrequency() / 1000) + " k");
                }

                lb[i].setTextFill(Color.WHITE);
                LbH.getChildren().add(lb[i]);
                slidC.getChildren().add(sd[i]);
            }


            MenuButton menB = new MenuButton("Preset");
            MenuItem HBbt = new MenuItem("Heavy Base");
            HBbt.setStyle("-fx-text-fill: BLACK;");
            MenuItem NObt = new MenuItem("Normal");
            NObt.setStyle("-fx-text-fill: BLACK;");
            HBbt.setOnAction((e) -> {
                for (int i = 0; i < eq.getBands().size(); i++) {
                    eq.getBands().get(i).setGain(HBg[i]);
                    sd[i].setValue(eq.getBands().get(i).getGain());
                    currentEq[i] = HBg[i];
                }
            });
            NObt.setOnAction((e) -> {
                for (int i = 0; i < eq.getBands().size(); i++) {
                    eq.getBands().get(i).setGain(0);
                    sd[i].setValue(eq.getBands().get(i).getGain());
                    currentEq[i] = 0;
                }
            });
            menB.setStyle("-fx-background-color: TRANSPARENT;");
            menB.setTextFill(Color.WHITE);
            menB.getItems().clear();
            menB.getItems().addAll(HBbt, NObt);
            vb.getChildren().add(menB);
            vb.setPrefSize(600, 500);
            process = new Process();
            process.setStack(pane);
            process.createCoustomDialog(vb);
            process = null;
        }
    }

    public void BtAct() {
        if (process == null)
            showEq();
    }

    public void setPane(StackPane pane) {
        this.pane = pane;
    }
}