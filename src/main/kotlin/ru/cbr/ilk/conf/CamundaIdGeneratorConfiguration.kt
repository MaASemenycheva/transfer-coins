package ru.cbr.ilk.conf

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration
import org.camunda.bpm.spring.boot.starter.configuration.CamundaHistoryLevelAutoHandlingConfiguration
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration
import org.springframework.context.annotation.Configuration


/**
* Use [StrongUuidGenerator] to avoid potential problems in cluster environments with [DbIdGenerator]
*/
//@Configuration
class CamundaIdGeneratorConfiguration : AbstractCamundaConfiguration(),
   CamundaHistoryLevelAutoHandlingConfiguration {
   override fun preInit(configuration: SpringProcessEngineConfiguration) {
       configuration.idGenerator = StrongUuidGenerator()
   }

   override fun postInit(processEngineConfiguration: ProcessEngineConfigurationImpl?) {
       TODO("Not yet implemented")
   }
}
