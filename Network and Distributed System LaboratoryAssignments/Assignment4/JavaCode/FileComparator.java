//FileComparator Class Code
import java.util.*;

public class FileComparator implements Comparator<String> {

    // Overriding compare method
    public int compare(String s1, String s2) {
        if (getPriority(s1) > getPriority(s2))
            return 1;
        else if (getPriority(s1) < getPriority(s2))
            return -1;
        return 0;
    }

    // Function to get priority of a file
    public static int getPriority(String fName) {
        // Document files
        if (fName.contains(".txt"))
            return 10;
        else if (fName.contains(".doc"))
            return 11;
        else if (fName.contains(".docx"))
            return 12;
        else if (fName.contains(".pdf"))
            return 13;
        else if (fName.contains(".ppt"))
            return 14;
        else if (fName.contains(".pptx"))
            return 15;
        // Audio files
        else if (fName.contains(".mp3"))
            return 30;
        else if (fName.contains(".aac"))
            return 31;
        else if (fName.contains(".flac"))
            return 32;
        else if (fName.contains(".wav"))
            return 33;
        // Video files
        else if (fName.contains(".mp4"))
            return 40;
        else if (fName.contains(".mkv"))
            return 41;
        else if (fName.contains(".avi"))
            return 42;
        // Other files
        return 99;
    }
}