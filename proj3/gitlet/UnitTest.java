package gitlet;

import ucb.junit.textui;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author Amit Bhat
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(UnitTest.class));
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void initTest() throws IOException {
        GitCommands.init();
        assertTrue(GitCommands.GITLET.exists());
        assertTrue(GitCommands.COMMITS.exists());
        assertTrue(GitCommands.STAGING_AREA.exists());
    }

}


