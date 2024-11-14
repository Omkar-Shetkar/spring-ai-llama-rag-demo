package com.example;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final VectorStore vectorStore;

    private ChatClient chatClient;

    private static final String PROMPT_BLUEPRINT = """
        Your are helpful AI assistant who responds to queries primarily based on the documents section below.
        
        context:
        
        {context}
        
        query:

        {query}
        In case you don't have any answer from the context provided, just say:
        I'm sorry I don't have the information you are looking for.
        """;

    public ChatController(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder
                .build();
    }

    @PostMapping("/chat")
    public String postMethodName(@RequestBody String query) {
        List<Document> relatedDocuments = vectorStore.similaritySearch(query);
        String documents = relatedDocuments.stream().map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

        return chatClient.prompt(createPrompt(query, relatedDocuments)).call().content();

        // return this.chatClient
        //         .prompt()
        //         .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()),
        //         new SimpleLoggerAdvisor())
        //         .system(s -> s.text(SYSTEM_PROMPT).params(Map.of("documents", documents, "question", "Who is Benny?")))
        //         .user(message)
        //         .call()
        //         .content();
    }

    private String createPrompt(String query, List<Document> context) {
        PromptTemplate promptTemplate = new PromptTemplate(PROMPT_BLUEPRINT);
        promptTemplate.add("query", query);
        promptTemplate.add("context", context);
        return promptTemplate.render();
    }

}
