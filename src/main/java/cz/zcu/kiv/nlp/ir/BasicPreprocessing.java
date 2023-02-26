package cz.zcu.kiv.nlp.ir;

import java.util.*;

/**
 * Created by Tigi on 29.2.2016.
 */
public class BasicPreprocessing implements Preprocessing {

    Map<String, Integer> wordFrequencies = new HashMap<String, Integer>();
    Stemmer stemmer;
    Tokenizer tokenizer;
    Set<String> stopwords;
    private final AccentRemovalPolicy accentRemovalPolicy;
    boolean toLowercase;

    public BasicPreprocessing(Stemmer stemmer, Tokenizer tokenizer, Set<String> stopwords,
            final AccentRemovalPolicy accentRemovalPolicy, boolean toLowercase) {
        this.stemmer = stemmer;
        this.tokenizer = tokenizer;
        this.stopwords = stopwords;
        this.accentRemovalPolicy = accentRemovalPolicy;
        this.toLowercase = toLowercase;
    }

    @Override
    public void index(String document) {
        if (toLowercase) {
            document = document.toLowerCase();
        }
        if (accentRemovalPolicy == AccentRemovalPolicy.BEFORE_STEMMING) {
            document = removeAccents(document);
        }
        for (String token : tokenizer.tokenize(document)) {
            if (stemmer != null) {
                token = stemmer.stem(token);
            }
            if (accentRemovalPolicy == AccentRemovalPolicy.AFTER_STEMMING) {
                token = removeAccents(token);
            }

            if (stopwords != null && stopwords.contains(token)) {
                continue;
            }

            if (!wordFrequencies.containsKey(token)) {
                wordFrequencies.put(token, 0);
            }

            wordFrequencies.put(token, wordFrequencies.get(token) + 1);
        }
    }

    @Override
    public String getProcessedForm(String text) {
        if (stopwords != null && stopwords.contains(text)) {
            return text;
        }

        if (toLowercase) {
            text = text.toLowerCase();
        }
        if (accentRemovalPolicy == AccentRemovalPolicy.BEFORE_STEMMING) {
            text = removeAccents(text);
        }
        if (stemmer != null) {
            text = stemmer.stem(text);
        }
        if (accentRemovalPolicy == AccentRemovalPolicy.AFTER_STEMMING) {
            text = removeAccents(text);
        }
        return text;
    }

    private String removeAccents(String text) {
        return AdvancedTokenizer.removeAccents(text);
    }

    public Map<String, Integer> getWordFrequencies() {
        return wordFrequencies;
    }
}
