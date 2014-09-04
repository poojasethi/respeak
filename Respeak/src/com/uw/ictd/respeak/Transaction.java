package com.uw.ictd.respeak;

import java.util.Date;
import java.util.UUID;

public class Transaction {
	private UUID mId;
	private String mRequestorName;
	private double mMaxRewardAmount;
	private double mEarnedRewardAmount;
	private Date mDateCompleted;

	public Transaction(UUID id, String requestorName, double maxRewardAmount,
			double earnedRewardAmount, Date dateCompleted) {
		mId = id;
		mRequestorName = requestorName;
		mMaxRewardAmount = maxRewardAmount;
		mEarnedRewardAmount = earnedRewardAmount;
		mDateCompleted = dateCompleted;
	}
	
	public UUID getId() {
		return mId;
	}

	public String getRequestorName() {
		return mRequestorName;
	}

	public void setRequestorName(String requestorName) {
		mRequestorName = requestorName;
	}

	public double getMaxRewardAmount() {
		return mMaxRewardAmount;
	}

	public void setMaxRewardAmount(double maxRewardAmount) {
		mMaxRewardAmount = maxRewardAmount;
	}

	public double getEarnedRewardAmount() {
		return mEarnedRewardAmount;
	}

	public void setEarnedRewardAmount(double earnedRewardAmount) {
		mEarnedRewardAmount = earnedRewardAmount;
	}

	public Date getDateCompleted() {
		return mDateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		mDateCompleted = dateCompleted;
	}
	
	@Override
	public String toString() {
		return mRequestorName + "\n" + mDateCompleted.toString();
	}
	

}
