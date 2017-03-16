package abstracts;

/**
 *
 * @author Santiago Hoyos Zea
 * @param <T>
 * @param <T2>
 */
public abstract class AbstractPresenter<T extends AbstractView, T2 extends AbstractInteractor> {
    
    protected T view;
    protected T2 interactor;
    
    protected T getView() {
        return view;
    }
    
    public void setView(T view) {
        this.view = view;
    }
    
    protected T2 getInteractor() {
        return interactor;
    }
    
    public void setInteractor(T2 interactor) {
        this.interactor = interactor;
    }
    
    public void captureError(String message) {
        getView().showError(message);
    }
    
}
