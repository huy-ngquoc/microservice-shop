package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.Product;

public interface SaveProductPort {
    public Product save(Product product);
    public List<Product> saveAll(List<Product> products);
}
