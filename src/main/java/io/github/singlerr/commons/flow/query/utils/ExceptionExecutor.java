package io.github.singlerr.commons.flow.query.utils;

/***
 * An exception executor that check if any exception thrown in task.
 * @author Singlerr
 */
public final class ExceptionExecutor {
    private ExceptionExecutor(){}

    /***
     * Run runnable and check if any exception thrown in task.
     * @param exceptionClass class of exception that checked
     * @param runnable runnable possibly throw the exception
     * @return true if the exception thrown and false if not.
     * @param <T> Type that extends RuntimeException
     */
    public static <T extends RuntimeException> boolean exceptionOccurred(Class<T> exceptionClass, Runnable runnable){
        try{
            runnable.run();
            return false;
        }catch (Exception e){
            return e.getClass().equals(exceptionClass);
        }
    }
}
