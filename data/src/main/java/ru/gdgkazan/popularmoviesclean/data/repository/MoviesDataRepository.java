package ru.gdgkazan.popularmoviesclean.data.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.gdgkazan.popularmoviesclean.data.cache.MoviesCacheTransformer;
import ru.gdgkazan.popularmoviesclean.data.cache.ReviewsCacheTransformer;
import ru.gdgkazan.popularmoviesclean.data.cache.VideosCacheTransformer;
import ru.gdgkazan.popularmoviesclean.data.mapper.MoviesMapper;
import ru.gdgkazan.popularmoviesclean.data.mapper.ReviewsMapper;
import ru.gdgkazan.popularmoviesclean.data.mapper.VideosMapper;
import ru.gdgkazan.popularmoviesclean.data.model.response.MoviesResponse;
import ru.gdgkazan.popularmoviesclean.data.model.response.ReviewsResponse;
import ru.gdgkazan.popularmoviesclean.data.model.response.VideosResponse;
import ru.gdgkazan.popularmoviesclean.data.network.ApiFactory;
import ru.gdgkazan.popularmoviesclean.domain.MoviesRepository;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import ru.gdgkazan.popularmoviesclean.domain.model.Video;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * @author Artur Vasilov
 */
public class MoviesDataRepository implements MoviesRepository {

    @Override
    public Observable<List<Movie>> popularMovies() {
        return ApiFactory.getMoviesService()
                .popularMovies()
                .map(MoviesResponse::getMovies)
                .compose(new MoviesCacheTransformer())
                .flatMap(Observable::from)
                .map(new MoviesMapper())
                .toList();
    }

    private Observable<ru.gdgkazan.popularmoviesclean.data.model.content.Movie> getMovieFromDb(Movie movie){

       return Observable.fromCallable((Func0<ru.gdgkazan.popularmoviesclean.data.model.content.Movie>) () -> {
            Realm realm = Realm.getDefaultInstance();
            return  realm.where(ru.gdgkazan.popularmoviesclean.data.model.content.Movie.class)
                    .equalTo("mTitle", movie.getTitle()).findFirst();
        });


    }



    @Override
    public Observable<List<Review>> getReviews(Movie movie) {


       return getMovieFromDb(movie).flatMap((Func1<ru.gdgkazan.popularmoviesclean.data.model.content.Movie,
                Observable<ReviewsResponse>>) movie1 -> ApiFactory.getMoviesService()
                .reviews(String.valueOf(movie1.getId())))
                .map(ReviewsResponse::getReviews)
                .compose(new ReviewsCacheTransformer())
                .flatMap(Observable::from)
                .map(new ReviewsMapper())
                .toList();

    }


    @Override
    public Observable<List<Video>> getVideos(Movie movie) {

       return getMovieFromDb(movie).flatMap((Func1<ru.gdgkazan.popularmoviesclean.data.model.content.Movie,
                Observable<VideosResponse>>) movie1 -> ApiFactory.getMoviesService()
                .trailers(String.valueOf(movie1.getId())))
                .map(VideosResponse::getVideos)
                .compose(new VideosCacheTransformer())
                .flatMap(Observable::from)
                .map(new VideosMapper())
                .toList();

    }
}

