package com.fasteque.pachubewidget;

import java.util.ArrayList;

public class ParsedFeed
{
	private int feedID = -1;
	private String feedTitle = "";
	private String feedDescription = "";
	private String feedStatus = "";
	ArrayList<ParsedFeedData> feedData = new ArrayList<ParsedFeedData>();
	
	
	public int getFeedID()
	{
		return feedID;
	}


	public void setFeedID(int feedID)
	{
		this.feedID = feedID;
	}


	public String getFeedTitle()
	{
		return feedTitle;
	}
	
	
	public void setFeedTitle(String feedTitle)
	{
		this.feedTitle = feedTitle;
	}
	
	
	public String getFeedDescription()
	{
		return feedDescription;
	}
	
	
	public void setFeedDescription(String feedDescription)
	{
		this.feedDescription = feedDescription;
	}


	public String getFeedStatus() {
		return feedStatus;
	}


	public void setFeedStatus(String feedStatus) {
		this.feedStatus = feedStatus;
	}
}
