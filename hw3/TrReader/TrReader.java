import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Amit Bhat
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */

    /** The reader used by the TrReader. */
    private Reader _str;

    /** The string containing characters that are going to be translated. */
    private String _from;

    /** The string containing the new characters that characters in _from are
     *  translated to. */
    private String _to;


    /** A new TrReader with reader STR, from string FROM, and to string TO. */
    public TrReader(Reader str, String from, String to) {
        if (from.length() != to.length()) {
            String message = "from and to must be same length";
            throw new IllegalArgumentException(message);
        }
        _str = str;
        _from = from;
        _to = to;
    }

    @Override
    public void close() throws IOException {
        _str = null;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int numRead = _str.read(cbuf, off, len);
        for (int i = off; i < off + len || i < cbuf.length; i += 1) {
            int inFrom = -1;
            for (int j = 0; j < _from.length(); j += 1) {
                if (cbuf[i] == _from.charAt(j)) {
                    inFrom = j;
                    break;
                }
            }
            if (inFrom >= 0) {
                cbuf[i] = _to.charAt(inFrom);
            }
        }
        if (numRead == -1) {
            close();
        }
        return numRead;
    }

    /* NOTE: Until you fill in the necessary methods, the compiler will
     *       reject this file, saying that you must declare TrReader
     *       abstract. Don't do that; define the right methods instead!
     */

}
