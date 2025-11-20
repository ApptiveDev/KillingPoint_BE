package apptive.team5.youtube;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class YoutubeApiKeyProvider {

    @Value("${youtube.api.key}")
    private String apiKey;

    private List<String> keys;
    private final AtomicInteger index = new AtomicInteger(0);

    @PostConstruct
    public void init() {

        keys = Arrays.stream(apiKey.split(","))
                .map(String::trim)
                .toList();
    }

    public String nextKey() {
        int i = Math.abs(index.getAndIncrement() % keys.size());
        return keys.get(i);
    }
}
