import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Huffman {

    public static void main(String[] args) {
        // Create scanner to ask for user input

        Scanner scanner = new Scanner(System.in);
        System.out.println("Bitte gib unten deinen Text ein. Zum komprimieren drücke Strg+D");
        scanner.useDelimiter("\\Z");
        System.out.print("Text: ");
        String message = scanner.next();

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
        // create empty priority queue
        PriorityQueue<HuffmanNode> nodes = new PriorityQueue<>(new HuffmanComparator());

        // save hashMap in ArrayList to make it simpler to iterate through all values
        ArrayList<Character> occurredCharacters = new ArrayList<>(data.keySet());

        // iterate through all occurred chars and create a node object for them and add them to the priority queue
        while (occurredCharacters.size() > 0) {
            HuffmanNode node = new HuffmanNode();
            node.setCharacter(occurredCharacters.remove(0));
            node.setAbsFrequency(data.get(node.getCharacter()));
            nodes.add(node);
        }
        return nodes;
    }

    private static HuffmanNode createTreeNodes(PriorityQueue<HuffmanNode> nodes) {
        // create empty root node
        HuffmanNode root = null;

        // create new node and set node1 as left Node and node2 as Right Node
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

        // at the end the only one node remains. This node is the root node of the Huffman-Tree
        return root;
    }

    private static HashMap<Character, String> createEncodingHashMap(HuffmanNode root, HashMap<Character, String> encodingMap, StringBuilder encodedChar) {

        // check if there are more then on char
        if (root.getLeft() == null && root.getRight() == null) {
            // if current node is a leaf the encoded char sequence gets added to encodingHashMap
            encodingMap.put(root.getCharacter(), encodedChar.toString());
            return encodingMap;
        }

        // check if a left node is available
        if (root.getLeft() != null) {
            // add 0 to encoded sequence of the current character
            encodedChar.append(0);
            createEncodingHashMap(root.getLeft(), encodingMap, encodedChar);
            encodedChar.deleteCharAt(encodedChar.length() - 1);
        }

        // check if a right node is available
        if (root.getRight() != null) {
            // add 1 to encoded sequence of the current character
            encodedChar.append(1);
            createEncodingHashMap(root.getRight(), encodingMap, encodedChar);
            encodedChar.deleteCharAt(encodedChar.length() - 1);
        }

        return encodingMap;
    }

    private static String code(String message, HashMap<Character, String> encodingData) {
        StringBuilder builder = new StringBuilder();

        // iterate trough original text
        for (char c : message.toCharArray()) {

            // get encoded sequence of current char c and add it to encoded string
            builder.append(encodingData.get(c));
            builder.append(" ");
        }

        return builder.toString();
    }

    /*
     * Decoding
     */
    private static void decode(String input, HashMap<Character, String> encodingMap) {
        // invert hashMap to get the char behind the encoded sequence
        HashMap<String, Character> decodingMap = getInvertedMap(encodingMap);

        // split the encoded text into it's sequences
        String[] encodedParts = input.split(" ");
        StringBuilder builder = new StringBuilder();

        // add the character of sequence to the decoded string
        for (String character : encodedParts) {
            builder.append(decodingMap.get(character));
        }

        System.out.println("Decoded Text: " + builder.toString());
    }

    private static HashMap<String, Character> getInvertedMap(HashMap<Character, String> encodingMap) {

        // create a new empty empty HashMap
        HashMap<String, Character> decodingMap = new HashMap<>();

        // iterate through the keySet of the encoding map and store the sequence of the of the character as key and
        // the characters as values in the invertedHashMap
        for (char key : encodingMap.keySet()) {
            decodingMap.put(encodingMap.get(key), key);
        }
        return decodingMap;
    }
}
