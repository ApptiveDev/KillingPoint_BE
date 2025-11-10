package apptive.team5.user.util;

import java.security.SecureRandom;
import java.util.UUID;

public class TagGenerator {

    private static final SecureRandom random = new SecureRandom();

    private static final String[] ADJECTIVES = {
            "happy", "brave", "quick", "bright", "calm", "lucky", "clever", "cute", "strong", "charming", "killing"
    };

    private static final String[] NOUNS = {
            "cat", "dog", "tiger", "rabbit", "fox", "lion", "otter", "panda", "penguin", "dolphin", "part"
    };

    public static String generateTag() {
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)].toLowerCase();
        String noun = NOUNS[random.nextInt(NOUNS.length)].toLowerCase();
        int number = random.nextInt(100);
        String unique = UUID.randomUUID().toString().substring(0, 6).toLowerCase();
        return adjective + noun + number + "_" + unique;
    }
}
