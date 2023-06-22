package com.redhat.eventdriven;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.quarkus.runtime.annotations.RegisterForReflection;

@JsonIgnoreProperties(ignoreUnknown = true)
@RegisterForReflection
public class Order implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    
    private Integer orderId;
    private Integer itemId;
    private String orderItemName;
    private Integer quantity;
    private Integer price;
    private String address;
    private Integer zipCode;
    private String datetime;
    private String department;

    public void setDepartment(String department){
        this.department = department;
    }
    public String getDepartment(){
        return "Inventory";
    }
    public void setDatetime(String datetime){
        this.datetime = datetime;
    }
    public String getDatetime(){
        return "2019-08-08 22:19:99";
    }
    public void setOrderId(Integer orderId){
        this.orderId=orderId;
    }
    public void setItemId(Integer itemId){
        this.itemId=itemId;
    }
    public void setOrderItemName(String orderItemName){
        this.orderItemName=orderItemName;
    }
    public void setQuantity(Integer quantity){
        this.quantity=quantity;
    }
    public void setPrice(Integer price){
        this.price=price;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public void setZipCode(Integer zipCode){
        this.zipCode=zipCode;
    }
    public Integer getOrderId(){
        return this.orderId;
    }
    public Integer getItemId(){
        return this.itemId;
    }
    public String getOrderItemName(){
        return this.orderItemName;
    }
    public Integer getQuantity(){
        return this.quantity;
    }
    public Integer getPrice(){
        return this.price;
    }
    public String getAddress(){
        return this.address;
    }
    public Integer getZipCode(){
        return this.zipCode;
    }
}