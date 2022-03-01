package com.example.article.repository;

import com.example.article.entity.Prices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricesRepos extends JpaRepository<Prices, Integer> {
}
