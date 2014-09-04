package com.uw.ictd.respeak;

import android.content.Context;

public class TransactionList {
	private static TransactionList sTransactionList;
	private Context mContext;
	
	private TransactionList(Context context) {
		mContext = context;
	}
	
	private static TransactionList get(Context c) {
		if (sTransactionList == null) {
			sTransactionList = new TransactionList(c.getApplicationContext());
		}
		return sTransactionList;
	}
	
}
