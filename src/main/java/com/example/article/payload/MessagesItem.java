package com.example.article.payload;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class MessagesItem{




	@JsonProperty("message-id")
	private String messageId;

	@JsonProperty("recipient")
	private String recipient;

	@JsonProperty("sms")
	private Sms sms;




	public String getRecipient(){
		return recipient;
	}

	public Sms getSms(){
		return sms;
	}

	public String getMessageId(){
		return messageId;
	}
}