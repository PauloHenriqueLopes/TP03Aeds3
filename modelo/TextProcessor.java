package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextProcessor {

    public static List<String> tokenize(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        // Converter para minúsculas e dividir em palavras
        String[] words = text.toLowerCase()
                .replaceAll("[^a-z0-9áéíóúâêîôûãõç ]", "")
                .split("\\s+");

        List<String> stopWords = Arrays.asList(
                "a", "o", "e", "é", "de", "do", "da", "dos", "das", "em", "no", "na",
                "nos", "nas", "por", "para", "com", "como", "que", "se", "ou", "um",
                "uma", "uns", "umas", "ao", "à", "às", "meu", "sua", "seu", "teu");

        List<String> tokens = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty() && !stopWords.contains(word)) {
                tokens.add(word);
            }
        }

        return tokens;
    }

    public static float calculateTf(String term, String text) {
        List<String> tokens = tokenize(text);
        if (tokens.isEmpty())
            return 0;

        int termCount = 0;
        for (String token : tokens) {
            if (token.equals(term)) {
                termCount++;
            }
        }

        return (float) termCount / tokens.size();
    }
}