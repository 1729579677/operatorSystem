package cpu.operatorsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CPUschedule extends Application {
    public void start(Stage primaryStage) throws Exception {

        AnchorPane anchorPane = FXMLLoader. load(this.getClass().getResource("CPUScheduling.fxml"));
        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CPU scheduling VM");
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }

}
