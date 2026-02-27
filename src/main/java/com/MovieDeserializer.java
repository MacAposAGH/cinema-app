package com;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;

public class MovieDeserializer extends StdDeserializer<Movie> {

    public MovieDeserializer() {
        super(Movie.class);
    }

    @Override
    public Movie deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode jsonNode = p.getCodec().readTree(p);
        ArrayList<String> genres = new ArrayList<>();
        for (JsonNode genre : jsonNode.get("genres")) {
            JsonNode genreName = genre.get("name") == null ? genre : genre.get("name");
            genres.add(genreName.asText());
        }
        return new Movie(jsonNode.get("title").asText(), jsonNode.get("runtime").asInt(), genres);
    }
}