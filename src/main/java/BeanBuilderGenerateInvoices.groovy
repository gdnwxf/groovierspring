import com.nearinfinity.model.Invoice
import com.nearinfinity.model.LineItem
import com.nearinfinity.spring.scripting.groovy.PerformanceLoggingCustomizer
import org.springframework.scripting.groovy.GroovyScriptFactory
import org.springframework.scripting.support.ScriptFactoryPostProcessor
import com.nearinfinity.service.impl.CompiledGroovyPdfGenerator
import com.nearinfinity.spring.scripting.support.CustomScriptFactoryPostProcessor
import org.springframework.jdbc.datasource.DriverManagerDataSource

// Build beans using Grails Bean Builder - no XML config
def builder = new grails.spring.BeanBuilder()

// EX #1: Use precompiled Groovy object
builder.beans {
    pdfGenerator(CompiledGroovyPdfGenerator) {
        companyName = 'Bean Builder Bookstore'
    }
}

// EX #2: Use Groovy script
//builder.beans {
//    scriptFactoryPostProcessor(ScriptFactoryPostProcessor)
//    pdfGenerator(GroovyScriptFactory, 'classpath:com/nearinfinity/service/impl/GroovyPdfGenerator.groovy') {
//        companyName = 'Bean Builder Bookstore'
//    }
//}

// EX #3: Use Groovy script with refresh and customizer
//builder.beans {
//    performanceLoggingCustomizer(PerformanceLoggingCustomizer)
//    scriptFactoryPostProcessor(ScriptFactoryPostProcessor) {
//        defaultRefreshCheckDelay = 10000
//    }
//    pdfGenerator(GroovyScriptFactory,
//            'classpath:com/nearinfinity/service/impl/GroovyPdfGenerator.groovy',
//            performanceLoggingCustomizer) { bean ->
//        companyName = 'Bean Builder Bookstore'
//        bean.beanDefinition.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, 6000)
//    }
//}

// EX #4: Use Groovy script that resides in a database
//builder.beans {
//    dataSource(DriverManagerDataSource) {
//        driverClassName = 'com.mysql.jdbc.Driver'
//        url = 'jdbc:mysql://localhost/groovierspring'
//        username = 'groovierspring'
//        password = 'groovy'
//    }
//    scriptFactoryPostProcessor(CustomScriptFactoryPostProcessor) {
//        dataSource = dataSource
//        defaultRefreshCheckDelay = 5000
//    }
//    pdfGenerator(GroovyScriptFactory,
//            'database:com/nearinfinity/service/impl/GroovyPdfGenerator.groovy') {
//        companyName = 'Database Groovy Bookstore'
//    }
//}

def context = builder.createApplicationContext()
def generator = context.pdfGenerator

Invoice invoice = new Invoice()
invoice.orderNumber = "12345"
invoice.orderDate = new Date()
invoice.lineItems = [
        new LineItem(quantity: 1, description: 'Groovy in Action (PDF)', price: 22.00),
        new LineItem(quantity: 1, description: 'Programming Erlang', price: 45.00),
        new LineItem(quantity: 2, description: 'iText in Action (PDF)', price: 22.00)
]

while (true) {
    try {
        byte[] invoicePdf = generator.pdfFor(invoice)
        FileOutputStream file = new FileOutputStream("/Users/wch/Downloads/groovierspring/target/Invoice${invoice.orderNumber}.pdf")
        file.withStream {
            file.write(invoicePdf)
        }
        println "Generated invoice $invoice.orderNumber"
    }
    catch (Exception ex) {
        println "Oops! " + ex
    }
    sleep(5000)
}