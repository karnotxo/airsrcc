package com.indra.srcc.airsrcc.client.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import com.google.common.collect.ImmutableList;

public class IconTool {

	protected static final Color transparent = new Color(0f,0f,0f, 0.0f );
	
	public static ImageIcon getIconMultiResolution(final int basesize, final String svgfilepath) throws IOException, TranscoderException {
		return getIconMultiResolution(basesize, svgfilepath, transparent);
	}
		
	public static ImageIcon getIconMultiResolution(final int basesize, final String svgfilepath, final Color color) throws IOException, TranscoderException {
		
		boolean colorize = (color.getAlpha() > 0); 
		final HSLColor hslcolor = new HSLColor(color); 
		
		 // Create a multi-resolution image with all 0.25 scaling steps up to 3x
		 // base size = 16, 24, 32 etc.

		  // Create all resolution variants that Windows 10 offers by default
		  // Could probably drop some, e.g. 1.25 = 2.50 / 2 (Swing should handle that...)
		  final List< Integer > sizes = ImmutableList.of(
		      (int) ( basesize * 1.00 ), // Base image
		      (int) ( basesize * 1.25 ),
		      (int) ( basesize * 1.50 ),
		      (int) ( basesize * 1.75 ),
		      (int) ( basesize * 2.00 ),
		      (int) ( basesize * 2.25 ),
		      (int) ( basesize * 2.50 ),
		      (int) ( basesize * 2.75 ),
		      (int) ( basesize * 3.00 ) );

		  byte[] rawSvgBytes = IconTool.class.getClassLoader().getResourceAsStream(svgfilepath).readAllBytes();
		 // Read bytes from SVG file

		  Image[] images = new Image[ sizes.size() ];
		  for ( int isize = 0; isize < sizes.size(); isize++ )
		  {
		    // Create a PNG transcoder
		    PNGTranscoder t = new PNGTranscoder();

		    // Set the transcoding hints
		    t.addTranscodingHint( SVGAbstractTranscoder.KEY_WIDTH, Float.valueOf( sizes.get( isize ) ) );
		    t.addTranscodingHint( SVGAbstractTranscoder.KEY_HEIGHT, Float.valueOf( sizes.get( isize ) ) );

		    // Create the transcoder input
		    TranscoderInput input = new TranscoderInput();
		    input.setInputStream( new ByteArrayInputStream( rawSvgBytes ) );
		    // Create the transcoder output
		    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		    TranscoderOutput output = new TranscoderOutput( ostream );

		    // Transcode the image
		    t.transcode( input, output );

		    // Create an image and ensure its size is initialised
		    Image image = Toolkit.getDefaultToolkit().createImage( ostream.toByteArray() );
		  		    
		    if (colorize) {
		    	ImageFilter filter = new RGBImageFilter() {

		            @Override
		            public final int filterRGB(int x, int y, int rgb) {
		            	return (color.getRGB()&0xFFFFFF)|(rgb&0xFF000000);
		                /*HSLColor newColor = new HSLColor(new Color(rgb, true));
		                newColor = new HSLColor(newColor.adjustHue(hslcolor.getHue()));
		                newColor = new HSLColor(newColor.adjustSaturation(hslcolor.getSaturation()));
		                newColor = new HSLColor(newColor.adjustLuminance(hslcolor.getLuminance()));
		                return newColor.getRGB().getRGB();*/
		            }
		        };

		        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
		        image = Toolkit.getDefaultToolkit().createImage(ip);
		    	
		    }
		    while ( image.getWidth( null ) == -1 )
		    {
		      // HACK! Wait for the image to be loaded, else icons may not render at the correct
		      // location as the width and height returned to Swing are -1
		    }
		    
		    images[ isize ] = image;
		  }
		  return new ImageIcon( new BaseMultiResolutionImage( images ) ); 
	}
	
    static private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
}
