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

package com.github.alexandreroman.chatfoundry.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.alexandreroman.chatfoundry.event.EventPublisher
import com.github.alexandreroman.chatfoundry.event.MessagesListUpdatedEvent
import com.github.alexandreroman.chatfoundry.rest.MessagesResource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer

/**
 * Setup events between components.
 */
@Configuration
class EventConfig {
    @Bean
    fun redisMessageListenerContainer(connFactory: RedisConnectionFactory,
                                      @Qualifier("messagesResourceListenerAdapter") a: MessageListenerAdapter)
            : RedisMessageListenerContainer {
        // Use Redis as a PUB/SUB server instance.
        val container = RedisMessageListenerContainer()
        container.connectionFactory = connFactory
        container.addMessageListener(a, ChannelTopic(EventPublisher.MESSAGES_LIST_UPDATED_TOPIC))
        return container
    }

    @Bean
    fun messagesResourceListenerAdapter(d: MessagesResource,
                                        objectMapper: ObjectMapper): MessageListenerAdapter {
        val serializer = Jackson2JsonRedisSerializer(MessagesListUpdatedEvent::class.java)
        serializer.setObjectMapper(objectMapper)
        val mld = MessageListenerAdapter(d, "onEvent")
        mld.setSerializer(serializer)
        return mld
    }
}
