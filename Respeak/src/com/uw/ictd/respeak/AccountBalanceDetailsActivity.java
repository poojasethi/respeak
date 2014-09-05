package com.uw.ictd.respeak;

import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AccountBalanceDetailsActivity extends Activity {
	public static final String EXTRA_TRANSACTION_ID = "com.uw.ictd.respeak.transaction_id";
	private Transaction mTransaction;
	private TextView mRequestorName;
	private TextView mMaxRewardAmount;
	private TextView mAmountEarned;
	private TextView mDateSubmitted;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_account_balance_details);
		UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_TRANSACTION_ID);
		mTransaction = TransactionList.get(this).getTransaction(crimeId);
		
		mRequestorName = (TextView) findViewById(R.id.accountRequestorName);
		mMaxRewardAmount = (TextView) findViewById(R.id.accountMaxRewardAmount);
		mAmountEarned = (TextView) findViewById(R.id.accountAmountEarned);
		mDateSubmitted = (TextView) findViewById(R.id.accountDateSubmitted);
		
		mRequestorName.setText(mTransaction.getRequestorName());
		mMaxRewardAmount.setText("\u20B9" + mTransaction.getMaxRewardAmount());
		mAmountEarned.setText("\u20B9" + mTransaction.getEarnedRewardAmount());
		mDateSubmitted.setText(mTransaction.getDateCompleted().toString());
	}
}
