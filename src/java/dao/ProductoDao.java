package dao; // paquete que agrupa las clases DAOs de la aplicación

import dbase.Conexion; // clase de utilidad que devuelve conexiones a la BD
import java.math.BigDecimal; // tipo para representar dinero/decimales
import java.sql.Connection; // interfaz JDBC para la conexión a la BD
import java.sql.PreparedStatement; // interfaz JDBC para sentencias precompiladas
import java.sql.ResultSet; // interfaz JDBC para resultados de consultas
import java.sql.Statement; // interfaz JDBC para constantes como RETURN_GENERATED_KEYS
import java.util.ArrayList; // implementación de lista dinámica
import java.util.List; // interfaz de colecciones para listas
import model.Producto; // modelo/VO que representa una fila de la tabla `producto`

/**
 * DAO de acceso a datos para la tabla `producto`.
 *
 * Este módulo implementa operaciones CRUD básicas (Create, Read, Update,
 * Delete)
 * sobre la tabla `producto` usando JDBC. Cada método obtiene una conexión
 * mediante
 * `Conexion.MySQL()` y usa try-with-resources para asegurar el cierre de
 * Connection/PreparedStatement/ResultSet.
 *
 * Resumen del comportamiento:
 * - findById: devuelve un objeto `Producto` o null si no existe o hay error.
 * - findAll: devuelve una lista (posiblemente vacía) de `Producto`.
 * - insert: inserta un producto y devuelve el id generado (o -1 en error).
 * - update: actualiza un producto por id y devuelve true si se modificó alguna
 * fila.
 * - delete: elimina un producto por id y devuelve true si se eliminó alguna
 * fila.
 *
 * Nota sobre errores: las excepciones se capturan e imprimen (stack trace). No
 * se
 * re-lanzan, por lo que el llamador debe validar los valores de retorno para
 * detectar fallos.
 */
/**
 * DAO de acceso a datos para la tabla `producto`.
 */
public class ProductoDao { // clase que encapsula operaciones CRUD sobre `producto`

    public ProductoDao() { // constructor por defecto (sin estado)
    }

    /**
     * Constructor por defecto. No mantiene estado entre instancias.
     */

