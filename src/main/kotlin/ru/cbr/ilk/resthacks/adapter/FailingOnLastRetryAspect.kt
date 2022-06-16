package ru.cbr.ilk.resthacks.adapter

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.impl.context.Context
import org.springframework.stereotype.Component


@Aspect
@Component
class FailingOnLastRetryAspect {
    @Around("@annotation(FailingOnLastRetry)")
    @Throws(Throwable::class)
    fun guardedExecute(joinPoint: ProceedingJoinPoint): Any {
        val jobExecutorContext = Context.getJobExecutorContext()
        if (jobExecutorContext != null && jobExecutorContext.currentJob != null) {
            // this is called from a Job
            if (jobExecutorContext.currentJob.retries <= 1) {
                // and the job will run out of retries when it fails again
                return try {
                    joinPoint.proceed()
                } catch (ex: Exception) {
                    // Probably save the exception somewhere
                    throw BpmnError(NO_RETRIES_ERROR)
                }
            }
        }
        // otherwise normal behavior (including retries possibly)
        return joinPoint.proceed()
    }

    companion object {
        var NO_RETRIES_ERROR = "Error_NoRetries"
    }
}