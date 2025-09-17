package testData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestUtils {
    public static String readJson(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String overrideField(String jsonPayload, String fieldName, String newValue) throws Exception {
        ObjectNode node = (ObjectNode) mapper.readTree(jsonPayload);
        node.put(fieldName, newValue);
        return node.toString();
    }


    public static String overrideNestedFieldAtIndex(
            String jsonPayload, String arrayName, int index, String fieldName, String newValue
    ) throws Exception {
        ObjectNode root = (ObjectNode) mapper.readTree(jsonPayload);

        if (root.has(arrayName) && root.get(arrayName).isArray()) {
            ArrayNode array = (ArrayNode) root.get(arrayName);
            if (index >= 0 && index < array.size()) {
                JsonNode element = array.get(index);
                if (element instanceof ObjectNode) {
                    ((ObjectNode) element).put(fieldName, newValue);
                }
            }
        }

        return root.toString();
    }

    public static String overrideNestedFieldAtIndex(
            String jsonPayload, String arrayName, int index, String fieldName, double newValue
    ) throws Exception {
        ObjectNode root = (ObjectNode) mapper.readTree(jsonPayload);

        if (root.has(arrayName) && root.get(arrayName).isArray()) {
            ArrayNode array = (ArrayNode) root.get(arrayName);
            if (index >= 0 && index < array.size()) {
                JsonNode element = array.get(index);
                if (element instanceof ObjectNode) {
                    ((ObjectNode) element).put(fieldName, newValue);
                }
            }
        }

        return root.toString();
    }

    public static String removeField(String jsonPayload, String fieldName) throws Exception {
        ObjectNode node = (ObjectNode) mapper.readTree(jsonPayload);
        node.remove(fieldName);
        return node.toString();
    }

}

