package ru.cbr.ilk.resthacks

import org.camunda.bpm.engine.ProcessEngine
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.variable.Variables
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import ru.cbr.ilk.resthacks.adapter.NotifySemaphorAdapter
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletResponse


/**
 * Use Camunda state machine for long-running retry, external task &
 * compensation
 */
@RestController
class TransferRestHacksControllerV6 {
    @Autowired
    private val camunda: ProcessEngine? = null
    @RequestMapping(path = ["/transfer/v6"], method = [RequestMethod.PUT])
    @Throws(Exception::class)
    fun retrieveTransfer(retrieveTransferPayload: String?, response: HttpServletResponse): String {
        val traceId = UUID.randomUUID().toString()
        val customerId = "0815" // get somehow from retrieveTransferPayload
        val amount: Long = 15 // get somehow from retrieveTransferPayload
        val newSemaphore: Semaphore = NotifySemaphorAdapter.newSemaphore(traceId)
        val pi = chargeTransferCard(traceId, customerId, amount)
        val finished = newSemaphore.tryAcquire(500, TimeUnit.MILLISECONDS)
        NotifySemaphorAdapter.removeSemaphore(traceId)
        return if (finished) {
            val failed = camunda!!.historyService.createHistoricActivityInstanceQuery().processInstanceId(pi.id) //
                .activityId("EndEvent_TransferFailed") //
                .count() > 0
            if (failed) {
                response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                "{\"status\":\"failed\", \"traceId\": \"$traceId\"}"
            } else {
                val historicVariableInstance =
                    camunda.historyService.createHistoricVariableInstanceQuery().processInstanceId(pi.id) //
                        .variableName("transferTransactionId") //
                        .singleResult()
                if (historicVariableInstance != null) {
                    val transferTransactionId = historicVariableInstance.value as String
                    "{\"status\":\"completed\", \"traceId\": \"$traceId\", \"transferTransactionId\": \"$transferTransactionId\"}"
                } else {
                    "{\"status\":\"completed\", \"traceId\": \"$traceId\", \"payedByTransfer\": \"true\"}"
                }
            }
        } else {
            response.status = HttpServletResponse.SC_ACCEPTED
            "{\"status\":\"pending\", \"traceId\": \"$traceId\"}"
        }
    }

    fun chargeTransferCard(traceId: String?, customerId: String?, remainingAmount: Long): ProcessInstance {
        return camunda!!.runtimeService //
            .startProcessInstanceByKey(
                "transferV6", traceId,  //
                Variables.putValue("amount", remainingAmount)
            )
    }
}