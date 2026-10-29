module pt.ipp.isep.dei.ui.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires java.logging;
    requires gs.core;


    opens pt.ipp.isep.dei.ui.gui to javafx.fxml;
    exports pt.ipp.isep.dei.ui.gui;
}