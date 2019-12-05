package Controller;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Common.Common;
import Common.DBHelper;
import Model.Bill;
import Model.BillItem;
import Model.Customer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CustomerBillController implements Initializable {

    @FXML
    private Label txtBillNum;

    @FXML
    private Label txtTinNum;

    @FXML
    private Label txtDate;

    @FXML
    private Label txtCustName;

    @FXML
    private TableView<BillItem> tableBillItem;

    @FXML
    private Label txtPayableAmount;

    @FXML
    private Label txtDebit;

    @FXML
    private TextField txtPaidAmount;
    
    @FXML
    private TextField txtProductItem;

    @FXML
    private TextField txtWidth;

    @FXML
    private TextField txtHeigth;

    @FXML
    private TextField txtPPQ;

    @FXML
    private TextField txtAmount;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCencel;
    
    
    @FXML
    private Button btnAddBillItem;
    
    private boolean isEdit = false;
    private DBHelper dh = new DBHelper();
    private Common com = new Common();
    private Bill bill;
    private List<BillItem> billitems;
    
    
    private Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

    @FXML
    void oncancelClick(ActionEvent event) {
    	currentStage(event).close();
    }
    
    private boolean isVaildBillItemData(BillItem item) {
    	if(item.getPPR().isEmpty() || item.getProduct_item().isEmpty() || item.getProduct_amount().isEmpty()) {
    		com.NotificationBox("Vaildation", "Plaese Enter value in Bill Item.");
    		return false;
    	}
    	else if(!com.isNumeric(item.getPPR())) {
    		com.NotificationBox("Vaildation", "Plaese Enter vaild value in Price per Quantity.");
    		return false;
    	}
    	else if(!com.isNumeric(item.getProduct_amount())) {
    		com.NotificationBox("Vaildation", "Plaese Enter vaild value in product amount");
    		return false;
    	}	
    	else if(!com.isNumeric(item.getWidth())) {
    		com.NotificationBox("Vaildation", "Plaese Enter vaild value in width");
    		return false;
    	}
    	else if(!com.isNumeric(item.getHeight())) {
    		com.NotificationBox("Vaildation", "Plaese Enter vaild value in height");
    		return false;
    	}
    	return true;
    }
    
    private boolean isVaildBillData() {
    	String input = txtPaidAmount.getText();
    	if(input == null || input.isEmpty()) {
    		com.NotificationBox("Notification", "Plaese Enter Value in Paid Amount Text box.");
    		return false;
    	}
    	else if(!com.isNumeric(input)) {
    		com.NotificationBox("Notification", "Plaese Enter Numeric Value in Paid Amount Text box.");
    		return false;
    	}
    	return true;
    }

    @FXML
    void saveBillData(ActionEvent event) {
    	if(isVaildBillData()) {
	    	this.bill.setDebitAmount(txtDebit.getText());
	    	this.bill.setTotal(txtPayableAmount.getText());
	    	this.bill.setPaidAmount(txtPaidAmount.getText());
	    	
	    	if(isEdit)
	    		dh.executeUpdate("Update `adesai35_customer_bill` set `bill_total_amount` = '"+this.bill.getTotal()+"', `bill_paid_amount` = '"+this.bill.getPaidAmount()+"', `bill_debit_amount` = '"+this.bill.getDebitAmount()+"' where `bill_id` = "+this.bill.getId());
	    	else
		    	dh.executeUpdate("Insert into `adesai35_customer_bill` (`bill_id`,`bill_company_id`,`bill_customer_id`,`bill_date`,`bill_total_amount`,`bill_paid_amount`,`bill_debit_amount`) values ("+
		    	+this.bill.getId()+","+this.bill.getCompany_id()+","+this.bill.getCustomer_id()+",'"+this.bill.getDate()+"','"+this.bill.getTotal()+"','"+this.bill.getPaidAmount()+"','"+this.bill.getDebitAmount()+"')");
	    	
	    	dh.executeUpdate("Update `adesai35_customer_bill_items` set `bill_id` = "+this.bill.getId()+" where bill_id is null");
	    	dh.executeUpdate("Insert into `adesai35_history`(`name`,`description`) values ('Bill Id : "+this.bill.getId()+"','Bill has Successfully Created')");
	    	alertBox(event);
    	}
    }
    
    private String getCustomerNameById(int id) {
    	String name = "";
    	
    	ResultSet rs = dh.executeQuery("Select cust_name from adesai35_customer where cust_id = "+id);
    	
    	try {
			while(rs.next()) {
				name = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return name;
    }
    
    
    public void initData(Bill bill, List<BillItem> billitems, boolean flag) {
    	this.isEdit = flag;
    	this.bill = bill;
    	this.billitems = billitems;
    	txtBillNum.setText(Integer.toString(bill.getId()));
    	txtCustName.setText(getCustomerNameById(bill.getCustomer_id()));
    	txtDate.setText(bill.getDate());
    	txtDebit.setText(bill.getDebitAmount());
    	txtPaidAmount.setText(bill.getPaidAmount());
    	txtPayableAmount.setText(bill.getTotal());
    	if(isEdit)
    		if(billitems != null)
    		 this.tableBillItem.setItems(FXCollections.observableList(billitems));
    }
    
    
    private int getNewBillItemId() throws SQLException {
    	int id = 1;
    	
    	ResultSet rs = dh.executeQuery("Select max(bill_item_id) as id from adesai35_customer_bill_items");
    	
    	while(rs.next()) {
    		id = rs.getInt(1)+1;
    	}
    	
    	return id;
    }
    
    public void alertBox(ActionEvent event) {
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Notification");
		alert.setHeaderText(null);
		alert.setContentText("Bill data has been saved");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			currentStage(event).close();
		} else {
		    // ... user chose CANCEL or closed the dialog
		}
    }

    public void addBilllItem(ActionEvent event) {
    	BillItem item = new BillItem();
    	try {
			item.setId(getNewBillItemId());
			item.setProduct_item(txtProductItem.getText());
			item.setWidth(txtWidth.getText());
			item.setHeight(txtHeigth.getText());
			item.setPPR(txtPPQ.getText());
			item.setProduct_amount(txtAmount.getText());
			
			if(isVaildBillItemData(item)) {
				this.tableBillItem.getItems().add(item);
				dh.executeUpdate("Insert into `adesai35_customer_bill_items` (`bill_item_id`, `bill_product_item`, `bill_width`, `bill_height`, `bill_price_per_quantity`, `bill_product_amount`) values ("+
				item.getId()+",'"+item.getProduct_item()+"','"+item.getWidth()+"','"+item.getHeight()+"','"+item.getPPR()+"','"+item.getProduct_amount()+"')");
				int id = Integer.parseInt(txtPayableAmount.getText())+ (Integer.parseInt(item.getPPR()) * Integer.parseInt(item.getProduct_amount()));
				this.txtPayableAmount.setText(Integer.toString(id));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		ContextMenu cmenu = new ContextMenu();
		MenuItem del = new MenuItem("Delete");
		del.setOnAction(e->{
			int i = this.tableBillItem.getSelectionModel().getSelectedIndex();
			BillItem bi = this.tableBillItem.getSelectionModel().getSelectedItem();
			if(bi!=null)
				dh.executeUpdate("Delete from `adesai35_customer_bill_items` where bill_item_id = "+bi.getId());
			this.tableBillItem.getItems().remove(i);
		});
		cmenu.getItems().addAll(del);
		this.tableBillItem.setContextMenu(cmenu);
		
		TableColumn<BillItem, Integer> idCol = new TableColumn<BillItem, Integer>("Bill Item ID");
        idCol.setCellValueFactory(new PropertyValueFactory<BillItem, Integer>("id"));
        TableColumn<BillItem, String> product_itemCol = new TableColumn<BillItem, String>("Product Item");
        product_itemCol.setCellValueFactory(new PropertyValueFactory<BillItem, String>("product_item"));
        TableColumn<BillItem, String> widthCol = new TableColumn<BillItem, String>("Wigth");
        widthCol.setCellValueFactory(new PropertyValueFactory<BillItem, String>("width"));
        TableColumn<BillItem, String> heightCol = new TableColumn<BillItem, String>("Height");
        heightCol.setCellValueFactory(new PropertyValueFactory<BillItem, String>("height"));
        TableColumn<BillItem, String> pricePerQuantityCol = new TableColumn<BillItem, String>("Price Per Quantity");
        pricePerQuantityCol.setCellValueFactory(new PropertyValueFactory<BillItem, String>("pricePerQuantity"));
        TableColumn<BillItem, String> product_amountCol = new TableColumn<BillItem, String>("Amount");
        product_amountCol.setCellValueFactory(new PropertyValueFactory<BillItem, String>("product_amount"));
        txtPaidAmount.textProperty().addListener((observable, oldValue, newValue) -> {
        	if(txtPayableAmount.getText() != "" && com.isNumeric(txtPayableAmount.getText()) && newValue != ""  && com.isNumeric(newValue)) {
        	int result = Integer.parseInt(txtPayableAmount.getText()) - Integer.parseInt(newValue);
        	txtDebit.setText(Integer.toString(result));
        	}
        });
        this.tableBillItem.getColumns().setAll(idCol, product_itemCol,widthCol,heightCol,pricePerQuantityCol,product_amountCol);
	}

}
