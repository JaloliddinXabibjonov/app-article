package com.example.article.payload;

import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NegativeOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sms{

	@SerializedName("originator")
	private String originator;

	@SerializedName("content")
	private Content content;

	public String getOriginator(){
		return originator;
	}

	public Content getContent(){
		return content;
	}
}