package com.bankinc.manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankinc.manager.entity.Card;


@Repository
public interface CardRepository extends JpaRepository<Card, String> {
	
	Optional<Card> findByCardNumber(String cardNumber);
	

}
