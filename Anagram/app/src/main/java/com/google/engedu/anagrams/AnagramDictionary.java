/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashSet<String> wordSet = new HashSet<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordList.add(word);
            wordSet.add(word);

            String sortedWord = sortLetters(word);

            if (!(lettersToWord.containsKey(sortedWord))) {
                ArrayList<String> listOfAnagrams = new ArrayList<String>();
                listOfAnagrams.add(word);
                lettersToWord.put(sortedWord, listOfAnagrams);
            } else {
                lettersToWord.get(sortedWord).add(word);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(!(wordSet.contains(word))) {
            return false;
        }
        if(word.contains(base)) {
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        String sortedWord = sortLetters(targetWord);
        return lettersToWord.get(sortedWord);
    }

    public String sortLetters(String word) {
        char[] wordToCharArray = word.toCharArray();
        Arrays.sort(wordToCharArray);
        String sortedWord = new String(wordToCharArray);
        return sortedWord;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedWord = sortLetters(word);

        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        char[] alphaCharArray = alphabet.toCharArray();

        for (int i = 0; i < alphabet.length(); ++i) {
            String sortedWordPlusAlpha = sortLetters(sortedWord + alphaCharArray[i]);
            if(lettersToWord.containsKey(sortedWordPlusAlpha)) {
                for (int j = 0; j < lettersToWord.get(sortedWordPlusAlpha).size(); ++j) {
                    result.add(lettersToWord.get(sortedWordPlusAlpha).get(j));
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int randomInt = random.nextInt(wordList.size()-1);
        String starterWord = wordList.get(randomInt);
        while(getAnagramsWithOneMoreLetter(starterWord).size() < MIN_NUM_ANAGRAMS) {
            if(randomInt + 1 == wordList.size()-1) {
                randomInt = 0;
            }
            else {
                ++randomInt;
            }
            starterWord = wordList.get(randomInt);

        }
        return starterWord;
    }
}
