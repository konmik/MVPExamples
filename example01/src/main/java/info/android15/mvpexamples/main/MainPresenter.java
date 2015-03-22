package info.android15.mvpexamples.main;

import java.util.concurrent.TimeUnit;

import info.android15.mvpexamples.base.App;
import info.android15.mvpexamples.base.ServerAPI;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainPresenter {

    public static final String DEFAULT_NAME = "Chuck Norris";

    private ServerAPI.Item[] items;
    private Throwable error;

    private MainActivity view;

    public MainPresenter() {
        App.getServerAPI()
            .getItems(DEFAULT_NAME.split("\\s+")[0], DEFAULT_NAME.split("\\s+")[1])
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<ServerAPI.Response>() {
                @Override
                public void call(ServerAPI.Response response) {
                    items = response.items;
                    publish();
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    error = throwable;
                    publish();
                }
            });
    }

    public void onTakeView(MainActivity view) {
        this.view = view;
        publish();
    }

    private void publish() {
        if (view != null) {
            if (items != null)
                view.onItemsNext(items);
            else if (error != null)
                view.onItemsError(error);
        }
    }
}
