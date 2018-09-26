package com.tooooolazy.util.pdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Provides pdf element creation functionality.
 * @author gpatoulas
 *
 */
public abstract class PdfPartBase {
	protected static BaseFont baseFont;
	protected List<Element> elements;
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

	protected PdfReport report;
	public void setReport(PdfReport report) {
		this.report = report;
	}
	public PdfReport getReport() {
		return report;
	}

	public PdfPartBase() {
		elements = new ArrayList<Element>();
	}

	public static String getFontPath() {
		return "c:/windows/fonts/arial.ttf";
	}

	public List<Element> getElements() {
		return elements;
	}

	public static BaseFont getBaseFont() {
		if (baseFont == null) {
			try {
				baseFont = BaseFont.createFont(getFontPath() ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//				baseFont.setSubset(true);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return baseFont;
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



	/**
	 * Creates a Report title
	 * @param reportName
	 */
	protected void createTitle(String reportName) {
		String title = PdfHelper.getFromBundle(reportName+".title");
		Paragraph p = createParagraph(title, getFontHeader(), Element.ALIGN_CENTER, 15f);
		elements.add(p);
	}
	/**
	 * Creates a centered (using paragraph) part title
	 * @param reportName
	 * @param part
	 * @return
	 */
	protected Paragraph createTitle(String reportName, String part) {
		String title = PdfHelper.getFromBundle(reportName, part, "title");
		Paragraph p = createParagraph(title, getFontPartTitle(), Element.ALIGN_CENTER, 15f);
		return p;
	}

	/**
	 * Creates a pdf paragraph with the given parameters
	 * @param text
	 * @param font
	 * @param alignment
	 * @param spacingAfter
	 * @return
	 */
	protected Paragraph createParagraph(String text, Font font, int alignment, float spacingAfter) {
		Paragraph p = new Paragraph(text, font);
		p.setAlignment(alignment);
		p.setSpacingAfter(spacingAfter);
		return p;
	}
	protected Chunk createPartTitle(String reportName, String part) {
		return createPartTitle(reportName, part, true);
	}
	/**
	 * Creates a title element (as text Chunk) for the given part.<br>
	 * If no title text is retrieved from bundle (or it is an empty string) then the element is not created.<br>
	 * @param reportName
	 * @param part
	 * @param simplePart used to select font to use. if true a smaller font is used
	 * @return
	 */
	protected Chunk createPartTitle(String reportName, String part, boolean simplePart) {
		String pTitle = PdfHelper.getFromBundle(reportName, part, "title");
		if (!pTitle.startsWith("!") && !pTitle.trim().equals("")) { 
			Chunk c = createChunk(pTitle, simplePart?getFontPartTitle():getFontBold());
			return c;
		}
		return null;
	}
	protected Chunk createPartSubTitle(String reportName, String part, boolean simplePart) {
		String pTitle = PdfHelper.getFromBundle(reportName, part, "subTitle");
		if (!pTitle.startsWith("!") && !pTitle.trim().equals("")) { 
			Chunk c = createChunk(pTitle, simplePart?getFontPartTitle():getFontBold());
			return c;
		}
		return null;
	}
	protected Chunk createChunk(String text, Font font) {
		Chunk c = new Chunk(text, font);
		return c;
	}


	/**
	 * Creates a PDF table and sets its width to 100%
	 * @param columns
	 * @return
	 */
	protected PdfPTable createTable(int columns) {
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
	protected PdfPCell createTableCell(boolean hasBorder, Element e, float padding) {
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
	protected PdfPCell createTableCell(boolean hasBorder, Element e, float padding, int rowSpan, int colSpan) {
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


	protected Integer getTotalSize(String reportName, String part, Object[] elements) {
		int columns = 0;
		for (Object key : elements) {
			int caption_colspan = 1;
			String caption = PdfHelper.getFromBundle(reportName, part, key.toString());
			if (caption.indexOf(",") > -1) {
				// we got colspan
				String[] s = caption.split(","); 
				caption_colspan = Integer.parseInt(s[1]); // caption colspan
			}
			columns += caption_colspan;
		}
		return columns;
	}


	/**
	 * Creates a pdf table with 2 columns. One left aligned and one right aligned.
	 * Each column displays key:value pairs.
	 * @param reportName
	 * @param part
	 * @param orderedElements
	 * @param data
	 * @param hasBorder
	 * @return
	 */
	protected Element createAsLeftRightPairs(String reportName, String part, String[] orderedElements, Vector<Map> data, boolean hasBorder) {
		PdfPTable table = createTable(2);
		Object[] elements = getPartElements(orderedElements, data);
		
		for (Map map : data) {
			boolean left = true;
			for (Object key : elements) {
				String caption = key.equals("dummy")?"":PdfHelper.getFromBundle(reportName, part, key.toString());
				Object value = key.equals("dummy")?null:map.get(key);
				String strValue = "";

				if (value != null)
					strValue = convertValue(value, reportName, part, key.toString());
				Paragraph p = (Paragraph)createHPairParagraph(caption, strValue, getFontSmallBold(), getFontSmall(), left ? Element.ALIGN_LEFT : Element.ALIGN_RIGHT);
				left = !left;
				
				PdfPCell c = createTableCell(hasBorder, p, -1);
				table.addCell(c);
			}
		}
		return table;
	}
	/**
	 * TODO: Format value depending on its type. Useful for dates.
	 * @param value
	 * @return
	 */
	protected String convertValue(Object value, String reportName, String part, String element) {
		String elementType = getElementType(reportName, part, element);

		if (elementType != null) {
			return convert(value, elementType);
		} else
		return value.toString();
	}
	protected String getElementType(String reportName, String part, String element) {
		// TODO we might want to replace '.type' with '_type' in order to distinguish between actual part types and element types
		// (it is possible to have a part name equal to an element name, which is not good!)
		String type = PdfHelper.getFromBundle(reportName, part, element + ".type");
		if (!type.startsWith("!"))
			return type;
		return null;
	}
	/**
	 * Converts the given value to the given type. The type is the one retrieved from the property file.
	 * @param value
	 * @param elementType
	 * @return
	 */
	protected String convert(Object value, String elementType) {
		return value.toString();
	}
	/**
	 * If there are no orderedElements all elements are returned.<br>
	 * If there are NO orderedElements AND there is NO data then an empty array is returned.
	 * @param orderedElements
	 * @param data
	 * @return
	 */
	protected Object[] getPartElements(String[] orderedElements, Vector<Map> data) {
		Object[] elements;

		if (orderedElements != null && orderedElements.length > 0) {
			elements = orderedElements;
		} else {
			if (data != null && data.size() > 0)
				elements = data.get(0).keySet().toArray();
			else
				elements = new Object[0];
		}
		return elements;
	}

	/**
	 * Creates a side-by-side key value pair aligned to the left. 
	 * @param caption
	 * @param value
	 * @return
	 */
	protected Element createHPairParagraph(String caption, String value) {
		return createHPairParagraph(caption, value, getFontSmallBold(), getFontSmall(), Element.ALIGN_LEFT);
	}
	/**
	 * Creates a side-by-side key value pair.
	 * @param caption
	 * @param value
	 * @param alignment
	 * @return
	 */
	protected Paragraph createHPairParagraph(String caption, String value, int alignment) {
		return createHPairParagraph(caption, value, getFontSmallBold(), getFontSmall(), alignment);
	}
	/**
	 * Creates a side-by-side key value pair using given fonts. Used by {@link #createAsLeftRightPairs}
	 * @param caption if caption is empty string ':' is not appended!
	 * @param value
	 * @param cf
	 * @param vf
	 * @param alignment
	 * @return
	 */
	public Paragraph createHPairParagraph(String caption, String value, Font cf, Font vf, Integer alignment) {
		Element c = new Chunk(caption + (caption.equals("")?"":": "), cf);
		Element v = new Chunk(value, vf);
		Paragraph p = new Paragraph();
		p.add(c);
		p.add(v);
		p.setAlignment(alignment);
		return p;
	}
	/**
	 * Creates a top-bottom key value pair using given fonts.
	 * @param caption if caption is empty string ':' is not appended!
	 * @param value
	 * @param cf
	 * @param vf
	 * @param alignment
	 * @return
	 */
	public Paragraph createVPairParagraph(String caption, String value, Font cf, Font vf, Integer alignment) {
		Element c = new Chunk(caption + (caption.equals("")?"":":\n"), cf);
		Element v = new Chunk(value, vf);
		Paragraph p = new Paragraph();
		p.setLeading(cf.getSize()+2);
		p.add(c);
		p.add(v);
		p.setAlignment(alignment);
		return p;
	}

	/**
	 * Helper. Common code to add a title to a table
	 * @param reportName
	 * @param part
	 * @param table
	 * @return
	 */
	protected Element addTitle(String reportName, String part, PdfPTable table) {
		PdfPTable t = createTable(1);
		t.setKeepTogether(true);
		Chunk c = createPartTitle(reportName, part);
		PdfPCell tc = createTableCell(false, c, -1);
		tc.setPaddingTop(10);
		tc.setUseAscender(false); // so we have some space above
		t.addCell(tc);
		t.addCell(createTableCell(false, table, 0));
		return t;
	}

	protected PdfPCell alignCellByType(PdfPCell c, String elementType) {
		if ("currency".equals(elementType))
			c.setHorizontalAlignment(Element.ALIGN_RIGHT);

		return c;
	}
	/**
	 * Creates a checkbox using a square image currently located in bin directory of jboss server
	 * @return
	 */
	protected Chunk createCheckBox() {
		try {
			Chunk c = new Chunk(Image.getInstance("square.gif"), 0, 0);
			return c;
		} catch (BadElementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
}
