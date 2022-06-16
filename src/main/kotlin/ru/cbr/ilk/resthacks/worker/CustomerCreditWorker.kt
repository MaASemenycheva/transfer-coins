package ru.cbr.ilk.resthacks.worker

import org.camunda.bpm.client.ExternalTaskClient
import java.util.*



object CustomerTransferWorker {
    private const val BASE_URL = "http://localhost:8100/rest/engine/default/"
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // bootstrap the client
        val client: ExternalTaskClient = ExternalTaskClient.create() //
            .baseUrl(BASE_URL) //
            .asyncResponseTimeout(5000) // long polling interval
            .build()

        // subscribe to the topic
        client.subscribe("customer-transfer") //
            .lockDuration(5000) //
            .handler { externalTask, externalTaskService ->

                // retrieve a variable from the Workflow Engine
                val amount: Long = externalTask.getVariable("amount")

                // complete the external task
                externalTaskService.complete(externalTask,
                    Collections.singletonMap("remainingAmount", 15) as Map<String, Any>?
                )
//                externalTaskService.complete(externalTask, Collections.singletonMap("remainingAmount", 15))

                println("deducted $amount from customer transfer")
            }.open()
        client.subscribe("customer-transfer-refund") //
            .lockDuration(5000) //
            .handler { externalTask, externalTaskService ->
                externalTaskService.complete(externalTask)
                println("refunded to customer transfer")
            }.open()
    }
}