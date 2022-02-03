package com.example.article.repository;


import com.example.article.entity.MessageRecorderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRecorderRepository extends JpaRepository<MessageRecorderEntity, Long> {
	List<MessageRecorderEntity> findAllByReceiverName(String receiverName);
}