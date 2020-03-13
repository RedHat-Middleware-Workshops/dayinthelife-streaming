package com.redhat.cloudnative;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InventoryNotification {
    private Integer orderId;
    private Integer itemId;
    private Integer quantity;
    private String department;
    private Date datetime;
    private String flavor;
    private String inventoryId;

    public static InventoryNotification getInventoryNotification(Order order ){
        InventoryNotification invenNotification  = new InventoryNotification();
        invenNotification.setOrderId(order.getOrderId());
        invenNotification.setItemId(order.getItemId());
        invenNotification.setDepartment("inventory");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        invenNotification.setDatetime(new Date(System.currentTimeMillis()));
        invenNotification.setQuantity(order.getQuantity());
        invenNotification.setFlavor(order.getOrderItemName());
        DecimalFormat numformat = new DecimalFormat("#");
        invenNotification.setInventoryId(numformat.format(Math.floor(100000 + Math.random() * 999999)) );
        return invenNotification;
    }


    public void setOrderId(Integer orderId){
        this.orderId=orderId;
    }
    public void setItemId(Integer itemId){
        this.itemId=itemId;
    }
    public void setQuantity(Integer quantity){
        this.quantity=quantity;
    }
    public Integer getOrderId(){
        return this.orderId;
    }
    public Integer getItemId(){
        return this.itemId;
    }
    public Integer getQuantity(){
        return this.quantity;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }


	public String getFlavor() {
		return flavor;
	}


	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}


	public String getInventoryId() {
		return inventoryId;
	}


	public void setInventoryId(String id) {
		this.inventoryId = id;
	}
}