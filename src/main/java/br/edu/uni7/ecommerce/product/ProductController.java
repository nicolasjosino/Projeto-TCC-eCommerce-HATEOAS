package br.edu.uni7.ecommerce.product;

import br.edu.uni7.ecommerce.client.ClientController;
import br.edu.uni7.ecommerce.order.Order;
import br.edu.uni7.ecommerce.order.OrderController;
import br.edu.uni7.ecommerce.review.Review;
import br.edu.uni7.ecommerce.review.ReviewController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<Product>> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        if (allProducts.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            for (Product product : allProducts) {
                product.add(linkTo(methodOn(ProductController.class).getProductById(product.getProductId())).withSelfRel());
                product.add(linkTo(methodOn(ProductController.class).getAllReviewsByProductId(product.getProductId())).withRel("reviews"));
            }
            CollectionModel<Product> productCollectionModel = CollectionModel.of(allProducts);
            productCollectionModel.add(linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
            return new ResponseEntity<>(productCollectionModel, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<EntityModel<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        EntityModel<Product> entityModel = generateLinks(product, id);
        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    @PostMapping(produces = {"application/hal+json"})
    public ResponseEntity<EntityModel<Product>> createProduct(@RequestBody Product product) {
        Product newProduct = productService.saveProduct(product);
        EntityModel<Product> entityModel = generateLinks(newProduct, newProduct.getProductId());
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = {"application/hal+json"})
    public ResponseEntity<EntityModel<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existingProduct = productService.getProductById(id);
        if (existingProduct != null) {
            product.setProductId(id);
            Product updatedProduct = productService.saveProduct(product);
            EntityModel<Product> entityModel = generateLinks(updatedProduct, id);
            return new ResponseEntity<>(entityModel, HttpStatus.OK);
        } else {
            Product createdProduct = productService.saveProduct(product);
            EntityModel<Product> entityModel = generateLinks(createdProduct, createdProduct.getProductId());
            return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/reviews", produces = {"application/hal+json"})
    public ResponseEntity<CollectionModel<Review>> getAllReviewsByProductId(@PathVariable Long id) {
        List<Review> reviews = productService.getAllReviewsByProductId(id);
        if (reviews.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        for (Review review : reviews) {
            Link selfLink = linkTo(methodOn(ReviewController.class).getReviewById(review.getReviewId())).withSelfRel();
            review.add(selfLink);
        }
        Link link = linkTo(methodOn(ProductController.class).getAllReviewsByProductId(id)).withSelfRel();
        return new ResponseEntity<>(CollectionModel.of(reviews, link), HttpStatus.OK);
    }

    private EntityModel<Product> generateLinks(Product product, Long id) {
        EntityModel<Product> entityModel = EntityModel.of(product);
        WebMvcLinkBuilder link = linkTo(methodOn(ProductController.class).getAllProducts());
        entityModel.add(link.withRel("all-products"));
        entityModel.add(linkTo(methodOn(ProductController.class).getAllReviewsByProductId(id)).withRel("reviews"));
        entityModel.add(linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel());
        return entityModel;
    }
}
