package com.wutsi.platform.security

import com.wutsi.platform.core.WutsiApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
@WutsiApplication
public class Application

public fun main(vararg args: String) {
    org.springframework.boot.runApplication<Application>(*args)
}
