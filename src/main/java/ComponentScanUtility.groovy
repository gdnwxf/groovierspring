import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.support.ClassPathXmlApplicationContext

def provider = new ClassPathScanningCandidateComponentProvider(true)
def basePackage = "com/nearinfinity/service"
def components = provider.findCandidateComponents(basePackage)
components.each {component ->
    println "Component info:"
    println "  ${component}"
    println "  ${component.beanClassName}"
    println "---"
}

def context = new ClassPathXmlApplicationContext("applicationContext.xml")
context.beanDefinitionNames.each {name ->
    def bean = context.getBean(name)
    println "BEAN: $name (${bean.getClass().name})"
}
