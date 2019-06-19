package com.nearinfinity.service.impl

import com.lowagie.text.Chunk
import com.lowagie.text.Document
import com.lowagie.text.Element
import com.lowagie.text.Font
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.Phrase
import com.lowagie.text.Rectangle
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import com.nearinfinity.model.Invoice
import com.nearinfinity.service.PdfGenerator
import java.awt.Color
import java.text.NumberFormat
import java.text.SimpleDateFormat

// This script contains multiple implementations of PdfGenerator. The actual implementation used depends
// in this case on a system property. You could use whatever logic you like, however. Note that the script
// must instantiate and return the specific implementation that should be used. See the end of the script
// which instantiates either SimpleCompiledGroovyPdfGenerator or ComplexGroovyPdfGenerator depending

class SimpleGroovyPdfGenerator implements PdfGenerator {

    String companyName

    public byte[] pdfFor(Invoice invoice) {
        Document document = new Document(PageSize.LETTER)
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        PdfWriter.getInstance(document, output)
        document.open()
        Font hdrFont = new Font(family: Font.HELVETICA, size: 24.0, style: Font.ITALIC)
        document.add(new Paragraph("$companyName", hdrFont))
        document.add(new Paragraph("Invoice $invoice.orderNumber"))
        document.add(new Paragraph("Total amount: \$ ${invoice.total}"))
        document.close()
        output.toByteArray()
    }

}

class ComplexGroovyPdfGenerator implements PdfGenerator {

    String companyName

    public byte[] pdfFor(Invoice invoice) {
        Document document = new Document(PageSize.LETTER)
        ByteArrayOutputStream output = new ByteArrayOutputStream()
        PdfWriter.getInstance(document, output)
        document.open()

        Font headerFont = new Font(family: Font.HELVETICA, size: 24.0, style: Font.ITALIC)
        Paragraph headerParagraph = new Paragraph(companyName, headerFont)
        headerParagraph.setSpacingAfter(16)
        document.add(headerParagraph)

        addLabelValuePair(document, "Order number", invoice.orderNumber)
        addLabelValuePair(document, "Order date", formatDate(invoice.orderDate))

        PdfPTable table = new PdfPTable(1, 6, 1)
        table.setHorizontalAlignment(Element.ALIGN_LEFT)
        table.setWidthPercentage(100)
        table.setSpacingBefore(12)
        addHeaderCell(table, "Qty")
        addHeaderCell(table, "Description")
        addHeaderCell(table, "Price")
        invoice.lineItems.each {lineItem ->
            table.addCell(lineItem.quantity.toString())
            table.addCell(lineItem.description)
            table.addCell(formatPrice(lineItem.price))
        }

        PdfPCell totalLabelCell = new PdfPCell(new Phrase("Total:", boldFont()))
        totalLabelCell.setColspan(2)
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT)
        totalLabelCell.setPaddingRight(5)
        totalLabelCell.setBorder(Rectangle.NO_BORDER)
        table.addCell(totalLabelCell)

        PdfPCell totalCell = new PdfPCell(new Phrase(formatPrice(invoice.total), boldFont()))
        totalCell.setBorder(Rectangle.NO_BORDER)
        table.addCell(totalCell)

        document.add(table)
        document.close()
        return output.toByteArray()
    }

    def addHeaderCell(table, text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, boldFont()))
        cell.setBackgroundColor(Color.LIGHT_GRAY)
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE)
        cell.setPadding(2)
        table.addCell(cell)
    }

    def boldFont() {
        return new Font(style: Font.BOLD)
    }

    def addLabelValuePair(document, label, value) {
        Chunk labelChunk = new Chunk("$label: ", boldFont())
        Chunk valueChunk = new Chunk("$value")
        Paragraph paragraph = new Paragraph()
        paragraph.add(labelChunk)
        paragraph.add(valueChunk)
        document.add(paragraph)
    }

    def formatPrice(price) {
        NumberFormat.getCurrencyInstance(Locale.US).format(price)
    }

    def formatDate(date) {
        new SimpleDateFormat("MM/dd/yyyy h:mm a").format(date)
    }

}

def type = System.properties['pdfGeneratorType']

if (type == 'simple') {
    println "Using simple PDF generatpr"
    return new SimpleCompiledGroovyPdfGenerator()
}
else {
    println "Using complex PDF generatpr"
    return new ComplexGroovyPdfGenerator()
}
