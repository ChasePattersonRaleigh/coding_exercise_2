import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is for question 2. 
 * The application here provides a helper method that takes a positive integer, N, and 
 * returns the maximal product of a set of positive integers whose sum is N. 
 * 
 * In this program, a tuple is an instance of List<Integer>
 */
public class MaximalProductOfSums {
    private static final boolean DEBUG = false;

    public static void main(String[] args) { 
        int n = 0;

        try {
            n = Integer.parseInt(args[0]);
        } catch(NumberFormatException | IndexOutOfBoundsException e) { 
            System.out.println("The provided argument was invalid. Please specify an integer greater than 1");
        }
        
        MaxProductAndTuple solution = solveCodingChallenge(n);
        System.out.println("The Solution is: " + solution);
    }

    private static MaxProductAndTuple solveCodingChallenge(int n) { 
        if (n <= 1) { 
            // invalid input
            return new MaxProductAndTuple(0, Collections.emptyList());
        }

        List<Integer> tupleWithMaxProduct = findTupleWithMaximalProductWhoseSumIsN(n);
        long maxProduct = getTupleProduct(tupleWithMaxProduct);

        return new MaxProductAndTuple(maxProduct, tupleWithMaxProduct);
    }

    /**
     * This method returns the tuple whose product is the largest and whose sum is "n"
     * It starts by answering the question for i = 2 first, then iterating.
     * @param n 
     * @return 
     */
    private static List<Integer> findTupleWithMaximalProductWhoseSumIsN(final int n) {
        
        Map<Integer, List<Integer>> mapFromXtoAddedWithMaxProduct = new HashMap<>();
        for (int i = 2; i <= n; i++) { 
            createTupleCollectionForN(i, mapFromXtoAddedWithMaxProduct);
        }

        return mapFromXtoAddedWithMaxProduct.get(n);
    }

    /**
     * Find the tuple with maximal product whose sum is "n". Add this tuple to the provided map.
     * 
     * @param n this represents the integer to generate the tuples for.
     * @param mapFromXtoAllPossibleAddends The cumulative map that associates an integer with all possible addends (tuples).
     */
    private static void createTupleCollectionForN(final int n, Map<Integer, List<Integer>> mapFromXtoTupleWithMaxProduct) {
        int a = n - 1;
        int b = 1;
        List<Integer> tupleWithLargestProduct = Collections.emptyList();



        while (a >= b) {
            /*
             * Part A computes all 2-tuples of form (x,y)
             * i.e. n = 4
             * (3,1), (2, 2) 
             */
            List<Integer> tuple = new LinkedList<>();
            tuple.add(a);
            tuple.add(b);
            logIfDebugOn(tuple.toString());
            tupleWithLargestProduct = getTupleWithLargerProduct(tupleWithLargestProduct, tuple);
            
            // Part B.
            // Create larger N-tuples with results from mapFromXtoAllPossibleAddends.get('a')
            // This contributes new tuples of form (X, b) where X is replaced with tuple whose sum is "a" and has maximal product (should already be in the map)
            // This is the expansion.
            // i.e. (3,1) --> (2,1,1), (1,1,1)
            // Not expanding on the rightward addend since that one has a smaller value and thus would have a smaller maximal product for its maximal tuple.
            if (a > 1) { 
                List<Integer> prevTupleWithMaxProduct = mapFromXtoTupleWithMaxProduct.get(a);
                List<Integer> tupleBasedOnPrev = new LinkedList<>();
                for (Integer tupleMember : prevTupleWithMaxProduct) { 
                    tupleBasedOnPrev.add(tupleMember);
                }
                tupleBasedOnPrev.add(b);
                logIfDebugOn(tupleBasedOnPrev.toString());
                tupleWithLargestProduct = getTupleWithLargerProduct(tupleWithLargestProduct, tupleBasedOnPrev);
            }
            
            a--;
            b++;
        }

        mapFromXtoTupleWithMaxProduct.put(n, tupleWithLargestProduct);
    }

    /**
     * Get product of a tuple.
     * 
     * @param tupleOne
     * @return
     */
    private static long getTupleProduct(List<Integer> tupleOne) { 
        if (tupleOne.isEmpty()) { 
            return 0;
        }

        long tupleProduct = 1;
        for (Integer x : tupleOne) { 
            tupleProduct *= x;
        }
        return tupleProduct;
    }

    /**
     * Gets the tuple with the larger product.
     * @param tupleOne
     * @param tupleTwo
     * @return
     */
    private static List<Integer> getTupleWithLargerProduct(List<Integer> tupleOne, List<Integer> tupleTwo) { 
        final long tupleOneProduct = getTupleProduct(tupleOne);
        final long tupleTwoProduct = getTupleProduct(tupleTwo);

        if (tupleTwoProduct > tupleOneProduct) { 
            return tupleTwo;
        }

        return tupleOne;
    }

    /**
     * This stores the maximal product and the first tuple 
     * found with that product.
     */
    private static class MaxProductAndTuple { 
        private long maxProduct;
        private List<Integer> tuple;

        public MaxProductAndTuple(long maxProduct, List<Integer> tuple) {
            this.maxProduct = maxProduct;
            this.tuple = tuple;
        }


        @Override
        public String toString() {
            return String.format("Product: %s; Tuple: %s", maxProduct, tuple);
        }
    }
    
    private static void logIfDebugOn(String msg) { 
        if (DEBUG) { 
            System.out.println(msg);
        }
    }
}
