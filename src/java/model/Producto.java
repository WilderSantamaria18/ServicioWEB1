
package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String modelo;
    private BigDecimal precio;
    private String almacenamiento;
    private String color;
    private int stock;
    
    // Constructores
    public Producto() {}
    
    public Producto(int id, String modelo, BigDecimal precio, 
                   String almacenamiento, String color, int stock) {
        this.id = id;
        this.modelo = modelo;
        this.precio = precio;
        this.almacenamiento = almacenamiento;
        this.color = color;
        this.stock = stock;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    
    public String getAlmacenamiento() { return almacenamiento; }
    public void setAlmacenamiento(String almacenamiento) { this.almacenamiento = almacenamiento; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}