package com.enfocareservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enfocareservice.entity.SubscriptionEntity;
import com.enfocareservice.model.Subscription;
import com.enfocareservice.model.mapper.SubscriptionMapper;
import com.enfocareservice.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

	@Autowired
	private SubscriptionMapper subscriptionMapper;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	public List<Subscription> getAllSubscriptions() {
		return subscriptionRepository.findAll().stream().map(subscriptionMapper::map).collect(Collectors.toList());
	}

	public Subscription createSubscription(String email, String subscriptionType) {
		SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
		subscriptionEntity.setEmail(email);
		subscriptionEntity.setExpiry(calculateExpiryDate(subscriptionType));
		SubscriptionEntity savedEntity = subscriptionRepository.save(subscriptionEntity);
		return subscriptionMapper.map(savedEntity);
	}

	public Subscription updateSubscription(Integer id, Subscription subscription, String subscriptionType) {
		Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findById(id);
		if (subscriptionEntityOptional.isPresent()) {
			SubscriptionEntity subscriptionEntity = subscriptionEntityOptional.get();
			subscriptionEntity.setEmail(subscription.getEmail());
			subscriptionEntity.setExpiry(calculateExpiryDate(subscriptionType));
			SubscriptionEntity updatedEntity = subscriptionRepository.save(subscriptionEntity);
			return subscriptionMapper.map(updatedEntity);
		}
		return null;
	}

	public SubscriptionEntity getActiveSubscription(String email) {
		Optional<SubscriptionEntity> subscriptionEntityOptional = subscriptionRepository.findByEmail(email);
		return subscriptionEntityOptional.map(subscription -> {
			if (subscription.getExpiry().isBefore(LocalDateTime.now())) {
				subscriptionRepository.delete(subscription);
				return null; // Subscription is expired, return null
			} else {
				return subscription; // Subscription is active, return the object
			}
		}).orElse(null); // No subscription found, return null
	}

	private LocalDateTime calculateExpiryDate(String subscriptionType) {
		LocalDateTime now = LocalDateTime.now();
		switch (subscriptionType.toLowerCase()) {
		case "weekly":
			return now.plusWeeks(1);
		case "monthly":
			return now.plusMonths(1);
		case "annually":
			return now.plusYears(1);
		default:
			throw new IllegalArgumentException("Invalid subscription type: " + subscriptionType);
		}
	}

}