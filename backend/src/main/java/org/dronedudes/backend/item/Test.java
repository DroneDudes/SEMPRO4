package org.dronedudes.backend.item;

import jakarta.annotation.PostConstruct;
import org.dronedudes.backend.Part.Part;
import org.dronedudes.backend.Part.PartRepository;
import org.dronedudes.backend.Product.Product;
import org.dronedudes.backend.Product.ProductRepository;
import org.dronedudes.backend.Warehouse.Warehouse;
import org.dronedudes.backend.Warehouse.WarehouseModel;
import org.dronedudes.backend.Warehouse.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class Test {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;
    private final WarehouseService warehouseService;

    public Test(PartRepository partRepository, ProductRepository productRepository, WarehouseService warehouseService) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
        this.warehouseService = warehouseService;
    }

    @PostConstruct
    public void test() {
        Part part = new Part();
        part.setName("Part 1");
        partRepository.save(part);

        Product product = new Product();
        product.setName("Product 1");
        productRepository.save(product);

        warehouseService.insertItem(product);
        for(Item item : (warehouseService.insertItem(part)).getItems()) {
            System.out.println(item.getName());
        }


    }
}
