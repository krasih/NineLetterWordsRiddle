import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final int MAX_LENGTH = 9;
    private static Map<Integer, Set<String>> dictionary;

    public static void main(String[] args) throws IOException {

        // 1. Load dictionary from URL:
        dictionary = loadAllWordsAsMap();

        long startTimeNs = System.nanoTime();

        // 2. Process the loaded dictionary (find chains for 9-letter words):
        Set<String> words = new HashSet<>();
        for (String word : dictionary.get(MAX_LENGTH)) {
            if (hasChainDownToOneLetterWord(word)) {
                words.add(word);
            }
        }

        // 3. Print the elapsed time and results
        long endTimeNs = System.nanoTime();
        long elapsedTimeNs = endTimeNs - startTimeNs;
        double elapsedTimeSec = (double) elapsedTimeNs / 1_000_000_000.0; // Nanoseconds to seconds
        System.out.printf("\n->> Elapsed time: %.3f seconds", elapsedTimeSec);
        System.out.printf("\n->> Total number of %d-letter words: %d\n", MAX_LENGTH, words.size());
    }

    private static Map<Integer, Set<String>> loadAllWordsAsMap() throws IOException {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            return br.lines()
                    .skip(2)
                    .filter(word -> word.length() <= MAX_LENGTH)
                    .collect(Collectors.groupingBy(
                            String::length,
                            Collectors.mapping(
                                    word -> word,
                                    Collectors.toSet()
                            )
                    ));
        }
    }

    private static boolean hasChainDownToOneLetterWord(String word) {
        if (word.length() == 2) {
            if (word.contains("A") || word.contains("I")) {
                return true;
            }
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            String subWord = word.substring(0, i) + word.substring(i + 1);

            if (dictionary.get(subWord.length()).contains(subWord)) {
                if (hasChainDownToOneLetterWord(subWord)) {
                    return true;
                }
            }
        }

        return false;
    }
}