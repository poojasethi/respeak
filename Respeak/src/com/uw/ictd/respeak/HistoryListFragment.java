package com.uw.ictd.respeak;

import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryListFragment extends ListFragment {
	private ArrayList<Transaction> mTransactions;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		mTransactions = TransactionList.get(getActivity()).getTransactions();
		
		ArrayAdapter<Transaction> adapter = new ArrayAdapter<Transaction>(getActivity(), android.R.layout.simple_list_item_1, mTransactions);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Transaction t = (Transaction) (getListAdapter()).getItem(position);
		
		Intent i = new Intent(getActivity(), AccountBalanceDetailsActivity.class);
		i.putExtra(AccountBalanceDetailsActivity.EXTRA_TRANSACTION_ID, t.getId());
		startActivity(i);
	}
}
