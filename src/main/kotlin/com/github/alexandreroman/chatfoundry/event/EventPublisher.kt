/*
 * Chatfoundry - a chatroom implementation using Kotlin/Redis, deployed to Cloud Foundry
 * Copyright (C) 2018 Alexandre Roman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.alexandreroman.chatfoundry.event

import com.github.alexandreroman.chatfoundry.repo.Message
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

/**
 * Component responsible for publishing app events.
 */
@Component
class EventPublisher(val redisTemplate: RedisTemplate<String, Message>) {
    companion object {
        const val MESSAGES_LIST_UPDATED_TOPIC = "MESSAGES_LIST_UPDATED_TOPIC"
    }

    private val logger = LoggerFactory.getLogger(EventPublisher::class.java)

    /**
     * Fire an event when the messages list is updated.
     */
    fun fire(e: MessagesListUpdatedEvent) {
        logger.info("Publishing event: {}", e)
        redisTemplate.convertAndSend(MESSAGES_LIST_UPDATED_TOPIC, e)
    }
}
