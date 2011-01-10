package com.fasteque.pachubewidget;

public class ParsedFeedData
{
	private int id = -1;
	private String tag = "";
	private String value = "";
	private String unitSymbol = "";
	private String unitName = "";
	
	public int getId()
	{
		return id;
	}
	
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	
	public String getTag()
	{
		return tag;
	}
	
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	
	public String getValue()
	{
		return value;
	}
	
	
	public void setValue(String value)
	{
		this.value = value;
	}
	
	
	public String getUnitSymbol()
	{
		return unitSymbol;
	}
	
	
	public void setUnitSymbol(String unitSymbol)
	{
		this.unitSymbol = unitSymbol;
	}
	
	
	public String getUnitName()
	{
		return unitName;
	}
	
	
	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}
}
