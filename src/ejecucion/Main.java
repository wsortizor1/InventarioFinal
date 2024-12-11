package ejecucion;

import inventario.Inventario;
import producto.Producto;

import java.io.BufferedWriter;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	public static String rutaArchivo = "../productos.txt";
	public static String rutaArchivoReporte = "../reporte_inventario.txt";
    public static void main(String[] args) throws IOException {
        Inventario inventario = new Inventario();
        List<Producto> productos = Inventario.leerProductos(rutaArchivo);
        
       // inventario.cargarDesdeArchivo(rutaArchivo);
        for (Producto producto : productos) {
            System.out.println(producto.toString());
        }

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    agregarProducto(scanner, inventario);
                    break;
                case 2:
                    actualizarProducto(scanner, inventario,rutaArchivo);
                    break;
                case 3:
                    eliminarProducto(scanner, inventario);
                    break;
                case 4:
                    buscarProductoPorCategoria(scanner, inventario);
                    break;
                case 5:
                	buscarProductoPorNombre(scanner, inventario);
                    break;
                case 6:
                    cantidadProductosPorCategoria(scanner, inventario);
                    break;
                case 7:
                    mostrarProductoMasCaro(inventario);
                    break;
                case 8:
                	generarReporte(inventario);
                    break;
                case 9:
                	System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opci√≥n no v√°lida.");
            }

        } while (opcion != 8);

        scanner.close();
        inventario.guardarEnArchivo(rutaArchivo); // Guardar los cambios al finalizar
    }

    private static void mostrarMenu() {
        System.out.println("\n---- Men√∫ de opciones ----");
        System.out.println("1. Agregar producto");
        System.out.println("2. Actualizar producto");
        System.out.println("3. Eliminar producto");
        System.out.println("4. Buscar por categor√≠a");
        System.out.println("5. Buscar por nombre");       
        System.out.println("6. Cantidad de productos por categor√≠a");
        System.out.println("7. Producto m√°s caro");
        System.out.println("8. Generar reporte");
        System.out.println("9. Salir");
        System.out.print("Elija una opci√≥n: ");
    }

    private static void agregarProducto(Scanner scanner, Inventario inventario) {
        try {
            System.out.print("Ingrese el ID del producto: ");
            String idProducto = scanner.nextLine();
            System.out.print("Ingrese el nombre del producto: ");
            String nombreProducto = scanner.nextLine();
            System.out.print("Ingrese la categorÌa del producto: ");
            String categoria = scanner.nextLine();

            System.out.print("Ingrese el precio del producto: ");
            double precio = scanner.nextDouble();
            if (precio <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor que 0.");
            }

            System.out.print("Ingrese la cantidad del producto: ");
            int cantidad = scanner.nextInt();
            if (cantidad < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa.");
            }

            scanner.nextLine(); // Limpiar buffer

            // Crear un nuevo producto
            Producto producto = new Producto(idProducto, nombreProducto, categoria, precio, cantidad);

            // Verificar si el producto ya existe en el inventario (opcional)
            if (inventario.existeProducto(idProducto)) {
                System.out.println("El producto con ID '" + idProducto + "' ya existe en el inventario.");
                return;
            }

            // Agregar el producto al inventario
            inventario.agregarProducto(producto);

            // Escribir el producto en el archivo
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) { // 'true' para agregar
                String nuevaLinea = idProducto + "," + nombreProducto + "," + categoria + "," + precio + "," + cantidad;
                escritor.write(nuevaLinea);
                escritor.newLine(); // Agregar una nueva lÌnea
            } catch (IOException e) {
                System.err.println("OcurriÛ un error al escribir en el archivo: " + e.getMessage());
            }

            System.out.println("Producto agregado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error en los datos ingresados: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("OcurriÛ un error inesperado: " + e.getMessage());
        }
    }

    private static void actualizarProducto(Scanner scanner, Inventario inventario, String rutaArchivo) throws IOException {
        System.out.print("Ingrese el ID del producto a actualizar: ");
        String idProducto = scanner.nextLine();
        System.out.print("Ingrese el nuevo nombre del producto: ");
        String nombreProducto = scanner.nextLine();
        System.out.print("Ingrese la nueva categorÌa: ");
        String categoria = scanner.nextLine();
        System.out.print("Ingrese el nuevo precio: ");
        double precio = scanner.nextDouble();
        System.out.print("Ingrese la nueva cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        if (inventario.actualizarProducto(idProducto, nombreProducto, categoria, precio, cantidad, rutaArchivo)) {
            System.out.println("Producto actualizado exitosamente.");
        } else {
            System.out.println("Producto no encontrado.");
        }
    }



    private static void eliminarProducto(Scanner scanner, Inventario inventario) throws IOException {
        System.out.print("Ingrese el ID del producto a eliminar: ");
        String idProducto = scanner.nextLine();
        if (inventario.eliminarProducto(idProducto,rutaArchivo)) {
            System.out.println("Producto eliminado exitosamente.");
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void buscarProductoPorCategoria(Scanner scanner, Inventario inventario) {
        System.out.print("Ingrese la categorÌa a buscar: ");
        String categoria = scanner.nextLine();

        List<Producto> productosEncontrados = inventario.buscarProductoPorCategoria(categoria);

        if (productosEncontrados.isEmpty()) {
            System.out.println("No se encontraron productos en la categorÌa: " + categoria);
        } else {
            System.out.println("Productos encontrados en la categorÌa " + categoria + ":");
            for (Producto producto : productosEncontrados) {
                System.out.println(producto); // Asumiendo que `Producto` tiene un mÈtodo `toString` bien definido
            }
        }
    }
    
    private static void buscarProductoPorNombre(Scanner scanner, Inventario inventario) {
        System.out.print("Ingrese el nombre a buscar: ");
        String nombre = scanner.nextLine();

        List<Producto> productosEncontrados = inventario.buscarProductoPorNombre(nombre);

        if (productosEncontrados.isEmpty()) {
            System.out.println("No se encontraron productos en la categorÌa: " + nombre);
        } else {
            System.out.println("Productos encontrados en la categorÌa " + nombre + ":");
            for (Producto producto : productosEncontrados) {
                System.out.println(producto); // Asumiendo que `Producto` tiene un mÈtodo `toString` bien definido
            }
        }
    }


    private static void generarReporte(Inventario inventario) {
        inventario.generarReporteInventario(rutaArchivoReporte);
        //System.out.println("Reporte generado exitosamente.");
    }

    private static void cantidadProductosPorCategoria(Scanner scanner, Inventario inventario) {
    	inventario.cantidadProductosPorTodasCategorias();
    }

    private static void mostrarProductoMasCaro(Inventario inventario) {
        Producto producto = inventario.obtenerProductoMasCaro();
        if (producto != null) {
            System.out.println("El producto m√°s caro es: " + producto.getNombreProducto() + " y su valor es $"+producto.getPrecio() );
        } else {
            System.out.println("No hay productos en el inventario.");
        }
    }
    
    private static void mostrarArchivo(Inventario inventario) {
        //inventario.leerArchivo(rutaArchivo);
        System.out.println("Reporte generado exitosamente.");
    }
    
    
        
}