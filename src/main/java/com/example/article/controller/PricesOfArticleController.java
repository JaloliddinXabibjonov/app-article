package com.example.article.controller;

import com.example.article.entity.Prices;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.PriceCounterDto;
import com.example.article.payload.PricesOfArticlesDto;
import com.example.article.servise.PricesOfArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prices")
public class PricesOfArticleController {
    @Autowired
    PricesOfArticlesService pricesService;

//    @PostMapping("/add")
//    public HttpEntity<?> add(@RequestBody PricesOfArticlesDto priceDto){
//        ApiResponse apiResponse = pricesService.add(priceDto);
//        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
//    }
//
//    @PostMapping("/edit")
//    public HttpEntity<?> edit(@RequestBody PricesOfArticlesDto prices){
//        ApiResponse apiResponse = pricesService.edit(prices);
//        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
//    }

    @PostMapping("/getPrices")
    public ApiResponse getPrices(@RequestBody PriceCounterDto priceCounterDto){
        return pricesService.pricesCounter(priceCounterDto);
    }


    @GetMapping("/getPrice")
    public Prices prices(){
        return pricesService.getPrice();
    }

    @PostMapping("/editPrice")
    public HttpEntity<ApiResponse>  editPrice(@RequestBody PricesOfArticlesDto pricesOfArticlesDto){
        ApiResponse apiResponse = pricesService.editPrice(pricesOfArticlesDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

}
