package com.uw.ictd.respeak;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class HistoryListFragment extends ListFragment {
	private ArrayList<Transaction> mTransactions;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTransactions = TransactionList.get(getActivity()).getTransactions();
		
		ArrayAdapter<Transaction> adapter = new ArrayAdapter<Transaction>(getActivity(), android.R.layout.simple_list_item_1, mTransactions);
		setListAdapter(adapter);
	}
}
