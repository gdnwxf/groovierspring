import com.nearinfinity.model.Invoice
import com.nearinfinity.model.LineItem
import org.springframework.context.support.ClassPathXmlApplicationContext

def context = new ClassPathXmlApplicationContext("applicationContext.xml")
def generator = context.getBean("pdfGenerator")

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
        FileOutputStream file = new FileOutputStream("target/Invoice${invoice.orderNumber}.pdf")
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
