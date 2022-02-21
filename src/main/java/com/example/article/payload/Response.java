package com.example.article.payload;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response{

	@SerializedName("messages")
	private List<MessagesItem> messages;

	public List<MessagesItem> getMessages(){
		return messages;
	}
}