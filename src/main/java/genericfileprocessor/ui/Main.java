package genericfileprocessor.ui;

import java.io.IOException;
import dnddockfx.DockManager;
import dnddockfx.DockPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
        getClass().getClassLoader().getResource("genericfileprocessor/fxml/Main.fxml"));
    DockPane dockPane = fxmlLoader.load();
    DockManager dockManager = new DockManager();
    dockManager.addDockPane(dockPane);
    dockManager.setLayoutConfigFile("resources/config/dockfx/app.xml");
    dockManager.loadDockLayout();
  }
}
