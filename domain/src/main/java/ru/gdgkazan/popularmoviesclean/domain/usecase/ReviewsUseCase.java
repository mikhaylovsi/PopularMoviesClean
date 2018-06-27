package ru.gdgkazan.popularmoviesclean.domain.usecase;

import java.util.List;

import ru.gdgkazan.popularmoviesclean.domain.MoviesRepository;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import rx.Observable;
import sun.security.krb5.Realm;

/**
 * @author Artur Vasilov
 */
public class ReviewsUseCase {

    private final MoviesRepository mRepository;
    private final Observable.Transformer<List<Review>, List<Review>> mAsyncTransformer;

    public ReviewsUseCase(MoviesRepository repository,
                          Observable.Transformer<List<Review>, List<Review>> asyncTransformer) {
        mRepository = repository;
        mAsyncTransformer = asyncTransformer;
    }


      public Observable<List<Review>> getReviews(Movie movie) {
        return mRepository.getReviews(movie)
                .compose(mAsyncTransformer);
    }
}


