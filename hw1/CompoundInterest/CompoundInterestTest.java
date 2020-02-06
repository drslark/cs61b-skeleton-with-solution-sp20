import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.
            assertEquals(0, 0); */
        assertEquals(5, CompoundInterest.numYears(2025));
        assertEquals(12, CompoundInterest.numYears(2032));
    }

    @Test
    public void testFutureValue() {

        double tolerance = 0.01;
        double test1 = 10 * Math.pow(1.61, 4);
        double test2 = 1465 * Math.pow(.84, 7);
        assertEquals(test1,
                CompoundInterest.futureValue(10, 61, 2024), tolerance);
        assertEquals(test2,
                CompoundInterest.futureValue(1465, -16, 2027), tolerance);
    }

    @Test
    public void testFutureValueReal() {

        double tolerance = 0.01;
        double test1 = 10 * Math.pow(1.61, 4) * Math.pow(0.91, 4);
        double test2 = 1465 * Math.pow(.84, 7) * Math.pow(0.95, 7);
        double test3 = 500 * Math.pow(2.28, 32) * Math.pow(1.04, 32);
        assertEquals(test1,
                CompoundInterest.futureValueReal(10, 61, 2024, 9), tolerance);
        assertEquals(test2,
                CompoundInterest.futureValueReal(1465, -16, 2027, 5), tolerance);
        assertEquals(test3,
                CompoundInterest.futureValueReal(500, 128, 2052, -4), tolerance);
    }


    @Test
    public void testTotalSavings() {

        double tolerance = 0.01;
        double test1 = 450 * Math.pow(1.17, 3) + 450 * Math.pow(1.17, 2)
                + 450 * Math.pow(1.17, 1) + 450;
        double test2 = 3600 * Math.pow(2.25, 6) + 3600 * Math.pow(2.25, 5)
                + 3600 * Math.pow(2.25, 4) + 3600 * Math.pow(2.25, 3)
                + 3600 * Math.pow(2.25, 2) + 3600 * Math.pow(2.25, 1) + 3600;
        assertEquals(test1, CompoundInterest.totalSavings(450, 2023, 17), tolerance);
        assertEquals(test2, CompoundInterest.totalSavings(3600, 2026, 125), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        double test1 = (450 * Math.pow(1.17, 3) + 450 * Math.pow(1.17, 2)
                + 450 * Math.pow(1.17, 1) + 450) * Math.pow(0.94, 3);
        double test2 = (3600 * Math.pow(2.25, 6)
                + 3600 * Math.pow(2.25, 5) + 3600 * Math.pow(2.25, 4)
                + 3600 * Math.pow(2.25, 3) + 3600 * Math.pow(2.25, 2)
                + 3600 * 2.25 + 3600) * Math.pow(0.88, 6);
        assertEquals(test1,
                CompoundInterest.totalSavingsReal(450, 2023, 17, 6), tolerance);
        assertEquals(test2,
                CompoundInterest.totalSavingsReal(3600, 2026, 125, 12), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
