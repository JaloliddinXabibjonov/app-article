package com.example.article.servise;

import com.example.article.payload.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

@Service
public class SmsService {


    public  void  sendSms( String recipient,  String text )  {

try {
    int min = 100000;
    int max = 999999;
    int number = (int) (Math.random() * (max - min + 1) + min);
    String username = "intalim";
    String password = "54DrbVEz83";
    String originator = "INTALIM_UZ";
    String messageId = String.valueOf(number);
    Response response = new Response();
    MessagesItem messagesItem = new MessagesItem();
    Sms sms = new Sms();
    Content content = new Content();
    content.setText(text);
    sms.setOriginator(originator);
    sms.setContent(content);
    messagesItem.setSms(sms);
    messagesItem.setMessageId(messageId);
    messagesItem.setRecipient(recipient);
    response.setMessages(List.of(messagesItem));


    RestTemplate restTemplate = new RestTemplate();
    String plainCreds = "intalim:54DrbVEz83";
    String base64Creds = Base64.getEncoder().encodeToString(plainCreds.getBytes());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", "Basic " + base64Creds);
    String url = "http://91.204.239.44/broker-api/send";
    HttpEntity<Response> entity = new HttpEntity<>(response, headers);
    ResponseEntity<Response> response1 = restTemplate.exchange(url, HttpMethod.POST, entity, Response.class);



        } catch (Exception e) {
            e.printStackTrace();
        }





    }


}