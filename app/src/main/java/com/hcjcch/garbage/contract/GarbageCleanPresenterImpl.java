package com.hcjcch.garbage.contract;

import android.app.Activity;
import android.content.Context;

import com.hcjcch.util.CleanHelper;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/5/2 15:37
 */

public class GarbageCleanPresenterImpl implements GarbageCleanContract.Presenter {

    private GarbageCleanContract.View view;
    private Context context;
    private boolean isToClean;
    private CleanHelper.DataDataPOJO dataDataPOJO;
    private boolean isDoing;

    public GarbageCleanPresenterImpl(GarbageCleanContract.View view) {
        this.view = view;
        this.context = (Activity) view;
    }

    @Override
    public void init() {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void startClean() {
        if (isDoing) {
            return;
        }
        isDoing = true;
        if (!isToClean) {
            view.setScanText();
            CleanHelper
                    .findDataDataObservable(context)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CleanHelper.DataDataPOJO>() {
                        @Override
                        public void onCompleted() {
                            isDoing = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            isDoing = false;
                            view.resetText();
                        }

                        @Override
                        public void onNext(CleanHelper.DataDataPOJO dataDataPOJO) {
                            GarbageCleanPresenterImpl.this.dataDataPOJO = dataDataPOJO;
                            view.setGarbageNumberText(dataDataPOJO.getSize());
                            view.setGarbageNumberVisible(true);
                            view.setFinishScanText();
                            isToClean = true;
                        }
                    });
        } else {
            CleanHelper
                    .deleteGarbageFiles(dataDataPOJO)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            isDoing = false;
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            view.setFinishCleaningText();
                            view.setGarbageNumberText(0);
                            isDoing = false;
                        }
                    });
        }
    }
}
