package com.airpapier.database.seed;

import java.util.List;
import java.util.Random;

public class RandomDataGenerator {
    public static final Random RANDOM = new Random();


    public static <T> T getRandomElement(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

    public static String generateRandomName() {
        String[] names = {"Vincent", "Brenden", "Billi", "Keenan", "Marc", "Tristan"};
        return names[RANDOM.nextInt(names.length)] + " " + names[RANDOM.nextInt(names.length)];
    }

    public static String generateRandomEmail() {
        String[] domains = {"example.com", "mail.com", "test.com"};
        return "user" + RANDOM.nextInt(1000) + "@" + domains[RANDOM.nextInt(domains.length)];
    }

    public static String generateRandomPhone() {
        return String.format("%03d-%03d-%04d", RANDOM.nextInt(1000), RANDOM.nextInt(1000), RANDOM.nextInt(10000));
    }

    public static String generateRandomAddress() {
        String[] streets = {"Elm Street", "Oak Avenue", "Maple Drive", "Pine Lane"};
        return RANDOM.nextInt(999) + " " + streets[RANDOM.nextInt(streets.length)];
    }

    public static String generateRandomCategoryName() {
        String[] categories = {"Electronics", "Furniture", "Books", "Clothing"};
        return categories[RANDOM.nextInt(categories.length)];
    }

    public static String generateRandomDescription() {
        String[] descriptions = {"High quality", "Affordable", "Premium", "Durable"};
        return descriptions[RANDOM.nextInt(descriptions.length)] + " products";
    }

    public static String generateRandomReference() {
        return "REF" + RANDOM.nextInt(1000);
    }

    public static double generateRandomPrice() {
        return Math.round(RANDOM.nextDouble() * 500 + 10 * 100.0) / 100.0;
    }

    public static int generateRandomQuantity() {
        return RANDOM.nextInt(100) + 1;
    }
}
