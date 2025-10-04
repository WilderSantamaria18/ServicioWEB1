package app;

import dao.ProductoDao;
import model.Producto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class app {

    public static void main(String[] args) {
        ProductoDao dao = new ProductoDao();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== CRUD PRODUCTOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Crear");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1: // LISTAR
                    System.out.println("\n--- LISTA DE PRODUCTOS ---");
                    List<Producto> productos = dao.findAll();
                    for (Producto p : productos) {
                        System.out.println("ID: " + p.getId() + " | " + p.getModelo() + " | $" + p.getPrecio() + " | "
                                + p.getColor() + " | Stock: " + p.getStock());
                    }
                    break;

                case 2: // BUSCAR
                    System.out.print("ID a buscar: ");
                    int idBuscar = sc.nextInt();
                    sc.nextLine();
                    Producto prod = dao.findById(idBuscar);
                    if (prod != null) {
                        System.out.println("Encontrado: " + prod.getModelo() + " - $" + prod.getPrecio());
                    } else {
                        System.out.println("No encontrado");
                    }
                    break;

                case 3: // CREAR
                    System.out.println("\n--- CREAR PRODUCTO ---");
                    Producto nuevo = new Producto();
                    System.out.print("Modelo: ");
                    nuevo.setModelo(sc.nextLine());
                    System.out.print("Precio: ");
                    nuevo.setPrecio(new BigDecimal(sc.nextLine()));
                    System.out.print("Almacenamiento: ");
                    nuevo.setAlmacenamiento(sc.nextLine());
                    System.out.print("Color: ");
                    nuevo.setColor(sc.nextLine());
                    System.out.print("Stock: ");
                    nuevo.setStock(sc.nextInt());
                    sc.nextLine();

                    int id = dao.insert(nuevo);
                    if (id > 0) {
                        System.out.println("Producto creado con ID: " + id);
                    } else {
                        System.out.println("Error al crear");
                    }
                    break;

                case 4: // ACTUALIZAR
                    System.out.print("ID a actualizar: ");
                    int idActualizar = sc.nextInt();
                    sc.nextLine();
                    Producto actualizar = dao.findById(idActualizar);
                    if (actualizar != null) {
                        System.out.print("Modelo [" + actualizar.getModelo() + "]: ");
                        String modelo = sc.nextLine();
                        if (!modelo.isEmpty())
                            actualizar.setModelo(modelo);

                        System.out.print("Precio [" + actualizar.getPrecio() + "]: ");
                        String precio = sc.nextLine();
                        if (!precio.isEmpty())
                            actualizar.setPrecio(new BigDecimal(precio));

                        System.out.print("Color [" + actualizar.getColor() + "]: ");
                        String color = sc.nextLine();
                        if (!color.isEmpty())
                            actualizar.setColor(color);

                        System.out.print("Stock [" + actualizar.getStock() + "]: ");
                        String stock = sc.nextLine();
                        if (!stock.isEmpty())
                            actualizar.setStock(Integer.parseInt(stock));

                        if (dao.update(actualizar)) {
                            System.out.println("Actualizado correctamente");
                        } else {
                            System.out.println("Error al actualizar");
                        }
                    } else {
                        System.out.println("Producto no encontrado");
                    }
                    break;

                case 5: // ELIMINAR
                    System.out.print("ID a eliminar: ");
                    int idEliminar = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Confirmar (S/N): ");
                    String confirm = sc.nextLine();
                    if (confirm.equalsIgnoreCase("S")) {
                        if (dao.delete(idEliminar)) {
                            System.out.println("Eliminado correctamente");
                        } else {
                            System.out.println("Error al eliminar");
                        }
                    }
                    break;

                case 0:
                    System.out.println("Saliendo...");
                    break;
            }
        } while (opcion != 0);

        sc.close();
    }
}
