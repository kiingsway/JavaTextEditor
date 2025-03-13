import java.util.regex.*;
import java.util.*;

public class Test {
  public static void main(String[] args) {
    String text = "abcdefabcdefabcdef";
    String search = "abcdef";

    Pattern pattern = Pattern.compile(search);
    Matcher matcher = pattern.matcher(text);

    List<int[]> searchOccurrences = new ArrayList<>();

    while (matcher.find()) searchOccurrences.add(new int[]{matcher.start(), matcher.end()});

    for (int[] occurrence : searchOccurrences) System.out.println("Indexes: " + occurrence[0] + "-" + occurrence[1]);
  }
}
