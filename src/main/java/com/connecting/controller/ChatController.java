package com.connecting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.connecting.model.ChatMessage;

@Controller
public class ChatController {

	private final Logger logger = LoggerFactory.getLogger(ChatController.class);
	private final SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@MessageMapping("/chat.register")
	public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		logger.info("User register " + chatMessage.getSender() + ".........." + headerAccessor.getSessionAttributes());
		return chatMessage;
	}

	@MessageMapping("/chat.send")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		logger.info("Message (chat.sent) sent from " + chatMessage.getSender() + " to " + chatMessage.getReciever()
				+ " and message is " + chatMessage.getContent());
		simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender(), "/queue/messages", chatMessage);
		if (!chatMessage.getReciever().equals(chatMessage.getSender())) {
			simpMessagingTemplate.convertAndSendToUser(chatMessage.getReciever(), "/queue/messages", chatMessage);
		}

		return chatMessage;
	}

}
