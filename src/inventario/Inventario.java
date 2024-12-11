package inventario;

import producto.Producto;

import java.io.*;
import java.util.*;

public class Inventario {
    public static List<Producto> productos;
    
    public Inventario() {
        this.productos = new ArrayList<>();
    }
    
    
    public static List<Producto> leerProductos(String rutaArchivo) {
        //List<Producto> listaProductos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Lee el archivo l?nea por l?nea
            while ((linea = br.readLine()) != null) {
                // Divide la l?nea en partes usando la coma como delimitador
                String[] partes = linea.split(",\\s*"); // Considera espacios despu?s de la coma

                // Crea un producto y agr?galo a la lista
                if (partes.length == 5) { // Verifica que haya exactamente 5 campos
                    String idProducto = partes[0];
                    String nombreProducto = partes[1];
                    String categoria = partes[2];
                    double precio = Double.parseDouble(partes[3]);
                    int cantidadDisponible = Integer.parseInt(partes[4]);

                    Producto producto = new Producto(idProducto, nombreProducto, categoria, precio, cantidadDisponible);
                    productos.add(producto);
                } else {
                    System.err.println("Formato incorrecto en la l?nea: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        return productos;
    }




    // Guardar productos en archivo
    public void guardarEnArchivo(String archivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Producto producto : productos) {
                bw.write(producto.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // Agregar producto al inventario
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    // Actualizar producto por ID
 // Metodo para actualizar un producto en la lista y en el archivo
    public boolean actualizarProducto(String idProducto, String nombreProducto, String categoria, double precio, int cantidad, String rutaArchivo) throws IOException {
        boolean actualizado = false;
        System.out.print(productos.size());

        // Actualizar el producto en la lista
        for (Producto producto : productos) {
            if (producto.getIdProducto().equals(idProducto)) {
                producto.setNombreProducto(nombreProducto);
                producto.setCategoria(categoria);
                producto.setPrecio(precio);
                producto.setCantidaDisponible(cantidad);
                actualizado = true;
                break;
            }
        }

        if (actualizado) {
        	actualizarArchivo();
        }

        return actualizado;
    }


    // Eliminar producto por ID
    public boolean eliminarProducto(String idProducto, String rutaArchivo) throws IOException {
        boolean eliminado = false;

        // Eliminar el producto de la lista
        for (Producto producto : productos) {
            if (producto.getIdProducto().equals(idProducto)) {
                productos.remove(producto);
                eliminado = true;
                break;
            }
        }

        if (eliminado) {
            actualizarArchivo();
        }

        return eliminado;
    }


    // Buscar productos por nombre
    public List<Producto> buscarProductoPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto.getNombreProducto().equalsIgnoreCase(nombre)) { // Comparaci?n ignorando may?sculas/min?sculas
                productosEncontrados.add(producto);
            }
        }

        return productosEncontrados;
    }


    // Buscar productos por categoría
    public List<Producto> buscarProductoPorCategoria(String categoria) {
        List<Producto> productosEncontrados = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto.getCategoria().equalsIgnoreCase(categoria)) { // Comparaci?n ignorando may?sculas/min?sculas
                productosEncontrados.add(producto);
            }
        }

        return productosEncontrados;
    }



    // Calcular cantidad total de productos por categoría
    public int cantidadPorCategoria(String categoria) {
        return productos.stream()
                .filter(producto -> producto.getCategoria().equalsIgnoreCase(categoria))
                .mapToInt(Producto::getCantidaDisponible)
                .sum();
    }

    // Generar reporte de inventario
    public void generarReporte() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_inventario.txt"))) {
            double totalInventario = 0;
            for (Producto producto : productos) {
                bw.write(producto.toString());
                bw.newLine();
                totalInventario += producto.getPrecio() * producto.getCantidaDisponible();
            }
            bw.write("Valor total del inventario: " + totalInventario);
        } catch (IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }
    
    // Metodo para leer el contenido de un archivo
    public static void leerArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea); // Imprime cada l?nea del archivo
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
    
    public boolean existeProducto(String idProducto) {
        for (Producto producto : productos) {
            if (producto.getIdProducto().equals(idProducto)) {
                return true;
            }
        }
        return false;
    }
    
 // Metodo para actualizar el archivo con todos los productos en la lista
    public void actualizarArchivo() throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("../productos.txt"))) {
            for (Producto producto : productos) {
                escritor.write(producto.getIdProducto() + ","
                        + producto.getNombreProducto() + ","
                        + producto.getCategoria() + ","
                        + producto.getPrecio() + ","
                        + producto.getCantidaDisponible());
                escritor.newLine();
            }
        }
    }
    
    public void cantidadProductosPorTodasCategorias() {
        List<String> categorias = new ArrayList<>();
        List<Integer> conteos = new ArrayList<>();

        for (Producto producto : productos) {
            String categoria = producto.getCategoria();

            if (categorias.contains(categoria)) {
                // Si la categor?a ya existe, incrementa su contador
                int index = categorias.indexOf(categoria);
                conteos.set(index, conteos.get(index) + 1);
            } else {
                // Si la categor?a no existe, agr?gala y empieza su contador en 1
                categorias.add(categoria);
                conteos.add(1);
            }
        }

        // Mostrar los resultados
        System.out.println("Cantidad de productos por categor?a:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println("Categor?a: " + categorias.get(i) + ", Cantidad: " + conteos.get(i));
        }
    }
    
    public Producto obtenerProductoMasCaro() {
        if (productos.isEmpty()) {
            return null; // Si la lista est? vac?a, no hay producto m?s caro
        }

        Producto productoMasCaro = productos.get(0); // Inicializar con el primer producto

        for (Producto producto : productos) {
            if (producto.getPrecio() > productoMasCaro.getPrecio()) {
                productoMasCaro = producto; // Actualizar si el producto actual tiene un precio mayor
            }
        }

        return productoMasCaro;
    }
    
    public void generarReporteInventario(String rutaArchivoReporte) {
        double valorTotalInventario = 0.0;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivoReporte))) {
            escritor.write("Reporte de Inventario\n");
            escritor.write("=======================\n");

            for (Producto producto : productos) {
                double valorProducto = producto.getPrecio() * producto.getCantidaDisponible();
                valorTotalInventario += valorProducto;

                // Escribir detalles del producto en el archivo
                escritor.write("ID: " + producto.getIdProducto() + "\n");
                escritor.write("Nombre: " + producto.getNombreProducto() + "\n");
                escritor.write("Categor?a: " + producto.getCategoria() + "\n");
                escritor.write("Precio: $" + producto.getPrecio() + "\n");
                escritor.write("Cantidad: " + producto.getCantidaDisponible() + "\n");
                escritor.write("Valor Total del Producto: $" + valorProducto + "\n");
                escritor.write("-----------------------\n");
            }

            // Escribir el valor total del inventario
            escritor.write("\nValor Total del Inventario: $" + valorTotalInventario + "\n");
            System.out.println("Reporte generado exitosamente en: " + rutaArchivoReporte);

        } catch (IOException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }



    
}