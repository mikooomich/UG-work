package QueuesLinkedListInterfaceSorting.PartB;

/**
 * This class is used to have custom exception messages
 *
 * @author Michael Zhou
 * @course COSC 1P03
 * @assignment #5
 * @version 1.0
 * @date 2022-04-07
 */
public class NoSuchElementException extends RuntimeException {

    public NoSuchElementException() {
    }

    public NoSuchElementException(String msg) {
        super(msg);
    }
}
