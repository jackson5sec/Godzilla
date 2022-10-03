/*     */ package com.kitfox.svg;
/*     */ 
/*     */ import com.kitfox.svg.animation.AnimationElement;
/*     */ import com.kitfox.svg.animation.TrackBase;
/*     */ import com.kitfox.svg.animation.TrackManager;
/*     */ import com.kitfox.svg.pathcmd.Arc;
/*     */ import com.kitfox.svg.pathcmd.BuildHistory;
/*     */ import com.kitfox.svg.pathcmd.Cubic;
/*     */ import com.kitfox.svg.pathcmd.CubicSmooth;
/*     */ import com.kitfox.svg.pathcmd.Horizontal;
/*     */ import com.kitfox.svg.pathcmd.LineTo;
/*     */ import com.kitfox.svg.pathcmd.MoveTo;
/*     */ import com.kitfox.svg.pathcmd.PathCommand;
/*     */ import com.kitfox.svg.pathcmd.Quadratic;
/*     */ import com.kitfox.svg.pathcmd.QuadraticSmooth;
/*     */ import com.kitfox.svg.pathcmd.Terminal;
/*     */ import com.kitfox.svg.pathcmd.Vertical;
/*     */ import com.kitfox.svg.xml.StyleAttribute;
/*     */ import com.kitfox.svg.xml.StyleSheet;
/*     */ import com.kitfox.svg.xml.XMLParseUtil;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public abstract class SVGElement
/*     */   implements Serializable
/*     */ {
/*     */   public static final long serialVersionUID = 0L;
/*     */   public static final String SVG_NS = "http://www.w3.org/2000/svg";
/*  83 */   protected SVGElement parent = null;
/*  84 */   protected final ArrayList<SVGElement> children = new ArrayList<SVGElement>();
/*  85 */   protected String id = null;
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected String cssClass = null;
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected final HashMap<String, StyleAttribute> inlineStyles = new HashMap<String, StyleAttribute>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected final HashMap<String, StyleAttribute> presAttribs = new HashMap<String, StyleAttribute>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   protected URI xmlBase = null;
/*     */ 
/*     */ 
/*     */   
/*     */   protected SVGDiagram diagram;
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected final TrackManager trackManager = new TrackManager();
/*     */   
/*     */   boolean dirty = true;
/*     */   
/*     */   LinkedList<SVGElement> contexts;
/*     */ 
/*     */   
/*     */   public SVGElement() {
/* 120 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGElement(String id, SVGElement parent) {
/* 125 */     this(id, null, parent);
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
/*     */   public SVGElement getParent() {
/* 139 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   void setParent(SVGElement parent) {
/* 144 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SVGElement> getPath(List<SVGElement> retVec) {
/* 153 */     if (retVec == null)
/*     */     {
/* 155 */       retVec = new ArrayList<SVGElement>();
/*     */     }
/*     */     
/* 158 */     if (this.parent != null)
/*     */     {
/* 160 */       this.parent.getPath(retVec);
/*     */     }
/* 162 */     retVec.add(this);
/*     */     
/* 164 */     return retVec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SVGElement> getChildren(List<SVGElement> retVec) {
/* 175 */     if (retVec == null)
/*     */     {
/* 177 */       retVec = new ArrayList<SVGElement>();
/*     */     }
/*     */     
/* 180 */     retVec.addAll(this.children);
/*     */     
/* 182 */     return retVec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SVGElement getChild(String id) {
/* 191 */     for (SVGElement ele : this.children) {
/* 192 */       String eleId = ele.getId();
/* 193 */       if (eleId != null && eleId.equals(id))
/*     */       {
/* 195 */         return ele;
/*     */       }
/*     */     } 
/*     */     
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfChild(SVGElement child) {
/* 210 */     return this.children.indexOf(child);
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
/*     */   public void swapChildren(int i, int j) throws SVGException {
/* 222 */     if (this.children == null || i < 0 || i >= this.children.size() || j < 0 || j >= this.children.size()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 227 */     SVGElement temp = this.children.get(i);
/* 228 */     this.children.set(i, this.children.get(j));
/* 229 */     this.children.set(j, temp);
/* 230 */     build();
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
/*     */   public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
/* 246 */     this.parent = parent;
/* 247 */     this.diagram = helper.diagram;
/*     */     
/* 249 */     this.id = attrs.getValue("id");
/* 250 */     if (this.id != null && !this.id.equals("")) {
/*     */       
/* 252 */       this.id = this.id.intern();
/* 253 */       this.diagram.setElement(this.id, this);
/*     */     } 
/*     */     
/* 256 */     String className = attrs.getValue("class");
/* 257 */     this.cssClass = (className == null || className.equals("")) ? null : className.intern();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     String style = attrs.getValue("style");
/* 263 */     if (style != null)
/*     */     {
/* 265 */       HashMap hashMap = XMLParseUtil.parseStyle(style, this.inlineStyles);
/*     */     }
/*     */     
/* 268 */     String base = attrs.getValue("xml:base");
/* 269 */     if (base != null && !base.equals("")) {
/*     */       
/*     */       try {
/*     */         
/* 273 */         this.xmlBase = new URI(base);
/* 274 */       } catch (Exception e) {
/*     */         
/* 276 */         throw new SAXException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 281 */     int numAttrs = attrs.getLength();
/* 282 */     for (int i = 0; i < numAttrs; i++) {
/*     */       
/* 284 */       String name = attrs.getQName(i).intern();
/* 285 */       String value = attrs.getValue(i);
/*     */       
/* 287 */       this.presAttribs.put(name, new StyleAttribute(name, (value == null) ? null : value.intern()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttribute(String name, int attribType) {
/* 293 */     switch (attribType) {
/*     */       
/*     */       case 0:
/* 296 */         this.inlineStyles.remove(name);
/*     */         return;
/*     */       case 1:
/* 299 */         this.presAttribs.remove(name);
/*     */         return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAttribute(String name, int attribType, String value) throws SVGElementException {
/* 306 */     if (hasAttribute(name, attribType))
/*     */     {
/* 308 */       throw new SVGElementException(this, "Attribute " + name + "(" + AnimationElement.animationElementToString(attribType) + ") already exists");
/*     */     }
/*     */ 
/*     */     
/* 312 */     if ("id".equals(name)) {
/*     */       
/* 314 */       if (this.diagram != null) {
/*     */         
/* 316 */         this.diagram.removeElement(this.id);
/* 317 */         this.diagram.setElement(value, this);
/*     */       } 
/* 319 */       this.id = value;
/*     */     } 
/*     */     
/* 322 */     switch (attribType) {
/*     */       
/*     */       case 0:
/* 325 */         this.inlineStyles.put(name, new StyleAttribute(name, value));
/*     */         return;
/*     */       case 1:
/* 328 */         this.presAttribs.put(name, new StyleAttribute(name, value));
/*     */         return;
/*     */     } 
/*     */     
/* 332 */     throw new SVGElementException(this, "Invalid attribute type " + attribType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAttribute(String name, int attribType) throws SVGElementException {
/* 337 */     switch (attribType) {
/*     */       
/*     */       case 0:
/* 340 */         return this.inlineStyles.containsKey(name);
/*     */       case 1:
/* 342 */         return this.presAttribs.containsKey(name);
/*     */       case 2:
/* 344 */         return (this.inlineStyles.containsKey(name) || this.presAttribs.containsKey(name));
/*     */     } 
/*     */     
/* 347 */     throw new SVGElementException(this, "Invalid attribute type " + attribType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getInlineAttributes() {
/* 355 */     return this.inlineStyles.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getPresentationAttributes() {
/* 363 */     return this.presAttribs.keySet();
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
/*     */   public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
/* 375 */     this.children.add(child);
/* 376 */     child.parent = this;
/* 377 */     child.setDiagram(this.diagram);
/*     */ 
/*     */     
/* 380 */     if (child instanceof AnimationElement)
/*     */     {
/* 382 */       this.trackManager.addTrackElement((AnimationElement)child);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDiagram(SVGDiagram diagram) {
/* 388 */     this.diagram = diagram;
/* 389 */     diagram.setElement(this.id, this);
/* 390 */     for (SVGElement ele : this.children) {
/* 391 */       ele.setDiagram(diagram);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeChild(SVGElement child) throws SVGElementException {
/* 397 */     if (!this.children.contains(child))
/*     */     {
/* 399 */       throw new SVGElementException(this, "Element does not contain child " + child);
/*     */     }
/*     */     
/* 402 */     this.children.remove(child);
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
/*     */   public void loaderAddText(SVGLoaderHelper helper, String text) {}
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
/*     */   public void loaderEndElement(SVGLoaderHelper helper) throws SVGParseException {}
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
/*     */   protected void build() throws SVGException {
/* 439 */     StyleAttribute sty = new StyleAttribute();
/*     */     
/* 441 */     if (getPres(sty.setName("id"))) {
/*     */       
/* 443 */       String newId = sty.getStringValue();
/* 444 */       if (!newId.equals(this.id)) {
/*     */         
/* 446 */         this.diagram.removeElement(this.id);
/* 447 */         this.id = newId;
/* 448 */         this.diagram.setElement(this.id, this);
/*     */       } 
/*     */     } 
/* 451 */     if (getPres(sty.setName("class")))
/*     */     {
/* 453 */       this.cssClass = sty.getStringValue();
/*     */     }
/* 455 */     if (getPres(sty.setName("xml:base")))
/*     */     {
/* 457 */       this.xmlBase = sty.getURIValue();
/*     */     }
/*     */ 
/*     */     
/* 461 */     for (int i = 0; i < this.children.size(); i++) {
/*     */       
/* 463 */       SVGElement ele = this.children.get(i);
/* 464 */       ele.build();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public URI getXMLBase() {
/* 470 */     return (this.xmlBase != null) ? this.xmlBase : ((this.parent != null) ? this.parent
/* 471 */       .getXMLBase() : this.diagram.getXMLBase());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 479 */     return this.id;
/*     */   } public SVGElement(String id, String cssClass, SVGElement parent) {
/* 481 */     this.contexts = new LinkedList<SVGElement>();
/*     */     this.id = id;
/*     */     this.cssClass = cssClass;
/*     */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pushParentContext(SVGElement context) {
/* 490 */     this.contexts.addLast(context);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SVGElement popParentContext() {
/* 495 */     return this.contexts.removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SVGElement getParentContext() {
/* 500 */     return this.contexts.isEmpty() ? null : this.contexts.getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGRoot getRoot() {
/* 505 */     return (this.parent == null) ? null : this.parent.getRoot();
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
/*     */   public boolean getStyle(StyleAttribute attrib) throws SVGException {
/* 519 */     return getStyle(attrib, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, int attribType, String value) throws SVGElementException {
/*     */     StyleAttribute styAttr;
/* 527 */     switch (attribType) {
/*     */ 
/*     */       
/*     */       case 0:
/* 531 */         styAttr = this.inlineStyles.get(name);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 536 */         styAttr = this.presAttribs.get(name);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 541 */         styAttr = this.inlineStyles.get(name);
/*     */         
/* 543 */         if (styAttr == null)
/*     */         {
/* 545 */           styAttr = this.presAttribs.get(name);
/*     */         }
/*     */         break;
/*     */       
/*     */       default:
/* 550 */         throw new SVGElementException(this, "Invalid attribute type " + attribType);
/*     */     } 
/*     */     
/* 553 */     if (styAttr == null)
/*     */     {
/* 555 */       throw new SVGElementException(this, "Could not find attribute " + name + "(" + AnimationElement.animationElementToString(attribType) + ").  Make sure to create attribute before setting it.");
/*     */     }
/*     */ 
/*     */     
/* 559 */     if ("id".equals(styAttr.getName())) {
/*     */       
/* 561 */       if (this.diagram != null) {
/*     */         
/* 563 */         this.diagram.removeElement(this.id);
/* 564 */         this.diagram.setElement(value, this);
/*     */       } 
/* 566 */       this.id = value;
/*     */     } 
/*     */     
/* 569 */     styAttr.setStringValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getStyle(StyleAttribute attrib, boolean recursive) throws SVGException {
/* 574 */     return getStyle(attrib, recursive, true);
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
/*     */   public boolean getStyle(StyleAttribute attrib, boolean recursive, boolean evalAnimation) throws SVGException {
/* 593 */     String styName = attrib.getName();
/*     */ 
/*     */     
/* 596 */     StyleAttribute styAttr = this.inlineStyles.get(styName);
/*     */     
/* 598 */     attrib.setStringValue((styAttr == null) ? "" : styAttr.getStringValue());
/*     */ 
/*     */     
/* 601 */     if (evalAnimation) {
/*     */       
/* 603 */       TrackBase track = this.trackManager.getTrack(styName, 0);
/* 604 */       if (track != null) {
/*     */         
/* 606 */         track.getValue(attrib, this.diagram.getUniverse().getCurTime());
/* 607 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 612 */     if (styAttr != null)
/*     */     {
/* 614 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 619 */     StyleAttribute presAttr = this.presAttribs.get(styName);
/*     */     
/* 621 */     attrib.setStringValue((presAttr == null) ? "" : presAttr.getStringValue());
/*     */ 
/*     */     
/* 624 */     if (evalAnimation) {
/*     */       
/* 626 */       TrackBase track = this.trackManager.getTrack(styName, 1);
/* 627 */       if (track != null) {
/*     */         
/* 629 */         track.getValue(attrib, this.diagram.getUniverse().getCurTime());
/* 630 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 635 */     if (presAttr != null)
/*     */     {
/* 637 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 641 */     SVGRoot root = getRoot();
/* 642 */     if (root != null) {
/*     */       
/* 644 */       StyleSheet ss = root.getStyleSheet();
/* 645 */       if (ss != null)
/*     */       {
/* 647 */         return ss.getStyle(attrib, getTagName(), this.cssClass);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 652 */     if (recursive) {
/*     */       
/* 654 */       SVGElement parentContext = getParentContext();
/* 655 */       if (parentContext != null)
/*     */       {
/* 657 */         return parentContext.getStyle(attrib, true);
/*     */       }
/* 659 */       if (this.parent != null)
/*     */       {
/* 661 */         return this.parent.getStyle(attrib, true);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 666 */     return false;
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
/*     */   public StyleAttribute getStyleAbsolute(String styName) {
/* 678 */     return this.inlineStyles.get(styName);
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
/*     */   public boolean getPres(StyleAttribute attrib) throws SVGException {
/* 690 */     String presName = attrib.getName();
/*     */ 
/*     */     
/* 693 */     StyleAttribute presAttr = this.presAttribs.get(presName);
/*     */ 
/*     */     
/* 696 */     attrib.setStringValue((presAttr == null) ? "" : presAttr.getStringValue());
/*     */ 
/*     */     
/* 699 */     TrackBase track = this.trackManager.getTrack(presName, 1);
/* 700 */     if (track != null) {
/*     */       
/* 702 */       track.getValue(attrib, this.diagram.getUniverse().getCurTime());
/* 703 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 707 */     if (presAttr != null)
/*     */     {
/* 709 */       return true;
/*     */     }
/*     */     
/* 712 */     return false;
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
/*     */   public StyleAttribute getPresAbsolute(String styName) {
/* 724 */     return this.presAttribs.get(styName);
/*     */   }
/*     */   
/* 727 */   private static final Pattern TRANSFORM_PATTERN = Pattern.compile("\\w+\\([^)]*\\)");
/*     */   
/*     */   protected static AffineTransform parseTransform(String val) throws SVGException {
/* 730 */     Matcher matchExpression = TRANSFORM_PATTERN.matcher("");
/*     */     
/* 732 */     AffineTransform retXform = new AffineTransform();
/*     */     
/* 734 */     matchExpression.reset(val);
/* 735 */     while (matchExpression.find())
/*     */     {
/* 737 */       retXform.concatenate(parseSingleTransform(matchExpression.group()));
/*     */     }
/*     */     
/* 740 */     return retXform;
/*     */   }
/*     */   
/* 743 */   private static final Pattern WORD_PATTERN = Pattern.compile("([a-zA-Z]+|-?\\d+(\\.\\d+)?(e-?\\d+)?|-?\\.\\d+(e-?\\d+)?)");
/*     */   
/*     */   public static AffineTransform parseSingleTransform(String val) throws SVGException {
/* 746 */     Matcher matchWord = WORD_PATTERN.matcher("");
/*     */     
/* 748 */     AffineTransform retXform = new AffineTransform();
/*     */     
/* 750 */     matchWord.reset(val);
/* 751 */     if (!matchWord.find())
/*     */     {
/*     */       
/* 754 */       return retXform;
/*     */     }
/*     */     
/* 757 */     String function = matchWord.group().toLowerCase();
/*     */     
/* 759 */     LinkedList<String> termList = new LinkedList<String>();
/* 760 */     while (matchWord.find())
/*     */     {
/* 762 */       termList.add(matchWord.group());
/*     */     }
/*     */ 
/*     */     
/* 766 */     double[] terms = new double[termList.size()];
/* 767 */     Iterator<String> it = termList.iterator();
/* 768 */     int count = 0;
/* 769 */     while (it.hasNext())
/*     */     {
/* 771 */       terms[count++] = XMLParseUtil.parseDouble((String)it.next());
/*     */     }
/*     */ 
/*     */     
/* 775 */     if (function.equals("matrix")) {
/*     */       
/* 777 */       retXform.setTransform(terms[0], terms[1], terms[2], terms[3], terms[4], terms[5]);
/* 778 */     } else if (function.equals("translate")) {
/*     */       
/* 780 */       if (terms.length == 1) {
/*     */         
/* 782 */         retXform.setToTranslation(terms[0], 0.0D);
/*     */       } else {
/*     */         
/* 785 */         retXform.setToTranslation(terms[0], terms[1]);
/*     */       } 
/* 787 */     } else if (function.equals("scale")) {
/*     */       
/* 789 */       if (terms.length > 1) {
/*     */         
/* 791 */         retXform.setToScale(terms[0], terms[1]);
/*     */       } else {
/*     */         
/* 794 */         retXform.setToScale(terms[0], terms[0]);
/*     */       } 
/* 796 */     } else if (function.equals("rotate")) {
/*     */       
/* 798 */       if (terms.length > 2) {
/*     */         
/* 800 */         retXform.setToRotation(Math.toRadians(terms[0]), terms[1], terms[2]);
/*     */       } else {
/*     */         
/* 803 */         retXform.setToRotation(Math.toRadians(terms[0]));
/*     */       } 
/* 805 */     } else if (function.equals("skewx")) {
/*     */       
/* 807 */       retXform.setToShear(Math.toRadians(terms[0]), 0.0D);
/* 808 */     } else if (function.equals("skewy")) {
/*     */       
/* 810 */       retXform.setToShear(0.0D, Math.toRadians(terms[0]));
/*     */     } else {
/*     */       
/* 813 */       throw new SVGException("Unknown transform type");
/*     */     } 
/*     */     
/* 816 */     return retXform;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static float nextFloat(LinkedList<String> l) {
/* 821 */     String s = l.removeFirst();
/* 822 */     return Float.parseFloat(s);
/*     */   }
/*     */   
/* 825 */   private static final Pattern COMMAND_PATTERN = Pattern.compile("([MmLlHhVvAaQqTtCcSsZz])|([-+]?((\\d*\\.\\d+)|(\\d+))([eE][-+]?\\d+)?)");
/*     */   
/*     */   protected static PathCommand[] parsePathList(String list) {
/* 828 */     Matcher matchPathCmd = COMMAND_PATTERN.matcher(list);
/*     */ 
/*     */     
/* 831 */     LinkedList<String> tokens = new LinkedList<String>();
/* 832 */     while (matchPathCmd.find())
/*     */     {
/* 834 */       tokens.addLast(matchPathCmd.group());
/*     */     }
/*     */ 
/*     */     
/* 838 */     boolean defaultRelative = false;
/* 839 */     LinkedList<PathCommand> cmdList = new LinkedList<PathCommand>();
/* 840 */     char curCmd = 'Z';
/* 841 */     while (tokens.size() != 0) {
/*     */       MoveTo moveTo; LineTo lineTo; Horizontal horizontal; Vertical vertical; Arc arc; Quadratic quadratic; QuadraticSmooth quadraticSmooth; Cubic cubic; CubicSmooth cubicSmooth; Terminal terminal;
/* 843 */       String curToken = tokens.removeFirst();
/* 844 */       char initChar = curToken.charAt(0);
/* 845 */       if ((initChar >= 'A' && initChar <= 'Z') || (initChar >= 'a' && initChar <= 'z')) {
/*     */         
/* 847 */         curCmd = initChar;
/*     */       } else {
/*     */         
/* 850 */         tokens.addFirst(curToken);
/*     */       } 
/*     */       
/* 853 */       PathCommand cmd = null;
/*     */       
/* 855 */       switch (curCmd) {
/*     */         
/*     */         case 'M':
/* 858 */           moveTo = new MoveTo(false, nextFloat(tokens), nextFloat(tokens));
/* 859 */           curCmd = 'L';
/*     */           break;
/*     */         case 'm':
/* 862 */           moveTo = new MoveTo(true, nextFloat(tokens), nextFloat(tokens));
/* 863 */           curCmd = 'l';
/*     */           break;
/*     */         case 'L':
/* 866 */           lineTo = new LineTo(false, nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         case 'l':
/* 869 */           lineTo = new LineTo(true, nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         case 'H':
/* 872 */           horizontal = new Horizontal(false, nextFloat(tokens));
/*     */           break;
/*     */         case 'h':
/* 875 */           horizontal = new Horizontal(true, nextFloat(tokens));
/*     */           break;
/*     */         case 'V':
/* 878 */           vertical = new Vertical(false, nextFloat(tokens));
/*     */           break;
/*     */         case 'v':
/* 881 */           vertical = new Vertical(true, nextFloat(tokens));
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 'A':
/* 887 */           arc = new Arc(false, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), (nextFloat(tokens) == 1.0F), (nextFloat(tokens) == 1.0F), nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */ 
/*     */ 
/*     */         
/*     */         case 'a':
/* 893 */           arc = new Arc(true, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), (nextFloat(tokens) == 1.0F), (nextFloat(tokens) == 1.0F), nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         
/*     */         case 'Q':
/* 897 */           quadratic = new Quadratic(false, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         
/*     */         case 'q':
/* 901 */           quadratic = new Quadratic(true, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         case 'T':
/* 904 */           quadraticSmooth = new QuadraticSmooth(false, nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         case 't':
/* 907 */           quadraticSmooth = new QuadraticSmooth(true, nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */ 
/*     */         
/*     */         case 'C':
/* 912 */           cubic = new Cubic(false, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */ 
/*     */         
/*     */         case 'c':
/* 917 */           cubic = new Cubic(true, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         
/*     */         case 'S':
/* 921 */           cubicSmooth = new CubicSmooth(false, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         
/*     */         case 's':
/* 925 */           cubicSmooth = new CubicSmooth(true, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
/*     */           break;
/*     */         case 'Z':
/*     */         case 'z':
/* 929 */           terminal = new Terminal();
/*     */           break;
/*     */         default:
/* 932 */           throw new RuntimeException("Invalid path element");
/*     */       } 
/*     */       
/* 935 */       cmdList.add(terminal);
/* 936 */       defaultRelative = ((PathCommand)terminal).isRelative;
/*     */     } 
/*     */     
/* 939 */     PathCommand[] retArr = new PathCommand[cmdList.size()];
/* 940 */     cmdList.toArray(retArr);
/* 941 */     return retArr;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static GeneralPath buildPath(String text, int windingRule) {
/* 946 */     PathCommand[] commands = parsePathList(text);
/*     */     
/* 948 */     int numKnots = 2;
/* 949 */     for (int i = 0; i < commands.length; i++)
/*     */     {
/* 951 */       numKnots += commands[i].getNumKnotsAdded();
/*     */     }
/*     */ 
/*     */     
/* 955 */     GeneralPath path = new GeneralPath(windingRule, numKnots);
/*     */     
/* 957 */     BuildHistory hist = new BuildHistory();
/*     */     
/* 959 */     for (int j = 0; j < commands.length; j++) {
/*     */       
/* 961 */       PathCommand cmd = commands[j];
/* 962 */       cmd.appendPath(path, hist);
/*     */     } 
/*     */     
/* 965 */     return path;
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
/*     */   public int getNumChildren() {
/* 981 */     return this.children.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public SVGElement getChild(int i) {
/* 986 */     return this.children.get(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public double lerp(double t0, double t1, double alpha) {
/* 991 */     return (1.0D - alpha) * t0 + alpha * t1;
/*     */   }
/*     */   
/*     */   public abstract String getTagName();
/*     */   
/*     */   public abstract boolean updateTime(double paramDouble) throws SVGException;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\SVGElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */