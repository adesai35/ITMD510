package Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	initCategoryTab();
    	initHistoryTab();
    }
    
    private void initCategoryTab() {
    	if(this.tabLeftPane.getTabs().size() > 0) reloadTreeView();
    	else {
    		Tab category = new Tab("Category");
    		reloadTreeView();
    		category.setContent(this.categoryTreeView);
    		this.tabLeftPane.getTabs().add(category);
    	}
    }
    
    private void initHistoryTab() {
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
    
    private void initCompanyTab() {
    	
    }
    
    private void initCustomerTab() {
    	
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
        	MenuItem add = new MenuItem("Add");
        	add.setOnAction(e->{
        		
        	});
        	MenuItem edit = new MenuItem("Edit");
        	edit.setOnAction(e->{
        		
        	});
        	MenuItem del = new MenuItem("Delete");
        	del.setOnAction(e->{
        		
        	});
        	menu.getItems().addAll(add,edit,del);
        	this.parentContextMenu = menu;
        }
        private void initChildContextMenu(){
        	ContextMenu menu = new ContextMenu();
        	MenuItem add = new MenuItem("Add");
        	add.setOnAction(e->{
        		
        	});
        	MenuItem edit = new MenuItem("Edit");
        	edit.setOnAction(e->{
        		
        	});
        	MenuItem del = new MenuItem("Delete");
        	del.setOnAction(e->{
        		
        	});
        	menu.getItems().addAll(add,edit,del);
        	this.childContextMenu = menu;
        }
        
        private void initSubChidContextMenu() {
        	ContextMenu menu = new ContextMenu();
        	MenuItem add = new MenuItem("Edit");
        	add.setOnAction(e->{
        		
        	});
        	MenuItem del = new MenuItem("Delete");
        	del.setOnAction(e->{
        		
        	});
        	menu.getItems().addAll(add,del);
        	this.subChildContextMenu = menu;
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
