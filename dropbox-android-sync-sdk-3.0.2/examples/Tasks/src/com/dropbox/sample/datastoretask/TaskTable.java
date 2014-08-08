package com.dropbox.sample.datastoretask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;

public class TaskTable {
    private DbxDatastore mDatastore;
    private DbxTable mTable;

    public class Task {
        private DbxRecord mRecord;

        public Task(DbxRecord record) {
            mRecord = record;
        }

        public String getId() {
            return mRecord.getId();
        }

        public String getName() {
            return mRecord.getString("taskname");
        }

        public Date getCreated() {
            return mRecord.getDate("created");
        }

        public boolean isCompleted() {
            return mRecord.getBoolean("completed");
        }

        public void delete() throws DbxException {
            mRecord.deleteRecord();
            mDatastore.sync();
        }

        public void toggleCompleted() throws DbxException {
            mRecord.set("completed", !isCompleted());
            mDatastore.sync();
        }
    }

    public TaskTable(DbxDatastore datastore) {
        mDatastore = datastore;
        mTable = datastore.getTable("tasks");
    }

    public void createTask(String taskname) throws DbxException {
        DbxFields taskFields = new DbxFields()
                .set("completed", false)
                .set("taskname", taskname)
                .set("created", new Date());
        mTable.insert(taskFields);
        mDatastore.sync();
    }

    public List<Task> getTasksSorted() throws DbxException {
        List<Task> resultList = new ArrayList<Task>();
        for (DbxRecord result : mTable.query()) {
            resultList.add(new Task(result));
        }
        Collections.sort(resultList, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getCreated().compareTo(o2.getCreated());
            }
        });
        return resultList;
    }
}
