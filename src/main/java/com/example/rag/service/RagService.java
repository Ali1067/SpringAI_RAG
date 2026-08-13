package com.example.rag.service;
import com.example.rag.dto.*;import org.springframework.ai.chat.client.ChatClient;import org.springframework.ai.document.Document;import org.springframework.ai.vectorstore.*;import org.springframework.stereotype.Service;import java.util.*;
@Service
public class RagService {

    private final ChatClient chat;
    private final VectorStore store;

    public RagService(ChatClient.Builder b, VectorStore s) {
        chat = b.build();
        store = s;
    }

    public AskResponse ask(String q) {

        List<Document> docs = store.similaritySearch(
                SearchRequest.builder()
                        .query(q)
                        .topK(5)
                        .similarityThreshold(0.45)
                        .build()
        );

        if (docs == null || docs.isEmpty()) {
            return new AskResponse(
                    "I don't have this information in the available business data.",
                    null,
                    null,
                    List.of()
            );
        }

        String context = docs.stream()
                .map(d ->
                        "TITLE: " + d.getMetadata().get("title") +
                                "\nURL: " + d.getMetadata().get("url") +
                                "\nCONTENT: " + d.getText()
                )
                .reduce("", (a, b) -> a + "\n\n" + b);

        String prompt = """
                You are a portal help assistant.
                Answer ONLY from the CONTEXT.
                Do not use outside knowledge.
                If unsupported, say: I don't have this information in the available business data.
                Do not invent URLs.
                Keep it concise.

                CONTEXT:
                %s

                QUESTION:
                %s
                """.formatted(context, q);

        String answer = chat.prompt()
                .user(prompt)
                .call()
                .content();

        List<AskResponse.Source> sources = docs.stream()
                .map(d -> new AskResponse.Source(
                        (String) d.getMetadata().get("title"),
                        (String) d.getMetadata().get("url")
                ))
                .toList();

        Document best = docs.get(0);

        return new AskResponse(
                answer,
                (String) best.getMetadata().get("title"),
                (String) best.getMetadata().get("url"),
                sources
        );
    }
}