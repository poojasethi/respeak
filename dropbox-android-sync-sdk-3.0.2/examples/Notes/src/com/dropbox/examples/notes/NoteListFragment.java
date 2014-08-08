package com.dropbox.examples.notes;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.util.FolderLoader;

public class NoteListFragment extends ListFragment implements LoaderCallbacks<List<DbxFileInfo>> {

    @SuppressWarnings("unused")
    private static final String TAG = NoteListFragment.class.getName();

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private static final int MENU_RENAME = 1;
    private static final int MENU_DELETE = 2;

    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private View mEmptyText;
    private View mLinkButton;
    private View mLoadingSpinner;
    private DbxAccountManager mAccountManager;


    public interface Callbacks {

        public void onItemSelected(DbxPath path);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(DbxPath path) {
        }
    };

    public NoteListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        doLoad(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                // We are now linked.
                showLinkedView(true);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mEmptyText = view.findViewById(R.id.empty_text);
        mLinkButton = view.findViewById(R.id.link_button);
        mLoadingSpinner = view.findViewById(R.id.list_loading);

        mLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountManager.startLinkFromSupportFragment(NoteListFragment.this, 0);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState
                .containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        getListView().setEmptyView(view.findViewById(android.R.id.empty));

        if (!mAccountManager.hasLinkedAccount()) {
            showUnlinkedView();
        } else {
            showLinkedView(false);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        DbxFileInfo fileInfo = (DbxFileInfo)getListAdapter().getItem(info.position);
        menu.setHeaderTitle(Util.stripExtension("txt", fileInfo.path.getName()));
        menu.add(Menu.NONE, MENU_RENAME, Menu.NONE, R.string.menu_rename);
        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        final DbxFileInfo fileInfo = (DbxFileInfo)getListAdapter().getItem(info.position);

        int itemId = item.getItemId();
        if (itemId == MENU_RENAME) {
            final EditText filenameInput = new EditText(getActivity());

            filenameInput.setText(Util.stripExtension("txt", fileInfo.path.getName()));
            filenameInput.setSelectAllOnFocus(true);

            new AlertDialog.Builder(getActivity())
            .setTitle(R.string.rename_note_dialog_title)
            .setView(filenameInput)
            .setPositiveButton(R.string.rename_note_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    String filename = filenameInput.getText().toString();
                    if (TextUtils.isEmpty(filename)) {
                        filename = filenameInput.getHint().toString();
                    }
                    if (!filename.endsWith(".txt")) {
                        filename += ".txt";
                    }

                    DbxPath p;
                    try {
                        p = new DbxPath("/" + filename);
                    } catch (DbxPath.InvalidPathException e) {
                        // TODO: build a custom dialog that won't even allow invalid filenames
                        Toast.makeText(getActivity(), R.string.error_invalid_filename, Toast.LENGTH_LONG).show();
                        return;
                    }

                    try {
                        DbxFileSystem.forAccount(mAccountManager.getLinkedAccount()).move(fileInfo.path, p);
                    } catch (DbxException.Exists e) {
                        Toast.makeText(getActivity(), R.string.error_file_already_exists, Toast.LENGTH_LONG).show();
                    } catch (DbxException e) {
                        e.printStackTrace();
                    }
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do nothing.
                }
            }).show();
        } else if (itemId == MENU_DELETE) {
            try {
                DbxFileSystem.forAccount(mAccountManager.getLinkedAccount()).delete(fileInfo.path);
            } catch (DbxException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mAccountManager = NotesAppConfig.getAccountManager(activity);

        mCallbacks = (Callbacks) activity;
        setHasOptionsMenu(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected(((DbxFileInfo)getListAdapter().getItem(position)).path);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mAccountManager.hasLinkedAccount()) {

            // New note
            MenuItem newNote = menu.add(R.string.new_note_option);
            newNote.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final EditText filenameInput = new EditText(getActivity());
                    filenameInput.setHint(R.string.new_note_name_hint);
                    filenameInput.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

                    new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.new_note_dialog_title)
                    .setView(filenameInput)
                    .setPositiveButton(R.string.new_note_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String filename = filenameInput.getText().toString();
                            if (TextUtils.isEmpty(filename)) {
                                filename = filenameInput.getHint().toString();
                            }
                            if (!filename.endsWith(".txt")) {
                                filename += ".txt";
                            }

                            DbxPath p;
                            try {
                                if (filename.contains("/")) {
                                    Toast.makeText(getActivity(), "invalid filename", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                p = new DbxPath("/" + filename);
                            } catch (DbxPath.InvalidPathException e) {
                                // TODO: build a custom dialog that won't even allow invalid filenames
                                Toast.makeText(getActivity(), "invalid filename", Toast.LENGTH_LONG).show();
                                return;
                            }
                            mCallbacks.onItemSelected(p);
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Do nothing.
                        }
                    }).show();
                    return true;
                }
            });

            // Unlink
            MenuItem unlink = menu.add(R.string.unlink_from_dropbox);
            unlink.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    mAccountManager.unlink();
                    setListAdapter(null);
                    showUnlinkedView();
                    return true;
                }
            });
        }
    }

    @Override
    public Loader<List<DbxFileInfo>> onCreateLoader(int id, Bundle args) {
        return new FolderLoader(getActivity(), mAccountManager, DbxPath.ROOT);
    }

    @Override
    public void onLoadFinished(Loader<List<DbxFileInfo>> loader, List<DbxFileInfo> data) {
        mLoadingSpinner.setVisibility(View.GONE);
        mEmptyText.setVisibility(View.VISIBLE);

        // TODO: would be nice to maintain the list's scroll position here when new data is delivered
        setListAdapter(new FolderAdapter(getActivity(), data));
        registerForContextMenu(getListView());
    }

    @Override
    public void onLoaderReset(Loader<List<DbxFileInfo>> loader) {
        // Do nothing.
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private void doLoad(boolean reset) {
        if (mAccountManager.hasLinkedAccount()) {
            mEmptyText.setVisibility(View.GONE);
            mLoadingSpinner.setVisibility(View.VISIBLE);
            if (reset) {
                getLoaderManager().restartLoader(0, null, this);
            } else {
                getLoaderManager().initLoader(0, null, this);
            }
        }
    }

    private void showUnlinkedView() {
        getListView().setVisibility(View.GONE);
        mEmptyText.setVisibility(View.GONE);
        mLoadingSpinner.setVisibility(View.GONE);
        mLinkButton.setVisibility(View.VISIBLE);
        getActivity().supportInvalidateOptionsMenu();
        getView().postInvalidate();
    }

    private void showLinkedView(boolean reset) {
        getListView().setVisibility(View.VISIBLE);
        mEmptyText.setVisibility(View.GONE);
        mLoadingSpinner.setVisibility(View.VISIBLE);
        mLinkButton.setVisibility(View.GONE);
        getActivity().supportInvalidateOptionsMenu();
        getView().postInvalidate();
        doLoad(reset);
    }
}
