package com.vluee.generalcode;

public class CodeMetadata {
	private String code;
	private String simpleName;

	public CodeMetadata(String code, String simpleName) {
		this.code = code;
		this.simpleName = simpleName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	@Override
	public String toString() {
		return String.format("CodeMetadata [code=%s, simpleName=%s]", code, simpleName);
	}

}
