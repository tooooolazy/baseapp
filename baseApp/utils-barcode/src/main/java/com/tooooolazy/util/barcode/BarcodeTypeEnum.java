package com.tooooolazy.util.barcode;

public enum BarcodeTypeEnum {
	auspost("AusPost 4 State Customer Code"),
	azteccode("Aztec Code"),
	
	code128("Code 128");

	private String desc;
	
	BarcodeTypeEnum(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return desc;
	}
}
