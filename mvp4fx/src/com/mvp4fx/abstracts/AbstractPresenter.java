package com.mvp4fx.abstracts;

/**
 *
 * @author Santiago Hoyos Zea
 * @param <T>
 * @param <T2>
 */
public abstract class AbstractPresenter<T extends AbstractView, T2 extends AbstractInteractor> {

    private T view;
    private T2 interactor;

    protected T getView() {
        return view;
    }

    protected T2 getInteractor() {
        return interactor;
    }

    public void captureError(String message) {
        getView().showError(message);
    }

}
