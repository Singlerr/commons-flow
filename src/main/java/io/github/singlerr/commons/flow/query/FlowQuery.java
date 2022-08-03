package io.github.singlerr.commons.flow.query;

import io.github.singlerr.commons.flow.query.utils.ExceptionExecutor;

/***
 * Flow query class
 * @see ExceptionQuery
 * @see RunnableQuery
 * @author Singlerr
 */
public abstract class FlowQuery{

    private FlowQuery previous;
    private FlowQuery next;

    public FlowQuery(FlowQuery previous){
        this.previous = previous;
    }
    public FlowQuery(){}

    /***
     * If current flow query is head of chain.
     * @return true if this is head of chain false if not
     */
    public boolean isHead(){
        return previous == null;
    }

    /***
     * Get next flow query
     * @return next flow query
     */
    public FlowQuery getNext() {
        return next;
    }

    /**
     * Get previous flow query
     * @return previous flow query
     */

    public FlowQuery getPrevious() {
        return previous;
    }

    /***
     * has next flow query
     * @return true if has next query false if not
     */
    public boolean hasNext(){
        return next != null;
    }

    /***
     * Get head of the chain
     * Entry point is ExceptionQuery
     * @return head of the chain
     */
    public ExceptionQuery getFront(){
        FlowQuery current;
        for(current = this;! current.isHead();current = current.previous);
        return (ExceptionQuery) current;
    }

    /***
     * Set next flow query
     * @param next next flow query
     */
    protected void setNext(FlowQuery next){
        this.next = next;
    }

    /***
     * Runnable query, inheritor of FlowQuery
     * Used as checking if exception will be thrown.
     * @author Singlerr
     */
    public final static class RunnableQuery extends FlowQuery{
        private final Runnable runnable;
        public RunnableQuery(Runnable runnable){
            super();
            this.runnable = runnable;
        }
        public RunnableQuery(Runnable runnable, FlowQuery previous){
            super(previous);
            this.runnable = runnable;
        }

        /***
         * Get runnable that will be invoked later to check if exception will be thrown
         * @return runnable
         */
        public Runnable getTask() {
            return runnable;
        }

        /***
         * Create new action that will be checked with new exception
         * Pass control to ExceptionQuery
         * @param exceptionClass Exception that will be checked
         * @return new instance of exception query
         * @param <T> Exception
         */
        public <T extends RuntimeException> ExceptionQuery expect(Class<? extends RuntimeException> exceptionClass){
            ExceptionQuery next = new ExceptionQuery(exceptionClass, this);
            setNext(next);
            return next;
        }
    }
    /***
     * Runnable query, inheritor of FlowQuery
     * @author Singlerr
     */
    public final static class ExceptionQuery extends FlowQuery{

        private Class<? extends RuntimeException> exceptionClass;

        public ExceptionQuery(Class<? extends RuntimeException> exceptionClass){
            super();
            this.exceptionClass = exceptionClass;
        }
        public ExceptionQuery(Class<? extends RuntimeException> exceptionClass, FlowQuery previous){
            super(previous);
            this.exceptionClass = exceptionClass;
        }

        /***
         * Register runnable that checked with exception registered before.
         * @param runnable runnable that checked with exception registered before
         * @return new instance of runnable query
         */
        public RunnableQuery tryInvoke(Runnable runnable){
            RunnableQuery next = new RunnableQuery(runnable,this);
            setNext(next);
            return next;
        }

        /***
         * Start flow of query.
         */
        public void execute(){
            ExceptionQuery current = getFront();

            while (current.hasNext()){
                RunnableQuery nextRunnable = (RunnableQuery) current.getNext();
                if(ExceptionExecutor.exceptionOccurred(current.exceptionClass,nextRunnable.runnable)){
                    current = (ExceptionQuery) nextRunnable.getNext();
                }else
                    break;
            }
        }
    }



}
