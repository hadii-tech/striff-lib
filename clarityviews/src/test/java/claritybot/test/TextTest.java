package claritybot.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.clarity.binary.DefaultText;
import com.clarity.binary.HtmlTagsStrippedText;
import com.clarity.binary.JavaDocSymbolStrippedText;
import com.clarity.binary.SimplifiedJavaDocText;

public class TextTest {

    @Test
    public void simplifyLink() throws Exception {
        String text = "/**\n" + "* Validates a chess move.\n" + "*\n"
                + "* Use {@link #doMove(int theFromFile, int theFromRank, int theToFile, int theToRank)} to move a piece.\n"
                + "*\n" + "* @param theFromFile file from which a piece is being moved\n"
                + "* @param theFromRank rank from which a piece is being moved\n"
                + "* @param theToFile   file to which a piece is being moved\n"
                + "* @param theToRank   rank to which a piece is being moved\n"
                + "* @return            true if the move is valid, otherwise false\n" + "* @since             1.0\n"
                + "*/";

        assertTrue(new HtmlTagsStrippedText(
                new JavaDocSymbolStrippedText(new SimplifiedJavaDocText(new DefaultText(text)))).value().equals(
                        "Validates a chess move. Use #doMove(int theFromFile, int theFromRank, int theToFile, int theToRank) to move a piece. @param theFromFile file from which a piece is being moved @param theFromRank rank from which a piece is being moved @param theToFile file to which a piece is being moved @param theToRank rank to which a piece is being moved"));
    }

    @Test
    public void simplifyPlainLink() throws Exception {
        String text = "/**\n" + "* Validates a chess move.\n" + "*\n" + "* Use {@linkplain org.junit.test.Tester}.\n"
                + "* @see org.junit.lolCakes" + "*\n" + "*/";

        assertTrue(new HtmlTagsStrippedText(
                new JavaDocSymbolStrippedText(new SimplifiedJavaDocText(new DefaultText(text)))).value()
                        .equals("Validates a chess move. Use org.junit.test.Tester. See org.junit.lolCakes"));
    }
}
