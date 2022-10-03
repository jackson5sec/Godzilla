/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.app.beans.SVGIcon;
/*     */ import com.kitfox.svg.util.Base64InputStream;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SVGUniverse
/*     */   implements Serializable
/*     */ {
/*     */   public static final long serialVersionUID = 0L;
/*  85 */   private transient PropertyChangeSupport changes = new PropertyChangeSupport(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   final HashMap<URI, SVGDiagram> loadedDocs = new HashMap<URI, SVGDiagram>();
/*  92 */   final HashMap<String, Font> loadedFonts = new HashMap<String, Font>();
/*  93 */   final HashMap<URL, SoftReference<BufferedImage>> loadedImages = new HashMap<URL, SoftReference<BufferedImage>>();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String INPUTSTREAM_SCHEME = "svgSalamander";
/*     */ 
/*     */   
/* 100 */   protected double curTime = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean verbose = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean imageDataInlineOnly = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener l) {
/* 115 */     this.changes.addPropertyChangeListener(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener l) {
/* 120 */     this.changes.removePropertyChangeListener(l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 128 */     this.loadedDocs.clear();
/* 129 */     this.loadedFonts.clear();
/* 130 */     this.loadedImages.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCurTime() {
/* 138 */     return this.curTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurTime(double curTime) {
/* 143 */     double oldTime = this.curTime;
/* 144 */     this.curTime = curTime;
/* 145 */     this.changes.firePropertyChange("curTime", new Double(oldTime), new Double(curTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateTime() throws SVGException {
/* 154 */     for (SVGDiagram dia : this.loadedDocs.values()) {
/* 155 */       dia.updateTime(this.curTime);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void registerFont(Font font) {
/* 165 */     this.loadedFonts.put(font.getFontFace().getFontFamily(), font);
/*     */   }
/*     */ 
/*     */   
/*     */   public Font getDefaultFont() {
/* 170 */     Iterator<Font> iterator = this.loadedFonts.values().iterator(); if (iterator.hasNext()) { Font font = iterator.next();
/* 171 */       return font; }
/*     */     
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Font getFont(String fontName) {
/* 178 */     return this.loadedFonts.get(fontName);
/*     */   }
/*     */ 
/*     */   
/*     */   URL registerImage(URI imageURI) {
/* 183 */     String scheme = imageURI.getScheme();
/* 184 */     if (scheme.equals("data")) {
/*     */       
/* 186 */       String path = imageURI.getRawSchemeSpecificPart();
/* 187 */       int idx = path.indexOf(';');
/* 188 */       String mime = path.substring(0, idx);
/* 189 */       String content = path.substring(idx + 1);
/*     */       
/* 191 */       if (content.startsWith("base64")) {
/*     */         
/* 193 */         content = content.substring(6);
/*     */         
/*     */         try {
/*     */           URL url;
/*     */           
/* 198 */           ByteArrayInputStream bis = new ByteArrayInputStream(content.getBytes());
/* 199 */           Base64InputStream bais = new Base64InputStream(bis);
/*     */           
/* 201 */           BufferedImage img = ImageIO.read((InputStream)bais);
/*     */ 
/*     */           
/* 204 */           int urlIdx = 0;
/*     */           
/*     */           while (true) {
/* 207 */             url = new URL("inlineImage", "localhost", "img" + urlIdx);
/* 208 */             if (!this.loadedImages.containsKey(url)) {
/*     */               break;
/*     */             }
/*     */             
/* 212 */             urlIdx++;
/*     */           } 
/*     */           
/* 215 */           SoftReference<BufferedImage> ref = new SoftReference<BufferedImage>(img);
/* 216 */           this.loadedImages.put(url, ref);
/*     */           
/* 218 */           return url;
/* 219 */         } catch (IOException ex) {
/*     */           
/* 221 */           Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not decode inline image", ex);
/*     */         } 
/*     */       } 
/*     */       
/* 225 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 230 */       URL url = imageURI.toURL();
/* 231 */       registerImage(url);
/* 232 */       return url;
/* 233 */     } catch (MalformedURLException ex) {
/*     */       
/* 235 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Bad url", ex);
/*     */ 
/*     */       
/* 238 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void registerImage(URL imageURL) {
/* 244 */     if (this.loadedImages.containsKey(imageURL)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*     */       SoftReference<BufferedImage> ref;
/*     */       
/* 252 */       String fileName = imageURL.getFile();
/* 253 */       if (".svg".equals(fileName.substring(fileName.length() - 4).toLowerCase())) {
/*     */         
/* 255 */         SVGIcon icon = new SVGIcon();
/* 256 */         icon.setSvgURI(imageURL.toURI());
/*     */         
/* 258 */         BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), 2);
/* 259 */         Graphics2D g = img.createGraphics();
/* 260 */         icon.paintIcon(null, g, 0, 0);
/* 261 */         g.dispose();
/* 262 */         ref = new SoftReference<BufferedImage>(img);
/*     */       } else {
/*     */         
/* 265 */         BufferedImage img = ImageIO.read(imageURL);
/* 266 */         ref = new SoftReference<BufferedImage>(img);
/*     */       } 
/* 268 */       this.loadedImages.put(imageURL, ref);
/* 269 */     } catch (Exception e) {
/*     */       
/* 271 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not load image: " + imageURL, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   BufferedImage getImage(URL imageURL) {
/* 278 */     SoftReference<BufferedImage> ref = this.loadedImages.get(imageURL);
/* 279 */     if (ref == null)
/*     */     {
/* 281 */       return null;
/*     */     }
/*     */     
/* 284 */     BufferedImage img = ref.get();
/*     */     
/* 286 */     if (img == null) {
/*     */ 
/*     */       
/*     */       try {
/* 290 */         img = ImageIO.read(imageURL);
/* 291 */       } catch (Exception e) {
/*     */         
/* 293 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not load image", e);
/*     */       } 
/*     */       
/* 296 */       ref = new SoftReference<BufferedImage>(img);
/* 297 */       this.loadedImages.put(imageURL, ref);
/*     */     } 
/*     */     
/* 300 */     return img;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGElement getElement(URI path) {
/* 309 */     return getElement(path, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGElement getElement(URL path) {
/*     */     try {
/* 316 */       URI uri = new URI(path.toString());
/* 317 */       return getElement(uri, true);
/* 318 */     } catch (Exception e) {
/*     */       
/* 320 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse url " + path, e);
/*     */ 
/*     */       
/* 323 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGElement getElement(URI path, boolean loadIfAbsent) {
/*     */     try {
/* 337 */       URI xmlBase = new URI(path.getScheme(), path.getSchemeSpecificPart(), null);
/*     */       
/* 339 */       SVGDiagram dia = this.loadedDocs.get(xmlBase);
/* 340 */       if (dia == null && loadIfAbsent) {
/*     */ 
/*     */ 
/*     */         
/* 344 */         URL url = xmlBase.toURL();
/*     */         
/* 346 */         loadSVG(url, false);
/* 347 */         dia = this.loadedDocs.get(xmlBase);
/* 348 */         if (dia == null)
/*     */         {
/* 350 */           return null;
/*     */         }
/*     */       } 
/*     */       
/* 354 */       String fragment = path.getFragment();
/* 355 */       return (fragment == null) ? dia.getRoot() : dia.getElement(fragment);
/* 356 */     } catch (Exception e) {
/*     */       
/* 358 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse path " + path, e);
/*     */       
/* 360 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGDiagram getDiagram(URI xmlBase) {
/* 366 */     return getDiagram(xmlBase, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGDiagram getDiagram(URI xmlBase, boolean loadIfAbsent) {
/* 375 */     if (xmlBase == null)
/*     */     {
/* 377 */       return null;
/*     */     }
/*     */     
/* 380 */     SVGDiagram dia = this.loadedDocs.get(xmlBase);
/* 381 */     if (dia != null || !loadIfAbsent)
/*     */     {
/* 383 */       return dia;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*     */       URL url;
/*     */       
/* 390 */       if ("jar".equals(xmlBase.getScheme()) && xmlBase.getPath() != null && !xmlBase.getPath().contains("!/")) {
/*     */ 
/*     */ 
/*     */         
/* 394 */         url = SVGUniverse.class.getResource(xmlBase.getPath());
/*     */       }
/*     */       else {
/*     */         
/* 398 */         url = xmlBase.toURL();
/*     */       } 
/*     */ 
/*     */       
/* 402 */       loadSVG(url, false);
/* 403 */       dia = this.loadedDocs.get(xmlBase);
/* 404 */       return dia;
/* 405 */     } catch (Exception e) {
/*     */       
/* 407 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse", e);
/*     */ 
/*     */ 
/*     */       
/* 411 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputStream createDocumentInputStream(InputStream is) throws IOException {
/* 424 */     BufferedInputStream bin = new BufferedInputStream(is);
/* 425 */     bin.mark(2);
/* 426 */     int b0 = bin.read();
/* 427 */     int b1 = bin.read();
/* 428 */     bin.reset();
/*     */ 
/*     */     
/* 431 */     if ((b1 << 8 | b0) == 35615) {
/*     */       
/* 433 */       GZIPInputStream iis = new GZIPInputStream(bin);
/* 434 */       return iis;
/*     */     } 
/*     */ 
/*     */     
/* 438 */     return bin;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URI loadSVG(URL docRoot) {
/* 444 */     return loadSVG(docRoot, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI loadSVG(URL docRoot, boolean forceLoad) {
/*     */     try {
/* 460 */       URI uri = new URI(docRoot.toString());
/* 461 */       if (this.loadedDocs.containsKey(uri) && !forceLoad)
/*     */       {
/* 463 */         return uri;
/*     */       }
/*     */       
/* 466 */       InputStream is = docRoot.openStream();
/* 467 */       URI result = loadSVG(uri, new InputSource(createDocumentInputStream(is)));
/* 468 */       is.close();
/* 469 */       return result;
/* 470 */     } catch (URISyntaxException ex) {
/*     */       
/* 472 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse", ex);
/*     */     }
/* 474 */     catch (IOException e) {
/*     */       
/* 476 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse", e);
/*     */     } 
/*     */ 
/*     */     
/* 480 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public URI loadSVG(InputStream is, String name) throws IOException {
/* 485 */     return loadSVG(is, name, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public URI loadSVG(InputStream is, String name, boolean forceLoad) throws IOException {
/* 490 */     URI uri = getStreamBuiltURI(name);
/* 491 */     if (uri == null)
/*     */     {
/* 493 */       return null;
/*     */     }
/* 495 */     if (this.loadedDocs.containsKey(uri) && !forceLoad)
/*     */     {
/* 497 */       return uri;
/*     */     }
/*     */     
/* 500 */     return loadSVG(uri, new InputSource(createDocumentInputStream(is)));
/*     */   }
/*     */ 
/*     */   
/*     */   public URI loadSVG(Reader reader, String name) {
/* 505 */     return loadSVG(reader, name, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI loadSVG(Reader reader, String name, boolean forceLoad) {
/* 538 */     URI uri = getStreamBuiltURI(name);
/* 539 */     if (uri == null)
/*     */     {
/* 541 */       return null;
/*     */     }
/* 543 */     if (this.loadedDocs.containsKey(uri) && !forceLoad)
/*     */     {
/* 545 */       return uri;
/*     */     }
/*     */     
/* 548 */     return loadSVG(uri, new InputSource(reader));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getStreamBuiltURI(String name) {
/* 558 */     if (name == null || name.length() == 0)
/*     */     {
/* 560 */       return null;
/*     */     }
/*     */     
/* 563 */     if (name.charAt(0) != '/')
/*     */     {
/* 565 */       name = '/' + name;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 571 */       return new URI("svgSalamander", name, null);
/* 572 */     } catch (Exception e) {
/*     */       
/* 574 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not parse", e);
/*     */       
/* 576 */       return null;
/*     */     } 
/*     */   }
/*     */   
/* 580 */   static ThreadLocal<SAXParser> threadSAXParser = new ThreadLocal<SAXParser>();
/*     */ 
/*     */   
/*     */   private XMLReader getXMLReader() throws SAXException, ParserConfigurationException {
/* 584 */     SAXParser saxParser = threadSAXParser.get();
/* 585 */     if (saxParser == null) {
/*     */       
/* 587 */       SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
/* 588 */       saxParserFactory.setNamespaceAware(true);
/* 589 */       saxParser = saxParserFactory.newSAXParser();
/* 590 */       threadSAXParser.set(saxParser);
/*     */     } 
/* 592 */     return saxParser.getXMLReader();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected URI loadSVG(URI xmlBase, InputSource is) {
/* 598 */     SVGLoader handler = new SVGLoader(xmlBase, this, this.verbose);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 603 */     this.loadedDocs.put(xmlBase, handler.getLoadedDiagram());
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 608 */       XMLReader reader = getXMLReader();
/* 609 */       reader.setEntityResolver(new EntityResolver()
/*     */           {
/*     */ 
/*     */             
/*     */             public InputSource resolveEntity(String publicId, String systemId)
/*     */             {
/* 615 */               return new InputSource(new ByteArrayInputStream(new byte[0]));
/*     */             }
/*     */           });
/* 618 */       reader.setContentHandler(handler);
/* 619 */       reader.parse(is);
/*     */       
/* 621 */       handler.getLoadedDiagram().updateTime(this.curTime);
/* 622 */       return xmlBase;
/* 623 */     } catch (SAXParseException sex) {
/*     */       
/* 625 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Error processing " + xmlBase, sex);
/*     */ 
/*     */       
/* 628 */       this.loadedDocs.remove(xmlBase);
/* 629 */       return null;
/* 630 */     } catch (Throwable e) {
/*     */       
/* 632 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Could not load SVG " + xmlBase, e);
/*     */ 
/*     */ 
/*     */       
/* 636 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<URI> getLoadedDocumentURIs() {
/* 645 */     return new ArrayList<URI>(this.loadedDocs.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeDocument(URI uri) {
/* 654 */     this.loadedDocs.remove(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVerbose() {
/* 659 */     return this.verbose;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 664 */     this.verbose = verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGUniverse duplicate() throws IOException, ClassNotFoundException {
/* 672 */     ByteArrayOutputStream bs = new ByteArrayOutputStream();
/* 673 */     ObjectOutputStream os = new ObjectOutputStream(bs);
/* 674 */     os.writeObject(this);
/* 675 */     os.close();
/*     */     
/* 677 */     ByteArrayInputStream bin = new ByteArrayInputStream(bs.toByteArray());
/* 678 */     ObjectInputStream is = new ObjectInputStream(bin);
/* 679 */     SVGUniverse universe = (SVGUniverse)is.readObject();
/* 680 */     is.close();
/*     */     
/* 682 */     return universe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isImageDataInlineOnly() {
/* 690 */     return this.imageDataInlineOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImageDataInlineOnly(boolean imageDataInlineOnly) {
/* 698 */     this.imageDataInlineOnly = imageDataInlineOnly;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\SVGUniverse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */