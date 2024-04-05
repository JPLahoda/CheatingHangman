import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;



public class CheatingHangman {
    private static int setSize = 0;
    private static int totalGuesses = 0;
    private static int correctGuesses = 0;
    private static int letters = 1;
    private static char finalGuess;

    public static Set setSize(Set<String> wordSet, int largeWord) {
        //changed set
        Set<String> set = new HashSet<>();
        Scanner input = new Scanner(System.in);
        System.out.print("How many letters is the word?");
        letters = input.nextInt();
        while (letters <= 0 || letters >= largeWord) {
            System.out.print("Please input an acceptable word size.");
            letters = input.nextInt();
        }
        if (letters > 0 && letters <= largeWord) {
            setSize = 0;
            //checking each word in the input set
            for (String word : wordSet) {
                if (word.length() == letters) {
                    set.add(word);
                    setSize++;
                }
            }
        }
        return set;
    }

    public static Map<String, List<String>> allFamilies(List<String> wordList, Set<Character> lettersSet) {
        Map<String, List<String>> familiesMap = new HashMap<>();
        for (String word : wordList) {
            String wordFam = "";
            for (int i = 0; i < word.length(); i++) {
                if(lettersSet.contains(word.charAt(i))){
                    wordFam+=word.charAt(i);
                } else {
                    wordFam += "?";
                }
            }
            List<String> list;
            if (familiesMap.containsKey(wordFam)) {
                list = familiesMap.get(wordFam);
            } else {
                list = new ArrayList<>();
            }
            list.add(word);
            familiesMap.put(wordFam, list);

        }
        return familiesMap;
    }

    public static String chooseFamily(Map<String, List<String>> families) {
        int size = 0;
        String family = "";
        for (Map.Entry<String, List<String>> entry : families.entrySet()) {
            if (entry.getValue().size() > size) {
                size = entry.getValue().size();
                family = entry.getKey();
            }
        }
        return family;
    }

    public static String winningWord(List<String> list, char charAtWord){
        for(String word : list){
            int counter = 0;
            for (int i = 0; i < word.length(); i++) {
                if(word.charAt(i) != charAtWord){
                    counter++;
                }
            }
            if(counter == word.length()){
                return word;
            }
        }
        return list.get(0);
    }
    public static boolean win(List<String> list, String puzzle){
        if(list.size() == 1 && list.get(0).equals(puzzle)){
            return true;
        }else{
            return false;
        }
    }

    public static void game(Set<String> wordSet) {
        List<String> wordList = new ArrayList<>();
        Map<String, List<String>> wordMap;
        for (String word : wordSet) {
            wordList.add(word);
        }
        Set<Character> lettersSet = new HashSet<>();
        String puzzle = "";

        for (int i = 0; i < letters; i++) {
            puzzle += "?";
        }
        Scanner input = new Scanner(System.in);
        while ((totalGuesses > 0) && !win(wordList, puzzle)) {
            System.out.println("Total guesses: " + totalGuesses);
            System.out.println(puzzle);
            System.out.println("Guessed letters: " + lettersSet);
            System.out.print("Please guess a letter.");
            String str = input.next();
            char guess = str.charAt(0);
            while (lettersSet.contains(guess)) {
                System.out.print("This letter was already guessed. Please guess a different one.");
                String str2 = input.next();
                guess = str2.charAt(0);
            }
            lettersSet.add(guess);
            wordMap = allFamilies(wordList,lettersSet);
            String family = chooseFamily(wordMap);
            wordList = wordMap.get(family);
            System.out.println(wordList);
            if (puzzle.equals(family)) {
                totalGuesses--;
                if(totalGuesses == 0){
                    finalGuess = guess;
                }
            } else {
                correctGuesses++;
            }
            puzzle = family;
        }
        if(win(wordList,puzzle)){
            System.out.println("You won!");
        } else {
            System.out.println("You lose! The word was: " + winningWord(wordList,finalGuess));
        }
    }

    public static void main(String[] args) throws FileNotFoundException{
        Set<String> set = new HashSet<>();
        Scanner sc = new Scanner(new File("C:\\Users\\lahod\\IdeaProjects\\Module 8\\src\\dictionary.txt"));
        int maximumSize = 0;
        while (sc.hasNext()) {
            String nextScan = sc.next();
            int len = nextScan.length();
            if (len > maximumSize) {
                maximumSize = len;
            }
            set.add(nextScan);
            setSize++;
        }
        set = setSize(set, maximumSize);
        Scanner input = new Scanner(System.in);
        //do{
        System.out.print("How many guesses do you want?");
        totalGuesses = input.nextInt();
        game(set);
    }
}
