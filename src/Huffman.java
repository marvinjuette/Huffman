import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Huffman {

    public static void main(String[] args) {
        // Create scanner to ask for user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Text: ");
        String message = scanner.nextLine();

        encode(message);
    }

    /*
     * Encoding
     */
    private static void encode(String message) {
        // create encodingMap to avoid traversing through the tree multiple times
        HashMap<Character, String> encodingMap = createEncodingHashMap(createTreeNodes(
                initQueue(countCharacterOccurrences(message))), new HashMap<>(), new StringBuilder());

        // safe encodingMap in file so that message could be decoded everywhere with this file
        String encodedMessage = code(message, encodingMap);
        System.out.println("Encoded Text: " + encodedMessage);

        // todo: safe HashMap to run decode independently from encode method
        decode(encodedMessage, encodingMap);
    }

    private static HashMap<Character, Integer> countCharacterOccurrences(String message) {
        // create empty HashMap to store all data in
        HashMap<Character, Integer> data = new HashMap<>();

        // iterate through text and count character occurrences and save them in HashMap
        for (char c : message.toCharArray()) {
            if (data.containsKey(c)) {
                data.put(c, data.get(c) + 1);
            } else {
                data.put(c, 1);
            }
        }
        return data;
    }

    private static PriorityQueue<HuffmanNode> initQueue(HashMap<Character, Integer> data) {
        PriorityQueue<HuffmanNode> nodes = new PriorityQueue<>(new HuffmanComparator());

        ArrayList<Character> occurredCharacters = new ArrayList<>(data.keySet());

        while (occurredCharacters.size() > 0) {
            HuffmanNode node = new HuffmanNode();
            node.setCharacter(occurredCharacters.remove(0));
            node.setAbsFrequency(data.get(node.getCharacter()));
            nodes.add(node);
        }
        return nodes;
    }

    private static HuffmanNode createTreeNodes(PriorityQueue<HuffmanNode> nodes) {
        HuffmanNode root = null;
        while (nodes.size() > 1) {
            HuffmanNode newFrequencyNode = new HuffmanNode();
            HuffmanNode node1 = nodes.poll();
            HuffmanNode node2 = nodes.poll();
            newFrequencyNode.setLeft(node1);
            newFrequencyNode.setRight(node2);
            assert node2 != null;
            newFrequencyNode.setAbsFrequency(node1.getAbsFrequency() + node2.getAbsFrequency());
            root = newFrequencyNode;
            nodes.add(newFrequencyNode);
        }
        return root;
    }

    private static HashMap<Character, String> createEncodingHashMap(HuffmanNode root, HashMap<Character, String> encodingMap, StringBuilder encodedChar) {

        if (root.getLeft() == null && root.getRight() == null) {
            encodingMap.put(root.getCharacter(), encodedChar.toString());
            return encodingMap;
        }
        if (root.getLeft() != null) {
            encodedChar.append(0);
            createEncodingHashMap(root.getLeft(), encodingMap, encodedChar);
            encodedChar.deleteCharAt(encodedChar.length() - 1);
        }
        if (root.getRight() != null) {
            encodedChar.append(1);
            createEncodingHashMap(root.getRight(), encodingMap, encodedChar);
            encodedChar.deleteCharAt(encodedChar.length() - 1);
        }

        return encodingMap;
    }

    private static String code(String message, HashMap<Character, String> encodingData) {
        StringBuilder builder = new StringBuilder();

        for (char c : message.toCharArray()) {
            builder.append(encodingData.get(c));
            builder.append(" ");
        }

        return builder.toString();
    }

    /*
     * Decoding
     */
    private static void decode(String input, HashMap<Character, String> encodingMap) {
        HashMap<String, Character> decodingMap = getReversedMap(encodingMap);
        String[] encodedParts = input.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String character : encodedParts) {
            builder.append(decodingMap.get(character));
        }

        System.out.println("Decoded Text: " + builder.toString());
    }

    private static HashMap<String, Character> getReversedMap(HashMap<Character, String> encodingMap) {

        HashMap<String, Character> decodingMap = new HashMap<>();
        for (char key : encodingMap.keySet()) {
            decodingMap.put(encodingMap.get(key), key);
        }
        return decodingMap;
    }
}
