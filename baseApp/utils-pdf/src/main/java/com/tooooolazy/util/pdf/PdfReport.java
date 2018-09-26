package com.tooooolazy.util.pdf;

import java.io.OutputStream;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.tooooolazy.util.TLZUtils;

public class PdfReport {
	static Logger log = Logger.getLogger(PdfReport.class.getName());
	protected JSONObject params;
	public JSONObject getParams() {
		return params;
	}

	public Document create(OutputStream out, JSONObject params) {
		this.params = params;
		Document document = new Document();
		try {
			//create PDF and open to write
			PdfWriter writer = PdfWriter.getInstance(document, out);
			// use page event to create header and footer for the pages!
			writer.setPageEvent(new CustomHeaderFooter( PdfPartBase.getBaseFont(), getLeftHeader(), getMiddleHeader(), getRightHeader() ));
			document.setPageSize(PageSize.A4);
	        document.setMargins(25, 25, 25, 25);
            document.open();
            
			//define font and BaseFont for Greek - can use "CP1253" instead of BaseFont.IDENTITY_H
			//TODO use method to define Windows-Linux path where arial.ttf is placed
//			String fontPath = PropertyHelper.singleton().getProperty( "font.path" );

			int reqId = params.optInt("reqId");
			String reportName = params.optString("name", "");
			JSONArray jaParts = params.getJSONArray("parts");
			for (int i=0; i<jaParts.length(); i++) {
				Object part = jaParts.get(i);
				if (part instanceof String) { // simple part
					PdfPart pdfp = loadPdfPart(reportName, (String)part);
					if (pdfp != null) {
						pdfp.setReport( this );
						pdfp.create(reqId, reportName, (String)part);
						String newPage = PdfHelper.getFromBundle(reportName, (String)part, "newPage");
						if (newPage.startsWith("!"))
							newPage = null;
						addPart(document, pdfp, newPage);
					} else {
						log.warning("pdf part " + part + " for " + reportName + " does not exist");
					}
				} else
				if (part instanceof JSONObject) { // composite part
					PdfPart pdfp = loadPdfPart(reportName, (JSONObject)part);
					if (pdfp != null) {
						pdfp.setReport( this );
						pdfp.create(reqId, reportName, (JSONObject)part);
						addPart(document, pdfp, null);
					} else {
						log.warning("pdf part " + part + " for " + reportName + " does not exist");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.warning("ERROR in PDF file: " + e.getMessage());
		} finally {
			document.close();			
		}
		return document;
	}

	protected String getLeftHeader() {
		// TODO Auto-generated method stub
		return null;
	}
	protected String getMiddleHeader() {
		// TODO Auto-generated method stub
		return null;
	}
	protected String getRightHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	protected PdfPart loadPdfPart(String reportName, String partName) {
		PdfPart pdfp = null;
		try {
			pdfp = (PdfPart)TLZUtils.loadObject(PdfHelper.DEFAULT_PACKAGE + "PdfPart_" + partName + "_" + reportName);
			Deprecated d = pdfp.getClass().getAnnotation(Deprecated.class);
			if (d != null) {
				log.info("PdfPart_" + partName + "_" + reportName + " is Deprecated. Ignoring.");
				throw new Exception(pdfp.getClass().getName() + " is deprecated. Ignoring...");
			}
		} catch (Exception e) {
			log.info("PdfPart_" + partName + "_" + reportName + " not found.");
			try {
				pdfp = (PdfPart)TLZUtils.loadObject(PdfHelper.DEFAULT_PACKAGE + "PdfPart_" + partName);
				Deprecated d = pdfp.getClass().getAnnotation(Deprecated.class);
				if (d != null) {
					log.info("PdfPart_" + partName + " is Deprecated. Ignoring.");
					throw new Exception(pdfp.getClass().getName() + " is deprecated. Ignoring...");
				}
			} catch (Exception e1) {
				log.info("PdfPart_" + partName + " not found. Using Default.");
				pdfp = new PdfPart();
			}
		}
		return pdfp;
	}
	protected PdfPart loadPdfPart(String reportName, JSONObject part) {
		String partName = (String) part.keys().next();
		return loadPdfPart(reportName, partName);
	}

	protected void addPart(Document document, PdfPart pdfp, String newPage) throws DocumentException {
		if ("before".equals(newPage))
			document.newPage();
		for (Element element : pdfp.getElements()) {
			document.add( element );
		}
		if ("after".equals(newPage))
			document.newPage();
	}
}
