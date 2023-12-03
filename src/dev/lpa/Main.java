package dev.lpa;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the first three digits of your bank account: ");
        String accountPrefix = scanner.nextLine();

        if (!accountPrefix.matches("\\d{3}")) {
            System.out.println("Invalid input. Please enter three numeric digits.");
            return;
        }

        String url = "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";

        try {
            String data = fetchData(url);

            String regex = String.format("%s\\s+(\\d+)\\s+(.+)\\n", accountPrefix);
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(data);

            if (matcher.find()) {
                String bankCode = matcher.group(1);
                String bankName = matcher.group(2);
                System.out.printf("You have an account in Bank %s: %s%n", bankCode, bankName);
            } else {
                System.out.println("Bank information not found.");
            }

        } catch (IOException e) {
            System.out.println("Error fetching data from the URL: " + e.getMessage());
        }
    }

    private static String fetchData(String urlString) throws IOException {
        URL url = new URL(urlString);
        StringBuilder content = new StringBuilder();

        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        }

        return content.toString();
    }
}

