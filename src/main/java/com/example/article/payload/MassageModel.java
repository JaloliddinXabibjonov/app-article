package com.example.article.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MassageModel {
    private String massage;
    private  String fromLogin;

    public MassageModel(String massage) {
        this.massage = massage;
    }

    @Override
    public String toString() {
        return "MassageModel{" +
                "massage='" + massage + '\'' +
                ", fromLogin='" + fromLogin + '\'' +
                '}';
    }
}
