package ru.cbr.ilk.listener

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.springframework.stereotype.Component


@Component
class ControlTimeListenerEnd : ExecutionListener {
    @Throws(Exception::class)
    override fun notify(delegateExecution: DelegateExecution) {
        val runtimeService = delegateExecution.processEngine.runtimeService
        val controlProcessInstanceId = delegateExecution.getVariable("controlProcessInstanceId") as String
        runtimeService.createMessageCorrelation("MessageCancelControl")
            .processInstanceId(controlProcessInstanceId).correlate()
    }
}