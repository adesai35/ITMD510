package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import Common.Common;
import Common.DBHelper;
import Model.Company;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    
    private boolean isEdit = false;

    private DBHelper dh = new DBHelper();
    private Common com = new Common();

    @FXML
    void onCancelClick(ActionEvent event) {
    	currentStage(event).close();
    }
    
    private int getNewCompanyId() throws SQLException {
    	int id = 1;
    	
    	ResultSet rs = dh.executeQuery("Select max(comp_id) as id from adesai35_company");
    	
    	while(rs.next()) {
    		id = rs.getInt(1)+1;
    	}
    	
    	return id;
    }

    @FXML
    void onSaveClick(ActionEvent event) throws SQLException {
    	Company comp = new Company();
    	comp.setId(isEdit ? Integer.parseInt(txtid.getText()) : getNewCompanyId());
    	comp.setName(txtName.getText());
    	comp.setMo_no(txtmo.getText());
    	comp.setPhone_no(txtph.getText());
    	comp.setEmail(txtemail.getText());
    	comp.setDesc(txtdes.getText());
    	comp.setAddress(txtaddress.getText());
    	
    	if(isEdit)
    		dh.executeUpdate("Update `adesai35_company` set `comp_name` = '"+comp.getName()+"', `comp_mo_no` = '"+comp.getMo_no()+"', `comp_phone_no` = '"+comp.getPhone_no()+"', `comp_email` = '"+comp.getEmail()+"', `comp_address` = '"+comp.getAddress()+"', `comp_desc` = '"+comp.getDesc()+"' where `comp_id` = "+comp.getId());
    	else
	    	dh.executeUpdate("Insert into `adesai35_customer_bill` (`comp_id`,`comp_name`,`comp_mo_no`,`comp_phone_no`,`comp_email`,`comp_address`,`comp_desc`) values ("+
	    	+comp.getId()+","+comp.getName()+","+comp.getMo_no()+",'"+comp.getPhone_no()+"','"+comp.getEmail()+"','"+comp.getAddress()+"','"+comp.getDesc()+"')");
    	
    	currentStage(event).close();
    }
    
    public void initData(Company comp) {
    	isEdit = comp != null;
    	txtid.setText(Integer.toString(comp.getId()));
    	txtName.setText(comp.getName());
    	txtmo.setText(comp.getMo_no());
    	txtph.setText(comp.getPhone_no());
    	txtemail.setText(comp.getEmail());
    	txtdes.setText(comp.getDesc());
    	txtaddress.setText(comp.getAddress());
    }
    
    private Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
}
