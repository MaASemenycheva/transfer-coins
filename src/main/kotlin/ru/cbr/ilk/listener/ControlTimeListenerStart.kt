package ru.cbr.ilk.listener

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.camunda.bpm.engine.variable.Variables
import org.joda.time.DateTime
import org.springframework.stereotype.Component


@Component
class ControlTimeListenerStart : ExecutionListener {
    @Throws(Exception::class)
    override fun notify(delegateExecution: DelegateExecution) {
        val runtimeService = delegateExecution.processEngine.runtimeService
        val processInstanceId = delegateExecution.processInstanceId
        val currentActivityId = delegateExecution.currentActivityId
        val timerDate = DateTime.now().plusMinutes(1).toDate()
        val variables = Variables.createVariables().putValue("processInstanceId", processInstanceId)
            .putValue("currentActivityID", currentActivityId).putValue("timerDate", timerDate)
        val controlProcessInstance = runtimeService.startProcessInstanceByKey("MonitorMessageEvent", variables)
        delegateExecution.setVariableLocal("controlProcessInstanceId", controlProcessInstance.id)
    }
}
