package com.nearinfinity.service;

import com.nearinfinity.model.Invoice;

/**
 * The contract for generating PDFs.
 */
public interface PdfGenerator {

    byte[] pdfFor(Invoice invoice);

}
