package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.org.apache.xalan.internal.xsltc.dom.LoadDocument;

import Common.Common;
import Common.DBHelper;
import Model.Customer;
import Model.History;
import Model.Bill;
import Model.Company;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


public class HomeController  implements Initializable {

    @FXML
    private TabPane tabLeftPane;

    @FXML
    private TabPane tabCentalPane;

    @FXML
    private TabPane tabBottonPane;
    
    private TreeView categoryTreeView = new TreeView();
    private Common com = new Common();
    private DBHelper dh = new DBHelper();
    private TableView historytable;
    private ObservableList historydata;
    private TableView companytable;
    private ObservableList companydata;
    private TableView customertable;
    private ObservableList customerdata;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	initCategoryTab(null);
    	initHistoryTab(null);
    }
    
    @FXML
    private void initCategoryTab(ActionEvent event) {
    	if(this.tabLeftPane.getTabs().size() > 0) reloadTreeView();
    	else {
    		Tab category = new Tab("Category");
    		reloadTreeView();
    		category.setContent(this.categoryTreeView);
    		this.tabLeftPane.getTabs().add(category);
    	}
    }
    
    @FXML
    void isCloseClick(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void about(ActionEvent event) {
    	com.NotificationBox("About", "Welcome to Billing System 1.0.0.0");
    }
    
    @FXML
    void addCompany(ActionEvent event) throws IOException {
    	Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddEditCompanyView.fxml"));
		Parent root = (Parent) loader.load();
		System.out.println("1");
		window.setTitle("Add Company");
		window.initModality(Modality.APPLICATION_MODAL);
		Scene scene = new Scene(root);
		window.setScene(scene);
		System.out.println("2");
		window.showAndWait();
    }
    
    @FXML
    private void initHistoryTab(ActionEvent event) {
    	if(this.tabBottonPane.getTabs().size() > 0) this.historytable.setItems(getInitialHistoryTableData());
    	else {
    		Tab history = new Tab("History");
    		historytable = new TableView();
    		historydata = getInitialHistoryTableData();
    		historytable.setItems(historydata);
    		
    		TableColumn nameCol = new TableColumn("Name");
    		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
            TableColumn desCol = new TableColumn("Description");
            desCol.setCellValueFactory(new PropertyValueFactory("description"));
     
            historytable.getColumns().setAll(nameCol, desCol);
    		
    		history.setContent(this.historytable);
    		this.tabBottonPane.getTabs().add(history);
    	}
    }
    
    @FXML
    private void initCompanyTab(ActionEvent event) {
    		Tab company = new Tab("Company List");
    		companytable = new TableView();
    		companydata = getInitialCompanyTableData();
    		companytable.setItems(companydata);
    		
    		TableColumn nameCol = new TableColumn("Name");
    		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
            TableColumn mCol = new TableColumn("Mobile No");
            mCol.setCellValueFactory(new PropertyValueFactory("mo_no"));
            TableColumn pCol = new TableColumn("Phone No");
            pCol.setCellValueFactory(new PropertyValueFactory("phone_no"));
            TableColumn eCol = new TableColumn("Email");
            eCol.setCellValueFactory(new PropertyValueFactory("email"));
            TableColumn aCol = new TableColumn("Address");
            aCol.setCellValueFactory(new PropertyValueFactory("address"));
            TableColumn desCol = new TableColumn("Description");
            desCol.setCellValueFactory(new PropertyValueFactory("desc"));
     
            companytable.getColumns().setAll(nameCol, mCol, pCol, eCol, aCol,desCol);
    		
            company.setContent(this.companytable);
    		this.tabCentalPane.getTabs().add(company);
    }
    
    @FXML
    private void initCustomerTab(ActionEvent event) {
    	Tab customer = new Tab("Customer List");
		customertable = new TableView();
		customerdata = getInitialCustomerTableData();
		customertable.setItems(customerdata);
		
		TableColumn nameCol = new TableColumn("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn mCol = new TableColumn("Mobile No");
        mCol.setCellValueFactory(new PropertyValueFactory("mo_no"));
        TableColumn pCol = new TableColumn("Phone No");
        pCol.setCellValueFactory(new PropertyValueFactory("phone_no"));
        TableColumn eCol = new TableColumn("Email");
        eCol.setCellValueFactory(new PropertyValueFactory("email"));
        TableColumn aCol = new TableColumn("Address");
        aCol.setCellValueFactory(new PropertyValueFactory("address"));
        TableColumn desCol = new TableColumn("Description");
        desCol.setCellValueFactory(new PropertyValueFactory("desc"));
 
        customertable.getColumns().setAll(nameCol, mCol, pCol, eCol, aCol,desCol);
		
        customer.setContent(this.customertable);
		this.tabCentalPane.getTabs().add(customer);
    }
    
    private ObservableList getInitialCustomerTableData() {
    	List<Customer> listCustomer = getCustomerDataByQuery("select * from adesai35_customer");
    	return FXCollections.observableList(listCustomer);
    }
    
    private ObservableList getInitialCompanyTableData() {
    	List<Company> listCompany = getCompanyDataByQuery("select * from adesai35_company");
    	return FXCollections.observableList(listCompany);
    }
    
    private ObservableList getInitialHistoryTableData() {
    	List<History> listHistory = getAllHistory();
    	return FXCollections.observableList(listHistory);
    }
    
    private void reloadTreeView() {
    	
    	TreeItem<String> rootNode = new TreeItem<String>("root");
    	
    	for (Company comp : getCompanyDataByQuery("select * from adesai35_company")) {
    		TreeItem<String> company = new TreeItem<String>(comp.getName());
    		for(Customer cust : getCustomerDataByQuery("select * from adesai35_customer where company_id = "+comp.getId())) {
    			TreeItem<String> customer = new TreeItem<String>(cust.getName());
    			for(Bill bills : getBillDataByQuery("select * from adesai35_customer_bill where bill_customer_id = " + cust.getId())) {
    				TreeItem<String> bill = new TreeItem<String>(Integer.toString(bills.getId()));
    				customer.getChildren().add(bill);
    			}
    			company.getChildren().add(customer);
    		}
    		rootNode.getChildren().add(company);
		}
    	
    	this.categoryTreeView.setRoot(rootNode);
    	this.categoryTreeView.setShowRoot(false);
    	categoryTreeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>()
        {

            @Override
            public TreeCell<String> call(TreeView<String> arg0)
            {
                // custom tree cell that defines a context menu for the root tree item
                return new MyTreeCell();
            }
        });
    	
    	
    }
    
    private List<History> getAllHistory(){
    	ResultSet rs = dh.executeQuery("select * from adesai35_history");
    	List<History> list = new ArrayList<History>();
    	
    	try {
			while(rs.next()) {
				History cust = new History();
				cust.setName(rs.getString("name"));
				cust.setDescription(rs.getString("description"));
				if(cust.getName() == null || cust.getDescription() == null || cust.getName().isEmpty() || cust.getDescription().isEmpty()) continue;
				list.add(cust);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return list;
    }
    
    
    private List<Customer> getCustomerDataByQuery(String sql){
    	ResultSet rs = dh.executeQuery(sql);
    	List<Customer> list = new ArrayList<Customer>();
    	
    	try {
			while(rs.next()) {
				Customer cust = new Customer();
				cust.setId(rs.getInt("cust_id"));
				cust.setName(rs.getString("cust_name"));
				cust.setMo_no(rs.getString("cust_mo_no"));
				cust.setPhone_no(rs.getString("cust_phone_no"));
				cust.setEmail(rs.getString("cust_email"));
				cust.setAddress(rs.getString("cust_address"));
				cust.setDesc(rs.getString("cust_desc"));
				cust.setComp_id(rs.getInt("company_id"));
				list.add(cust);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return list;
    }
    
    private List<Company> getCompanyDataByQuery(String sql){
    	ResultSet rs = dh.executeQuery(sql);
    	List<Company> list = new ArrayList<Company>();
    	
    	try {
			while(rs.next()) {
				Company comp = new Company();
				comp.setId(rs.getInt("comp_id"));
				comp.setName(rs.getString("comp_name"));
				comp.setMo_no(rs.getString("comp_mo_no"));
				comp.setPhone_no(rs.getString("comp_phone_no"));
				comp.setEmail(rs.getString("comp_email"));
				comp.setAddress(rs.getString("comp_address"));
				comp.setDesc(rs.getString("comp_desc"));
				list.add(comp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return list;
    }
    
    private List<Bill> getBillDataByQuery(String sql){
    	ResultSet rs = dh.executeQuery(sql);
    	List<Bill> list = new ArrayList<Bill>();
    	
    	try {
			while(rs.next()) {
				Bill bill = new Bill();
				bill.setId(rs.getInt("bill_id"));
				bill.setCompany_id(rs.getInt("bill_company_id"));
				bill.setCustomer_id(rs.getInt("bill_customer_id"));
				bill.setDate(rs.getString("bill_date"));
				bill.setDebitAmount(rs.getString("bill_debit_amount"));
				bill.setPaidAmount(rs.getString("bill_paid_amount"));
				bill.setTotal(rs.getString("bill_total_amount"));
				
				list.add(bill);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return list;
    }
    
    private final class MyTreeCell extends TextFieldTreeCell<String>
    {
        private ContextMenu rootContextMenu;
        private ContextMenu parentContextMenu;
        private ContextMenu childContextMenu;
        private ContextMenu subChildContextMenu;

        public MyTreeCell()
        {
        	initRootContextMenu();
        	initParentContextMenu();
        	initChildContextMenu();
        	initSubChidContextMenu();
        	setContextMenu(this.rootContextMenu);
        }
        
        private void initRootContextMenu(){
        	ContextMenu menu = new ContextMenu();
        	MenuItem add = new MenuItem("Add");
        	add.setOnAction(e->{
        		try {
        			System.out.println("Test");
        			initCompanyView(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	});
        	MenuItem refresh = new MenuItem("Refresh");
        	add.setOnAction(e->{
        		reloadTreeView();
        	});
        	menu.getItems().addAll(add,refresh);
        	this.rootContextMenu = menu;
        }
        
        private void initParentContextMenu() {
        	ContextMenu menu = new ContextMenu();
        	MenuItem add1 = new MenuItem("Add");
        	add1.setOnAction(e->{
        		try {
        			initCustomerView(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	});
        	MenuItem edit = new MenuItem("Edit");
        	edit.setOnAction(e->{
        		try {
        			initCompanyView(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	});
        	MenuItem del = new MenuItem("Delete");
        	del.setOnAction(e->{
        		deleteCompany();
        		reloadTreeView();
        	});
        	menu.getItems().addAll(add1,edit,del);
        	this.parentContextMenu = menu;
        }
        private void initChildContextMenu(){
        	ContextMenu menu = new ContextMenu();
        	MenuItem add = new MenuItem("Add");
        	add.setOnAction(e->{
        		try {
        			initBillView(false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	});
        	MenuItem edit = new MenuItem("Edit");
        	edit.setOnAction(e->{
        		try {
        			initCustomerView(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	});
        	MenuItem del = new MenuItem("Delete");
        	del.setOnAction(e->{
        		deleteCustomer();
        		reloadTreeView();
        	});
        	menu.getItems().addAll(add,edit,del);
        	this.childContextMenu = menu;
        }
        
        private void deleteCompany() {
        	Company comp = getCompany();
        	for (Bill bill : getBillDataByQuery("select * from adesai35_customer_bill where bill_company_id = "+comp.getId())) {
        		dh.executeUpdate("Delete from adesai35_customer_bill_items where bill_id = " + bill.getId());
			}
        	
        	dh.executeUpdate("Delete from adesai35_customer_bill where bill_company_id = "+comp.getId());
        	dh.executeUpdate("Delete from adesai35_customer where company_id = "+comp.getId());
        	dh.executeUpdate("Delete from adesai35_company where comp_id = "+comp.getId());
        }
        
        private void deleteCustomer() {
        	Customer cust = getCustomer();
        	for (Bill bill : getBillDataByQuery("select * from adesai35_customer_bill where bill_customer_id = "+cust.getId())) {
        		dh.executeUpdate("Delete from adesai35_customer_bill_items where bill_id = " + bill.getId());
			}
        	
        	dh.executeUpdate("Delete from adesai35_customer_bill where bill_customer_id = "+cust.getId());
        	dh.executeUpdate("Delete from adesai35_customer where cust_id = "+cust.getId());
        }
        
        private void deleteBill() {
        	dh.executeQuery("Delete from adesai35_customer_bill_items where bill_id = "+getTreeItem().getValue());
        	dh.executeUpdate("Delete from adesai35_customer_bill where bill_id = "+getTreeItem().getValue());
        }
        
        private void initSubChidContextMenu() {
        	ContextMenu menu = new ContextMenu();
        	MenuItem add = new MenuItem("Edit");
        	add.setOnAction(e->{
        		try {
        			initBillView(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	});
        	MenuItem del = new MenuItem("Delete");
        	del.setOnAction(e->{
        		deleteBill();
        		reloadTreeView();
        	});
        	menu.getItems().addAll(add,del);
        	this.subChildContextMenu = menu;
        }
        
        private void initCompanyView(boolean isEdit) throws IOException {
        	Stage window = new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddEditCompanyView.fxml"));
    		if(isEdit) {
    		AddEditCompanyController controller = loader.getController();
    		Company comp =  getCompany();
    		//if(comp!=null)controller.initData(comp);
    		}
    		Parent root = (Parent) loader.load();
    		System.out.println("1");
    		window.setTitle(isEdit ? "Edit Company" : "Add Company");
    		window.initModality(Modality.APPLICATION_MODAL);
    		Scene scene = new Scene(root);
    		window.setScene(scene);
    		System.out.println("2");
    		window.showAndWait();
        }
        
        private Company getCompany() {
        	Company result = null;
        	List<Company> list = getCompanyDataByQuery("select * from adesai35_company where comp_name = '"+getTreeItem().getValue()+"'");
        	
        	if(list.size() == 1) {
        		result = list.get(0);
        	}
        	return result;
        }
        
        private Customer getCustomer() {
        	Customer result = null;
        	List<Customer> list = getCustomerDataByQuery("select * from adesai35_customer where cust_name = '"+getTreeItem().getValue()+"'");
        	
        	if(list.size() == 1) {
        		result = list.get(0);
        	}
        	return result;
        }
        
        private Bill getBill() {
        	Bill result = null;
        	List<Bill> list = getBillDataByQuery("select * from adesai35_customer_bill where bill_id = "+getTreeItem().getValue());
        	
        	if(list.size() == 1) {
        		result = list.get(0);
        	}
        	return result;
        }
        
        private void initCustomerView(boolean isEdit) throws IOException {
        	Stage window = new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddEditCustomerView.fxml"));
    		Parent root = (Parent) loader.load();
    		window.setTitle(isEdit ? "Edit Customer" : "Add Customer");
    		window.initModality(Modality.APPLICATION_MODAL);
    		Scene scene = new Scene(root);
    		window.setScene(scene);
    		window.showAndWait();
        }
        
        private void initBillView(boolean isEdit) throws IOException {
        	Stage window = new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CustomerBillView.fxml"));
    		Parent root = (Parent) loader.load();
    		window.setTitle(isEdit ? "Edit Bill" : "Add Bill");
    		window.initModality(Modality.APPLICATION_MODAL);
    		Scene scene = new Scene(root);
    		window.setScene(scene);
    		window.showAndWait();
        }
        
        @Override
        public void updateItem(String item, boolean empty)
        {
            super.updateItem(item, empty);
            if(!empty) {
	            TreeItem<String> ti = getTreeItem();
	            if(ti.getParent().getParent() != null && ti.getParent().getParent().getParent() != null &&  ti.getParent().getParent().getParent().getParent() == null) {
	            	setContextMenu(this.subChildContextMenu);
	            }else if(ti.getParent().getParent() != null && ti.getParent().getParent().getParent() == null) {
	            	setContextMenu(this.childContextMenu);
	            } else if(ti.getParent().getParent() == null) {
	            	setContextMenu(this.parentContextMenu);
	            } else {
	            	setContextMenu(this.rootContextMenu);
	            }
	            // if the item is not empty and is a root...
	            if (!empty && getTreeItem().getParent() == null)
	            {
	                setContextMenu(rootContextMenu);
	            }
            }
        }
    }
    
}
