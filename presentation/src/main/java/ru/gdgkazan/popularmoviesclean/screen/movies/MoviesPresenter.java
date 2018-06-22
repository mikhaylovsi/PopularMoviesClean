package ru.gdgkazan.popularmoviesclean.screen.movies;

import android.support.annotation.NonNull;

import java.util.List;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.popularmoviesclean.R;
import ru.gdgkazan.popularmoviesclean.domain.model.Movie;
import ru.gdgkazan.popularmoviesclean.domain.usecase.MoviesUseCase;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Artur Vasilov
 */
public class MoviesPresenter {

    private final MoviesView mMoviesView;
    private final MoviesUseCase mMoviesUseCase;
    private final LifecycleHandler mLifecycleHandler;

    public MoviesPresenter(@NonNull MoviesView moviesView, @NonNull MoviesUseCase moviesUseCase,
                           @NonNull LifecycleHandler lifecycleHandler) {
        mMoviesView = moviesView;
        mMoviesUseCase = moviesUseCase;
        mLifecycleHandler = lifecycleHandler;
    }

    public void init() {
        mMoviesUseCase.popularMovies()
                .doOnSubscribe(mMoviesView::showLoadingIndicator)
                .map(new Func1<List<Movie>, List<Movie>>() {
                    @Override
                    public List<Movie> call(List<Movie> movies) {
                        return movies;
                    }
                })
                .doAfterTerminate(mMoviesView::hideLoadingIndicator)
                .compose(mLifecycleHandler.load(R.id.movies_request_id))
                .subscribe(mMoviesView::showMovies, throwable -> mMoviesView.showError());
    }
}

