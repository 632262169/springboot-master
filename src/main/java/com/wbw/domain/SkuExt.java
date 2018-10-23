/**  
* <p>Title: SkuExt</p>  
* <p>Description: </p>  
* @author wbw  
* @date 2018年10月23日  
* @version 1.0  
*/
package com.wbw.domain;

import java.math.BigDecimal;

public class SkuExt {

	private String sn;
	private String productname;
	private BigDecimal cost;
	private String brankname;
	private String specificationvalues;
	private String areaname;
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public String getBrankname() {
		return brankname;
	}
	public void setBrankname(String brankname) {
		this.brankname = brankname;
	}
	public String getSpecificationvalues() {
		return specificationvalues;
	}
	public void setSpecificationvalues(String specificationvalues) {
		this.specificationvalues = specificationvalues;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	
	
}
