package com.example.article.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUser {
   private String search;
   private Integer roles_id;
   private boolean enabled;
   private  Integer categoryId;
   private Integer page;
   private Integer size;
private UUID id;

}
