package com.enfocareservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enfocareservice.entity.SubscriptionEntity;
import com.enfocareservice.model.Subscription;
import com.enfocareservice.model.SubscriptionRequest;
import com.enfocareservice.service.SubscriptionService;

@RestController
@RequestMapping("/enfocare/subscriptions")
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;

	@GetMapping
	public List<Subscription> getAllSubscriptions() {
		return subscriptionService.getAllSubscriptions();
	}

	@PostMapping("/subscribe")
	public Subscription createSubscription(@RequestBody SubscriptionRequest subscription) {
		return subscriptionService.createSubscription(subscription.getEmail(), subscription.getSubscriptionType());
	}

	@GetMapping("/check-subscription")
	public ResponseEntity<?> checkSubscriptionStatus(@RequestParam String email) {

		System.err.println("CHECK SYBS");
		SubscriptionEntity subscription = subscriptionService.getActiveSubscription(email);
		if (subscription != null) {
			return ResponseEntity.ok(subscription); // Return OK with the subscription object
		} else {
			return ResponseEntity.notFound().build(); // Return 404 if no active subscription found
		}
	}

}