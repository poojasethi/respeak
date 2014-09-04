package com.uw.ictd.respeak;

import java.util.ArrayList;

import android.content.Context;

public class TransactionList {
	private ArrayList<Transaction> mTransactions;
	private static TransactionList sTransactionList;
	private Context mContext;
	
	private TransactionList(Context context) {
		mContext = context;
		mTransactions = new ArrayList<Transaction>();
	}
	
	private static TransactionList get(Context c) {
		if (sTransactionList == null) {
			sTransactionList = new TransactionList(c.getApplicationContext());
		}
		return sTransactionList;
	}
}
