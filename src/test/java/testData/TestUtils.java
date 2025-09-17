package testData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestUtils {
    public static String readJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String overrideCartId(String jsonPayload, String newCartId) throws Exception {
        ObjectNode node = (ObjectNode) mapper.readTree(jsonPayload);
        node.put("cartId", newCartId);
        return node.toString();
    }

    public static String overrideUserId(String jsonPayload, String newUserId) throws Exception {
        ObjectNode node = (ObjectNode) mapper.readTree(jsonPayload);
        node.put("userId", newUserId);
        return node.toString();
    }

    public static String removeField(String jsonPayload, String fieldName) throws Exception {
        ObjectNode node = (ObjectNode) mapper.readTree(jsonPayload);
        node.remove(fieldName);
        return node.toString();
    }
}

