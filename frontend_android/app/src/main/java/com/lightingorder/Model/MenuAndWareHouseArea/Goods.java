package com.lightingorder.Model.MenuAndWareHouseArea;

import java.util.ArrayList;
import java.util.List;

/**JSON Format:
 * {
 *      id : "goods_id"
 *      quantity : "goods_quantity"
 *      priceAsAdditive : "price_as_additive_value"
 *      quantityLowerBound:"quantityLowerBound_value"
 *      name:"goods_name"
 *      unitPrice:"unitPriceName"
 *      inStock : "inStock_value"
 *      menuItems : ["menuItemName1", "MenuItemName2", ... , "MenuItemNameN"]
 * }
 * */
public class Goods {
    private String id;
    private int quantity;
    private double priceAsAdditive;
    private int quantityLowerBound;
    private String name;
    private double unitPrice;
    private boolean inStock;
    private List<String> menuItems;


    public Goods(String id, int quantity, double priceAsAdditive, int quantityLowerBound, String name, double unitPrice, boolean inStock) {
        this.id = id;
        this.quantity = quantity;
        this.priceAsAdditive = priceAsAdditive;
        this.quantityLowerBound = quantityLowerBound;
        this.name = name;
        this.unitPrice = unitPrice;
        this.inStock = inStock;
        this.menuItems = new ArrayList<String>();
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id;}

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity;}

    public double getPriceAsAdditive() {  return priceAsAdditive;}

    public void setPriceAsAdditive(double priceAsAdditive) { this.priceAsAdditive = priceAsAdditive;}

    public int getQuantityLowerBound() { return quantityLowerBound; }

    public void setQuantityLowerBound(int quantityLowerBound) { this.quantityLowerBound = quantityLowerBound; }

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public double getUnitPrice() { return unitPrice; }

    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public boolean isInStock() { return inStock; }

    public void setStock(boolean s){ this.inStock = s;}

    public void setInStock() { this.inStock = true;}

    public void setOutOfStock(){this.inStock = false;}

}
