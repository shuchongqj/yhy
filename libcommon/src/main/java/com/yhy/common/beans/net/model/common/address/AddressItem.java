package com.yhy.common.beans.net.model.common.address;

import java.io.Serializable;


public class AddressItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3067236185246929590L;
	public long id;
	public String code;
	public String name;
	public int isSpecial;
	public String fatherCode;

	public AddressItem() {
		super();
	}

	public AddressItem(String code, String name, int isSpecial) {
		super();
		this.code = code;
		this.name = name;
		this.isSpecial = isSpecial;
	}

	public AddressItem(String code, String name, String fatherCode) {
		super();
		this.code = code;
		this.name = name;
		this.fatherCode = fatherCode;
	}
}
