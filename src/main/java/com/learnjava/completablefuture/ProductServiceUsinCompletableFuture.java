package com.learnjava.completablefuture;

import com.learnjava.domain.*;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsinCompletableFuture {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;
    private InventoryService inventoryService;

    public ProductServiceUsinCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public ProductServiceUsinCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService, InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetails(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfoCompletableFuture =  CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture, (productFutureResult, reviewFutureResult) -> new Product(productId, productFutureResult, reviewFutureResult))
                .join();

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    public CompletableFuture<Product> retrieveProductDetailsApproach2(String productId) {
        CompletableFuture<ProductInfo> productInfoCompletableFuture =  CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        CompletableFuture<Product> product = productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture, (productFutureResult, reviewFutureResult) -> new Product(productId, productFutureResult, reviewFutureResult));
        return product;
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfoCompletableFuture =  CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                                                                                        .thenApply(productInfo -> {
                                                                                            productInfo.setProductOptions(updateInventory(productInfo));
                                                                                            return productInfo;
                                                                                        });
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId))
                .exceptionally(exception -> {
                    log("Exception in reviewService: " + exception.getMessage());
                    return Review.builder().noOfReviews(0).overallRating(0d).build();
                });

        Product product = productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture, (productFutureResult, reviewFutureResult) -> new Product(productId, productFutureResult, reviewFutureResult))
                .whenComplete((product1, throwable) -> {
                    log("Inside whenComplete product: " + product1 + " exception: " + throwable);
                })
                .join();

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    private List<ProductOption> updateInventory(ProductInfo productInfo) {
        return productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    Inventory inventory = inventoryService.retreiveInventory(productOption);
                    productOption.setInventory(inventory);
                    return productOption;
                })
                .collect(Collectors.toList());

    }

    private List<ProductOption> updateInventoryApproach2(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> productOptionFutureList = productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    return CompletableFuture.supplyAsync(() -> inventoryService.retreiveInventory(productOption))
                            .exceptionally((exception) -> {
                                log("Exception in inventory service: " + exception.getMessage());
                                return Inventory.builder()
                                        .count(1).build();
                            })
                            .thenApply(inventory -> {
                                productOption.setInventory(inventory);
                                return productOption;
                            });
                }).collect(Collectors.toList());
        return productOptionFutureList.stream().map(productOptionFuture -> productOptionFuture.join()).collect(Collectors.toList());

    }

    public Product retrieveProductDetailsWithInventoryOptimised(String productId) {
        stopWatch.start();

        CompletableFuture<ProductInfo> productInfoCompletableFuture =  CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventoryApproach2(productInfo));
                    return productInfo;
                });
        CompletableFuture<Review> reviewCompletableFuture = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoCompletableFuture
                .thenCombine(reviewCompletableFuture, (productFutureResult, reviewFutureResult) -> new Product(productId, productFutureResult, reviewFutureResult))
                .join();

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsinCompletableFuture productService = new ProductServiceUsinCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
}
