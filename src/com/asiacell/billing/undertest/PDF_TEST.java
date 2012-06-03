package com.asiacell.billing.undertest;

import java.io.*;
import com.lowagie.text.*;
//import com.lowagie.tools.*;
public class PDF_TEST
{
    public static void main(String args[])
        throws Exception
    {        
        Document doc01 = new Document(PageSize.A4,50,50,50,50);
        FileOutputStream fout = new FileOutputStream("d:\\test_pdf.pdf");        
        com.lowagie.text.pdf.PdfWriter wr = com.lowagie.text.pdf.PdfWriter.getInstance(doc01,fout);
        doc01.open();
        doc01.add(new Paragraph("<h2>Asiacell Telecom"));                
        doc01.add(new Paragraph("Billing Reporter"));
        doc01.close();        
    }
}