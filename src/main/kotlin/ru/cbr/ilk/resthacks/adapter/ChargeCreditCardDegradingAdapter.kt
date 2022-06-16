package ru.cbr.ilk.resthacks.adapter

import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
@ConfigurationProperties
class ChargeTransferCardDegradingAdapter : JavaDelegate {
    @Autowired
    var rest: RestTemplate? = null


    var stripeChargeUrl = "http://localhost:8099/charge"

    @FailingOnLastRetry
    @Throws(Exception::class)
    override fun execute(ctx: DelegateExecution) {
        val request = CreateChargeRequest()
        request.amount = ctx.getVariable("remainingAmount") as Long
        val response = rest!!.postForObject( //
            stripeChargeUrl,  //
            request,  //
            CreateChargeResponse::class.java
        )
        if (response!!.errorCode != null && response.errorCode!!.length > 0) {
            // raise error to be handled in BPMN model in case there was an error in transfer  handling
            ctx.setVariable("transferCardErrorCode", response.errorCode)
            throw BpmnError("Error_TransferCardError")
        }
        ctx.setVariable("transferTransactionId", response.transactionId)
    }

    class CreateChargeRequest {
        var amount: Long = 0
    }

    class CreateChargeResponse {
        var transactionId: String? = null
        var errorCode: String? = null
    }
}
