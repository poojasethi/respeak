package com.uw.ictd.respeak;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.content.Context;

public class TransactionList {
	private ArrayList<Transaction> mTransactions;
	private static TransactionList sTransactionList;
	private Context mContext;
	
	private TransactionList(Context context) {
		mContext = context;
		mTransactions = new ArrayList<Transaction>();
		
		// TODO remove test transaction later
		Transaction test = new Transaction(UUID.randomUUID(), "MSRI", 2, 1, new Date());
		mTransactions.add(test);
	}
	
	private static TransactionList get(Context c) {
		if (sTransactionList == null) {
			sTransactionList = new TransactionList(c.getApplicationContext());
		}
		return sTransactionList;
	}
	
	public ArrayList<Transaction> getTransactions() {
		return mTransactions;
	}
	
	public Transaction getTransaction(UUID id) {
		for (Transaction t: mTransactions) {
			if (t.getId().equals(id)) {
				return t;
			}
		}
		return null;
	}
}
