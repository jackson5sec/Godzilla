/*     */ package com.kitfox.svg.xml;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.lang.reflect.Array;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
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
/*     */ public class XMLParseUtil
/*     */ {
/*  55 */   static final Matcher fpMatch = Pattern.compile("([-+]?((\\d*\\.\\d+)|(\\d+))([eE][+-]?\\d+)?)(\\%|in|cm|mm|pt|pc|px|em|ex)?").matcher("");
/*  56 */   static final Matcher intMatch = Pattern.compile("[-+]?\\d+").matcher("");
/*  57 */   static final Matcher quoteMatch = Pattern.compile("^'|'$").matcher("");
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
/*     */   public static String getTagText(Element ele) {
/*  69 */     NodeList nl = ele.getChildNodes();
/*  70 */     int size = nl.getLength();
/*     */     
/*  72 */     Node node = null;
/*  73 */     int i = 0;
/*  74 */     for (; i < size; i++) {
/*     */       
/*  76 */       node = nl.item(i);
/*  77 */       if (node instanceof Text)
/*     */         break; 
/*  79 */     }  if (i == size || node == null) return null;
/*     */     
/*  81 */     return ((Text)node).getData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Element getFirstChild(Element root, String name) {
/*  90 */     NodeList nl = root.getChildNodes();
/*  91 */     int size = nl.getLength();
/*  92 */     for (int i = 0; i < size; i++) {
/*     */       
/*  94 */       Node node = nl.item(i);
/*  95 */       if (node instanceof Element) {
/*  96 */         Element ele = (Element)node;
/*  97 */         if (ele.getTagName().equals(name)) return ele; 
/*     */       } 
/*     */     } 
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] parseStringList(String list) {
/* 106 */     Matcher matchWs = Pattern.compile("[^\\s]+").matcher("");
/* 107 */     matchWs.reset(list);
/*     */     
/* 109 */     LinkedList<String> matchList = new LinkedList<String>();
/* 110 */     while (matchWs.find())
/*     */     {
/* 112 */       matchList.add(matchWs.group());
/*     */     }
/*     */     
/* 115 */     String[] retArr = new String[matchList.size()];
/* 116 */     return matchList.<String>toArray(retArr);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isDouble(String val) {
/* 121 */     fpMatch.reset(val);
/* 122 */     return fpMatch.matches();
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
/*     */   public static double parseDouble(String val) {
/* 137 */     return findDouble(val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized double findDouble(String val) {
/* 146 */     if (val == null) return 0.0D;
/*     */     
/* 148 */     fpMatch.reset(val);
/*     */     
/*     */     try {
/* 151 */       if (!fpMatch.find()) return 0.0D;
/*     */     
/* 153 */     } catch (StringIndexOutOfBoundsException e) {
/*     */       
/* 155 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "XMLParseUtil: regex parse problem: '" + val + "'", e);
/*     */     } 
/*     */ 
/*     */     
/* 159 */     val = fpMatch.group(1);
/*     */ 
/*     */     
/* 162 */     double retVal = 0.0D;
/*     */     try {
/*     */       float pixPerInch;
/* 165 */       retVal = Double.parseDouble(val);
/*     */ 
/*     */       
/*     */       try {
/* 169 */         pixPerInch = Toolkit.getDefaultToolkit().getScreenResolution();
/*     */       }
/* 171 */       catch (NoClassDefFoundError err) {
/*     */ 
/*     */         
/* 174 */         pixPerInch = 72.0F;
/*     */       } 
/* 176 */       float inchesPerCm = 0.3936F;
/* 177 */       String units = fpMatch.group(6);
/*     */       
/* 179 */       if ("%".equals(units)) { retVal /= 100.0D; }
/* 180 */       else if ("in".equals(units))
/*     */       
/* 182 */       { retVal *= pixPerInch; }
/*     */       
/* 184 */       else if ("cm".equals(units))
/*     */       
/* 186 */       { retVal *= (0.3936F * pixPerInch); }
/*     */       
/* 188 */       else if ("mm".equals(units))
/*     */       
/* 190 */       { retVal *= (0.3936F * pixPerInch * 0.1F); }
/*     */       
/* 192 */       else if ("pt".equals(units))
/*     */       
/* 194 */       { retVal *= (0.013888889F * pixPerInch); }
/*     */       
/* 196 */       else if ("pc".equals(units))
/*     */       
/* 198 */       { retVal *= (0.16666667F * pixPerInch); }
/*     */ 
/*     */     
/* 201 */     } catch (Exception exception) {}
/*     */     
/* 203 */     return retVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized double[] parseDoubleList(String list) {
/* 214 */     if (list == null) return null;
/*     */     
/* 216 */     fpMatch.reset(list);
/*     */     
/* 218 */     LinkedList<Double> doubList = new LinkedList<Double>();
/* 219 */     while (fpMatch.find()) {
/*     */       
/* 221 */       String val = fpMatch.group(1);
/* 222 */       doubList.add(Double.valueOf(val));
/*     */     } 
/*     */     
/* 225 */     double[] retArr = new double[doubList.size()];
/* 226 */     Iterator<Double> it = doubList.iterator();
/* 227 */     int idx = 0;
/* 228 */     while (it.hasNext())
/*     */     {
/* 230 */       retArr[idx++] = ((Double)it.next()).doubleValue();
/*     */     }
/*     */     
/* 233 */     return retArr;
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
/*     */   public static float parseFloat(String val) {
/* 248 */     return findFloat(val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized float findFloat(String val) {
/* 257 */     if (val == null) return 0.0F;
/*     */     
/* 259 */     fpMatch.reset(val);
/* 260 */     if (!fpMatch.find()) return 0.0F;
/*     */     
/* 262 */     val = fpMatch.group(1);
/*     */ 
/*     */     
/* 265 */     float retVal = 0.0F;
/*     */     
/*     */     try {
/* 268 */       retVal = Float.parseFloat(val);
/* 269 */       String units = fpMatch.group(6);
/* 270 */       if ("%".equals(units)) retVal /= 100.0F;
/*     */     
/* 272 */     } catch (Exception exception) {}
/*     */     
/* 274 */     return retVal;
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized float[] parseFloatList(String list) {
/* 279 */     if (list == null) return null;
/*     */     
/* 281 */     fpMatch.reset(list);
/*     */     
/* 283 */     LinkedList<Float> floatList = new LinkedList<Float>();
/* 284 */     while (fpMatch.find()) {
/*     */       
/* 286 */       String val = fpMatch.group(1);
/* 287 */       floatList.add(Float.valueOf(val));
/*     */     } 
/*     */     
/* 290 */     float[] retArr = new float[floatList.size()];
/* 291 */     Iterator<Float> it = floatList.iterator();
/* 292 */     int idx = 0;
/* 293 */     while (it.hasNext())
/*     */     {
/* 295 */       retArr[idx++] = ((Float)it.next()).floatValue();
/*     */     }
/*     */     
/* 298 */     return retArr;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int parseInt(String val) {
/* 303 */     if (val == null) return 0;
/*     */     
/* 305 */     int retVal = 0;
/*     */     try {
/* 307 */       retVal = Integer.parseInt(val);
/* 308 */     } catch (Exception exception) {}
/*     */     
/* 310 */     return retVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int findInt(String val) {
/* 319 */     if (val == null) return 0;
/*     */     
/* 321 */     intMatch.reset(val);
/* 322 */     if (!intMatch.find()) return 0;
/*     */     
/* 324 */     val = intMatch.group();
/*     */ 
/*     */     
/* 327 */     int retVal = 0;
/*     */     try {
/* 329 */       retVal = Integer.parseInt(val);
/* 330 */     } catch (Exception exception) {}
/*     */     
/* 332 */     return retVal;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] parseIntList(String list) {
/* 337 */     if (list == null) return null;
/*     */     
/* 339 */     intMatch.reset(list);
/*     */     
/* 341 */     LinkedList<Integer> intList = new LinkedList<Integer>();
/* 342 */     while (intMatch.find()) {
/*     */       
/* 344 */       String val = intMatch.group();
/* 345 */       intList.add(Integer.valueOf(val));
/*     */     } 
/*     */     
/* 348 */     int[] retArr = new int[intList.size()];
/* 349 */     Iterator<Integer> it = intList.iterator();
/* 350 */     int idx = 0;
/* 351 */     while (it.hasNext())
/*     */     {
/* 353 */       retArr[idx++] = ((Integer)it.next()).intValue();
/*     */     }
/*     */     
/* 356 */     return retArr;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static double parseRatio(String val) {
/* 392 */     if (val == null || val.equals("")) return 0.0D;
/*     */     
/* 394 */     if (val.charAt(val.length() - 1) == '%')
/*     */     {
/* 396 */       parseDouble(val.substring(0, val.length() - 1));
/*     */     }
/* 398 */     return parseDouble(val);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NumberWithUnits parseNumberWithUnits(String val) {
/* 403 */     if (val == null) return null;
/*     */     
/* 405 */     return new NumberWithUnits(val);
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
/*     */   public static String getAttribString(Element ele, String name) {
/* 451 */     return ele.getAttribute(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getAttribInt(Element ele, String name) {
/* 459 */     String sval = ele.getAttribute(name);
/* 460 */     int val = 0; 
/* 461 */     try { val = Integer.parseInt(sval); } catch (Exception exception) {}
/*     */     
/* 463 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getAttribIntHex(Element ele, String name) {
/* 472 */     String sval = ele.getAttribute(name);
/* 473 */     int val = 0; 
/* 474 */     try { val = Integer.parseInt(sval, 16); } catch (Exception exception) {}
/*     */     
/* 476 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getAttribFloat(Element ele, String name) {
/* 484 */     String sval = ele.getAttribute(name);
/* 485 */     float val = 0.0F; 
/* 486 */     try { val = Float.parseFloat(sval); } catch (Exception exception) {}
/*     */     
/* 488 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getAttribDouble(Element ele, String name) {
/* 496 */     String sval = ele.getAttribute(name);
/* 497 */     double val = 0.0D; 
/* 498 */     try { val = Double.parseDouble(sval); } catch (Exception exception) {}
/*     */     
/* 500 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean getAttribBoolean(Element ele, String name) {
/* 509 */     String sval = ele.getAttribute(name);
/*     */     
/* 511 */     return sval.toLowerCase().equals("true");
/*     */   }
/*     */ 
/*     */   
/*     */   public static URL getAttribURL(Element ele, String name, URL docRoot) {
/* 516 */     String sval = ele.getAttribute(name);
/*     */ 
/*     */     
/*     */     try {
/* 520 */       return new URL(docRoot, sval);
/*     */     }
/* 522 */     catch (Exception e) {
/*     */       
/* 524 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReadableXMLElement getElement(Class<?> classType, Element root, String name, URL docRoot) {
/* 533 */     if (root == null) return null;
/*     */ 
/*     */     
/* 536 */     if (!ReadableXMLElement.class.isAssignableFrom(classType))
/*     */     {
/* 538 */       return null;
/*     */     }
/*     */     
/* 541 */     NodeList nl = root.getChildNodes();
/* 542 */     int size = nl.getLength();
/* 543 */     for (int i = 0; i < size; i++) {
/*     */       
/* 545 */       Node node = nl.item(i);
/* 546 */       if (node instanceof Element) {
/* 547 */         Element ele = (Element)node;
/* 548 */         if (ele.getTagName().equals(name)) {
/*     */           
/* 550 */           ReadableXMLElement newObj = null;
/*     */           
/*     */           try {
/* 553 */             newObj = (ReadableXMLElement)classType.newInstance();
/*     */           }
/* 555 */           catch (Exception e) {
/*     */             
/* 557 */             Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */           } 
/*     */           
/* 560 */           newObj.read(ele, docRoot);
/*     */           
/* 562 */           if (newObj != null)
/*     */           {
/* 564 */             return newObj; } 
/*     */         } 
/*     */       } 
/* 567 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashMap<String, ReadableXMLElement> getElementHashMap(Class<?> classType, Element root, String name, String key, URL docRoot) {
/* 578 */     if (root == null) return null;
/*     */ 
/*     */     
/* 581 */     if (!ReadableXMLElement.class.isAssignableFrom(classType))
/*     */     {
/* 583 */       return null;
/*     */     }
/*     */     
/* 586 */     HashMap<String, ReadableXMLElement> retMap = new HashMap<String, ReadableXMLElement>();
/*     */     
/* 588 */     NodeList nl = root.getChildNodes();
/* 589 */     int size = nl.getLength();
/* 590 */     for (int i = 0; i < size; i++) {
/*     */       
/* 592 */       Node node = nl.item(i);
/* 593 */       if (node instanceof Element) {
/* 594 */         Element ele = (Element)node;
/* 595 */         if (ele.getTagName().equals(name)) {
/*     */           
/* 597 */           ReadableXMLElement newObj = null;
/*     */           
/*     */           try {
/* 600 */             newObj = (ReadableXMLElement)classType.newInstance();
/*     */           }
/* 602 */           catch (Exception e) {
/*     */             
/* 604 */             Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */           } 
/*     */           
/* 607 */           newObj.read(ele, docRoot);
/*     */           
/* 609 */           if (newObj != null)
/*     */           
/* 611 */           { String keyVal = getAttribString(ele, key);
/* 612 */             retMap.put(keyVal, newObj); } 
/*     */         } 
/*     */       } 
/* 615 */     }  return retMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public static HashSet<ReadableXMLElement> getElementHashSet(Class<?> classType, Element root, String name, URL docRoot) {
/* 620 */     if (root == null) return null;
/*     */ 
/*     */     
/* 623 */     if (!ReadableXMLElement.class.isAssignableFrom(classType))
/*     */     {
/* 625 */       return null;
/*     */     }
/*     */     
/* 628 */     HashSet<ReadableXMLElement> retSet = new HashSet<ReadableXMLElement>();
/*     */     
/* 630 */     NodeList nl = root.getChildNodes();
/* 631 */     int size = nl.getLength();
/* 632 */     for (int i = 0; i < size; i++) {
/*     */       
/* 634 */       Node node = nl.item(i);
/* 635 */       if (node instanceof Element) {
/* 636 */         Element ele = (Element)node;
/* 637 */         if (ele.getTagName().equals(name)) {
/*     */           
/* 639 */           ReadableXMLElement newObj = null;
/*     */           
/*     */           try {
/* 642 */             newObj = (ReadableXMLElement)classType.newInstance();
/*     */           }
/* 644 */           catch (Exception e) {
/*     */             
/* 646 */             Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */           } 
/*     */           
/* 649 */           newObj.read(ele, docRoot);
/*     */           
/* 651 */           if (newObj != null)
/*     */           {
/*     */ 
/*     */ 
/*     */             
/* 656 */             retSet.add(newObj); } 
/*     */         } 
/*     */       } 
/* 659 */     }  return retSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinkedList<ReadableXMLElement> getElementLinkedList(Class<?> classType, Element root, String name, URL docRoot) {
/* 665 */     if (root == null) return null;
/*     */ 
/*     */     
/* 668 */     if (!ReadableXMLElement.class.isAssignableFrom(classType))
/*     */     {
/* 670 */       return null;
/*     */     }
/*     */     
/* 673 */     NodeList nl = root.getChildNodes();
/* 674 */     LinkedList<ReadableXMLElement> elementCache = new LinkedList<ReadableXMLElement>();
/* 675 */     int size = nl.getLength();
/* 676 */     for (int i = 0; i < size; i++) {
/*     */       
/* 678 */       Node node = nl.item(i);
/* 679 */       if (node instanceof Element) {
/* 680 */         Element ele = (Element)node;
/* 681 */         if (ele.getTagName().equals(name)) {
/*     */           
/* 683 */           ReadableXMLElement newObj = null;
/*     */           
/*     */           try {
/* 686 */             newObj = (ReadableXMLElement)classType.newInstance();
/*     */           }
/* 688 */           catch (Exception e) {
/*     */             
/* 690 */             Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */           } 
/*     */           
/* 693 */           newObj.read(ele, docRoot);
/*     */           
/* 695 */           elementCache.addLast(newObj);
/*     */         } 
/*     */       } 
/* 698 */     }  return elementCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object[] getElementArray(Class<?> classType, Element root, String name, URL docRoot) {
/* 703 */     if (root == null) return null;
/*     */ 
/*     */     
/* 706 */     if (!ReadableXMLElement.class.isAssignableFrom(classType))
/*     */     {
/* 708 */       return null;
/*     */     }
/*     */     
/* 711 */     LinkedList<ReadableXMLElement> elementCache = getElementLinkedList(classType, root, name, docRoot);
/*     */     
/* 713 */     Object[] retArr = (Object[])Array.newInstance(classType, elementCache.size());
/* 714 */     return elementCache.toArray(retArr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] getElementArrayInt(Element root, String name, String attrib) {
/* 724 */     if (root == null) return null;
/*     */     
/* 726 */     NodeList nl = root.getChildNodes();
/* 727 */     LinkedList<Integer> elementCache = new LinkedList<Integer>();
/* 728 */     int size = nl.getLength();
/*     */     
/* 730 */     for (int i = 0; i < size; i++) {
/*     */       
/* 732 */       Node node = nl.item(i);
/* 733 */       if (node instanceof Element) {
/* 734 */         Element ele = (Element)node;
/* 735 */         if (ele.getTagName().equals(name)) {
/*     */           
/* 737 */           String valS = ele.getAttribute(attrib);
/* 738 */           int eleVal = 0; try {
/* 739 */             eleVal = Integer.parseInt(valS);
/* 740 */           } catch (Exception exception) {}
/*     */           
/* 742 */           elementCache.addLast(new Integer(eleVal));
/*     */         } 
/*     */       } 
/* 745 */     }  int[] retArr = new int[elementCache.size()];
/* 746 */     Iterator<Integer> it = elementCache.iterator();
/* 747 */     int idx = 0;
/* 748 */     while (it.hasNext())
/*     */     {
/* 750 */       retArr[idx++] = ((Integer)it.next()).intValue();
/*     */     }
/*     */     
/* 753 */     return retArr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getElementArrayString(Element root, String name, String attrib) {
/* 763 */     if (root == null) return null;
/*     */     
/* 765 */     NodeList nl = root.getChildNodes();
/* 766 */     LinkedList<String> elementCache = new LinkedList<String>();
/* 767 */     int size = nl.getLength();
/*     */     
/* 769 */     for (int i = 0; i < size; i++) {
/*     */       
/* 771 */       Node node = nl.item(i);
/* 772 */       if (node instanceof Element) {
/* 773 */         Element ele = (Element)node;
/* 774 */         if (ele.getTagName().equals(name)) {
/*     */           
/* 776 */           String valS = ele.getAttribute(attrib);
/*     */           
/* 778 */           elementCache.addLast(valS);
/*     */         } 
/*     */       } 
/* 781 */     }  String[] retArr = new String[elementCache.size()];
/* 782 */     Iterator<String> it = elementCache.iterator();
/* 783 */     int idx = 0;
/* 784 */     while (it.hasNext())
/*     */     {
/* 786 */       retArr[idx++] = it.next();
/*     */     }
/*     */     
/* 789 */     return retArr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashMap<String, StyleAttribute> parseStyle(String styleString) {
/* 798 */     return parseStyle(styleString, new HashMap<String, StyleAttribute>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HashMap<String, StyleAttribute> parseStyle(String styleString, HashMap<String, StyleAttribute> map) {
/* 808 */     Pattern patSemi = Pattern.compile(";");
/*     */     
/* 810 */     String[] styles = patSemi.split(styleString);
/*     */     
/* 812 */     for (int i = 0; i < styles.length; i++) {
/*     */       
/* 814 */       if (styles[i].length() != 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 819 */         int colon = styles[i].indexOf(':');
/* 820 */         if (colon != -1) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 825 */           String key = styles[i].substring(0, colon).trim().intern();
/* 826 */           String value = quoteMatch.reset(styles[i].substring(colon + 1).trim()).replaceAll("").intern();
/*     */           
/* 828 */           map.put(key, new StyleAttribute(key, value));
/*     */         } 
/*     */       } 
/* 831 */     }  return map;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\xml\XMLParseUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */