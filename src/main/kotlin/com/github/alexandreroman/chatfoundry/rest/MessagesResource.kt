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

package com.github.alexandreroman.chatfoundry.rest

import com.github.alexandreroman.chatfoundry.event.MessagesListUpdatedEvent
import com.github.alexandreroman.chatfoundry.repo.Message
import com.github.alexandreroman.chatfoundry.repo.MessageRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.time.OffsetDateTime
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

/**
 * REST service for messages.
 */
@RestController
@RequestMapping("/messages")
class MessagesResource(val messageRepo: MessageRepository) {
    private val logger = LoggerFactory.getLogger(MessagesResource::class.java)
    private val emitters = CopyOnWriteArrayList<SseEmitter>()

    @GetMapping("/stream")
    fun getMessageStream(): SseEmitter {
        val emitter = SseEmitter(TimeUnit.MINUTES.toMillis(1))
        emitters.add(emitter)

        val emitterCleanup = Runnable { emitters.remove(emitter) }
        emitter.onCompletion(emitterCleanup)
        return emitter
    }

    fun onEvent(e: MessagesListUpdatedEvent) {
        logger.info("Sending new message to clients: {}", e.message)
        emitters.forEach({ it.send(e.message, MediaType.APPLICATION_JSON) })
    }

    @GetMapping
    fun getMessages(): List<Message> {
        return messageRepo.list()
    }

    @PostMapping
    fun postMessage(@RequestBody req: NewMessageRequest): ResponseEntity<Void> {
        logger.info("Posting new message: \"{}\", from {}", req.content, req.from ?: "anonymous")
        messageRepo.save(req.content, OffsetDateTime.now(), req.from)

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    class NewMessageRequest(val content: String, val from: String?)
}

