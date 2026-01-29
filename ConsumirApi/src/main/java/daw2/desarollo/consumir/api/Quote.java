package daw2.desarollo.consumir.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Quote(int id, String type, String setup, String punchline) {}
