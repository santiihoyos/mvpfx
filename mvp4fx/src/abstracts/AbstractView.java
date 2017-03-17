package abstracts;

import Exceptions.Mvp4FxException;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Santiago Hoyos Zea
 * @param <T1>
 */
public abstract class AbstractView<T1 extends AbstractPresenter> implements Initializable {

    /**
     * Presentador de las vistas
     */
    protected T1 presenter;

    /**
     * Ultimo directorio que se selecciono.
     */
    private String ultimoDiretorio;

    private HashMap<String, Object> recursos;

    /**
     *
     * @return
     */
    public T1 getPresenter() {

        if (presenter == null) {
            try {
                throw new Mvp4FxException("El presentador es nulo, ¿ha inyectado las dependencias?..."
                        + " para inyectar dependencias se ha de llamar"
                        + " a inyertarDependencias(presenter.class, interactor.class);");
            } catch (Mvp4FxException ex) {
                Logger.getLogger(AbstractView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return presenter;
    }

    /**
     *
     * @param presenter
     */
    public void setPresenter(T1 presenter) {
        this.presenter = presenter;
    }

    /**
     * Dialogo simple sin ventana dueña.
     *
     * @param message
     */
    public void showError(String message) {

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(message);
            alert.setTitle("¡Error!");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
        });
    }

    /**
     * Defne un dueño para evitar mostrar dialogo detras.
     *
     * @param message
     * @param owner
     */
    public void showError(String message, Window owner) {

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(owner);

            Image image = new Image(ClassLoader.getSystemResource("fxmls/recursos/error.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            alert.setGraphic(imageView);

            ButtonBar buttonBar = (ButtonBar) alert.getDialogPane().lookup(".button-bar");
            buttonBar.getButtons().get(0).setStyle("-fx-background-color: -swatch-500; -fx-text-fill: -fx-text-button-colored;");

            alert.setContentText(message);
            alert.setHeaderText("¡ERROR!");
            alert.setTitle("¡Error!");
            alert.showAndWait();
        });
    }

    /**
     *
     * @param title
     * @param ventanaPadre
     * @return
     */
    protected File seleccionarDirectorio(String title, Window ventanaPadre) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        directoryChooser.setInitialDirectory(ultimoDiretorio != null ? new File(ultimoDiretorio) : new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(ventanaPadre);

        if (file != null) {
            ultimoDiretorio = file.getParentFile().getAbsolutePath();
        }

        return file;
    }

    /**
     *
     * @param tituloDialogo
     * @param ventanaPadre
     * @return
     */
    protected File seleccionarFichero(String tituloDialogo, Window ventanaPadre) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(tituloDialogo);
        fileChooser.setInitialDirectory(ultimoDiretorio != null ? new File(ultimoDiretorio) : new File(System.getProperty("user.home")));

        File file = fileChooser.showOpenDialog(ventanaPadre);

        if (file != null) {
            ultimoDiretorio = file.getParent();
        }

        return file;
    }

    /**
     * Crea una ventana dado el nombre, fxml y el estilo.
     *
     * @param nombreVentana
     * @param rutaFXML
     * @param rutaEstilo
     * @param owner
     * @return deveulve el Stage asociado para ser mostrado cuando se quiera
     */
    protected Stage obtenerNuevaVentana(String nombreVentana, String rutaFXML, String rutaEstilo, Stage owner) {

        try {

            Parent parentConfigs = FXMLLoader.load(ClassLoader.getSystemResource(rutaFXML));
            Stage stage = new Stage();
            Scene scene = new Scene(parentConfigs);

            if (rutaEstilo != null && !rutaEstilo.isEmpty()) {
                scene.getStylesheets().add(ClassLoader.getSystemResource(rutaEstilo).toExternalForm());
            }

            stage.setTitle(nombreVentana);
            stage.initOwner(owner);
            stage.setScene(scene);
            return stage;

        } catch (Exception ex) {
            showError("Error al cargar la ventana de " + nombreVentana + " " + ex);
        }

        return null;
    }

    /**
     * Crea una ventana dado el nombre, fxml y el estilo.
     *
     * @param nombreVentana
     * @param rutaFXML
     * @param rutaEstilo
     * @param owner
     * @return deveulve el Stage asociado para ser mostrado cuando se quiera
     */
    protected Stage obtenerNuevaVentanaConRecursos(String nombreVentana, String rutaFXML, String rutaEstilo, Stage owner, HashMap<String, Object> parametrosClaveValor) {

        try {

            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(rutaFXML));
            loader.load();

            ((AbstractView) loader.getController()).setRecursos(parametrosClaveValor);

            Stage stage = new Stage();
            Scene scene = new Scene(loader.getRoot());

            if (rutaEstilo != null && !rutaEstilo.isEmpty()) {
                scene.getStylesheets().add(ClassLoader.getSystemResource(rutaEstilo).toExternalForm());
            }

            stage.setTitle(nombreVentana);
            stage.initOwner(owner);
            stage.setScene(scene);
            return stage;

        } catch (Exception ex) {
            showError("Error al cargar la ventana de " + nombreVentana + " " + ex);
        }

        return null;
    }

    /**
     *
     * @return
     */
    protected HashMap<String, Object> getRecursos() {

        if (recursos == null) {
            recursos = new HashMap<>();
        }

        return recursos;
    }

    /**
     *
     * @param recursos
     */
    protected void setRecursos(HashMap<String, Object> recursos) {
        this.recursos = recursos;
    }

    /**
     * Inyecta las dependencias MVP. y crea toda la estrucutra MVP
     *
     * @param tipoPresenter
     * @param tipoInteractor
     */
    protected void inyectarDependencias(Class<T1> tipoPresenter, Class<? extends AbstractInteractor<T1>> tipoInteractor) {

        try {

            AbstractInteractor interactorTmp = (AbstractInteractor) tipoInteractor.newInstance();
            AbstractPresenter presenterTmp = (AbstractPresenter) tipoPresenter.newInstance();

            presenterTmp.setView(this);
            presenterTmp.setInteractor(interactorTmp);
            interactorTmp.setPresenter(presenterTmp);
            this.setPresenter((T1) presenterTmp);

        } catch (InstantiationException | IllegalAccessException ex) {
            try {
                throw new Mvp4FxException("Error al inyectar dependencias MVP..."
                        + " ¿has pasado los tipos de clase adecuados al llamar a inyectarDependencias?");
            } catch (Mvp4FxException ex1) {
                Logger.getLogger(AbstractView.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

}
