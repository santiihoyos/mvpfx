package abstracts;

/**
 *
 * @author Santaigo Hoyos
 * @param <T>
 */
public abstract class AbstractInteractor<T extends AbstractPresenter> {

    protected T presenter;

    protected T getPresenter() {
        return presenter;
    }

    public void notifyError(String mensajeCausa) {
        getPresenter().captureError(mensajeCausa);
    }

}
