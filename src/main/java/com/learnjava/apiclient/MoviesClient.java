package com.learnjava.apiclient;

import com.learnjava.domain.movie.Review;
import com.learnjava.domain.movie.Movie;
import com.learnjava.domain.movie.MovieInfo;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MoviesClient {

    private final WebClient webClient;

    public MoviesClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Movie retrieveMovie(Long movieInfoId){
        var movieInfo = invokeMovieInfoService(movieInfoId);
        var reviewList = invokeReviewService(movieInfoId);
        return new Movie(movieInfo, reviewList);
    }

    public List<Movie> retrieveMovies(List<Long> movieInfoIdList){
        return movieInfoIdList
                .stream()
                .map(movieInfoId -> retrieveMovie(movieInfoId))
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMoviesParallel(List<Long> movieInfoIdList){
        List<CompletableFuture<Movie>> completableFutureList = movieInfoIdList
                .stream()
                .map(movieInfoId -> retrieveMovieParallel(movieInfoId))
                .collect(Collectors.toList());
        return completableFutureList
                .stream()
                .map(completableFuture -> completableFuture.join())
                .collect(Collectors.toList());
    }

    public List<Movie> retrieveMoviesParallelAllOf(List<Long> movieInfoIdList){
        List<CompletableFuture<Movie>> completableFutureList = movieInfoIdList
                .stream()
                .map(movieInfoId -> retrieveMovieParallel(movieInfoId))
                .collect(Collectors.toList());

        var moviesListCF = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));

        return moviesListCF
                .thenApply(v -> completableFutureList
                        .stream()
                        .map(completableFuture -> completableFuture.join())
                        .collect(Collectors.toList()))
                .join();
    }

    public CompletableFuture<Movie> retrieveMovieParallel(Long movieInfoId){
        var movieInfo = CompletableFuture.supplyAsync(() -> invokeMovieInfoService(movieInfoId));
        var reviewList = CompletableFuture.supplyAsync(() -> invokeReviewService(movieInfoId));
        return movieInfo.thenCombine(reviewList, (movieInfoResult, reviewListResult) -> {
            return new Movie(movieInfoResult, reviewListResult);
        });
    }

    private MovieInfo invokeMovieInfoService(Long movieInfoId) {
        var moviesInfoUrlPath = "/v1/movie_infos/{movieInfoId}";
        MovieInfo movieInfo = webClient
                .get()
                .uri(moviesInfoUrlPath, movieInfoId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .block();
        return  movieInfo;
    }

    private List<Review> invokeReviewService(Long movieInfoId) {
        //v1/reviews?movieInfoId=1
        var reviewsPath = UriComponentsBuilder.fromUriString("/v1/reviews")
                .queryParam("movieInfoId", movieInfoId)
                .buildAndExpand()
                .toUriString();

        List<Review> reviewList = webClient
                .get()
                .uri(reviewsPath)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();
        return  reviewList;
    }
}
