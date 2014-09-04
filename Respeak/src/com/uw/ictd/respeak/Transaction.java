package com.uw.ictd.respeak;

import java.util.Date;

public class Transaction {
	private String mRequestorName;
	private double mMaxRewardAmount;
	private double mEarnedRewardAmount;
	private Date mDateCompleted;

	public Transaction(String requestorName, double maxRewardAmount,
			double earnedRewardAmount, Date dateCompleted) {
		mRequestorName = requestorName;
		mMaxRewardAmount = maxRewardAmount;
		mEarnedRewardAmount = earnedRewardAmount;
		mDateCompleted = dateCompleted;
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
	
	

}
