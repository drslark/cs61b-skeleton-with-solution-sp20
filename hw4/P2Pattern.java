/** P2Pattern class.
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /** Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static String P1 = "([0]?[0-9]|[1][12])"
            + "/([0]?[0-9]|[12][0-9]|[3][0-1])"
            + "/[1-9][0-9]{3}";

    /** Pattern to match 61b notation for literal IntLists. */
    public static String P2 = "\\((\\d+,\\s+)*\\d+\\)";

    /** Pattern to match a valid domain name.
     * Eg: www.support.facebook-login.com */
    public static String P3 = "[a-z]+\\.(([a-z]|\\d|\\-)+\\.)*[a-z]+";

    /** Pattern to match a valid java variable name. Eg: _child13$ */
    public static String P4 = "([a-z]|\\_|\\$)(\\d|\\_|\\$|[a-z])*";

    /** Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static String P5 = "(([01]?\\d?\\d|[2][0-5][0-5])\\.){3}"
            + "([01]?\\d?\\d|[2][0-5][0-5])";
}
