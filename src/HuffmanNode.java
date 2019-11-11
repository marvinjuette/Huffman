class HuffmanNode {
    private int absFrequency;
    private char character;

    private HuffmanNode left;
    private HuffmanNode right;

    int getAbsFrequency() {
        return absFrequency;
    }
    char getCharacter() {
        return character;
    }
    HuffmanNode getLeft() {
        return left;
    }
    HuffmanNode getRight() {
        return right;
    }
    void setCharacter(char c) {
        character = c;
    }
    void setAbsFrequency(int absFrequency) {
        this.absFrequency = absFrequency;
    }
    void setLeft(HuffmanNode node) {
        left = node;
    }
    void setRight(HuffmanNode node) {
        right = node;
    }
}