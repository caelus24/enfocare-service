package com.enfocareservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

@Component
public class WebSocketMetricsLogger {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketMetricsLogger.class);

	private final WebSocketMessageBrokerStats webSocketStats;

	public WebSocketMetricsLogger(WebSocketMessageBrokerStats webSocketStats) {
		this.webSocketStats = webSocketStats;
	}

	@Scheduled(fixedRate = 5000) // Log every 5 seconds (5000 milliseconds)
	public void logWebSocketMetrics() {
		logger.info("WebSocket Metrics: {}", webSocketStats.getWebSocketSessionStatsInfo());
		logger.info("STOMP Metrics: {}", webSocketStats.getStompSubProtocolStatsInfo());

	}
}
