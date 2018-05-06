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

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory

/**
 * Local configuration (dev mode).
 */
@Configuration
@Profile("!cloudfoundry")
class DevConfig {
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        // Connect to a local Redis server instance.
        return JedisConnectionFactory()
    }
}
