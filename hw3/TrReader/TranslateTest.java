import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import ucb.junit.textui;

/** Test of the Translate class.
 *  @author Amit Bhat
 */

public class TranslateTest {

    @Test
    public void testTranslate() throws IOException {
        String r = "big data structure";
        String result = Translate.translate(r, "abcd", "ABCD");
        assertEquals(TRANSLATION, result);
    }

    public static void main(String[] args) {
        System.exit(textui.runClasses(TrReaderTest.class));
    }

    static final String TRANSLATION = "BAD DAtA struCture";

}
