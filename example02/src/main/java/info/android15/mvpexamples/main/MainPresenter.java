package info.android15.mvpexamples.main;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import info.android15.mvpexamples.base.App;
import info.android15.mvpexamples.base.ServerAPI;
import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainPresenter extends RxPresenter<MainActivity> {

    public static final String DEFAULT_NAME = "Chuck Norris";

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        App.getServerAPI()
            .getItems(DEFAULT_NAME.split("\\s+")[0], DEFAULT_NAME.split("\\s+")[1])
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .compose(this.<ServerAPI.Response>deliverLatestCache())
            .subscribe(new Action1<ServerAPI.Response>() {
                @Override
                public void call(ServerAPI.Response response) {
                    getView().onItemsNext(response.items);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    getView().onItemsError(throwable);
                }
            });
    }
}
