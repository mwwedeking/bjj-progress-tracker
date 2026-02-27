package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestApiConsoleTester {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        System.out.println("========== TESTING TECHNIQUE ==========");
        testTechnique();

        System.out.println("\n========== TESTING ROLL ==========");
        testRoll();

        System.out.println("\n========== TESTING SESSION ==========");
        testSession();

        System.out.println("\nALL TESTS COMPLETE");
    }

    // =============================
    // Technique
    // =============================
    private static void testTechnique() throws Exception {
        // CREATE
        ObjectNode create = mapper.createObjectNode();
        create.put("name", "Console-test0");
        create.put("position", "Console-test0");
        create.put("numFinishes", 0);
        create.put("numTaps", 0);

        HttpResponse<String> createResp = sendPostRaw("/techniques", create.toString());
        if (!is2xx(createResp)) {
            System.err.println("Create Technique failed: " + createResp.statusCode() + " " + createResp.body());
            return;
        }
        System.out.println("Created Technique: " + createResp.body());

        JsonNode createdJson = mapper.readTree(createResp.body());
        JsonNode idNode = createdJson.get("id");
        if (idNode == null) {
            System.err.println("Create response missing id field. Response: " + createResp.body());
            return;
        }

        // UPDATE: preserve id type (number or string)
        ObjectNode update = mapper.createObjectNode();
        if (idNode.isNumber()) update.put("id", idNode.longValue());
        else update.put("id", idNode.asText());

        update.put("name", "Console-test0-updated");
        update.put("position", "Console-test0");
        update.put("numFinishes", 1);
        update.put("numTaps", 0);

        HttpResponse<String> updateResp = sendPostRaw("/techniques", update.toString());
        if (!is2xx(updateResp)) {
            System.err.println("Update Technique failed: " + updateResp.statusCode() + " " + updateResp.body());
        } else {
            System.out.println("Updated Technique: " + updateResp.body());
        }

        // DELETE: call only if we have a usable id
        String idForDelete = idNode.isNumber() ? String.valueOf(idNode.longValue()) : idNode.asText();
        HttpResponse<String> delResp = sendDeleteRaw("/techniques/" + encodePath(idForDelete));
        if (!is2xx(delResp)) {
            System.err.println("Delete Technique failed: " + delResp.statusCode() + " " + delResp.body());
        } else {
            System.out.println("Deleted Technique ID: " + idForDelete);
        }
    }

    // =============================
    // Roll
    // =============================
    private static void testRoll() throws Exception {
        ObjectNode create = mapper.createObjectNode();
        create.put("lengthMinutes", 5);
        create.put("partner", "John");
        create.put("numRounds", 3);
        create.putArray("subs");
        create.putArray("taps");

        HttpResponse<String> createResp = sendPostRaw("/rolls", create.toString());
        if (!is2xx(createResp)) {
            System.err.println("Create Roll failed: " + createResp.statusCode() + " " + createResp.body());
            return;
        }
        System.out.println("Created Roll: " + createResp.body());

        JsonNode createdJson = mapper.readTree(createResp.body());
        JsonNode idNode = createdJson.get("id");
        if (idNode == null) {
            System.err.println("Create response missing id field. Response: " + createResp.body());
            return;
        }

        ObjectNode update = mapper.createObjectNode();
        if (idNode.isNumber()) update.put("id", idNode.longValue());
        else update.put("id", idNode.asText());

        update.put("lengthMinutes", 6);
        update.put("partner", "John Updated");
        update.put("numRounds", 4);
        update.putArray("subs");
        update.putArray("taps");

        HttpResponse<String> updateResp = sendPostRaw("/rolls", update.toString());
        if (!is2xx(updateResp)) {
            System.err.println("Update Roll failed: " + updateResp.statusCode() + " " + updateResp.body());
        } else {
            System.out.println("Updated Roll: " + updateResp.body());
        }

        String idForDelete = idNode.isNumber() ? String.valueOf(idNode.longValue()) : idNode.asText();
        HttpResponse<String> delResp = sendDeleteRaw("/rolls/" + encodePath(idForDelete));
        if (!is2xx(delResp)) {
            System.err.println("Delete Roll failed: " + delResp.statusCode() + " " + delResp.body());
        } else {
            System.out.println("Deleted Roll ID: " + idForDelete);
        }
    }

    // =============================
    // Session
    // =============================
    private static void testSession() throws Exception {
        ObjectNode create = mapper.createObjectNode();
        create.put("date", "2026-02-23");
        create.put("time", "19:00:00");
        // match your API's field name for gi/isGi; use the field name your controller/model expects
        // if your response earlier showed "gi":false, prefer "gi" here
        create.put("gi", false);
        create.put("instructor", "Test-Coach");
        create.put("currentBelt", (String) null);
        create.putArray("rolls");

        HttpResponse<String> createResp = sendPostRaw("/sessions", create.toString());
        if (!is2xx(createResp)) {
            System.err.println("Create Session failed: " + createResp.statusCode() + " " + createResp.body());
            return;
        }
        System.out.println("Created Session: " + createResp.body());

        JsonNode createdJson = mapper.readTree(createResp.body());
        JsonNode idNode = createdJson.get("id");
        if (idNode == null) {
            System.err.println("Create response missing id field. Response: " + createResp.body());
            return;
        }

        ObjectNode update = mapper.createObjectNode();
        if (idNode.isNumber()) update.put("id", idNode.longValue());
        else update.put("id", idNode.asText());

        update.put("date", "2026-02-24");
        update.put("time", "18:30:00");
        update.put("gi", true);
        update.put("instructor", "Coach Updated");
        update.put("currentBelt", (String) null);
        update.putArray("rolls");

        HttpResponse<String> updateResp = sendPostRaw("/sessions", update.toString());
        if (!is2xx(updateResp)) {
            System.err.println("Update Session failed: " + updateResp.statusCode() + " " + updateResp.body());
        } else {
            System.out.println("Updated Session: " + updateResp.body());
        }

        String idForDelete = idNode.isNumber() ? String.valueOf(idNode.longValue()) : idNode.asText();
        HttpResponse<String> delResp = sendDeleteRaw("/sessions/" + encodePath(idForDelete));
        if (!is2xx(delResp)) {
            System.err.println("Delete Session failed: " + delResp.statusCode() + " " + delResp.body());
        } else {
            System.out.println("Deleted Session ID: " + idForDelete);
        }
    }

    // =============================
    // HTTP Helpers
    // =============================
    private static HttpResponse<String> sendPostRaw(String endpoint, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> sendDeleteRaw(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .DELETE()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static boolean is2xx(HttpResponse<?> resp) {
        int sc = resp.statusCode();
        return sc >= 200 && sc < 300;
    }

    private static String encodePath(String s) {
        return s.replace(" ", "%20");
    }
}