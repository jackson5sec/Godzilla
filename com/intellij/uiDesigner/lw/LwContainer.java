/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import com.intellij.uiDesigner.compiler.UnexpectedFormElementException;
/*     */ import com.intellij.uiDesigner.shared.BorderType;
/*     */ import com.intellij.uiDesigner.shared.XYLayoutManager;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.jdom.Element;
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
/*     */ public class LwContainer
/*     */   extends LwComponent
/*     */   implements IContainer
/*     */ {
/*     */   private final ArrayList myComponents;
/*     */   private BorderType myBorderType;
/*     */   private StringDescriptor myBorderTitle;
/*     */   private int myBorderTitleJustification;
/*     */   private int myBorderTitlePosition;
/*     */   private FontDescriptor myBorderTitleFont;
/*     */   private ColorDescriptor myBorderTitleColor;
/*     */   private Insets myBorderSize;
/*     */   private ColorDescriptor myBorderColor;
/*     */   private LayoutManager myLayout;
/*     */   private String myLayoutManager;
/*     */   protected LayoutSerializer myLayoutSerializer;
/*     */   
/*     */   public LwContainer(String className) {
/*  60 */     super(className);
/*  61 */     this.myComponents = new ArrayList();
/*     */ 
/*     */     
/*  64 */     setBorderType(BorderType.NONE);
/*     */     
/*  66 */     this.myLayout = createInitialLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   protected LayoutManager createInitialLayout() {
/*  71 */     return (LayoutManager)new XYLayoutManager();
/*     */   }
/*     */   
/*     */   public final LayoutManager getLayout() {
/*  75 */     return this.myLayout;
/*     */   }
/*     */   
/*     */   public final void setLayout(LayoutManager layout) {
/*  79 */     this.myLayout = layout;
/*     */   }
/*     */   
/*     */   public String getLayoutManager() {
/*  83 */     return this.myLayoutManager;
/*     */   }
/*     */   
/*     */   public final boolean isGrid() {
/*  87 */     return getLayout() instanceof com.intellij.uiDesigner.core.GridLayoutManager;
/*     */   }
/*     */   
/*     */   public final boolean isXY() {
/*  91 */     return getLayout() instanceof XYLayoutManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addComponent(LwComponent component) {
/* 102 */     if (component == null) {
/* 103 */       throw new IllegalArgumentException("component cannot be null");
/*     */     }
/* 105 */     if (this.myComponents.contains(component)) {
/* 106 */       throw new IllegalArgumentException("component is already added: " + component);
/*     */     }
/* 108 */     if (component.getParent() != null) {
/* 109 */       throw new IllegalArgumentException("component already added to another container");
/*     */     }
/*     */ 
/*     */     
/* 113 */     this.myComponents.add(component);
/* 114 */     component.setParent(this);
/*     */   }
/*     */   
/*     */   public final IComponent getComponent(int index) {
/* 118 */     return this.myComponents.get(index);
/*     */   }
/*     */   
/*     */   public final int getComponentCount() {
/* 122 */     return this.myComponents.size();
/*     */   }
/*     */   
/*     */   public int indexOfComponent(IComponent lwComponent) {
/* 126 */     return this.myComponents.indexOf(lwComponent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final BorderType getBorderType() {
/* 135 */     return this.myBorderType;
/*     */   }
/*     */   
/*     */   public boolean accept(ComponentVisitor visitor) {
/* 139 */     if (!super.accept(visitor)) {
/* 140 */       return false;
/*     */     }
/*     */     
/* 143 */     for (int i = 0; i < getComponentCount(); i++) {
/* 144 */       IComponent c = getComponent(i);
/* 145 */       if (!c.accept(visitor)) {
/* 146 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setBorderType(BorderType type) {
/* 160 */     if (type == null) {
/* 161 */       throw new IllegalArgumentException("type cannot be null");
/*     */     }
/* 163 */     this.myBorderType = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final StringDescriptor getBorderTitle() {
/* 171 */     return this.myBorderTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setBorderTitle(StringDescriptor title) {
/* 179 */     this.myBorderTitle = title;
/*     */   }
/*     */   
/*     */   public int getBorderTitleJustification() {
/* 183 */     return this.myBorderTitleJustification;
/*     */   }
/*     */   
/*     */   public int getBorderTitlePosition() {
/* 187 */     return this.myBorderTitlePosition;
/*     */   }
/*     */   
/*     */   public FontDescriptor getBorderTitleFont() {
/* 191 */     return this.myBorderTitleFont;
/*     */   }
/*     */   
/*     */   public ColorDescriptor getBorderTitleColor() {
/* 195 */     return this.myBorderTitleColor;
/*     */   }
/*     */   
/*     */   public Insets getBorderSize() {
/* 199 */     return this.myBorderSize;
/*     */   }
/*     */   
/*     */   public ColorDescriptor getBorderColor() {
/* 203 */     return this.myBorderColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readConstraintsForChild(Element element, LwComponent component) {
/* 212 */     if (this.myLayoutSerializer != null) {
/* 213 */       Element constraintsElement = LwXmlReader.getRequiredChild(element, "constraints");
/* 214 */       this.myLayoutSerializer.readChildConstraints(constraintsElement, component);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void readBorder(Element element) {
/* 222 */     Element borderElement = LwXmlReader.getRequiredChild(element, "border");
/* 223 */     setBorderType(BorderType.valueOf(LwXmlReader.getRequiredString(borderElement, "type")));
/*     */     
/* 225 */     StringDescriptor descriptor = LwXmlReader.getStringDescriptor(borderElement, "title", "title-resource-bundle", "title-key");
/*     */ 
/*     */ 
/*     */     
/* 229 */     if (descriptor != null) {
/* 230 */       setBorderTitle(descriptor);
/*     */     }
/*     */     
/* 233 */     this.myBorderTitleJustification = LwXmlReader.getOptionalInt(borderElement, "title-justification", 0);
/* 234 */     this.myBorderTitlePosition = LwXmlReader.getOptionalInt(borderElement, "title-position", 0);
/* 235 */     Element fontElement = LwXmlReader.getChild(borderElement, "font");
/* 236 */     if (fontElement != null) {
/* 237 */       this.myBorderTitleFont = LwXmlReader.getFontDescriptor(fontElement);
/*     */     }
/* 239 */     this.myBorderTitleColor = LwXmlReader.getOptionalColorDescriptor(LwXmlReader.getChild(borderElement, "title-color"));
/* 240 */     this.myBorderColor = LwXmlReader.getOptionalColorDescriptor(LwXmlReader.getChild(borderElement, "color"));
/* 241 */     Element sizeElement = LwXmlReader.getChild(borderElement, "size");
/* 242 */     if (sizeElement != null) {
/*     */       try {
/* 244 */         this.myBorderSize = LwXmlReader.readInsets(sizeElement);
/*     */       }
/* 246 */       catch (Exception e) {
/* 247 */         this.myBorderSize = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void readChildren(Element element, PropertiesProvider provider) throws Exception {
/* 256 */     Element childrenElement = LwXmlReader.getRequiredChild(element, "children");
/* 257 */     for (Iterator i = childrenElement.getChildren().iterator(); i.hasNext(); ) {
/* 258 */       Element child = i.next();
/* 259 */       LwComponent component = createComponentFromTag(child);
/* 260 */       addComponent(component);
/* 261 */       component.read(child, provider);
/*     */     } 
/*     */   }
/*     */   public static LwComponent createComponentFromTag(Element child) throws Exception {
/*     */     LwComponent component;
/* 266 */     String name = child.getName();
/*     */     
/* 268 */     if ("component".equals(name)) {
/* 269 */       String className = LwXmlReader.getRequiredString(child, "class");
/* 270 */       component = new LwAtomicComponent(className);
/*     */     }
/* 272 */     else if ("nested-form".equals(name)) {
/* 273 */       component = new LwNestedForm();
/*     */     }
/* 275 */     else if ("vspacer".equals(name)) {
/* 276 */       component = new LwVSpacer();
/*     */     }
/* 278 */     else if ("hspacer".equals(name)) {
/* 279 */       component = new LwHSpacer();
/*     */     }
/* 281 */     else if ("xy".equals(name) || "grid".equals(name)) {
/* 282 */       String className = LwXmlReader.getOptionalString(child, "class", "javax.swing.JPanel");
/* 283 */       component = new LwContainer(className);
/*     */     }
/* 285 */     else if ("scrollpane".equals(name)) {
/* 286 */       String className = LwXmlReader.getOptionalString(child, "class", "javax.swing.JScrollPane");
/* 287 */       component = new LwScrollPane(className);
/*     */     }
/* 289 */     else if ("tabbedpane".equals(name)) {
/* 290 */       String className = LwXmlReader.getOptionalString(child, "class", "javax.swing.JTabbedPane");
/* 291 */       component = new LwTabbedPane(className);
/*     */     }
/* 293 */     else if ("splitpane".equals(name)) {
/* 294 */       String className = LwXmlReader.getOptionalString(child, "class", "javax.swing.JSplitPane");
/* 295 */       component = new LwSplitPane(className);
/*     */     }
/* 297 */     else if ("toolbar".equals(name)) {
/* 298 */       String className = LwXmlReader.getOptionalString(child, "class", "javax.swing.JToolBar");
/* 299 */       component = new LwToolBar(className);
/*     */     } else {
/*     */       
/* 302 */       throw new UnexpectedFormElementException("unexpected element: " + child);
/*     */     } 
/* 304 */     return component;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void readLayout(Element element) {
/* 311 */     this.myLayoutManager = element.getAttributeValue("layout-manager");
/* 312 */     if ("xy".equals(element.getName())) {
/* 313 */       this.myLayoutSerializer = XYLayoutSerializer.INSTANCE;
/*     */     }
/* 315 */     else if ("grid".equals(element.getName())) {
/* 316 */       createLayoutSerializer();
/*     */     } else {
/*     */       
/* 319 */       throw new UnexpectedFormElementException("unexpected element: " + element);
/*     */     } 
/* 321 */     this.myLayoutSerializer.readLayout(element, this);
/*     */   }
/*     */   
/*     */   public void setLayoutManager(String layoutManager) {
/* 325 */     this.myLayoutManager = layoutManager;
/* 326 */     createLayoutSerializer();
/*     */   }
/*     */   
/*     */   private void createLayoutSerializer() {
/* 330 */     if ("BorderLayout".equals(this.myLayoutManager)) {
/* 331 */       this.myLayoutSerializer = BorderLayoutSerializer.INSTANCE;
/*     */     }
/* 333 */     else if ("FlowLayout".equals(this.myLayoutManager)) {
/* 334 */       this.myLayoutSerializer = FlowLayoutSerializer.INSTANCE;
/*     */     }
/* 336 */     else if ("CardLayout".equals(this.myLayoutManager)) {
/* 337 */       this.myLayoutSerializer = CardLayoutSerializer.INSTANCE;
/*     */     }
/* 339 */     else if ("XYLayout".equals(this.myLayoutManager)) {
/* 340 */       this.myLayoutSerializer = XYLayoutSerializer.INSTANCE;
/*     */     }
/* 342 */     else if ("FormLayout".equals(this.myLayoutManager)) {
/* 343 */       this.myLayoutSerializer = FormLayoutSerializer.INSTANCE;
/*     */     }
/* 345 */     else if ("GridBagLayout".equals(this.myLayoutManager)) {
/* 346 */       this.myLayoutSerializer = GridBagLayoutSerializer.INSTANCE;
/*     */     } else {
/*     */       
/* 349 */       this.myLayoutSerializer = GridLayoutSerializer.INSTANCE;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void read(Element element, PropertiesProvider provider) throws Exception {
/* 354 */     readBase(element);
/*     */ 
/*     */     
/* 357 */     readLayout(element);
/*     */ 
/*     */     
/* 360 */     readConstraints(element);
/* 361 */     readProperties(element, provider);
/*     */ 
/*     */     
/* 364 */     readBorder(element);
/*     */     
/* 366 */     readChildren(element, provider);
/*     */   }
/*     */   
/*     */   protected void readNoLayout(Element element, PropertiesProvider provider) throws Exception {
/* 370 */     readBase(element);
/*     */ 
/*     */     
/* 373 */     readConstraints(element);
/* 374 */     readProperties(element, provider);
/*     */ 
/*     */     
/* 377 */     readBorder(element);
/*     */     
/* 379 */     readChildren(element, provider);
/*     */   }
/*     */   
/*     */   public boolean areChildrenExclusive() {
/* 383 */     return "CardLayout".equals(this.myLayoutManager);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwContainer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */