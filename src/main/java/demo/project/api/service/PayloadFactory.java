package demo.project.api.service;

import java.util.HashMap;
import com.github.javafaker.Faker;
import org.json.JSONObject;

public class PayloadFactory {

    public static JSONObject payloadAuth(String username, String password) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        return new JSONObject(user);
    }

    public static JSONObject payloadProduct() {
        Faker faker = new Faker();
        HashMap<String, Object> product = new HashMap<>();
        product.put("title", faker.commerce().productName());
        product.put("description", faker.lorem().sentence());
        product.put("price", faker.number().numberBetween(10, 100));
        product.put("discountPercentage", faker.number().randomDouble(1, 5, 15));
        product.put("rating", faker.number().randomDouble(2, 3, 5));
        product.put("stock", faker.number().numberBetween(10, 100));
        product.put("brand", faker.company().name());
        product.put("category", "fragrances");
        product.put("thumbnail", faker.internet().url() + ".png");
        return new JSONObject(product);
    }
}
