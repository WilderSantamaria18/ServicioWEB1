/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WS;

import java.math.BigDecimal;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import dao.ProductoDao;
import model.Producto;

/**
 *
 * @author USER
 */
@WebService(serviceName = "WS01")
public class WS01 {

    private ProductoDao productoDao = new ProductoDao();

    @WebMethod(operationName = "listarProductos")
    public Producto[] listarProductos() {
        try {
            List<Producto> lista = productoDao.findAll();
            return lista.toArray(new Producto[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return new Producto[0];
        }
    }

    @WebMethod(operationName = "obtenerProducto")
    public Producto obtenerProducto(@WebParam(name = "id") int id) {
        try {
            return productoDao.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @WebMethod(operationName = "agregarProducto")
    public int agregarProducto(
            @WebParam(name = "modelo") String modelo,
            @WebParam(name = "precio") BigDecimal precio,
            @WebParam(name = "almacenamiento") String almacenamiento,
            @WebParam(name = "color") String color,
            @WebParam(name = "stock") int stock) {
        try {
            Producto p = new Producto();
            p.setModelo(modelo);
            p.setPrecio(precio);
            p.setAlmacenamiento(almacenamiento);
            p.setColor(color);
            p.setStock(stock);
            return productoDao.insert(p);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @WebMethod(operationName = "editarProducto")
    public boolean editarProducto(
            @WebParam(name = "id") int id,
            @WebParam(name = "modelo") String modelo,
            @WebParam(name = "precio") BigDecimal precio,
            @WebParam(name = "almacenamiento") String almacenamiento,
            @WebParam(name = "color") String color,
            @WebParam(name = "stock") int stock) {
        try {
            Producto p = new Producto();
            p.setId(id);
            p.setModelo(modelo);
            p.setPrecio(precio);
            p.setAlmacenamiento(almacenamiento);
            p.setColor(color);
            p.setStock(stock);
            return productoDao.update(p);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @WebMethod(operationName = "eliminarProducto")
    public boolean eliminarProducto(@WebParam(name = "id") int id) {
        try {
            return productoDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}