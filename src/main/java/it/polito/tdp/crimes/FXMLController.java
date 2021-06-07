/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.DistrettoNumero;
import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<String> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<String> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<String> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	txtResult.clear();
    	String s = this.boxAnno.getValue();
    	if(s == null) {
    		txtResult.appendText("ERRORE: scegliere un anno di riferimento\n");
    		return;
    	}
    	
    	model.creaGrafo(Integer.parseInt(s));
    	txtResult.appendText("NELL'ANNO " + s + "i 7 distretti erano distanti (km)");
    	Map<District, List<DistrettoNumero>> result = model.getAdiacenze();
    	int i = 1;
    	for(List<DistrettoNumero> l : result.values()) {
    		txtResult.appendText("\n\nDISTRETTO: " + i);
    		for(DistrettoNumero dn : l) {
    			txtResult.appendText("\n" + dn.toString());
    		}
    		i++;
    	}
    	

    	this.boxMese.setDisable(false);

    }
    
    @FXML
    void changeGiorni(ActionEvent event) {

    	String s = this.boxMese.getValue();
    	if(s == null) {
    		return;
    	}
    	
    	this.boxGiorno.getItems().setAll(model.getGiorni(Integer.parseInt(s), Integer.parseInt(this.boxAnno.getValue())));
    	this.boxGiorno.setDisable(false);
    }
    

    @FXML
    void allowSim(ActionEvent event) {
    	if(this.boxGiorno.getValue() != null) {
        	this.txtN.setDisable(false);
        	this.btnSimula.setDisable(false);
    	}
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	if(!isValid()) {
    		return;
    	}
    	int giorno = Integer.parseInt(boxGiorno.getValue());
    	int mese = Integer.parseInt(boxMese.getValue());
    	int anno = Integer.parseInt(boxAnno.getValue());
    	int n =  Integer.parseInt(txtN.getText());
    	
    	
    }

    private boolean isValid() {
		String s = this.txtN.getText();
		if(s.equals("")) {
			txtResult.appendText("ERRORE: scrivere un numero di poliziotti.");
			return false;
		}
		try {
			int n = Integer.parseInt(s);
			if(n<1 || n>10) {
				txtResult.appendText("ERRORE: n deve essere un valore tra 1 e 10");
				return false;
			}
		}catch(NumberFormatException nfe) {
			txtResult.appendText("ERRORE: n deve essere un numero intero");
			return false;
		}
		return true;
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().setAll(model.getAnni());
    	this.boxMese.getItems().setAll(model.getMesi());
    }
}
