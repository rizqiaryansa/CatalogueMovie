package com.rizqi.aryansa.cataloguemovie.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by RizqiAryansa on 1/23/2018.
 */

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
