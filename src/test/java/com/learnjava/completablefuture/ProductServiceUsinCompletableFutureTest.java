package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsinCompletableFutureTest {
    ProductInfoService productInfoService = new ProductInfoService();
    ReviewService reviewService = new ReviewService();

    InventoryService inventoryService = new InventoryService();
    ProductServiceUsinCompletableFuture productService = new ProductServiceUsinCompletableFuture(productInfoService, reviewService);
    ProductServiceUsinCompletableFuture productServiceWithInventory = new ProductServiceUsinCompletableFuture(productInfoService, reviewService, inventoryService);

    @Test
    void retrieveProductDetails() {
        //given
        String productId = "ABC123";

        //when
        Product product = productService.retrieveProductDetails(productId);

        //then
        assertNotNull(product);
        assertNotNull(product.getProductInfo());
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsApproach2() {
        //given
        String productId = "ABC123";

        //when
        startTimer();
        Product product = productService.retrieveProductDetailsApproach2(productId).join();
        timeTaken();

        //then
        assertNotNull(product);
        assertNotNull(product.getProductInfo());
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsWithInventory() {
        //given
        String productId = "ABC123";

        //when
        Product product = productServiceWithInventory.retrieveProductDetailsWithInventory(productId);

        //than
        assertNotNull(product);
        assertNotNull(product.getProductInfo());
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().stream().forEach(productOption -> {
            assertNotNull(productOption.getInventory());
            assertTrue(2 == productOption.getInventory().getCount());
        });
    }

    @Test
    void retrieveProductDetailsWithInventoryOptimised() {
        //given
        String productId = "ABC123";

        //when
        Product product = productServiceWithInventory.retrieveProductDetailsWithInventoryOptimised(productId);

        //than
        assertNotNull(product);
        assertNotNull(product.getProductInfo());
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().stream().forEach(productOption -> {
            assertNotNull(productOption.getInventory());
            assertTrue(2 == productOption.getInventory().getCount());
        });
    }
}