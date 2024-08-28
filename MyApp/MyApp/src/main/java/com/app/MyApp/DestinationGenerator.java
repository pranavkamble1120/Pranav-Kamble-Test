package com.app.MyApp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Iterator;
public class DestinationGenerator {

	

	    public void main(String[] args) {
	        if (args.length != 2) {
	            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN_Number> <Path_to_JSON_File>");
	            return;
	        }

	        String prnNumber = args[0].toLowerCase();
	        String jsonFilePath = args[1];

	        try {
	            // Parse the JSON file
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

	            // Find the destination value
	            String destinationValue = findDestination(rootNode);
	            if (destinationValue == null) {
	                System.out.println("No 'destination' key found in the JSON file.");
	                return;
	            }

	            // Generate random 8-character alphanumeric string
	            String randomString = generateRandomString(8);

	            // Concatenate PRN, destination value, and random string
	            String concatenatedValue = prnNumber + destinationValue + randomString;

	            // Generate MD5 hash
	            String md5Hash = generateMD5Hash(concatenatedValue);

	            // Print the result
	            System.out.println(md5Hash + ";" + randomString);

	        } catch (IOException e) {
	            e.printStackTrace();
	            System.out.println("Error reading the JSON file.");
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            System.out.println("Error generating MD5 hash.");
	        }
	    }

	    private String findDestination(JsonNode rootNode) {
	        Iterator<String> fieldNames = rootNode.fieldNames();
	        while (fieldNames.hasNext()) {
	            String fieldName = fieldNames.next();
	            JsonNode node = rootNode.get(fieldName);
	            if (fieldName.equals("destination")) {
	                return node.asText();
	            }
	            if (node.isObject()) {
	                String result = findDestination(node);
	                if (result != null) {
	                    return result;
	                }
	            }
	            if (node.isArray()) {
	                for (JsonNode arrayNode : node) {
	                    if (arrayNode.isObject()) {
	                        String result = findDestination(arrayNode);
	                        if (result != null) {
	                            return result;
	                        }
	                    }
	                }
	            }
	        }
	        return null;
	    }

	    private String generateRandomString(int length) {
	        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        SecureRandom random = new SecureRandom();
	        StringBuilder sb = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(characters.length());
	            sb.append(characters.charAt(index));
	        }
	        return sb.toString();
	    }

	    private String generateMD5Hash(String input) throws NoSuchAlgorithmException {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] hashBytes = md.digest(input.getBytes());
	        StringBuilder sb = new StringBuilder();
	        for (byte b : hashBytes) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb.toString();
	    }
	}


