package com.xukeer.udp.plus.utils;


import java.util.*;
import java.util.concurrent.TimeUnit;

/*
 * @author xqw
 * @description
 * @date 16:10 2021/11/23
 * @since jdk 1.8
 **/
public class CacheSet<T> {
    private final Set<T> instance = new HashSet<>();
    private final List<CacheTime<T>> cacheTimeList = new LinkedList<>();
    private final static int DEFAULT_SAVE_TIME = 60*1000;   // 默认在缓存中存储的时间

    private final int saveTime;

    public CacheSet() {
        this.saveTime = DEFAULT_SAVE_TIME;
        init();
    }

    public CacheSet(int saveTimeSecond) {
        this.saveTime = saveTimeSecond * 1000;
        init();
    }

    private void init() {
        ScheduleUtil.timer().scheduleAtFixedRate(() -> {
            synchronized (cacheTimeList) {
                if (cacheTimeList.isEmpty()) {
                    return;
                }
                Iterator<CacheTime<T>> iterator = cacheTimeList.iterator();
                long currentTime = System.currentTimeMillis() - saveTime;
                while (iterator.hasNext()) {
                    CacheTime<T> cacheTime = iterator.next();
                    if (cacheTime.getSaveTime() < currentTime) {
                        iterator.remove();
                        instance.remove(cacheTime.getKey());
                    }
                }
            }
        }, 0, 20, TimeUnit.MILLISECONDS);
    }

    public void add(T key) {
        instance.add(key);
        CacheTime<T> cacheTime = new CacheTime<>(key, System.currentTimeMillis());
        synchronized (cacheTimeList) {
            cacheTimeList.add(cacheTime);
        }
    }

    public boolean contains(T key) {
         return instance.contains(key);
    }

    static class CacheTime<T> {
        private T key;
        private long saveTime;

        public CacheTime(T key, long saveTime) {
            this.key = key;
            this.saveTime = saveTime;
        }
        public T getKey() {
            return key;
        }
        public long getSaveTime() {
            return saveTime;
        }
    }
}
