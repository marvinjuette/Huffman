import java.util.Comparator;

class HuffmanComparator implements Comparator<HuffmanNode> {
  public int compare(HuffmanNode node1, HuffmanNode node2) {
    int difference = node1.getAbsFrequency() - node2.getAbsFrequency();
    if(difference == 0) return Character.compare(node1.getCharacter(), node2.getCharacter());
    return difference;
  }
}
