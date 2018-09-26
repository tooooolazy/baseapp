package com.tooooolazy.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletContext;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class TLZPdfHelper {
	protected static BaseFont baseFont;
	protected static Font font;
	protected static Font fontTiny;
	protected static Font fontTinyUnderline;
	protected static Font fontSmall;
	protected static Font fontSmallBold;
	protected static Font fontSmallItalic;
	protected static Font fontBold;
	protected static Font fontHeader;
	protected static Font fontPartTitle;
	protected static float baseSize = 8;
	public static ServletContext servletContext;
	protected static String fontPath;

	public static String getFontPath() {
		if (fontPath == null) {
			String s = servletContext.getRealPath("");
			fontPath =  s + File.separator + "arial.ttf";
		}
		return fontPath;
	}
	public static void setFontPath(String path) {
		fontPath = path;
	}
	public static BaseFont getBaseFont() {
		if (baseFont == null) {
			try {
				baseFont = BaseFont.createFont(getFontPath() ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return baseFont;
	}
	public static void main(String[] args) {
		URL url = TLZPdfHelper.class.getResource("./pdf/-arial.ttf");
		url = TLZPdfHelper.class.getResource("util/pdf/-arial.ttf");
		url = TLZPdfHelper.class.getResource("./util/pdf/-arial.ttf");
		url = TLZPdfHelper.class.getResource("tooooolazy/util/pdf/-arial.ttf");
		url = TLZPdfHelper.class.getResource("./tooooolazy/util/pdf/pdf/-arial.ttf");
		url = TLZPdfHelper.class.getResource("com/tooooolazy/util/pdf/pdf/-arial.ttf");
		url = TLZPdfHelper.class.getResource("./com/tooooolazy/util/pdf/pdf/-arial.ttf");
	}
	public static void setBaseSize(int size) {
		baseSize = size;
		font = null;
		fontTiny = null;
		fontTinyUnderline = null;
		fontSmall = null;
		fontSmallBold = null;
		fontSmallItalic = null;
		fontBold = null;
		fontHeader = null;
		fontPartTitle = null;
	}
	public static Font getFont() {
		if (font == null)
			font = new Font(getBaseFont(), baseSize);

		return font;
	}
	public static Font getFontTiny() {
		if (fontTiny == null)
			fontTiny = new Font(getBaseFont(), baseSize-4);

		return fontTiny;
	}
	public static Font getFontTinyUnderline() {
		if (fontTinyUnderline == null)
			fontTinyUnderline = new Font(getBaseFont(), baseSize-4, Font.UNDERLINE);

		return fontTinyUnderline;
	}
	public static Font getFontSmall() {
		if (fontSmall == null)
			fontSmall = new Font(getBaseFont(), baseSize-2);

		return fontSmall;
	}
	public static Font getFontSmallBold() {
		if (fontSmallBold == null)
			fontSmallBold = new Font(getBaseFont(), baseSize-2, Font.BOLD);

		return fontSmallBold;
	}
	public static Font getFontSmallItalic() {
		if (fontSmallItalic == null)
			fontSmallItalic = new Font(getBaseFont(), baseSize-2, Font.ITALIC);

		return fontSmallItalic;
	}
	public static Font getFontBold() {
		if (fontBold == null)
			fontBold = new Font(getBaseFont(), baseSize, Font.BOLD);

		return fontBold;
	}
	public static Font getFontPartTitle() {
		if (fontPartTitle == null)
			fontPartTitle = new Font(getBaseFont(), baseSize+2, Font.BOLD);

		return fontPartTitle;
	}
	public static Font getFontHeader() {
		if (fontHeader == null)
			fontHeader = new Font(getBaseFont(), baseSize+4, Font.BOLD);

		return fontHeader;
	}

	public static float getSpaceUnits(int mm) {
		return (float) (mm/25.4)*72;
	}

	public static Chunk createChunk(String text) {
		Chunk c = new Chunk(text, getFont());
		return c;
	}
	public static Chunk createChunk(String text, Font font) {
		Chunk c = new Chunk(text, font);
		return c;
	}
	/**
	 * Creates a pdf paragraph with the given parameters
	 * @param text
	 * @param font
	 * @param alignment
	 * @param spacingAfter
	 * @return
	 */
	public static Paragraph createParagraph(String text, Font font, int alignment, float spacingAfter) {
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(alignment);
		p.setSpacingAfter(spacingAfter);
		return p;
	}
	public static Paragraph createParagraph(String text, int alignment, float spacingAfter) {
		Paragraph p = new Paragraph(text);
		p.setAlignment(alignment);
		p.setSpacingAfter(spacingAfter);
		return p;
	}
	/**
	 * Creates a PDF table and sets its width to 100%
	 * @param columns
	 * @return
	 */
	public static PdfPTable createTable(int columns) {
		PdfPTable table = new PdfPTable(columns);
		table.setWidthPercentage(100);
        return table;
	}
	/**
	 * Creates a single table cell containing the given element.
	 * @param hasBorder
	 * @param e the element to add
	 * @param padding if -1, nothing is set
	 * @return
	 */
	public static PdfPCell createTableCell(boolean hasBorder, Element e, float padding) {
		return createTableCell(hasBorder, e, padding, 1, 1);
	}
	/**
	 * Creates a single table cell containing the given element.<br>
	 * If the cell spans over 2 or more rows then it is vertically aligned to middle
	 * @param hasBorder
	 * @param e
	 * @param padding
	 * @param rowSpan
	 * @param colSpan
	 * @return
	 */
	public static PdfPCell createTableCell(boolean hasBorder, Element e, float padding, int rowSpan, int colSpan) {
		PdfPCell c = null;
		if (e instanceof Chunk) // work around to make horizontal alignment work with Chunks
			c = new PdfPCell(new Phrase((Chunk)e));
		else {
			c = new PdfPCell();
			c.addElement(e);
		}
		c.setColspan(colSpan);
		c.setRowspan(rowSpan);
		if (rowSpan > 1)
			c.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c.setUseAscender(true); // so we do not have any space above!
        c.setUseDescender(true); // so we have a bit more space below!
		if (padding > -1)
			c.setPadding(padding);
		if (!hasBorder) {
			c.setBorder(0);
		}
		return c;
	}
}
