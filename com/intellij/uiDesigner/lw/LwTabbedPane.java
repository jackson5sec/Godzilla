/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import java.awt.LayoutManager;
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
/*     */ public final class LwTabbedPane
/*     */   extends LwContainer
/*     */   implements ITabbedPane
/*     */ {
/*     */   public LwTabbedPane(String className) {
/*  29 */     super(className);
/*     */   }
/*     */   
/*     */   protected LayoutManager createInitialLayout() {
/*  33 */     return null;
/*     */   }
/*     */   
/*     */   public void read(Element element, PropertiesProvider provider) throws Exception {
/*  37 */     readNoLayout(element, provider);
/*     */   }
/*     */   
/*     */   protected void readConstraintsForChild(Element element, LwComponent component) {
/*  41 */     Element constraintsElement = LwXmlReader.getRequiredChild(element, "constraints");
/*  42 */     Element tabbedPaneChild = LwXmlReader.getRequiredChild(constraintsElement, "tabbedpane");
/*     */     
/*  44 */     StringDescriptor descriptor = LwXmlReader.getStringDescriptor(tabbedPaneChild, "title", "title-resource-bundle", "title-key");
/*     */ 
/*     */ 
/*     */     
/*  48 */     if (descriptor == null) {
/*  49 */       throw new IllegalArgumentException("String descriptor value required");
/*     */     }
/*  51 */     Constraints constraints = new Constraints(descriptor);
/*     */     
/*  53 */     Element tooltipElement = LwXmlReader.getChild(tabbedPaneChild, "tooltip");
/*  54 */     if (tooltipElement != null) {
/*  55 */       constraints.myToolTip = LwXmlReader.getStringDescriptor(tooltipElement, "value", "resource-bundle", "key");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     String icon = tabbedPaneChild.getAttributeValue("icon");
/*  62 */     if (icon != null) {
/*  63 */       constraints.myIcon = new IconDescriptor(icon);
/*     */     }
/*  65 */     icon = tabbedPaneChild.getAttributeValue("disabled-icon");
/*  66 */     if (icon != null) {
/*  67 */       constraints.myDisabledIcon = new IconDescriptor(icon);
/*     */     }
/*  69 */     constraints.myEnabled = LwXmlReader.getOptionalBoolean(tabbedPaneChild, "enabled", true);
/*     */     
/*  71 */     component.setCustomLayoutConstraints(constraints);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Constraints
/*     */   {
/*     */     public StringDescriptor myTitle;
/*     */     
/*     */     public StringDescriptor myToolTip;
/*     */     public IconDescriptor myIcon;
/*     */     public IconDescriptor myDisabledIcon;
/*     */     public boolean myEnabled = true;
/*     */     
/*     */     public Constraints(StringDescriptor title) {
/*  85 */       if (title == null) {
/*  86 */         throw new IllegalArgumentException("title cannot be null");
/*     */       }
/*  88 */       this.myTitle = title;
/*     */     }
/*     */     
/*     */     public StringDescriptor getProperty(String propName) {
/*  92 */       if (propName.equals("Tab Title")) {
/*  93 */         return this.myTitle;
/*     */       }
/*  95 */       if (propName.equals("Tab Tooltip")) {
/*  96 */         return this.myToolTip;
/*     */       }
/*  98 */       throw new IllegalArgumentException("Unknown property name " + propName);
/*     */     }
/*     */   }
/*     */   
/*     */   public StringDescriptor getTabProperty(IComponent component, String propName) {
/* 103 */     LwComponent lwComponent = (LwComponent)component;
/* 104 */     Constraints constraints = (Constraints)lwComponent.getCustomLayoutConstraints();
/* 105 */     if (constraints == null) {
/* 106 */       return null;
/*     */     }
/* 108 */     return constraints.getProperty(propName);
/*     */   }
/*     */   
/*     */   public boolean areChildrenExclusive() {
/* 112 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwTabbedPane.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */