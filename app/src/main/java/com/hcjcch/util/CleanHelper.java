package com.hcjcch.util;

import android.content.Context;


import com.hcjcch.garbage.event.CurrentCleanDirectoryEvent;
import com.hcjcch.garbage.event.DeleteFileEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Description:
 *
 * @author huangchen
 * @version 1.0
 * @time 16/4/30 20:58
 */

public class CleanHelper {
    //清除应用缓存 /data/data/*cache*
    public static final String appCacheDir = "/sdcard";

    private static DataDataPOJO findDataData(Context context) {
        File file = new File(appCacheDir);
        DataDataPOJO dataDataPOJO = new DataDataPOJO(0, new ArrayList<String>());
        findDataData(file, dataDataPOJO);
        return dataDataPOJO;
    }

    private static void findDataData(File file, DataDataPOJO dataDataPOJO) {
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                EventBus.getDefault().post(new CurrentCleanDirectoryEvent(file1.getAbsolutePath()));
                findDataData(file1, dataDataPOJO);
            } else {
                if (file.getAbsolutePath().toLowerCase().contains("cache")
//                        || file.getAbsolutePath().toLowerCase().endsWith(".log")
//                        || file.getAbsolutePath().toLowerCase().endsWith(".mp4")
//                        || file.getAbsolutePath().toLowerCase().contains("temp")
//                        || file.getAbsolutePath().toLowerCase().endsWith(".flv")
//                        || file.getAbsolutePath().toLowerCase().contains("download")
                        ) {
                    dataDataPOJO.getCacheFile().add(file1.getAbsolutePath());
                    dataDataPOJO.setSize(dataDataPOJO.getSize() + file1.length());
                }
            }
        }
    }

    public static Observable<DataDataPOJO> findDataDataObservable(final Context context) {
        return Observable
                .create(new Observable.OnSubscribe<DataDataPOJO>() {
                    @Override
                    public void call(Subscriber<? super DataDataPOJO> subscriber) {
                        subscriber.onNext(findDataData(context));
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Boolean> deleteGarbageFiles(final DataDataPOJO dataDataPOJO) {
        return Observable
                .create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        List<String> paths = dataDataPOJO.getCacheFile();
                        for (String path : paths) {
                            File file = new File(path);
                            if (file.exists()) {
                                EventBus.getDefault().post(new DeleteFileEvent(file.getAbsolutePath()));
                                file.delete();
                            }
                        }
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io());
    }


    public static class DataDataPOJO {
        public long size;
        public ArrayList<String> cacheFile;

        public DataDataPOJO(long size, ArrayList<String> cacheFile) {
            this.size = size;
            this.cacheFile = cacheFile;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }


        public ArrayList<String> getCacheFile() {
            return cacheFile;
        }

        public void setCacheFile(ArrayList<String> cacheFile) {
            this.cacheFile = cacheFile;
        }

        @Override
        public String toString() {
            return "DataDataPOJO{" +
                    "size=" + size +
                    ", cacheFile=" + cacheFile +
                    '}';
        }
    }

}
