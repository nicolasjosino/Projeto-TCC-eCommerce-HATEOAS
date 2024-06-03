package br.edu.uni7.ecommerce.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByClientClientId(Long clientId);
}
