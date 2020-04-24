package com.jemmaengz.pvj;

import com.jemmaengz.pvj.model.HibernateUtil;
import com.jfoenix.controls.JFXDecorator;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.hibernate.Session;

public class Main extends Application {
    public static boolean logueado = false;
    
    // Session usada para entrar a la BD
    public static Session session;
    
    // TextFormatter para campos numéricos
    public static TextFormatter<Integer> getNumberFormatter() {
        return new TextFormatter<>(
            new IntegerStringConverter(), 
            null,  
            c -> Pattern.matches("\\d*", c.getText()) ? c : null 
        );
    }
    
    // Para campos de letras
    public static TextFormatter<String> getCharactersFormatter() {
        Pattern pattern = Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚ\\u0020]*");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (pattern.matcher(c.getControlNewText()).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        return new TextFormatter<>(filter);
    }

    public Main() {
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        if (!logueado) {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml"));

            JFXDecorator decoratedRoot = new JFXDecorator(stage, root, false, false, true);

            Scene scene = new Scene(decoratedRoot);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("PVJ - Login");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("/imagenes/favicon-32x32.png")));

            stage.show();

            stage.setOnCloseRequest(e -> {
                Platform.exit();
            });
        } else {
            System.out.println("Logueado correctamente");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/PrincipalView.fxml"));
            JFXDecorator decoratedRoot = new JFXDecorator(stage, root, false, false, true);

            Scene scene = new Scene(decoratedRoot);
            stage.setScene(scene);
            stage.setTitle("PVJ");
            stage.show();
            
            stage.setOnCloseRequest(e -> {
                Platform.exit();
            });
        }
    }
    public static void run(String[] args) {
        launch(args);
    }
    
    // Detener el gestor de la BD al finalizar la aplicación
    @Override
    public void stop() {
        HibernateUtil.shutdown();
        if (session != null) {
            session.close();
        }
    }
    
}
