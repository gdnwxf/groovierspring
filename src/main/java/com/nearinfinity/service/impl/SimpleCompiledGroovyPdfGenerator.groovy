package com.nearinfinity.service.impl

import com.lowagie.text.Document
import com.lowagie.text.Font
import com.lowagie.text.PageSize
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import com.nearinfinity.model.Invoice
import com.nearinfinity.service.PdfGenerator

/**
 * This Groovy implementation class is used in the examples where we use compiled Groovy classes
 * as opposed to scripts.
 */
class SimpleCompiledGroovyPdfGenerator implements PdfGenerator {

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
