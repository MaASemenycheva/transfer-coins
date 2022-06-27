package ru.cbr.ilk

import org.assertj.core.api.Assertions.*
import ru.cbr.ilk.listener.ControlTimeListenerEnd
import ru.cbr.ilk.listener.ControlTimeListenerStart
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.test.Deployment
import org.camunda.bpm.engine.test.ProcessEngineRule
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*
import org.camunda.bpm.engine.test.mock.Mocks
//import org.junit.Rule
//import org.junit.Test
import org.junit.jupiter.api.Test

@Deployment(resources = ["control-message-event.bpmn", "changingBalanceOfWallet.bpmn"])
class TestProcessWithControlTimeProcess {
    private val PROCESS_KEY = "MessageControlExampleProcess"

//    @Rule
    var processEngineRule = ProcessEngineRule()
    private var processInstance: ProcessInstance? = null
    private var cancelControlProcessInstance: ProcessInstance? = null
    @Test
    fun testMessageWithoutTimeout() {
        mainTestBlock()
        runtimeService().createMessageCorrelation("MessageToControl").correlate()
        assertThat(cancelControlProcessInstance).isEnded()
        assertThat(processInstance).isEnded()
        assertThat(cancelControlProcessInstance).hasPassed("EndWithoutTimeout")
        assertThat(cancelControlProcessInstance).hasNotPassed("EndWithTimeout")
    }

    @Test
    fun testMessageWithTimeout() {
        mainTestBlock()
        execute(job())
        assertThat(cancelControlProcessInstance).isNotEnded()
        assertThat(processInstance).isNotEnded()
        assertThat(cancelControlProcessInstance).hasPassed("NotificationTask").hasPassed("EndWithTimeout")
            .hasNotPassed("EndWithoutTimeoutit ")
    }

    private fun mainTestBlock() {
        Mocks.register("controlTimeListenerStart", ControlTimeListenerStart())
        Mocks.register("controlTimeListenerEnd", ControlTimeListenerEnd())
        processInstance = runtimeService().startProcessInstanceByKey(PROCESS_KEY)
        assertThat(processInstance).isWaitingAt("HumanTask")
        complete(task())
        assertThat(processInstance).isWaitingAt("MessageTask")
        val cancelControlProcesses: List<ProcessInstance> =
            processInstanceQuery().processDefinitionKey("MonitorMessageEvent").list()
        assertThat(cancelControlProcesses.size).isEqualTo(1)
        cancelControlProcessInstance = cancelControlProcesses[0]
        assertThat(cancelControlProcessInstance).isWaitingAt("CancelControlTask")
        assertThat(jobQuery().timers().list().size).isEqualTo(1)
    }
}
