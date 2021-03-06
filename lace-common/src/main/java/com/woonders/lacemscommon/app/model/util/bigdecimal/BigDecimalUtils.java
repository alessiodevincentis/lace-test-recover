package com.woonders.lacemscommon.app.model.util.bigdecimal;

/**
 * Created by ema98 on 8/21/2017.
 */

import java.math.BigDecimal;

/**
 * Utility to help comparison of {@link BigDecimal}.
 * <p>
 * The only way to compare {@link BigDecimal} is to get result of compare
 * function of {@link BigDecimal} and compare the result with -1, 0 and 1.
 * <p>
 * Although it is straight forward however it lacks expressiveness and decreases
 * readability. For instance look at this line of code :
 * <p>
 * <pre>
 * <code>
 *     if(balance.compareTo(maxAmount) < 0))
 * </code>
 * </pre>
 * <p>
 * the code above try to check condition "balance &lt; maxAmount". You
 * definitely spotted the problem. now imagine how hard it can be if you have to
 * read some code with a lot of {@link BigDecimal} comparison!! </b>
 * {@link BigDecimalUtils} makes comparison of {@link BigDecimal}s more easier
 * and more readable than the comparator method. look how above code are written
 * by the help of this library.
 * <p>
 * <pre>
 * <code>
 *     if( is(balance).lt(maxAmount) )
 * </code>
 * </pre>
 *
 * @author adigozalpour
 */
public class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    /**
     * Entry points of {@link BigDecimalUtils} <br/>
     * <br/>
     * Example usage:
     * <p>
     * <pre>
     * <code>
     *      is(three).eq(four); //Equal
     * 		is(two).gt(two);    //Greater than
     * 		is(two).gte(one);   //Greater than equal
     * 		is(three).lt(two);  //Less than
     * 		is(three).lte(two); //Less than equal
     *
     *      is(three).notEq(four); //Not Equal
     * 		is(two).notGt(two);    //Not Greater than
     * 		is(two).notGte(one);   //Not Greater than equal
     * 		is(three).notLt(two);  //Not Less than
     * 		is(three).notLte(two); //Not Less than equal
     *
     *      is(three).isZero();
     *      is(three).notZero();
     *      is(three).isPositive(); // greater than zero
     *      is(three).isNegative(); // less than zero
     *      is(three).isNonPositive(); //less than or equal zero
     *      is(three).isNonNegative(); //greater than or equal zero
     * </code>
     * </pre>
     *
     * @param decimal your {@link BigDecimal}
     * @return {@link BigDecimalWrapper}
     */
    public static BigDecimalWrapper is(BigDecimal decimal) {

        return new BigDecimalWrapper(decimal);
    }

    /**
     * Entry points of {@link BigDecimalUtils} <br/>
     * <br/>
     * Example usage:
     * <p>
     * <pre>
     * <code>
     *      is(three).eq(four); //Equal
     * 		is(two).gt(two);    //Greater than
     * 		is(two).gte(one);   //Greater than equal
     * 		is(three).lt(two);  //Less than
     * 		is(three).lte(two); //Less than equal
     *
     *      is(three).notEq(four); //Not Equal
     * 		is(two).notGt(two);    //Not Greater than
     * 		is(two).notGte(one);   //Not Greater than equal
     * 		is(three).notLt(two);  //Not Less than
     * 		is(three).notLte(two); //Not Less than equal
     *
     *      is(three).isZero();
     *      is(three).notZero();
     *      is(three).isPositive(); // greater than zero
     *      is(three).isNegative(); // less than zero
     *      is(three).isNonPositive(); //less than or equal zero
     *      is(three).isNonNegative(); //greater than or equal zero
     *
     *      is(three).isNullOrZero(); //is null or zero
     *      is(three).notNullOrZero(); //not null or zero
     * </code>
     * </pre>
     *
     * @param dbl your {@link BigDecimal}
     * @return {@link BigDecimalWrapper}
     */
    public static BigDecimalWrapper is(double dbl) {
        return is(BigDecimal.valueOf(dbl));
    }

}