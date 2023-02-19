package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import com.learnjava.util.CommonUtil;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.learnjava.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;

class MoviesClientTest {
    WebClient webClient = WebClient
            .builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    private MoviesClient moviesClient = new MoviesClient(webClient);

    @RepeatedTest(10)
    public void testRetrieveMovie(){
        //given
        Long movieInfoId = 1L;
        //when
        CommonUtil.startTimer();
        Movie movie = moviesClient.retrieveMovie(movieInfoId);
        CommonUtil.timeTaken();
        log("Movie is: " + movie);
        //then
        assertNotNull(movie);
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assertEquals(1, movie.getReviewList().size());

    }

    @RepeatedTest(10)
    void retrieveMovieParallel() {
        //given
        Long movieInfoId = 1L;
        //when
        CommonUtil.startTimer();
        Movie movie = moviesClient.retrieveMovieParallel(movieInfoId).join();
        CommonUtil.timeTaken();
        log("Movie is: " + movie);
        //then
        assertNotNull(movie);
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assertEquals(1, movie.getReviewList().size());

    }

    @RepeatedTest(10)
    void retrieveMovies() {
        //given
        var movieInfoIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        //when
        CommonUtil.startTimer();
        List<Movie> movieList = moviesClient.retrieveMovies(movieInfoIdList);
        CommonUtil.timeTaken();
        //then
        assertNotNull(movieList);
        assertEquals(7, movieList.size());
    }

    @RepeatedTest(10)
    void retrieveMoviesParallel() {
        //given
        var movieInfoIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        //when
        CommonUtil.startTimer();
        List<Movie> movieList = moviesClient.retrieveMoviesParallel(movieInfoIdList);
        CommonUtil.timeTaken();
        //then
        assertNotNull(movieList);
        assertEquals(7, movieList.size());
    }

    @RepeatedTest(10)
    void retrieveMoviesParallelAllof() {
        //given
        var movieInfoIdList = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        //when
        CommonUtil.startTimer();
        List<Movie> movieList = moviesClient.retrieveMoviesParallelAllOf(movieInfoIdList);
        CommonUtil.timeTaken();
        //then
        assertNotNull(movieList);
        assertEquals(7, movieList.size());
    }
}