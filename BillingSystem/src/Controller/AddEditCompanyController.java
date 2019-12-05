package Controller;

import Model.Company;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddEditCompanyController {
	@FXML
    private TextField txtid;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtmo;

    @FXML
    private TextField txtph;

    @FXML
    private TextField txtemail;

    @FXML
    private TextField txtaddress;

    @FXML
    private TextField txtdes;

    @FXML
    void onCancelClick(ActionEvent event) {

    }

    @FXML
    void onSaveClick(ActionEvent event) {

    }
    
    public void initData(Company comp) {
    	
    }
}
