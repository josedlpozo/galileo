package com.josedlpozo.galileo.realm.realmbrowser.models.model;

import io.realm.RealmModel;

public class ModelPojo {
    final Class<? extends RealmModel> klass;
    long count;

    public ModelPojo(Class<? extends RealmModel> klass, long count) {
        this.klass = klass;
        this.count = count;
    }

    public Class<? extends RealmModel> getKlass() {
        return klass;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
