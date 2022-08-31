package com.revature.controllers;

import com.revature.annotations.Authorized;
import com.revature.dtos.ProductInfo;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.Purchase;
import com.revature.models.User;
import com.revature.services.OrderService;
import com.revature.services.ProductService;
import com.revature.services.PurchaseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = { "http://localhost:4200",
        "http://swagbag.us-east-1.elasticbeanstalk.com" }, allowCredentials = "true")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final PurchaseService purchaseService;

    public ProductController(ProductService productService, OrderService orderService,
            PurchaseService purchaseService) {
        this.productService = productService;
        this.orderService = orderService;
        this.purchaseService = purchaseService;
    }

    @Authorized
    @GetMapping
    public ResponseEntity<List<Product>> getInventory() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Authorized
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
        Optional<Product> optional = productService.findById(id);

        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }

    @Authorized
    @PutMapping
    public ResponseEntity<Product> upsert(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @Authorized
    @PatchMapping
    public ResponseEntity<List<Product>> purchase(@RequestBody List<ProductInfo> metadata, HttpServletRequest req) {

        // Get the user making the purchase
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = (User) session.getAttribute("user");

        List<Product> productList = new ArrayList<Product>();
        List<Purchase> purchaseList = new ArrayList<Purchase>();
        Order order = new Order(0, user, LocalDateTime.now(), null);

        for (int i = 0; i < metadata.size(); i++) {
            Optional<Product> optional = productService.findById(metadata.get(i).getId());

            if (!optional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Product product = optional.get();
            int quantity = metadata.get(i).getQuantity();
            Purchase purchase = new Purchase(0, order, product, quantity, product.getPrice());

            if (product.getQuantity() - quantity < 0) {
                return ResponseEntity.badRequest().build();
            }

            product.setQuantity(product.getQuantity() - metadata.get(i).getQuantity());
            productList.add(product);
            purchaseList.add(purchase);
        }

        productService.saveAll(productList, metadata);
        orderService.save(order);
        purchaseService.saveAll(purchaseList);

        return ResponseEntity.ok(productList);
    }

    @Authorized
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") int id) {
        Optional<Product> optional = productService.findById(id);

        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productService.delete(id);

        return ResponseEntity.ok(optional.get());
    }
}
