package com.example.rag.controller;
import com.example.rag.dto.*;import com.example.rag.service.RagService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/ai") public class AiController {private final RagService service;public AiController(RagService service){this.service=service;}@PostMapping("/ask") public AskResponse ask(@Valid @RequestBody AskRequest request){return service.ask(request.question());}}
