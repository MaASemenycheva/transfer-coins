package ru.cbr.ilk

import org.camunda.bpm.engine.delegate.BpmnError
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component

//@Component
//class СhangingBalanceOfWallet {
//    @Throws(Exception::class)
//    fun execute(delegateExecution: DelegateExecution) {
//        val transferAmount = (Math.random() * 100).toInt()
//        val transferStatus = "Undefined"
//        delegateExecution.setVariable("transferAmount", transferAmount)
//        delegateExecution.setVariable("transferStatus", transferStatus)
//    }
//}

@Component
internal class СhangingBalanceOfWallet : JavaDelegate {
    @Throws(Exception::class)
    override fun execute(delegateExecution: DelegateExecution) {
        val transferAmount = delegateExecution.getVariable("transferAmount") as Int
        val numberOfCoins = (Math.random() * 100).toInt()
        var transferStatus = "Undefined"
        var isTransfer = false

        if (transferAmount < 0) {
            throw  BpmnError("transferError")
        }

        if (numberOfCoins-transferAmount>=0) {
            isTransfer = true
            transferStatus = "Transferred"
        } else {
            transferStatus = "Transfer fail! :( "
        }
        delegateExecution.setVariable("transferAmount", transferAmount)
        delegateExecution.setVariable("numberOfCoins", numberOfCoins)
        delegateExecution.setVariable("transferStatus", transferStatus)
        delegateExecution.setVariable("isTransfer", isTransfer)
    }
}
