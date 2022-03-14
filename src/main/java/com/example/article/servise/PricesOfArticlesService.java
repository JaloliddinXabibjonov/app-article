package com.example.article.servise;

import com.example.article.entity.Prices;
import com.example.article.entity.PricesOfArticle;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.PriceCounterDto;
import com.example.article.payload.PricesOfArticlesDto;
import com.example.article.repository.PricesOfArticleRepository;
import com.example.article.repository.PricesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PricesOfArticlesService {
    @Autowired
    PricesRepository pricesRepository;
    @Autowired
    PricesOfArticleRepository pricesOfArticleRepository;

    /**
     * MAQOLANI BAZAVIY HISOBLASH NARXLARINI TAHRIRLASH
     */
    public ApiResponse editPricesArticle(PricesOfArticlesDto pricesDto) {
        try {
            Prices prices = pricesRepository.getById(1);
            prices.setDoi(pricesDto.getDoi());
            prices.setBittaBosmaJunalNarxi(pricesDto.getBittaBosmaJunalNarxi());
            prices.setBittaSertifikatNarxi(pricesDto.getBittaSertifikatNarxi());
            prices.setSahifaNarxi(pricesDto.getSahifaNarxi());
            pricesRepository.save(prices);
            return new ApiResponse("Muvaffaqiyatli tahrirlandi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik yuz berdi", false);
        }
    }
//    public ApiResponse add(PricesOfArticlesDto priceDto) {
//        try {
//            PricesOfArticle pricesOfArticle = new PricesOfArticle();
//            pricesOfArticle.setDoi(priceDto.getDoi());
//            pricesOfArticle.setBittaBosmaJunalNarxi(priceDto.getBittaBosmaJunalNarxi());
//            pricesOfArticle.setSahifaNarxi(priceDto.getSahifaNarxi());
//            pricesOfArticleRepository.save(pricesOfArticle);
//            return new ApiResponse("Muvaffaqiyatli sadlandi",true);
//        }catch (Exception e) {
//            return new ApiResponse("Xatolik yuz berdi", false);
//        }
//    }

    public ApiResponse editPrice(PricesOfArticlesDto pricesDto) {
        Optional<Prices> optionalPrices = pricesRepository.findById(1);
        if (optionalPrices.isPresent()) {
            Prices prices = optionalPrices.get();
            prices.setDoi(pricesDto.getDoi());
            prices.setBittaBosmaJunalNarxi(pricesDto.getBittaBosmaJunalNarxi());
            prices.setSahifaNarxi(pricesDto.getSahifaNarxi());
            prices.setBittaSertifikatNarxi(pricesDto.getBittaSertifikatNarxi());
            pricesRepository.save(prices);
            return new ApiResponse("Tahrirlandi", true);
        }
        return new ApiResponse("Xatolik", false);
    }

    public ApiResponse pricesCounter(PriceCounterDto pricesDto) {
        Prices prices = pricesRepository.findById(1).get();
        double p1 = pricesDto.getSertifikatSoni() * prices.getBittaSertifikatNarxi();
        double p2 = pricesDto.isDoi() ? prices.getDoi() : 0;
        double p3 = pricesDto.getSahifaSoni() * prices.getSahifaNarxi();
//        double p4 = pricesDto.getJurnaldaChopEtishSoni() * prices.getBittaJurnaldaChopEtishNarxi();
        double p5 = pricesDto.getBosmaJurnalSoni() * prices.getBittaBosmaJunalNarxi();
        double ob = p1 + p2 + p3 + p5;

        return new ApiResponse("ok ", true, ob);
    }

    public Prices getPrice() {
        return pricesRepository.findById(1).get();
    }
}
