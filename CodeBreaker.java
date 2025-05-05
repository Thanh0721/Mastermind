import java.util.Scanner;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class CodeBreaker {
    private static final int CODE_LENGTH = 4;
    private static final int MAX_ATTEMPTS = 10;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        
        // Generate a random 4-digit code with unique digits
        int[] secretCode = generateSecretCode(random);
        
        System.out.println("Welcome to CodeBreaker!");
        System.out.println("Try to guess the " + CODE_LENGTH + "-digit code.");
        System.out.println("After each guess, you'll get feedback:");
        System.out.println("  'X' means correct digit in correct position");
        System.out.println("  'O' means correct digit but wrong position");
        System.out.println("  '-' means digit not in the code at all");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts to guess the code.\n");
        
        int attempts = 0;
        boolean solved = false;
        
        while (attempts < MAX_ATTEMPTS && !solved) {
            System.out.print("Enter your guess (attempt " + (attempts + 1) + "/" + MAX_ATTEMPTS + "): ");
            String input = scanner.nextLine().trim();
            
            // Validate input
            if (!input.matches("\\d{" + CODE_LENGTH + "}")) {
                System.out.println("Please enter exactly " + CODE_LENGTH + " digits (0-9).\n");
                continue;
            }
            
            int[] guess = new int[CODE_LENGTH];
            for (int i = 0; i < CODE_LENGTH; i++) {
                guess[i] = Character.getNumericValue(input.charAt(i));
            }
            
            // Check for duplicates in guess
            if (hasDuplicates(guess)) {
                System.out.println("All digits in your guess must be unique. Try again.\n");
                continue;
            }
            
            // Process the guess
            String feedback = getFeedback(secretCode, guess);
            System.out.println("Feedback: " + feedback + "\n");
            
            if (feedback.equals("XXXX")) {
                solved = true;
            }
            
            attempts++;
        }
        
        if (solved) {
            System.out.println("Congratulations! You broke the code in " + attempts + " attempts!");
        } else {
            System.out.print("Game over! The secret code was: ");
            for (int digit : secretCode) {
                System.out.print(digit);
            }
            System.out.println();
        }
        
        scanner.close();
    }
    
    private static int[] generateSecretCode(Random random) {
        Set<Integer> usedDigits = new HashSet<>();
        int[] code = new int[CODE_LENGTH];
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            int digit;
            do {
                digit = random.nextInt(10); // 0-9
            } while (usedDigits.contains(digit));
            
            code[i] = digit;
            usedDigits.add(digit);
        }
        
        return code;
    }
    
    private static boolean hasDuplicates(int[] guess) {
        Set<Integer> digits = new HashSet<>();
        for (int digit : guess) {
            if (digits.contains(digit)) {
                return true;
            }
            digits.add(digit);
        }
        return false;
    }
    
    private static String getFeedback(int[] secretCode, int[] guess) {
        char[] feedback = new char[CODE_LENGTH];
        boolean[] secretMatched = new boolean[CODE_LENGTH];
        boolean[] guessMatched = new boolean[CODE_LENGTH];
        
        // First check for correct digits in correct positions
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess[i] == secretCode[i]) {
                feedback[i] = 'X';
                secretMatched[i] = true;
                guessMatched[i] = true;
            }
        }
        
        // Then check for correct digits in wrong positions
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (!guessMatched[i]) {
                for (int j = 0; j < CODE_LENGTH; j++) {
                    if (!secretMatched[j] && guess[i] == secretCode[j]) {
                        feedback[i] = 'O';
                        secretMatched[j] = true;
                        break;
                    }
                }
            }
        }
        
        // Fill in remaining positions with '-'
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (feedback[i] != 'X' && feedback[i] != 'O') {
                feedback[i] = '-';
            }
        }
        
        return new String(feedback);
    }
}