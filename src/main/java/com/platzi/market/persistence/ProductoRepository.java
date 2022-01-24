package com.platzi.market.persistence;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.repository.ProductRepository;
import com.platzi.market.persistence.crud.ProductoCrudRepository;
import com.platzi.market.persistence.entity.Producto;
import com.platzi.market.persistence.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//  indicamos que la clase interactua con la base de datos
@Repository
public class ProductoRepository implements ProductRepository {

    // Esto puede dar un null pointer exception con la siguiente anotacion se da el control a spring para que
    // cree las instacion del objeto
    // para que esta anotacion no de error debe ser un componente de spring
    @Autowired
    private ProductoCrudRepository productoCrudRepository;
    @Autowired
    private ProductMapper mapper;

    @Override
    public List<Product> getAll() {
        List<Producto> productos = (List<Producto>) productoCrudRepository.findAll();
//        return (List<Producto>) productoCrudRepository.findAll();
        return mapper.toProducts(productos);
    }

    @Override
    public Optional<List<Product>> getByCategory(int categoryId) {
        List<Producto> productos = productoCrudRepository.findByIdCategoriaOrderByNombreAsc(categoryId);
        return Optional.of(mapper.toProducts(productos));
    }

    @Override
    public Optional<List<Product>> getScarseProducts(int quantity) {
        Optional<List<Producto>> productos = productoCrudRepository.findByCantidadStockLessThanAndEstado(quantity, true);
        return productos.map(prods -> mapper.toProducts(prods));
    }

    @Override
    public Optional<Product> getProduct(int productId) {
        return productoCrudRepository.findById(productId).map(producto -> mapper.toProduct(producto));
    }

    @Override
    public Product save(Product product) {
        Producto producto = mapper.toProducto(product); // conversion inversa
        return mapper.toProduct(productoCrudRepository.save(producto));
    }

    public void delete(int productId) {
        productoCrudRepository.deleteById(productId);
    }

    /* este ya no se usa
    public List<Producto> getByCategoria(int idCategoria) {
        return productoCrudRepository.findByIdCategoriaOrderByNombreAsc(idCategoria);
    }
    */

    /*public Optional<List<Producto>> getEscasos(int cantidad) {
        return productoCrudRepository.findByCantidadStockLessThanAndEstado(cantidad, true);
    }*/

    /*public Optional<Producto> getProducto(int idProducto) {
        return productoCrudRepository.findById(idProducto);
    }*/

    /*public Producto save(Producto producto) {
        return productoCrudRepository.save(producto);
    }*/

    /*public void delete(int idProducto) {
        productoCrudRepository.deleteById(idProducto);
    }*/
}
