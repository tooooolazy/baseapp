package com.tooooolazy.util.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Helper to create a header and a footer for the PDF file.<br>
 * Currently adds page numbering and bank name in the footer of every page.
 * @author gpatoulas
 *
 */
public class CustomHeaderFooter extends PdfPageEventHelper {
	PdfTemplate template;
	protected BaseFont bf;
	protected float fs;
	protected String leftHeader, middleHeader, rightHeader;

	public CustomHeaderFooter(BaseFont bf, String leftHeader, String middleHeader, String rightHeader) {
		super();
		this.bf = bf;
		fs=7;
		this.leftHeader = leftHeader;
		this.middleHeader = middleHeader;
		this.rightHeader = rightHeader;
	}

	public void onOpenDocument(PdfWriter writer, Document document) {
		template = writer.getDirectContent().createTemplate(30, 16);
	}

	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();
		// show some text if want only on page number 1
//		if (document.getPageNumber() == 1) {
//			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(
//					"test"), (document.right() - document.left()) / 2
//					+ document.leftMargin(), document.top() - 5, 0f);
//		}
		ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("�������������� ������� ������� ���.�.�.", PdfPartBase.getFontSmallItalic()),
				document.left(), document.bottom() - 15, 0f);

		if (leftHeader != null) {
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(leftHeader, PdfPartBase.getFontSmallItalic()),
					document.left(), document.top() , 0f);
		}
		if (rightHeader != null) {
			ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(rightHeader, PdfPartBase.getFontSmallItalic()),
					document.right() - document.rightMargin() - 20, document.top() , 0f);
		}
		int pageN = writer.getPageNumber();
		String text = "������ " + pageN + " ��� ";
		cb.beginText();
		cb.setFontAndSize(bf, fs);
		cb.setTextMatrix(document.right() - document.rightMargin() - 20,
				document.bottom() - 15);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(template, document.right(), document.bottom() - 15);
	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		template.beginText();
		template.setFontAndSize(bf, fs);
		template.setTextMatrix(0, 0);
		template.showText("" + (writer.getPageNumber() - 1));
		template.endText();
	}
}
