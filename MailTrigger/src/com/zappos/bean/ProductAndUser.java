package com.zappos.bean;

import java.util.ArrayList;

public class ProductAndUser {
	private String productUrl;
	String productId;
	private String percentOff;
	private String productName;
	public ArrayList<String> emailIds=new ArrayList<String>();

	public ProductAndUser(ArrayList<String> eid) {
		this.emailIds = eid;
	}

	public ArrayList<String> getEmailIds() {
		return emailIds;
	}

	public void setEmailIds(ArrayList<String> emailIds) {
		this.emailIds = emailIds;
	}

	public void setPid(String pid) {
		this.productId = pid;
	}

	public void setPname(String pname) {
		this.setProductName(pname);
	}

	public void setPoff(String po) {
		this.setPercentOff(po);
	}

	public void setPurl(String purl) {
		this.setProductUrl(purl);
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setPercentOff(String percentOff) {
		this.percentOff = percentOff;
	}

	public String getPercentOff() {
		return percentOff;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public String getProductUrl() {
		return productUrl;
	}


}
