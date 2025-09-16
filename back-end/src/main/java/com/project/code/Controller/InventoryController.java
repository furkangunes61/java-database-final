package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ServiceClass serviceClass;

    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {
        Product product = request.getProduct();
        Inventory inventory = request.getInventory();
        Map<String, String> map = new HashMap<>();
        System.out.println("Stock Level: " + inventory.getStockLevel());
        if (!serviceClass.ValidateProductId(product.getId())) {
            map.put("message", "Id " + product.getId() + " not present in database");
            return map;
        }
        productRepository.save(product);
        map.put("message", "Successfully updated product with id: " + product.getId());
        if (inventory != null) {
            try {
                Inventory result = serviceClass.getInventoryId(inventory);
                if (result != null) {
                    inventory.setId(result.getId());
                    inventoryRepository.save(inventory);
                } else {
                    map.put("message", "No data available for this product or store id");
                    return map;
                }
            } catch (DataIntegrityViolationException e) {
                map.put("message", "Error: " + e);
                System.out.println(e);
                return map;
            } catch (Exception e) {
                map.put("message", "Error: " + e);
                System.out.println(e);
                return map;
            }
        }
        return map;
    }

    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> map = new HashMap<>();
        try {
            if (serviceClass.validateInventory(inventory)) {
                inventoryRepository.save(inventory);
            } else {
                map.put("message", "Data Already present in inventory");
                return map;
            }
        } catch (DataIntegrityViolationException e) {
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        } catch (Exception e){
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        }
        map.put("message", "Product added to inventory successfully");
        return map;
    }

    @GetMapping("/{storeId}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeId){
        Map<String, Object> map = new HashMap<>();
        List<Product> result = productRepository.findProductsByStoreId(storeId);
        map.put("product", result);
        return map;
    }

    @GetMapping("/filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(@PathVariable String category, @PathVariable String name, @PathVariable long storeId){
        Map<String, Object> map = new HashMap<>();
        if (category.equals("null")) {
            map.put("product", productRepository.findByNameLike(storeId, name));
            return map;
        } else if (name.equals("null")) {
            map.put("product", productRepository.findByCategoryAndStoreId(storeId, category));
            return map;
        }
        map.put("product", productRepository.findByNameAndCategory(storeId, name, category));
        return map;
    }

    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable String name, @PathVariable long storeId){
        Map<String, Object> map = new HashMap<>();
        map.put("product", productRepository.findByNameLike(storeId, name));
        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable Long id){
        Map<String, String> map = new HashMap<>();

        if (!serviceClass.ValidateProductId(id)){
            map.put("message", "Id" + id + "not present in database");
            return map;
        }
        inventoryRepository.deleteByProductId(id);
        map.put("message", "Deleted product successfully with id: " + id);
        return map;
    }

    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable int quantity, @PathVariable long storeId, @PathVariable long productId){
        Inventory result = inventoryRepository.findByProductIdandStoreId(productId, storeId);

        if(result.getStockLevel() >= quantity){
            return true;
        }
        return false;
    }
}
