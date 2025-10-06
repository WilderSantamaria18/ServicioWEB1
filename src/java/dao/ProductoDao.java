package dao;

import dbase.Conexion;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Producto;

public class ProductoDao {

    public ProductoDao() {
    }

    public Producto findById(int id) {
        Producto p = null;
        String sql = "SELECT id, modelo, precio, almacenamiento, color, stock FROM producto WHERE id = ?";

        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setModelo(rs.getString("modelo"));
                    p.setPrecio(rs.getBigDecimal("precio"));
                    p.setAlmacenamiento(rs.getString("almacenamiento"));
                    p.setColor(rs.getString("color"));
                    p.setStock(rs.getInt("stock"));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return p;
    }

    public List<Producto> findAll() {
        List<Producto> list = new ArrayList<>();
        String sql = "SELECT id, modelo, precio, almacenamiento, color, stock FROM producto ORDER BY id";

        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setModelo(rs.getString("modelo"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setAlmacenamiento(rs.getString("almacenamiento"));
                p.setColor(rs.getString("color"));
                p.setStock(rs.getInt("stock"));
                list.add(p);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public int insert(Producto producto) {
        int generatedId = -1;
        String sql = "INSERT INTO producto (modelo, precio, almacenamiento, color, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getModelo());
            BigDecimal precio = producto.getPrecio();
            if (precio != null) {
                ps.setBigDecimal(2, precio);
            } else {
                ps.setNull(2, java.sql.Types.DECIMAL);
            }
            ps.setString(3, producto.getAlmacenamiento());
            ps.setString(4, producto.getColor());
            ps.setInt(5, producto.getStock());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        generatedId = keys.getInt(1);
                        producto.setId(generatedId);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return generatedId;
    }

    public boolean update(Producto producto) {
        String sql = "UPDATE producto SET modelo = ?, precio = ?, almacenamiento = ?, color = ?, stock = ? WHERE id = ?";
        boolean ok = false;

        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, producto.getModelo());
            BigDecimal precio = producto.getPrecio();
            if (precio != null) {
                ps.setBigDecimal(2, precio);
            } else {
                ps.setNull(2, java.sql.Types.DECIMAL);
            }
            ps.setString(3, producto.getAlmacenamiento());
            ps.setString(4, producto.getColor());
            ps.setInt(5, producto.getStock());
            ps.setInt(6, producto.getId());

            int affected = ps.executeUpdate();
            ok = affected > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ok;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM producto WHERE id = ?";
        boolean ok = false;

        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            ok = affected > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ok;
    }

}
