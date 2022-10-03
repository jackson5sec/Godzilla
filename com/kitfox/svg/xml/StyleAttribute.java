/*     */ package com.kitfox.svg.xml;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Toolkit;
/*     */ import java.io.File;
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class StyleAttribute
/*     */   implements Serializable
/*     */ {
/*     */   public static final long serialVersionUID = 0L;
/*  55 */   static final Pattern patternUrl = Pattern.compile("\\s*url\\((.*)\\)\\s*");
/*  56 */   static final Matcher matchFpNumUnits = Pattern.compile("\\s*([-+]?((\\d*\\.\\d+)|(\\d+))([-+]?[eE]\\d+)?)\\s*(px|cm|mm|in|pc|pt|em|ex)\\s*").matcher("");
/*     */   
/*     */   String name;
/*     */   
/*     */   String stringValue;
/*     */   
/*     */   boolean colorCompatable = false;
/*     */   
/*     */   boolean urlCompatable = false;
/*     */   
/*     */   public StyleAttribute() {
/*  67 */     this(null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public StyleAttribute(String name) {
/*  72 */     this.name = name;
/*  73 */     this.stringValue = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public StyleAttribute(String name, String stringValue) {
/*  78 */     this.name = name;
/*  79 */     this.stringValue = stringValue;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  83 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public StyleAttribute setName(String name) {
/*  88 */     this.name = name;
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStringValue() {
/*  94 */     return this.stringValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] getStringList() {
/*  99 */     return XMLParseUtil.parseStringList(this.stringValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStringValue(String value) {
/* 104 */     this.stringValue = value;
/*     */   }
/*     */   
/*     */   public boolean getBooleanValue() {
/* 108 */     return this.stringValue.toLowerCase().equals("true");
/*     */   }
/*     */   
/*     */   public int getIntValue() {
/* 112 */     return XMLParseUtil.findInt(this.stringValue);
/*     */   }
/*     */   
/*     */   public int[] getIntList() {
/* 116 */     return XMLParseUtil.parseIntList(this.stringValue);
/*     */   }
/*     */   
/*     */   public double getDoubleValue() {
/* 120 */     return XMLParseUtil.findDouble(this.stringValue);
/*     */   }
/*     */   
/*     */   public double[] getDoubleList() {
/* 124 */     return XMLParseUtil.parseDoubleList(this.stringValue);
/*     */   }
/*     */   
/*     */   public float getFloatValue() {
/* 128 */     return XMLParseUtil.findFloat(this.stringValue);
/*     */   }
/*     */   
/*     */   public float[] getFloatList() {
/* 132 */     return XMLParseUtil.parseFloatList(this.stringValue);
/*     */   }
/*     */   
/*     */   public float getRatioValue() {
/* 136 */     return (float)XMLParseUtil.parseRatio(this.stringValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUnits() {
/* 143 */     matchFpNumUnits.reset(this.stringValue);
/* 144 */     if (!matchFpNumUnits.matches()) return null; 
/* 145 */     return matchFpNumUnits.group(6);
/*     */   }
/*     */   
/*     */   public NumberWithUnits getNumberWithUnits() {
/* 149 */     return XMLParseUtil.parseNumberWithUnits(this.stringValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloatValueWithUnits() {
/* 154 */     NumberWithUnits number = getNumberWithUnits();
/* 155 */     return convertUnitsToPixels(number.getUnits(), number.getValue());
/*     */   }
/*     */   
/*     */   public static float convertUnitsToPixels(int unitType, float value) {
/*     */     float pixPerInch;
/* 160 */     if (unitType == 0 || unitType == 9)
/*     */     {
/* 162 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 168 */       pixPerInch = Toolkit.getDefaultToolkit().getScreenResolution();
/*     */     }
/* 170 */     catch (HeadlessException ex) {
/*     */ 
/*     */       
/* 173 */       pixPerInch = 72.0F;
/*     */     } 
/* 175 */     float inchesPerCm = 0.3936F;
/*     */     
/* 177 */     switch (unitType) {
/*     */       
/*     */       case 4:
/* 180 */         return value * pixPerInch;
/*     */       case 2:
/* 182 */         return value * 0.3936F * pixPerInch;
/*     */       case 3:
/* 184 */         return value * 0.1F * 0.3936F * pixPerInch;
/*     */       case 7:
/* 186 */         return value * 0.013888889F * pixPerInch;
/*     */       case 8:
/* 188 */         return value * 0.16666667F * pixPerInch;
/*     */     } 
/*     */     
/* 191 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getColorValue() {
/* 196 */     return ColorTable.parseColor(this.stringValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public String parseURLFn() {
/* 201 */     Matcher matchUrl = patternUrl.matcher(this.stringValue);
/* 202 */     if (!matchUrl.matches())
/*     */     {
/* 204 */       return null;
/*     */     }
/* 206 */     return matchUrl.group(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getURLValue(URL docRoot) {
/* 211 */     String fragment = parseURLFn();
/* 212 */     if (fragment == null) return null; 
/*     */     try {
/* 214 */       return new URL(docRoot, fragment);
/*     */     }
/* 216 */     catch (Exception e) {
/*     */       
/* 218 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/* 219 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public URL getURLValue(URI docRoot) {
/* 225 */     String fragment = parseURLFn();
/* 226 */     if (fragment == null) return null; 
/*     */     try {
/* 228 */       URI ref = docRoot.resolve(fragment);
/* 229 */       return ref.toURL();
/*     */     }
/* 231 */     catch (Exception e) {
/*     */       
/* 233 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/* 234 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getURIValue() {
/* 240 */     return getURIValue(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURIValue(URI base) {
/*     */     try {
/*     */       URI relUri;
/* 253 */       String fragment = parseURLFn();
/* 254 */       if (fragment == null) fragment = this.stringValue.replaceAll("\\s+", ""); 
/* 255 */       if (fragment == null) return null;
/*     */ 
/*     */ 
/*     */       
/* 259 */       if (Pattern.matches("[a-zA-Z]:!\\\\.*", fragment)) {
/*     */         
/* 261 */         File file = new File(fragment);
/* 262 */         return file.toURI();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 268 */       URI uriFrag = new URI(fragment);
/* 269 */       if (uriFrag.isAbsolute())
/*     */       {
/*     */         
/* 272 */         return uriFrag;
/*     */       }
/*     */       
/* 275 */       if (base == null) return uriFrag;
/*     */       
/* 277 */       URI relBase = new URI(null, base.getSchemeSpecificPart(), null);
/*     */       
/* 279 */       if (relBase.isOpaque()) {
/*     */         
/* 281 */         relUri = new URI(null, base.getSchemeSpecificPart(), uriFrag.getFragment());
/*     */       }
/*     */       else {
/*     */         
/* 285 */         relUri = relBase.resolve(uriFrag);
/*     */       } 
/* 287 */       return new URI(base.getScheme() + ":" + relUri);
/*     */     }
/* 289 */     catch (Exception e) {
/*     */       
/* 291 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/* 292 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 300 */       URI uri = new URI("jar:http://www.kitfox.com/jackal/jackal.jar!/res/doc/about.svg");
/* 301 */       uri = uri.resolve("#myFragment");
/*     */       
/* 303 */       System.err.println(uri.toString());
/*     */       
/* 305 */       uri = new URI("http://www.kitfox.com/jackal/jackal.html");
/* 306 */       uri = uri.resolve("#myFragment");
/*     */       
/* 308 */       System.err.println(uri.toString());
/*     */     }
/* 310 */     catch (Exception e) {
/*     */       
/* 312 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\xml\StyleAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */