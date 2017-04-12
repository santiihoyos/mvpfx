package com.mvp4fx.abstracts;

/**
 *
 * @author Santaigo Hoyos
 * @param <T>
 */
public abstract class AbstractInteractor<T extends AbstractPresenter> {

    private T presenter;

    protected T getPresenter() {
        return presenter;
    }

    public void notifyError(String mensajeCausa) {
        getPresenter().captureError(mensajeCausa);
    }

}
