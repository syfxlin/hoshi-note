package me.ixk.hoshi.test

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.core.annotation.AliasFor
import kotlin.reflect.KClass

/**
 * @author Otstar Lin
 * @date 2021/5/21 16:04
 */
@WebMvcTest
@OverrideAutoConfiguration(enabled = true)
annotation class SpringWebTest(
    @get: AliasFor("controllers")
    vararg val value: KClass<*> = [],
    val properties: Array<String> = [],
    @get: AliasFor("value")
    val controllers: Array<KClass<*>> = [],
    val useDefaultFilters: Boolean = true,
    val includeFilters: Array<ComponentScan.Filter> = [],
    val excludeFilters: Array<ComponentScan.Filter> = [],
    @get: AliasFor(annotation = ImportAutoConfiguration::class, attribute = "exclude")
    val excludeAutoConfiguration: Array<KClass<*>> = []
)
