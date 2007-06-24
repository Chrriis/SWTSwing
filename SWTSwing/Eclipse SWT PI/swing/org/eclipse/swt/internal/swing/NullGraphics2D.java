/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class NullGraphics2D extends Graphics2D {
  
  protected Font systemFont;
  
  public NullGraphics2D(Font systemFont) {
    this.systemFont = systemFont;
  }
  
  // TODO: save the attributes for the copyAttributes()
  public void addRenderingHints(Map arg0) {
  }
  public void clip(Shape s) {
  }
  public void draw(Shape s) {
  }
  public void drawGlyphVector(GlyphVector g, float x, float y) {
  }
  public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
  }
  public boolean drawImage(java.awt.Image img, AffineTransform xform, ImageObserver obs) {
    return true;
  }
  public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
  }
  public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
  }
  public void drawString(AttributedCharacterIterator iterator, float x, float y) {
  }
  public void drawString(AttributedCharacterIterator iterator, int x, int y) {
  }
  public void drawString(String s, float x, float y) {
  }
  public void drawString(String str, int x, int y) {
  }
  public void fill(Shape s) {
  }
  protected java.awt.Color background = java.awt.Color.BLACK;
  public java.awt.Color getBackground() {
    return background;
  }
  public java.awt.Composite getComposite() {
    return null;
  }
  public GraphicsConfiguration getDeviceConfiguration() {
    return null;
  }
  public FontRenderContext getFontRenderContext() {
    return null;
  }
  public Paint getPaint() {
    return null;
  }
  public Object getRenderingHint(Key hintKey) {
    return null;
  }
  public RenderingHints getRenderingHints() {
    return null;
  }
  public Stroke getStroke() {
    return null;
  }
  public AffineTransform getTransform() {
    return null;
  }
  public boolean hit(java.awt.Rectangle rect, Shape s, boolean onStroke) {
    return false;
  }
  public void rotate(double theta, double x, double y) {
  }
  public void rotate(double theta) {
  }
  public void scale(double sx, double sy) {
  }
  public void setBackground(java.awt.Color color) {
    background = color;
  }
  public void setComposite(java.awt.Composite comp) {
  }
  public void setPaint(Paint paint) {
  }
  public void setRenderingHint(Key hintKey, Object hintValue) {
  }
  public void setRenderingHints(Map arg0) {
  }
  public void setStroke(Stroke s) {
  }
  public void setTransform(AffineTransform Tx) {
  }
  public void shear(double shx, double shy) {
  }
  public void transform(AffineTransform Tx) {
  }
  public void translate(double tx, double ty) {
  }
  public void translate(int x, int y) {
  }
  public void clearRect(int x, int y, int width, int height) {
  }
  public void clipRect(int x, int y, int width, int height) {
  }
  public void copyArea(int x, int y, int width, int height, int dx, int dy) {
  }
  public Graphics create() {
    return this;
  }
  public void dispose() {
  }
  public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
  }
  public boolean drawImage(java.awt.Image img, int x, int y, java.awt.Color bgcolor, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(java.awt.Image img, int x, int y, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(java.awt.Image img, int x, int y, int width, int height, java.awt.Color bgcolor, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(java.awt.Image img, int x, int y, int width, int height, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, java.awt.Color bgcolor, ImageObserver observer) {
    return true;
  }
  public boolean drawImage(java.awt.Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
    return true;
  }
  public void drawLine(int x1, int y1, int x2, int y2) {
  }
  public void drawOval(int x, int y, int width, int height) {
  }
  public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
  }
  public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
  }
  public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
  }
  public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
  }
  public void fillOval(int x, int y, int width, int height) {
  }
  public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
  }
  public void fillRect(int x, int y, int width, int height) {
  }
  public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
  }
  public Shape getClip() {
    return null;
  }
  public java.awt.Rectangle getClipBounds() {
    return null;
  }
  public java.awt.Color getColor() {
    return null;
  }
  protected java.awt.Font font;
  public java.awt.Font getFont() {
    if(font == null) {
      return systemFont;
    }
    return font;
  }
  public java.awt.FontMetrics getFontMetrics(java.awt.Font f) {
    return Toolkit.getDefaultToolkit().getFontMetrics(f);
  }
  public void setClip(int x, int y, int width, int height) {
  }
  public void setClip(Shape clip) {
  }
  public void setColor(java.awt.Color c) {
  }
  public void setFont(java.awt.Font font) {
    this.font = font;
  }
  public void setPaintMode() {
  }
  public void setXORMode(java.awt.Color c1) {
  }
}
