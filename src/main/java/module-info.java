module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens cpu.operatorsystem to javafx.fxml;
    exports cpu.operatorsystem;
}