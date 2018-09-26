package com.tooooolazy.util.pdf;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.tooooolazy.util.TLZUtils;

public class PdfPart extends PdfPartBase {

	/**
	 * In case we need to 'remember' some values and use them inside other parts.
	 * @see override {@link com.icap.attikis.common.pdf.domain.PdfPart_header#rememberInParams this override as an example} 
	 * @param data
	 */
	protected void rememberInParams(Vector<Map> data) {
		// see override in PdfPart_header as an example
	}
	/**
	 * Creates a simple (standard) part
	 * @param reqId
	 * @param reportName
	 * @param part
	 */
	public void create(int reqId, String reportName, String part) {
		create(reqId, reportName, part, true);
	}
	/**
	 * @param reqId
	 * @param reportName
	 * @param part
	 * @param simplePart if false element is not added to the element list
	 */
	protected Element create(int reqId, String reportName, String part, boolean simplePart) {
		Element e = null;

		Vector<Map> data = getPartData(reportName, part, reqId);
		rememberInParams(data);
		if (data == null || data.size() == 0)
			return null;

		String[] orderedElements = PdfHelper.getOrderedPartElements(reportName, part);
		String partType = PdfHelper.getFromBundle(reportName, part, "type");
		if (partType.equals("!type!") || partType.equals("")) {
			if (data != null && data.size() > 1)
				partType = simplePart?"createAsSimpleTableListWithCaption":"createAsSimpleTableList";
			else
				partType = simplePart?"createAsSimpleTableWithCaption":"createAsSimpleTable";
		}
		String[] pts = partType.split(",");
		Integer columns = 2;
		String cellType = "createVPairParagraph";
		if (pts.length > 1) {
			partType = pts[0];
			columns = Integer.parseInt(pts[1]);
		}
		boolean hasBorder = true;
		if (pts.length >= 3) { // so that border flag is supported
			cellType = pts[2]; // has meaning only for createAsKeyValueTableWithCaption
			try {
				if (partType.startsWith("createAsKeyValueTable"))
					getReport().getParams().put("cellType", cellType);
				if (partType.startsWith("createAsSimpleListOfTables"))
					getReport().getParams().put("tableType", cellType);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try { // read the border flag (if any)
				if (pts.length == 4)
					hasBorder = Boolean.parseBoolean(pts[3]);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		if (!simplePart) { // remove caption (if any) when composite part
			if (partType.indexOf("WithCaption") > -1)
				partType = partType.replace("WithCaption", "");
		}
		e = createDynamicElement(reportName, part, data, orderedElements, partType, columns, hasBorder);

		if (simplePart)
			elements.add(e);
		return e;
	}

	/**
	 * Creates a composite part
	 * @param reqId
	 * @param reportName
	 * @param compPart
	 */
	public void create(int reqId, String reportName, JSONObject compPart) {
		Iterator it =  compPart.keys();
		while (it.hasNext()) {
			String partName = (String)it.next();
			JSONArray parts = compPart.optJSONArray(partName);

			Element e = create(reqId, reportName, partName, false);

			if (e != null) {
				PdfPTable t = (PdfPTable) addTitle(reportName, partName, (PdfPTable) e);
				// loop through all sub parts
				try {
					// so that inner parts know where they belong!
					getReport().getParams().put("mainPart", partName);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				for (int p=0; p<parts.length(); p++) {
					String part = parts.optString(p);
					PdfPart innerPart = getReport().loadPdfPart(reportName, part);
					innerPart.setReport(getReport());
					Element se = innerPart.create(reqId, reportName, part, false);
					Chunk title = createPartTitle(reportName, partName+"."+part, false);
					if (title == null)
						title = createPartTitle(reportName, part, false);
					if (se != null) {
						if (title != null) { // skip title if not defined in property file
							Paragraph pst = new Paragraph( title );
							pst.setAlignment(Element.ALIGN_CENTER);
							t.addCell(createTableCell(true, pst, -1));
						}
						t.addCell(createTableCell(true, se, 0));
					} else {
						String hideIfEmpty = PdfHelper.getFromBundle(reportName, partName+"."+part, "hideIfEmpty");
						if (hideIfEmpty.startsWith("!"))
							hideIfEmpty = PdfHelper.getFromBundle(reportName, part, "hideIfEmpty");
						if (!"true".equals(hideIfEmpty)) {
							Paragraph pst = new Paragraph( (title!=null?title:new Chunk("")) );
							pst.add(new Chunk(": ������"));
	//						pst.setAlignment(Element.ALIGN_CENTER);
							t.addCell(createTableCell(true, pst, -1));
						} else {
							System.out.println("Hidding " + reportName + "." + partName + "." + part);
						}
					}
				}
				// no longer need this
				getReport().getParams().remove("mainPart");
				elements.add(t);
			}

			// only the first key is considered
			break;
		}
	}

	/**
	 * Just a helper to hide the reflection and its exceptions
	 * @param reportName
	 * @param part
	 * @param data
	 * @param orderedElements
	 * @param partType
	 * @param columns
	 * @return
	 */
	protected Element createDynamicElement(String reportName, String part, Vector<Map> data, String[] orderedElements, String partType, Integer columns, boolean hasBorder) {
		Element e = null;
		try {
			e = (Element)TLZUtils.invokeMethod(this, partType, new Object[] {reportName, part, orderedElements, data, columns, hasBorder});
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return e;
	}
	/**
	 * Helper to hide reflection and its exceptions.
	 * @param method
	 * @param cTypes
	 * @param values
	 * @return
	 */
	protected Object invoke(String method, Class[] cTypes, Object[] values) {
		Object p = null;
		try {
			p = (Paragraph)TLZUtils.invokeMethod(this, method, cTypes, values);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return p;
	}

	protected Vector<Map> getPartData(String reportName, String part, int reqId) {
		String query = getQuery(reportName, part);
		if (query.startsWith("!"))
			return null;

		query += reqId;
//		if(query.contains("&ID"))
//			query = StringUtils.addMultipleParameter(query, "&ID", String.valueOf(reqId));
		Vector<Map> data = ExportHelper.getVectorData( query );

		return data;
	}
	protected String getQuery(String reportName, String part) {
		String mainPart = getReport().getParams().optString("mainPart", null);
		if (mainPart != null) // this means we are creating an inner part of a composite part
			mainPart += ".";
		else
			mainPart = "";
		String query = PdfHelper.getFromBundle(reportName, mainPart+part, "query");
		if (query.startsWith("!"))
			query = PdfHelper.getFromBundle(reportName, part, "query");
		return query;
	}

	// Helpers for creating different kind of PDF elements

	public Element createAsSimpleTableWithCaption(String reportName, String part, String[] orderedElements, Vector data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createAsSimpleTable(reportName, part, orderedElements, data, columns, hasBorder);

		return addTitle(reportName, part, table);
	}

	/**
	 * For each data element two columns are needed, one for the caption and one for the value.
	 * Thus the <code>columns</code> parameter is always multiplied by 2 before he table is created.
	 * @param reportName
	 * @param part
	 * @param orderedElements
	 * @param data
	 * @param columns
	 * @param hasBorder
	 * @return
	 */
	public PdfPTable createAsSimpleTable(String reportName, String part, String[] orderedElements, Vector<Map> data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createTable(columns*2);

		Object[] elements = getPartElements(orderedElements, data);
		for (Map map : data) {
			for (Object key : elements) {
				int caption_colspan = 1;
				int value_colspan = 1;
				int caption_rowspan = 1;
				int value_rowspan = 1;
				if (!key.equals("dummy")) { // an element we should add
					if (!key.toString().startsWith("inner_list_")) { // a standard element
						String caption = getCaption(reportName, part, key);
						if (caption.indexOf(",") > -1) {
							// we got colspan
							String[] s = caption.split(","); 
							caption_colspan = Integer.parseInt(s[1]); // caption colspan
							value_colspan = Integer.parseInt(s[2]); // value colspan
							if (s.length == 5) {
								caption_rowspan = Integer.parseInt(s[3]); // caption rowspan
								value_rowspan = Integer.parseInt(s[4]); // value rowspan
							}
							caption = s[0];
						}
						Object value = getValue(map, key);
						String strValue = "";
						if (value != null)
							strValue = convertValue(value, reportName, part, key.toString());
						Chunk c = new Chunk(caption, getFontBold());
						Chunk v = new Chunk(strValue, getFont());

						if (caption_colspan > 0) {
							PdfPCell tc = createTableCell(hasBorder, c, -1, caption_rowspan, caption_colspan);
							table.addCell(tc);
						}
						if (value_colspan > 0) {
							PdfPCell tv = createTableCell(hasBorder, v, -1, value_rowspan, value_colspan);
							alignCellByType(tv, getElementType(reportName, part, key.toString()));
							table.addCell(tv);
						}
		
					} else { // an inner list element, where the value is an underscore separated caption:value pair
						Object value = getValue(map, key);
						String strValue = value.toString();
						String[] values = strValue.split("_");

						for (int i=0; i<values.length; i+=2) {
							String innerC = values[i];
							String innerV = values[i+1];
							String elementType = null;

							caption_colspan = 1;
							value_colspan = 1;
							if (innerC.indexOf(",") > -1) {
								// we got colspan
								String[] s = innerC.split(","); 
								caption_colspan = Integer.parseInt(s[1]);
								value_colspan = Integer.parseInt(s[2]);
								innerC = s[0];
								if (s.length == 4) { // we also got element type!
									elementType = s[3];
									innerV = convert(innerV, s[3]);
								}
							}

							Chunk c = new Chunk(innerC, getFontBold());
							Chunk v = new Chunk(innerV, getFont());
							PdfPCell tc = createTableCell(hasBorder, c, -1, 1, caption_colspan);
							PdfPCell tv = createTableCell(hasBorder, v, -1, 1, value_colspan);
							alignCellByType(tv, elementType);
							
							table.addCell(tc);
							table.addCell(tv);
						}
					}
				} else {
					// just add a spanned place holder
					PdfPCell dummy = createTableCell(hasBorder, new Chunk(""), -1, 1, 2);
					
					table.addCell(dummy);
				}
			}
		}
		table.completeRow(); // to make sure every row is shown
		return table;
	}
	public Element createAsKeyValueTableWithCaption(String reportName, String part, String[] orderedElements, Vector<Map> data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createAsKeyValueTable(reportName, part, orderedElements, data, columns, hasBorder);

		return addTitle(reportName, part, table);
	}
	public PdfPTable createAsKeyValueTable(String reportName, String part, String[] orderedElements, Vector<Map> data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createTable(columns);

		String cellType = getReport().getParams().optString("cellType", "createVPairParagraph");
		Object[] elements = getPartElements(orderedElements, data);
		for (Map map : data) {
			for (Object key : elements) {
				int caption_colspan = 1;
				int value_colspan = 1;
				int caption_rowspan = 1;
				int value_rowspan = 1;
				if (!key.equals("dummy")) { // an element we should add
					if (!key.toString().startsWith("inner_list_")) { // a standard element
						String caption = getCaption(reportName, part, key);
						if (caption.indexOf(",") > -1) {
							// we got colspan
							String[] s = caption.split(","); 
							caption_colspan = Integer.parseInt(s[1]); // caption colspan
							value_colspan = Integer.parseInt(s[2]); // value colspan
							if (s.length == 5) {
								caption_rowspan = Integer.parseInt(s[3]); // caption rowspan
								value_rowspan = Integer.parseInt(s[4]); // value rowspan
							}
							caption = s[0];
						}
						Object value = getValue(map, key);
						String strValue = "";
						if (value != null)
							strValue = convertValue(value, reportName, part, key.toString());
						Paragraph p = null;//createVPairParagraph(caption, strValue, getFontBold(), getFont(), Element.ALIGN_LEFT);

						p = (Paragraph) invoke(cellType, new Class[] {String.class, String.class, Font.class, Font.class, Integer.class}, new Object[] {caption, strValue, getFontBold(), getFont(), Element.ALIGN_LEFT});

						PdfPCell tc = createTableCell(hasBorder, p, -1, caption_rowspan, caption_colspan);
						table.addCell(tc);
					} else { // an inner list element, where the value is an underscore separated caption:value pair
						Object value = getValue(map, key);
						String strValue = value.toString();
						String[] values = strValue.split("_");

						for (int i=0; i<values.length; i+=2) {
							String innerC = values[i];
							String innerV = values[i+1];
							String elementType = null;

							caption_colspan = 1;
							value_colspan = 1;
							if (innerC.indexOf(",") > -1) {
								// we got colspan
								String[] s = innerC.split(","); 
								caption_colspan = Integer.parseInt(s[1]);
								value_colspan = Integer.parseInt(s[2]);
								innerC = s[0];
								if (s.length == 4) { // we also got element type!
									elementType = s[3];
									innerV = convert(innerV, s[3]);
								}
							}

							Paragraph p = null;//createVPairParagraph(innerC, innerV, getFontBold(), getFont(), Element.ALIGN_LEFT);

							p = (Paragraph) invoke(cellType, new Class[] {String.class, String.class, Font.class, Font.class, Integer.class}, new Object[] {innerC, innerV, getFontBold(), getFont(), Element.ALIGN_LEFT});

							PdfPCell tc = createTableCell(hasBorder, p, -1, 1, caption_colspan);
							table.addCell(tc);
						}
					}
				} else {
					// just add a spanned place holder
					PdfPCell dummy = createTableCell(hasBorder, new Chunk(""), -1, 1, 1);
					
					table.addCell(dummy);
				}
			}
		}
		table.completeRow(); // to make sure every row is shown
		getReport().getParams().remove("cellType");
		return table;
	}

	public Element createAsSimpleTableListWithCaption(String reportName, String part, String[] orderedElements, Vector data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createAsSimpleTableList(reportName, part, orderedElements, data, columns, hasBorder);

		return addTitle(reportName, part, table);
	}
	public PdfPTable createAsSimpleTableList(String reportName, String part, String[] orderedElements, Vector<Map> data, Integer columns, Boolean hasBorder) {
		Object[] elements = getPartElements(orderedElements, data);

		columns = getTotalSize(reportName, part, elements);
		PdfPTable table = createTable(columns);

		int row = 0;

		for (Map map : data) {
			if (row == 0) {
				// first row is the header row!
				for (Object key : elements) {
					int caption_colspan = 1;
					int value_colspan = 1;
					if (!key.equals("dummy")) {
						String caption = getCaption(reportName, part, key);
						if (caption.indexOf(",") > -1) {
							// we got colspan
							String[] s = caption.split(","); 
							caption_colspan = Integer.parseInt(s[1]); // caption colspan
							value_colspan = Integer.parseInt(s[2]); // value colspan
							caption = s[0];
						}
						Chunk c = new Chunk(caption, getFontBold());
						PdfPCell tc = createTableCell(hasBorder, c, -1, 1, caption_colspan);
						table.addCell(tc);
					} else {
						// just add a spanned place holder
						PdfPCell dummy = createTableCell(hasBorder, new Chunk(""), -1, 1, caption_colspan);
						
						table.addCell(dummy);
					}
				}
			}
			for (Object key : elements) {
				int caption_colspan = 1;
				int value_colspan = 1;
				if (!key.equals("dummy")) {
					String caption = getCaption(reportName, part, key);
					if (caption.indexOf(",") > -1) {
						// we got colspan
						String[] s = caption.split(","); 
						caption_colspan = Integer.parseInt(s[1]); // caption colspan
						value_colspan = Integer.parseInt(s[2]); // value colspan
						caption = s[0];
					}
					Object value = getValue(map, key);
					String strValue = "";
					if (value != null)
						strValue = convertValue(value, reportName, part, key.toString());
	
					Chunk v = new Chunk(strValue, getFont());
					PdfPCell tv = createTableCell(hasBorder, v, -1, 1, value_colspan);
					alignCellByType(tv, getElementType(reportName, part, key.toString()) );
					table.addCell(tv);
				} else {
					// just add a spanned place holder
					PdfPCell dummy = createTableCell(hasBorder, new Chunk(""), -1);
					
					table.addCell(dummy);
				}
			}
			row++;
		}
		return table;
	}

	public Element createAsSimpleListOfTablesWithCaption(String reportName, String part, String[] orderedElements, Vector<Map> data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createAsSimpleListOfTables(reportName, part, orderedElements, data, columns, false);

		return addTitle(reportName, part, table);
	}
	public PdfPTable createAsSimpleListOfTables(String reportName, String part, String[] orderedElements, Vector<Map> data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createTable(1);

		String tableType = getReport().getParams().optString("tableType", "createAsSimpleTable");

		int total = data.size();
		int count = 0;
		for (Map map : data) {
			count++;
			Vector<Map> _data = new Vector<Map>();
			_data.add(map);
			Element e = createDynamicElement(reportName, part, _data, orderedElements, tableType, columns, true);
			table.addCell(createTableCell(false, e, 0));
			if (count<total)
				table.addCell(createTableCell(false, createChunk(" ", font),0)); // just to add space between tables
		}
		getReport().getParams().remove("tableType");

		return table;
	}

	public Element createAsListOfHorizontalPairsWithCaption(String reportName, String part, String[] orderedElements, Vector<Map> data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createAsListOfHorizontalPairs(reportName, part, orderedElements, data, columns, false);

		return addTitle(reportName, part, table);
	}
	public PdfPTable createAsListOfHorizontalPairs(String reportName, String part, String[] orderedElements, Vector<Map> data, Integer columns, Boolean hasBorder) {
		PdfPTable table = createTable(1);

		int spacingBefore = 0;
		int spacingAfter = 0;

		String partType = PdfHelper.getFromBundle(reportName, part, "type");
		String[] pts = partType.split(",");

		Object[] elements = getPartElements(orderedElements, data);
		for (Map map : data) {
			for (Object key : elements) {
				if (pts.length == 3) { // reset spacing parameters to default values
					spacingBefore = Integer.parseInt(pts[1]);
					spacingAfter = Integer.parseInt(pts[2]);
				}
				String caption = "";
				String strValue = "";

				if (!key.toString().startsWith("inner_list_")) { // a standard element
					caption = getCaption(reportName, part, key);
					if (caption.indexOf(",") > -1) {
						// we got spacing before and after
						String[] s = caption.split(","); 
						caption = s[0];
						spacingBefore = Integer.parseInt(s[1]);
						spacingAfter = Integer.parseInt(s[2]);
					}
					Object value = getValue(map, key);
					if (value != null)
						strValue = convertValue(value, reportName, part, key.toString());

					Paragraph p = createHPairParagraph(caption, strValue, getFontBold(), getFont(), Element.ALIGN_LEFT);
					p.setSpacingBefore(spacingBefore);
					p.setSpacingAfter(spacingAfter);
					PdfPCell cell = createTableCell(false, p, -1, 1, 1);
					
					table.addCell(cell);
				} else {
					Object value = getValue(map, key);
					strValue = value.toString();
					String[] values = strValue.split("_");

					for (int i=0; i<values.length; i+=2) {
						String innerC = values[i];
						String innerV = values[i+1];

						if (innerC.indexOf(",") > -1) {
							// we got spacing before and after
							String[] s = innerC.split(","); 
							spacingBefore = Integer.parseInt(s[1]);
							spacingAfter = Integer.parseInt(s[2]);
							innerC = s[0];
							if (s.length == 4) { // we also got element type!
								innerV = convert(innerV, s[3]);
							}
						}

						Paragraph p = createHPairParagraph(innerC, innerV, getFontBold(), getFont(), Element.ALIGN_LEFT);
						p.setSpacingBefore(spacingBefore);
						p.setSpacingAfter(spacingAfter);
						PdfPCell cell = createTableCell(false, p, -1, 1, 1);
						
						table.addCell(cell);
					}
				}
			}
		}
		return table;
	}

	/**
	 * Retrieves the caption of a key-value pair. Takes into account the mainPart (if we are currently creating an inner component)
	 * @param reportName
	 * @param part
	 * @param key
	 * @return
	 */
	protected String getCaption(String reportName, String part, Object key) {
		String mainPart = getReport().getParams().optString("mainPart", null);
		if (mainPart != null) // this means we are creating an inner part of a composite part
			mainPart += ".";
		else
			mainPart = "";
		String caption = PdfHelper.getFromBundle(reportName, mainPart+part, key.toString());
		if (caption.startsWith("!"))
			caption = PdfHelper.getFromBundle(reportName, part, key.toString());

		return caption;
	}

	/**
	 * Retrieves the value related to the given key from the given map. The map is an element of the Vector containing the query results.<br>
	 * If value == null then the same key is used in params stored in main Report object
	 * @param map
	 * @param key
	 * @return
	 */
	protected Object getValue(Map map, Object key) {
		Object value = map.get(key);
		if (value == null) { // try getting it from params in Report object
			try {
				value = getReport().getParams().get(key.toString());
			} catch (JSONException e) {
			}
		}
		return value;
	}
}
