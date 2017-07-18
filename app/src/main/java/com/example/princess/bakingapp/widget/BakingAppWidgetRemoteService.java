package com.example.princess.bakingapp.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Princess on 7/18/2017.
 */

public class BakingAppWidgetRemoteService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppWidgetRemoteFactory(this.getApplicationContext());
    }
}