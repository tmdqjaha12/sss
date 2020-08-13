package com.sbs.jhs.at.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class ResultData {
	private String resultCode;
	private String msg;
	private Object body;

	public ResultData(String resultCode, String msg, Object body) {
		this.resultCode = resultCode;
		this.msg = msg;
		this.body = body;
	}
}
