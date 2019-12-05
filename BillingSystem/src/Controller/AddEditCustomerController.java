package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import Common.Common;
import Common.DBHelper;
import Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEditCustomerController {
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
    private Label txtcustId;
    
    @FXML
    private Label txtid;
    

    private DBHelper dh = new DBHelper();
    private Common com = new Common();
    private boolean isEdit = false;
    
    private int getNewCustomerId() throws SQLException {
    	int id = 1;
    	
    	ResultSet rs = dh.executeQuery("Select max(cust_id) as id from adesai35_custany");
    	
    	while(rs.next()) {
    		id = rs.getInt(1)+1;
    	}
    	
    	return id;
    }

    @FXML
    void onCancelClick(ActionEvent event)  {
    	currentStage(event).close();
    }

    @FXML
    void onSaveClick(ActionEvent event) throws NumberFormatException, SQLException {
    	Customer cust = new Customer();
    	cust.setId(isEdit ? Integer.parseInt(txtid.getText()) : getNewCustomerId());
    	cust.setName(txtName.getText());
    	cust.setMo_no(txtmo.getText());
    	cust.setPhone_no(txtph.getText());
    	cust.setEmail(txtemail.getText());
    	cust.setDesc(txtdes.getText());
    	cust.setAddress(txtaddress.getText());
    	
    	if(isEdit)
    		dh.executeUpdate("Update `adesai35_customer` set `cust_name` = '"+cust.getName()+"', `cust_mo_no` = '"+cust.getMo_no()+"', `cust_phone_no` = '"+cust.getPhone_no()+"', `cust_email` = '"+cust.getEmail()+"', `cust_address` = '"+cust.getAddress()+"', `cust_desc` = '"+cust.getDesc()+"' where `cust_id` = "+cust.getId());
    	else
	    	dh.executeUpdate("Insert into `adesai35_customer_bill` (`cust_id`,`cust_name`,`cust_mo_no`,`cust_phone_no`,`cust_email`,`cust_address`,`cust_desc`) values ("+
	    	+cust.getId()+","+cust.getName()+","+cust.getMo_no()+",'"+cust.getPhone_no()+"','"+cust.getEmail()+"','"+cust.getAddress()+"','"+cust.getDesc()+"')");
    	
    	currentStage(event).close();
    }
    
    private Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
    
    public void initData(Customer cust) {
    	isEdit = cust != null;
    	txtid.setText(Integer.toString(cust.getId()));
    	txtName.setText(cust.getName());
    	txtmo.setText(cust.getMo_no());
    	txtph.setText(cust.getPhone_no());
    	txtemail.setText(cust.getEmail());
    	txtdes.setText(cust.getDesc());
    	txtaddress.setText(cust.getAddress());
    }

}
