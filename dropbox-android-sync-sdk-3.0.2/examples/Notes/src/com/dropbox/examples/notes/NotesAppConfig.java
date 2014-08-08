package com.dropbox.examples.notes;

import android.content.Context;

import com.dropbox.sync.android.DbxAccountManager;

public final class NotesAppConfig {
    private NotesAppConfig() {}

    public static final String appKey = "t3ow4tvu36zlh5s";
    public static final String appSecret = "w4nmqlk5ul1uiw8";

    public static DbxAccountManager getAccountManager(Context context)
    {
        return DbxAccountManager.getInstance(context.getApplicationContext(), appKey, appSecret);
    }
}
