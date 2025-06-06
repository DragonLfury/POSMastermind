/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lexso.util;

/**
 *
 * @author Hirusha
 */
public class InvoiceItem {
    
    private String StockID;
    private String CategoryName;
    private String productID;
    private String name;
    private String qty;
    private String sellingPrice;
    private String mfd;
    private String exp;
    private String discount;

    /**
     * @return the StockID
     */
    public String getStockID() {
        return StockID;
    }

    /**
     * @param StockID the StockID to set
     */
    public void setStockID(String StockID) {
        this.StockID = StockID;
    }

    /**
     * @return the category
     */
    public String getCategoryName() {
        return CategoryName;
    }

   
    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }
    /**
     * @return the brand
     */
    public String getProductID() {
        return productID;
    }

    
    public void setProductID(String productID) {
        this.productID = productID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the qty
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return the sellingPrice
     */
    public String getSellingPrice() {
        return sellingPrice;
    }

    /**
     * @param sellingPrice the sellingPrice to set
     */
    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
     /**
     * @return the discount
     */
    public String getDiscount() {
        return discount;
    }

    
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    /**
     * @return the mfd
     */
    public String getMfd() {
        return mfd;
    }

    /**
     * @param mfd the mfd to set
     */
    public void setMfd(String mfd) {
        this.mfd = mfd;
    }

    /**
     * @return the exp
     */
    public String getExp() {
        return exp;
    }

    /**
     * @param exp the exp to set
     */
    public void setExp(String exp) {
        this.exp = exp;
    }
    
}
