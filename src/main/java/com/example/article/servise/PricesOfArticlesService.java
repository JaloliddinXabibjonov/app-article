package com.example.article.servise;

import com.example.article.entity.PricesOfArticle;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.PriceCounterDto;
import com.example.article.payload.PricesOfArticlesDto;
import com.example.article.repository.PricesOfArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PricesOfArticlesService {
@Autowired
PricesOfArticleRepository pricesOfArticleRepository;

    public ApiResponse add(PricesOfArticlesDto priceDto) {
        try {
            PricesOfArticle pricesOfArticle = new PricesOfArticle();
            pricesOfArticle.setDoi(priceDto.getDoi());
            pricesOfArticle.setBittaBosmaJunalNarxi(priceDto.getBittaBosmaJunalNarxi());
            pricesOfArticle.setBittaJurnaldaChopEtishNarxi(priceDto.getBittaJurnaldaChopEtishNarxi());
            pricesOfArticle.setSahifaNarxi(priceDto.getSahifaNarxi());
            pricesOfArticleRepository.save(pricesOfArticle);
            return new ApiResponse("Muvaffaqiyatli sadlandi",true);
        }catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }

    public ApiResponse edit(PricesOfArticlesDto prices) {
        Optional<PricesOfArticle> pricesOfArticleOptional = pricesOfArticleRepository.findById(prices.getId());
        if (pricesOfArticleOptional.isPresent()){
            PricesOfArticle prices1 = pricesOfArticleOptional.get();
            prices1.setDoi(prices.getDoi());
            prices1.setBittaBosmaJunalNarxi(prices.getBittaBosmaJunalNarxi());
            prices1.setBittaJurnaldaChopEtishNarxi(prices.getBittaJurnaldaChopEtishNarxi());
            prices1.setSahifaNarxi(prices.getSahifaNarxi());
            pricesOfArticleRepository.save(prices1);
            return new ApiResponse("Tahrirlandi", true);
        }
        return new ApiResponse("Xatolik", false);
    }

    public Double pricesCounter(PriceCounterDto prices){
        PricesOfArticle article = pricesOfArticleRepository.findById(1).get();
        double p1 = article.getBittaSertifikatNarxi() * prices.getSertifikatSoni();
        double p2 = prices.isDoi() ? article.getDoi() : 0;
        double p3 = article.getSahifaNarxi() * prices.getSahifaSoni();
        double p4 = article.getBittaJurnaldaChopEtishNarxi() * prices.getJurnaldaChopEtishSoni();
        double p5 = article.getBittaBosmaJunalNarxi() * prices.getBosmaJurnalSoni();
        return p1+p2+p3+p4+p5;
    }
}
