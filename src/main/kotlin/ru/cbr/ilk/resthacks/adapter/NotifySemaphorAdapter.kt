package ru.cbr.ilk.resthacks.adapter

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import java.util.concurrent.Semaphore


@Component
class NotifySemaphorAdapter : JavaDelegate {
    @Throws(Exception::class)
    override fun execute(context: DelegateExecution) {
        val s = semaphors[context.businessKey]
        if (s != null) {
            s.release()
            semaphors.remove(context.businessKey)
        }
    }

    companion object {
        var semaphors: MutableMap<String, Semaphore> = HashMap()
        fun newSemaphore(traceId: String): Semaphore {
            val sema = Semaphore(0)
            semaphors[traceId] = sema
            return sema
        }

        fun removeSemaphore(traceId: String) {
            semaphors.remove(traceId)
        }
    }
}
