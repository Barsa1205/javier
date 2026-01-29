package daw2.desarollo.consumir.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class JokeController {

    private final JokeService jokeService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GetMapping("/api/joke")
    public Mono<Quote> getJoke() {
        return jokeService.getRandomJoke();
    }
}
