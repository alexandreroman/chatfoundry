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
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * REST services configuration.
 */
@Configuration
class RestConfig {
    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        // Using Kotlin with Spring Web and Spring Data Redis
        // requires some tuning.
        val objectMapper = ObjectMapper()

        // Add support for java.time classes.
        objectMapper.registerModule(JavaTimeModule())
        // Fix date serialization: we want date in ISO-8601 format.
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        // An instance of ObjectMapper supporting Kotlin is required.
        objectMapper.registerKotlinModule()

        return objectMapper
    }
}