    public Producto findById(int id) { // busca un producto por su id
        Producto p = null; // variable que contendrá el producto encontrado (o null)
        String sql = "SELECT id, modelo, precio, almacenamiento, color, stock FROM producto WHERE id = ?"; // consulta
                                                                                                           // SQL
                                                                                                           // parametrizada

        // try-with-resources: abre la conexión y el PreparedStatement y los cierra
        // automáticamente
        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id); // asigna el parámetro ? con el id recibido
            try (ResultSet rs = ps.executeQuery()) { // ejecuta la consulta y obtiene el ResultSet
                if (rs.next()) { // si hay una fila, mapea columna a objeto
                    p = new Producto(); // crea instancia del VO
                    p.setId(rs.getInt("id")); // mapea columna "id"
                    p.setModelo(rs.getString("modelo")); // mapea columna "modelo"
                    p.setPrecio(rs.getBigDecimal("precio")); // mapea columna "precio"
                    p.setAlmacenamiento(rs.getString("almacenamiento")); // mapea columna "almacenamiento"
                    p.setColor(rs.getString("color")); // mapea columna "color"
                    p.setStock(rs.getInt("stock")); // mapea columna "stock"
                }
            }

        } catch (Exception ex) { // captura SQLException y otras excepciones
            ex.printStackTrace(); // imprime el stack trace (registro simple de error)
        }

        return p; // devuelve el producto o null si no existe/error
    }

    public List<Producto> findAll() { // recupera todos los productos
        List<Producto> list = new ArrayList<>(); // lista donde se almacenarán los resultados
        String sql = "SELECT id, modelo, precio, almacenamiento, color, stock FROM producto ORDER BY id"; // consulta
                                                                                                          // SQL

        // abre conexión, prepara la sentencia y ejecuta la consulta (ResultSet) en
        // try-with-resources
        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) { // itera cada fila del ResultSet
                Producto p = new Producto(); // crea VO por cada fila
                p.setId(rs.getInt("id")); // mapea id
                p.setModelo(rs.getString("modelo")); // mapea modelo
                p.setPrecio(rs.getBigDecimal("precio")); // mapea precio
                p.setAlmacenamiento(rs.getString("almacenamiento")); // mapea almacenamiento
                p.setColor(rs.getString("color")); // mapea color
                p.setStock(rs.getInt("stock")); // mapea stock
                list.add(p); // añade el VO a la lista de resultados
            }

        } catch (Exception ex) { // captura errores de BD
            ex.printStackTrace(); // imprime el error
        }

        return list; // devuelve la lista (vacía si no hay resultados o hubo error)
    }

    public int insert(Producto producto) { // inserta un nuevo producto y devuelve el id generado
        int generatedId = -1; // valor por defecto si falla la inserción
        String sql = "INSERT INTO producto (modelo, precio, almacenamiento, color, stock) VALUES (?, ?, ?, ?, ?)"; // SQL
                                                                                                                   // de
                                                                                                                   // inserción

        // preparar la sentencia con RETURN_GENERATED_KEYS para obtener el id
        // autogenerado
        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getModelo()); // asigna el campo modelo
            BigDecimal precio = producto.getPrecio(); // obtiene el precio del VO
            if (precio != null) { // si el precio no es null lo asigna
                ps.setBigDecimal(2, precio);
            } else { // si es null, inserta un NULL en la columna decimal
                ps.setNull(2, java.sql.Types.DECIMAL);
            }
            ps.setString(3, producto.getAlmacenamiento()); // asigna almacenamiento
            ps.setString(4, producto.getColor()); // asigna color
            ps.setInt(5, producto.getStock()); // asigna stock (int)

            int affected = ps.executeUpdate(); // ejecuta la inserción y devuelve filas afectadas
            if (affected > 0) { // si se insertó al menos una fila
                try (ResultSet keys = ps.getGeneratedKeys()) { // obtiene claves generadas
                    if (keys.next()) { // si hay clave generada
                        generatedId = keys.getInt(1); // lee el id (primera columna)
                        producto.setId(generatedId); // asigna el id al objeto pasado
                    }
                }
            }

        } catch (Exception ex) { // captura errores al insertar
            ex.printStackTrace(); // registro simple del error
        }

        return generatedId; // devuelve id generado o -1 si falló
    }

    public boolean update(Producto producto) { // actualiza un producto existente
        String sql = "UPDATE producto SET modelo = ?, precio = ?, almacenamiento = ?, color = ?, stock = ? WHERE id = ?"; // SQL
                                                                                                                          // de
                                                                                                                          // actualización
        boolean ok = false; // indicador de éxito

        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql)) { // prepara la sentencia

            ps.setString(1, producto.getModelo()); // asigna modelo al parámetro 1
            BigDecimal precio = producto.getPrecio(); // obtiene precio del VO
            if (precio != null) { // si existe precio lo asigna
                ps.setBigDecimal(2, precio);
            } else { // si es null, setea NULL en la columna
                ps.setNull(2, java.sql.Types.DECIMAL);
            }
            ps.setString(3, producto.getAlmacenamiento()); // asigna almacenamiento
            ps.setString(4, producto.getColor()); // asigna color
            ps.setInt(5, producto.getStock()); // asigna stock
            ps.setInt(6, producto.getId()); // asigna id en la cláusula WHERE

            int affected = ps.executeUpdate(); // ejecuta la actualización
            ok = affected > 0; // true si al menos una fila fue afectada

        } catch (Exception ex) { // captura errores SQL
            ex.printStackTrace(); // imprime el stack trace
        }

        return ok; // devuelve true si se actualizó
    }

    public boolean delete(int id) { // elimina un producto por id
        String sql = "DELETE FROM producto WHERE id = ?"; // SQL de borrado
        boolean ok = false; // indicador de éxito

        try (Connection conn = Conexion.MySQL();
                PreparedStatement ps = conn.prepareStatement(sql)) { // prepara la sentencia

            ps.setInt(1, id); // asigna el id al parámetro
            int affected = ps.executeUpdate(); // ejecuta el borrado
            ok = affected > 0; // true si se borró alguna fila

        } catch (Exception ex) { // captura errores
            ex.printStackTrace(); // imprime el error
        }

        return ok; // devuelve true si se eliminó el registro
    }

}
