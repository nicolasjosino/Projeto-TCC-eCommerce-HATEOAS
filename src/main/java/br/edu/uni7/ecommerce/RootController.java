package br.edu.uni7.ecommerce;

import br.edu.uni7.ecommerce.client.ClientController;
import br.edu.uni7.ecommerce.order.OrderController;
import br.edu.uni7.ecommerce.product.ProductController;
import br.edu.uni7.ecommerce.review.ReviewController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class RootController {

    @GetMapping("/api")
    ResponseEntity<RepresentationModel> root() {

        RepresentationModel model = new RepresentationModel();

        model.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
        model.add(linkTo(methodOn(ClientController.class).getAllClients()).withRel("clients"));
        model.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));
        model.add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"));
        model.add(linkTo(methodOn(ReviewController.class).getAllReviews()).withRel("reviews"));

        return ResponseEntity.ok(model);
    }
}