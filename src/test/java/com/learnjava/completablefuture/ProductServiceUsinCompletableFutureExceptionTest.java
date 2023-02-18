package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.print.attribute.standard.RequestingUserName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUsinCompletableFutureExceptionTest {

    @Mock
    private ProductInfoService productInfoService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    ProductServiceUsinCompletableFuture productServiceUsinCompletableFuture;

    @Test
    @DisplayName("Retrieve product details with inventory - exception in review service")
    void retrieveProductDetailsWithInventoryTest(){
        //given
        String productID = "A1234";
        when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
        when(reviewService.retrieveReviews(any())).thenThrow(new RuntimeException("Runtime exception"));
        when(inventoryService.retreiveInventory(any())).thenCallRealMethod();

        //when
        Product product = productServiceUsinCompletableFuture.retrieveProductDetailsWithInventoryOptimised(productID);

        //then
        assertNotNull(product);
        assertNotNull(product.getProductInfo());
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().stream().forEach(productOption -> {
            assertNotNull(productOption.getInventory());
            assertTrue(2 == productOption.getInventory().getCount());
        });
        assertNotNull(product.getReview());
        assertEquals(0, product.getReview().getNoOfReviews());

    }

    @Test
    @DisplayName("Retrieve product details with inventory - exception in product service")
    void retrieveProductDetailsWithInventory1Test(){
        //given
        String productID = "A1234";
        when(productInfoService.retrieveProductInfo(any())).thenThrow(new RuntimeException("Runtime exception"));
        when(reviewService.retrieveReviews(any())).thenCallRealMethod();

        //then
        Assertions.assertThrows(RuntimeException.class, () -> productServiceUsinCompletableFuture.retrieveProductDetailsWithInventoryOptimised(productID));
    }

    @Test
    @DisplayName("Retrieve product details with inventory - exception in inventory service")
    void retrieveProductDetailsWithInventory2Test(){
        //given
        String productID = "A1234";
        when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
        when(reviewService.retrieveReviews(any())).thenCallRealMethod();
        when(inventoryService.retreiveInventory(any())).thenThrow(new RuntimeException("Runtime exception"));

        //when
        Product product = productServiceUsinCompletableFuture.retrieveProductDetailsWithInventoryOptimised(productID);

        //then
        assertNotNull(product);
        assertNotNull(product.getProductInfo());
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().stream().forEach(productOption -> {
            assertNotNull(productOption.getInventory());
            assertTrue(1 == productOption.getInventory().getCount());
        });
        assertNotNull(product.getReview());
        assertTrue(product.getReview().getNoOfReviews() > 0);

    }
}