package com.tooooolazy.util.barcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoder;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoderRegistry;
import org.krysalis.barcode4j.tools.UnitConv;

public class BarCodeGenerator {
    public static void generate(String msg) throws IOException {
//        String[] paramArr = new String[] {"0", "61400", "2240000000"};

        //Create the barcode bean
        
        Code128Bean bean = new Code128Bean();
        File outputFile = new File("c:/temp/"+bean.getClass().getSimpleName() + ".png");

        final int dpi = 200;

        //Configure the barcode generator
        bean.setModuleWidth(UnitConv.in2mm(8.0f / dpi)); //makes a dot/module exactly eight pixels
        bean.setModuleWidth(.25); //makes a dot/module exactly eight pixels
        
        bean.doQuietZone(false);
//        bean.setShape(SymbolShapeHint.FORCE_RECTANGLE);

        boolean antiAlias = false;
        int orientation = 0;
        //Set up the canvas provider to create a monochrome bitmap
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                dpi, BufferedImage.TYPE_BYTE_BINARY, antiAlias, orientation);

        //Generate the barcode
        bean.generateBarcode(canvas, msg);

        //Signal end of generation
        canvas.finish();

        //Get generated bitmap
        BufferedImage symbol = canvas.getBufferedImage();

        int fontSize = 32; //pixels
        int lineHeight = (int)(fontSize * 1.2);
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        int width = symbol.getWidth();
        int height = symbol.getHeight();
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), antiAlias, true);
//        for (int i = 0; i < paramArr.length; i++) {
//            String line = paramArr[i];
//            Rectangle2D bounds = font.getStringBounds(line, frc);
//            width = (int)Math.ceil(Math.max(width, bounds.getWidth()));
//            height += lineHeight;
//        }

        //Add padding
        int padding = 2;
        width += 2 * padding;
        height += 3 * padding;

        BufferedImage bitmap = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = (Graphics2D)bitmap.getGraphics();
        g2d.setBackground(Color.white);
        g2d.setColor(Color.black);
        g2d.clearRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        g2d.setFont(font);

        //Place the barcode symbol
        AffineTransform symbolPlacement = new AffineTransform();
        symbolPlacement.translate(padding, padding);
        g2d.drawRenderedImage(symbol, symbolPlacement);

        //Add text lines (or anything else you might want to add)
        int y = padding + symbol.getHeight() + padding;
//        for (int i = 0; i < paramArr.length; i++) {
//            String line = paramArr[i];
//            y += lineHeight;
//            g2d.drawString(line, padding, y);
//        }
        g2d.dispose();

        //Encode bitmap as file
        String mime = "image/png";
        OutputStream out = new FileOutputStream(outputFile);
        try {
            final BitmapEncoder encoder = BitmapEncoderRegistry.getInstance(mime);
            encoder.encode(bitmap, out, mime, dpi);
        } finally {
            out.close();
        }
    }
    public static void main(String[] args) {
		try {
			generate("0614002240000000");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
