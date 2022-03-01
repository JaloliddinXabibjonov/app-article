package com.example.article.servise;

import com.example.article.entity.Prices;
import com.example.article.payload.ApiResponse;
import com.example.article.payload.PriceCounterDto;
import com.example.article.repository.PricesRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricesOfArticlesService {
@Autowired
PricesRepos pricesRepo;


//    public ApiResponse add(PricesOfArticlesDto priceDto) {
//        try {
//            PricesOfArticle pricesOfArticle = new PricesOfArticle();
//            pricesOfArticle.setDoi(priceDto.getDoi());
//            pricesOfArticle.setBittaBosmaJunalNarxi(priceDto.getBittaBosmaJunalNarxi());
//            pricesOfArticle.setBittaJurnaldaChopEtishNarxi(priceDto.getBittaJurnaldaChopEtishNarxi());
//            pricesOfArticle.setSahifaNarxi(priceDto.getSahifaNarxi());
//            pricesOfArticleRepository.save(pricesOfArticle);
//            return new ApiResponse("Muvaffaqiyatli sadlandi",true);
//        }catch (Exception e) {
//            return new ApiResponse("Xatolik yuz berdi", false);
//        }
//    }

//    public ApiResponse edit(PricesOfArticlesDto prices) {
//        Optional<PricesOfArticle> pricesOfArticleOptional = pricesOfArticleRepository.findById(prices.getId());
//        if (pricesOfArticleOptional.isPresent()){
//            PricesOfArticle prices1 = pricesOfArticleOptional.get();
//            prices1.setDoi(prices.getDoi());
//            prices1.setBittaBosmaJunalNarxi(prices.getBittaBosmaJunalNarxi());
//            prices1.setBittaJurnaldaChopEtishNarxi(prices.getBittaJurnaldaChopEtishNarxi());
//            prices1.setSahifaNarxi(prices.getSahifaNarxi());
//            pricesOfArticleRepository.save(prices1);
//            return new ApiResponse("Tahrirlandi", true);
//        }
//        return new ApiResponse("Xatolik", false);
//    }

    public ApiResponse pricesCounter(PriceCounterDto pricesDto){
        System.out.println("-----"+pricesDto.getSertifikatSoni());
        System.out.println("-----"+pricesDto.getSahifaSoni());
        System.out.println("-----"+pricesDto.getJurnaldaChopEtishSoni());
        System.out.println("-----"+pricesDto.getBosmaJurnalSoni());
        System.out.println("-----"+pricesDto.isDoi());
        Prices prices = pricesRepo.findById(1).get();
        double p1 = pricesDto.getSertifikatSoni() * prices.getBittaSertifikatNarxi();
        double p2 = pricesDto.isDoi() ? prices.getDoi() : 0;
        double p3 = pricesDto.getSahifaSoni() * prices.getSahifaNarxi();
        double p4 = pricesDto.getJurnaldaChopEtishSoni() * prices.getBittaJurnaldaChopEtishNarxi();
        double p5 = pricesDto.getBosmaJurnalSoni() * prices.getBittaBosmaJunalNarxi();
     double ob=p1+p2+p3+p4+p5;

        return new ApiResponse("ok ",true ,ob);
    }
}
