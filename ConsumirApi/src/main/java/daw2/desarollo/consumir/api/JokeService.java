package daw2.desarollo.consumir.api;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JokeService {

    private final WebClient webClient;

    public JokeService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://official-joke-api.appspot.com").build();
    }

    public Mono<Quote> getRandomJoke() {
        return webClient.get()
                .uri("/random_joke")
                .retrieve()
                .bodyToMono(Quote.class);
    }
}
