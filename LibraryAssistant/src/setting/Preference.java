package setting;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Sab
 */
public class Preference {

    public static final String CONFIG_FILE = "config.txt";
    private int nDaysWithoutFine;
    private float finePreDay;
    private String userName;
    private String Password;
    static Alert alert;
    public Preference() {
        nDaysWithoutFine = 14;
        finePreDay = 2;
        userName = "Admin";
        setPassword("admin");
    }

    public int getnDaysWithoutFine() {
        return nDaysWithoutFine;
    }

    public void setnDaysWithoutFine(int nDaysWithoutFine) {
        this.nDaysWithoutFine = nDaysWithoutFine;
    }

    public float getFinePreDay() {
        return finePreDay;
    }

    public void setFinePreDay(float finePreDay) {
        this.finePreDay = finePreDay;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        
        return Password;
    }

    public void setPassword(String Password) {
        if(Password.length()<16){
        this.Password = DigestUtils.shaHex(Password);
        }else{
            this.Password = Password;
        }
    }

    public static void initconfig() {
        Writer writer = null;
        try {
            Preference preference = new Preference();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
        } catch (IOException ex) {
            Logger.getLogger(Preference.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preference.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public static Preference  getPreference(){
         Gson gson = new Gson();
         Preference preference = new Preference();
        try {
           
              preference = gson.fromJson(new FileReader(CONFIG_FILE), Preference.class);
            
            
        } catch (FileNotFoundException ex) {
            initconfig();
            Logger.getLogger(Preference.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preference;
    }
   public static void whriteTopreferenceFile(Preference preference){
        Writer writer = null;
        try {
            
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
            
            alert = new Alert(Alert.AlertType.INFORMATION,"Settings updated", ButtonType.OK);
            alert.setTitle("Success");
            alert.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(Preference.class.getName()).log(Level.SEVERE, null, ex);
            alert = new Alert(Alert.AlertType.ERROR, "Can not save Configer file ", ButtonType.OK);
            alert.setTitle("Failed");
            alert.showAndWait();
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preference.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

   }
}
