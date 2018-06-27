package ru.gdgkazan.popularmoviesclean.screen.details;

import android.support.annotation.NonNull;

import java.util.List;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.popularmoviesclean.R;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.model.Review;
import ru.gdgkazan.popularmoviesclean.domain.model.Video;
import ru.gdgkazan.popularmoviesclean.domain.usecase.MoviesUseCase;
import ru.gdgkazan.popularmoviesclean.domain.usecase.ReviewsUseCase;
import ru.gdgkazan.popularmoviesclean.domain.usecase.VideosUseCase;
import ru.gdgkazan.popularmoviesclean.screen.movies.MoviesView;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author Artur Vasilov
 */
public class MovieDetailsPresenter {

    private final MovieDetailsView movieDetailsView;
    private final ReviewsUseCase reviewsUseCase;
    private final VideosUseCase videosUseCase;
    private final Movie movie;
    private final LifecycleHandler mLifecycleHandler;

    public MovieDetailsPresenter(@NonNull MovieDetailsView movieDetailsView,
                                 @NonNull ReviewsUseCase reviewsUseCase,
                                 @NonNull VideosUseCase videosUseCase,
                                 @NonNull Movie movie,
                                 @NonNull LifecycleHandler lifecycleHandler) {

        this.movieDetailsView = movieDetailsView;
        this.reviewsUseCase = reviewsUseCase;
        this.videosUseCase = videosUseCase;
        this.movie = movie;
        this.mLifecycleHandler = lifecycleHandler;

    }

    public void init() {


        Observable<List<Review>> reviewsObservable =  reviewsUseCase.getReviews(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(reviews -> {
                    movieDetailsView.showReviews(reviews);
                    return reviews;
                });
//                .compose(mLifecycleHandler.load(R.id.reviews_request_id));



        Observable<List<Video>> videosObservable =  videosUseCase.getVideos(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(reviews -> {
                    movieDetailsView.showTrailers(reviews);
                    return reviews;
                });
//                .compose(mLifecycleHandler.load(R.id.videos_request_id));


        reviewsObservable.zipWith(videosObservable, new Func2<List<Review>, List<Video>, Boolean>() {
            @Override
            public Boolean call(List<Review> reviews, List<Video> videos) {
                return true;
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(movieDetailsView::showLoadingIndicator)
                .doAfterTerminate(movieDetailsView::hideLoadingIndicator)
                .subscribe(aBoolean -> {

                });



//        mMoviesUseCase.popularMovies()
//                .doOnSubscribe(mMoviesView::showLoadingIndicator)
//                .map(new Func1<List<Movie>, List<Movie>>() {
//                    @Override
//                    public List<Movie> call(List<Movie> movies) {
//                        return movies;
//                    }
//                })
//                .doAfterTerminate(mMoviesView::hideLoadingIndicator)
//                .compose(mLifecycleHandler.load(R.id.movies_request_id))
//                .subscribe(mMoviesView::showMovies, throwable -> mMoviesView.showError());
    }
}

