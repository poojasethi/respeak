package com.dropbox.sync.android.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxFileSystem.PathListener.Mode;
import com.dropbox.sync.android.DbxPath;

/**
 * A general-use loader for loading the contents of a folder. Registers for
 * changes and automatically updates when the folder contents change.
 */
public class FolderLoader extends AsyncTaskLoader<List<DbxFileInfo>> {

    private final DbxPath mPath;
    private final DbxAccountManager mAccountManager;
    private final Comparator<DbxFileInfo> mSortComparator;

    private List<DbxFileInfo> mCachedContents;

    /**
     * Creates a FolderLoader for the given path.  Defaults to a case-insensitive i18n-aware sort.
     *
     * @param context
     *            Used to retrieve the application context
     * @param path
     *            Path of folder to load
     */
    public FolderLoader(Context context, DbxAccountManager accountManager, DbxPath path) {
        this(context, accountManager, path, FolderListComparator.getNameFirst(true));
    }

    /**
     * Creates a FolderLoader for the given path.
     *
     * @param context
     *            Used to retrieve the application context
     * @param path
     *            Path of folder to load
     * @param sortComparator
     *            A comparator for sorting the folder contents before they're
     *            delivered. May be null for no sort.
     */
    public FolderLoader(Context context, DbxAccountManager accountManager, DbxPath path, Comparator<DbxFileInfo> sortComparator) {
        super(context);
        mAccountManager = accountManager;
        mPath = path;
        mSortComparator = sortComparator;
    }

    /** a listener that forces a reload when folder contents change */
    private DbxFileSystem.PathListener mChangeListener = new DbxFileSystem.PathListener() {
        @Override
        public void onPathChange(DbxFileSystem fs, DbxPath registeredPath, Mode registeredMode) {
            onContentChanged();
        }
    };

    @Override
    protected void onStartLoading() {
        DbxFileSystem fs = getDbxFileSystem();
        if (fs != null) {
            fs.addPathListener(mChangeListener, mPath, Mode.PATH_OR_CHILD);
        }
        if (mCachedContents != null) {
            deliverResult(mCachedContents);
        }
        if (takeContentChanged() || mCachedContents == null) {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    protected void onStopLoading() {
        DbxFileSystem fs = getDbxFileSystem();
        if (fs != null) {
            fs.removePathListener(mChangeListener, mPath, Mode.PATH_OR_CHILD);
        }
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        mCachedContents = null;
    }

    @Override
    public void deliverResult(List<DbxFileInfo> data) {
        if (isReset()) {
            // An async result came back after the loader is stopped
            return;
        }

        mCachedContents = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    public List<DbxFileInfo> loadInBackground() {
        DbxFileSystem fs = getDbxFileSystem();
        if (fs != null) {
            try {
                List<DbxFileInfo> entries = fs.listFolder(mPath);

                if (mSortComparator != null) {
                    Collections.sort(entries, mSortComparator);
                }

                return entries;
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<DbxFileInfo>(0);
    }

    private DbxFileSystem getDbxFileSystem() {
        DbxAccount account = mAccountManager.getLinkedAccount();
        if (account != null) {
            try {
                return DbxFileSystem.forAccount(account);
            } catch (DbxException.Unauthorized e) {
                // Account was unlinked asynchronously from server.
                return null;
            }
        }
        return null;
    }
}