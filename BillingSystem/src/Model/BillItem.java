package Model;

public class BillItem {
	private int id;
	private String product_item;
	private String dec;
	private String width;
	private String height;
	private String quantity;
	private String PPR;
	private String product_amount;
	private int bill_id;
	public String getProduct_amount() {
		return product_amount;
	}
	public void setProduct_amount(String product_amount) {
		this.product_amount = product_amount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProduct_item() {
		return product_item;
	}
	public void setProduct_item(String product_item) {
		this.product_item = product_item;
	}
	public String getDec() {
		return dec;
	}
	public void setDec(String dec) {
		this.dec = dec;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getPPR() {
		return PPR;
	}
	public void setPPR(String pPR) {
		PPR = pPR;
	}
	public int getBill_id() {
		return bill_id;
	}
	public void setBill_id(int bill_id) {
		this.bill_id = bill_id;
	}
}
