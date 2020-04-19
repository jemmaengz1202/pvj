package com.jemmaengz.pvj;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.validation.RequiredFieldValidator;

import io.github.cdimascio.dotenv.Dotenv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
public class Utilities {
    public static Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().ignoreIfMissing().load();
    
//    TextFormatter para numeros
    public static TextFormatter<Integer> getNumberFormatter() {
        return new TextFormatter<>(
            new IntegerStringConverter(), 
            null,  
            c -> Pattern.matches("\\d*", c.getText()) ? c : null 
        );
    }
    
    public static TextFormatter<String> getNumberCodeFormatter() {
        String regex = "[0-9]{0,15}";
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (Pattern.matches(regex, c.getControlNewText())) {
                return c ;
            } else {
                return null ;
            }
        };
        return new TextFormatter<>(filter);
    }
    
    public static TextFormatter<String> getDecimalFormatter() {
        String regex = "[0-9]*[.]?[0-9]{0,2}";
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (Pattern.matches(regex, c.getControlNewText())) {
                return c ;
            } else {
                return null ;
            }
        };
        return new TextFormatter<>(filter);
    }
    
//    TextFormatter para letras
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
    
    public static void corregirNumberField(TextField tf) {
        String string = tf.getText();
        if(string.equals("")) {
            tf.setText("0");
        }
        if(string.substring(string.length()- 1).equals("."))  {
            tf.setText(string.substring(0, -1));
        }
    }

    /**
     *
     * @param str
     * @return
     */
    public static boolean isInt(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        if (length == 0) {
            return false;
        }

        int i = 0;

        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }

            i = 1;
        }

        for (; i < length; i++) {
            char c = str.charAt(i);

            if ((c <= '/') || (c >= ':')) {
                return false;
            }
        }

        return true;
    }
    
    public static void showToast(String string) {
        JFXSnackbar snackbar = new JFXSnackbar(PrincipalController.instance.mainPane);
        snackbar.fireEvent(new SnackbarEvent(new JFXSnackbarLayout(string)));
    }
    
    public static void showToast(String string, long time, Pane pane) {
        JFXSnackbar snackbar = new JFXSnackbar(pane);
        snackbar.fireEvent(new SnackbarEvent(
                        new JFXSnackbarLayout(string),
                        Duration.millis(time), null));
    }

    public static void showAlert(String string) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Mensaje");
        alert.setContentText(string);
        alert.initStyle(StageStyle.UTILITY);
        alert.showAndWait();
    }
    
    public static boolean showConfirmationDialog(String string) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Mensaje");
        alert.setContentText(string);
        alert.initStyle(StageStyle.UTILITY);
        Optional <ButtonType> action = alert.showAndWait();
        
        return action.get() == ButtonType.OK;
    }
    
    public static Optional<String> showTextInputDialog(String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
 
        dialog.setTitle("PVJ");
        dialog.setHeaderText(header);
        dialog.setContentText(content);
 
        Optional<String> result = dialog.showAndWait();
        return result;
    }
    
    public static void setRequiredValidator(JFXTextField txtf) {
        RequiredFieldValidator rfv = new RequiredFieldValidator();
        rfv.setMessage("Requerido");
        txtf.getValidators().add(rfv);
        txtf.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    if (! txtf.validate()) {
                        txtf.getStyleClass().add("txtf-invalid");
                        System.out.println(txtf.validate());
                    } else {
                        txtf.getStyleClass().removeAll("txtf-invalid");
                    }
                }
        });
    }
    
    public static void setRequiredValidator(JFXTextArea txtf) {
        RequiredFieldValidator rfv = new RequiredFieldValidator();
        rfv.setMessage("Requerido");
        txtf.getValidators().add(rfv);
        txtf.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    if (! txtf.validate()) {
                        txtf.getStyleClass().add("txtf-invalid");
                        System.out.println(txtf.validate());
                    } else {
                        txtf.getStyleClass().removeAll("txtf-invalid");
                    }
                }
        });
    }
    
    public static void setRequiredValidator(JFXPasswordField txtf) {
        RequiredFieldValidator rfv = new RequiredFieldValidator();
        rfv.setMessage("Requerido");
        txtf.getValidators().add(rfv);
        txtf.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    if (! txtf.validate()) {
                        txtf.getStyleClass().add("txtf-invalid");
                        System.out.println(txtf.validate());
                    } else {
                        txtf.getStyleClass().removeAll("txtf-invalid");
                    }
                }
        });
    }
    
    public static void setMaxCharacters(TextField textfield, int max) {
        textfield.setOnKeyTyped(event -> {
            int maxCharacters = max;
            if(textfield.getText().length() > maxCharacters) event.consume();
        });
    }
    
    public static Date dateToDateWithoutTime(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date dateWithZeroTime = formatter.parse(formatter.format(date));
            return dateWithZeroTime;
        } catch (ParseException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static String getDateSpanishFormatted(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);
    }
    
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
    
}
