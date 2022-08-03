package io.github.singlerr.commons.flow;

import io.github.singlerr.commons.flow.query.FlowQuery;

/***
 * FlowQuery creator.
 * @author Singlerr
 */
public final class ExceptionOptional {

    private ExceptionOptional(){}

    /***
     * Create new exception query instance. This is the entry point of flow query.
     * @param exceptionClass The class of exception that possibly thrown in task.
     * @return instance of exception query
     */
    public static FlowQuery.ExceptionQuery expect(Class<? extends RuntimeException> exceptionClass){
        return new FlowQuery.ExceptionQuery(exceptionClass);
    }
}
