package ru.cbr.ilk.conf

import org.camunda.bpm.engine.impl.jobexecutor.CallerRunsRejectedJobsHandler
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor
import org.camunda.bpm.engine.spring.components.jobexecutor.SpringJobExecutor
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultJobConfiguration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.task.TaskExecutor


//@Configuration
object CamundaJobExecutorConfiguration : DefaultJobConfiguration() {
    //  @Bean
    //  @ConditionalOnProperty(prefix = "camunda.bpm.job-execution", name = "enabled", havingValue = "true", matchIfMissing = true)
    fun jobExecutor(@Qualifier(JobConfiguration.CAMUNDA_TASK_EXECUTOR_QUALIFIER) taskExecutor: TaskExecutor?): JobExecutor {
        val springJobExecutor = SpringJobExecutor()
        springJobExecutor.taskExecutor = taskExecutor
        springJobExecutor.rejectedJobsHandler = CallerRunsRejectedJobsHandler()
        springJobExecutor.waitTimeInMillis = 10
        //    springJobExecutor.setWaitIncreaseFactor(1.0f);
        springJobExecutor.maxWait = 20
        return springJobExecutor
    }
}