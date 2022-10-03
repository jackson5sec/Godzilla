/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.core.GridLayoutManager;
/*     */ import java.awt.Insets;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GridLayoutSerializer
/*     */   extends LayoutSerializer
/*     */ {
/*  33 */   public static GridLayoutSerializer INSTANCE = new GridLayoutSerializer();
/*     */   
/*     */   void readLayout(Element element, LwContainer container) {
/*  36 */     int rowCount = LwXmlReader.getRequiredInt(element, "row-count");
/*  37 */     int columnCount = LwXmlReader.getRequiredInt(element, "column-count");
/*     */     
/*  39 */     int hGap = LwXmlReader.getOptionalInt(element, "hgap", -1);
/*  40 */     int vGap = LwXmlReader.getOptionalInt(element, "vgap", -1);
/*     */ 
/*     */     
/*  43 */     boolean sameSizeHorizontally = LwXmlReader.getOptionalBoolean(element, "same-size-horizontally", false);
/*  44 */     boolean sameSizeVertically = LwXmlReader.getOptionalBoolean(element, "same-size-vertically", false);
/*     */     
/*  46 */     Element marginElement = LwXmlReader.getRequiredChild(element, "margin");
/*  47 */     Insets margin = new Insets(LwXmlReader.getRequiredInt(marginElement, "top"), LwXmlReader.getRequiredInt(marginElement, "left"), LwXmlReader.getRequiredInt(marginElement, "bottom"), LwXmlReader.getRequiredInt(marginElement, "right"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     GridLayoutManager layout = new GridLayoutManager(rowCount, columnCount);
/*  55 */     layout.setMargin(margin);
/*  56 */     layout.setVGap(vGap);
/*  57 */     layout.setHGap(hGap);
/*  58 */     layout.setSameSizeHorizontally(sameSizeHorizontally);
/*  59 */     layout.setSameSizeVertically(sameSizeVertically);
/*  60 */     container.setLayout((LayoutManager)layout);
/*     */   }
/*     */ 
/*     */   
/*     */   void readChildConstraints(Element constraintsElement, LwComponent component) {
/*  65 */     Element gridElement = LwXmlReader.getChild(constraintsElement, "grid");
/*  66 */     if (gridElement != null) {
/*  67 */       GridConstraints constraints = new GridConstraints();
/*     */       
/*  69 */       constraints.setRow(LwXmlReader.getRequiredInt(gridElement, "row"));
/*  70 */       constraints.setColumn(LwXmlReader.getRequiredInt(gridElement, "column"));
/*  71 */       constraints.setRowSpan(LwXmlReader.getRequiredInt(gridElement, "row-span"));
/*  72 */       constraints.setColSpan(LwXmlReader.getRequiredInt(gridElement, "col-span"));
/*  73 */       constraints.setVSizePolicy(LwXmlReader.getRequiredInt(gridElement, "vsize-policy"));
/*  74 */       constraints.setHSizePolicy(LwXmlReader.getRequiredInt(gridElement, "hsize-policy"));
/*  75 */       constraints.setAnchor(LwXmlReader.getRequiredInt(gridElement, "anchor"));
/*  76 */       constraints.setFill(LwXmlReader.getRequiredInt(gridElement, "fill"));
/*  77 */       constraints.setIndent(LwXmlReader.getOptionalInt(gridElement, "indent", 0));
/*  78 */       constraints.setUseParentLayout(LwXmlReader.getOptionalBoolean(gridElement, "use-parent-layout", false));
/*     */ 
/*     */       
/*  81 */       Element minSizeElement = LwXmlReader.getChild(gridElement, "minimum-size");
/*  82 */       if (minSizeElement != null) {
/*  83 */         constraints.myMinimumSize.width = LwXmlReader.getRequiredInt(minSizeElement, "width");
/*  84 */         constraints.myMinimumSize.height = LwXmlReader.getRequiredInt(minSizeElement, "height");
/*     */       } 
/*     */ 
/*     */       
/*  88 */       Element prefSizeElement = LwXmlReader.getChild(gridElement, "preferred-size");
/*  89 */       if (prefSizeElement != null) {
/*  90 */         constraints.myPreferredSize.width = LwXmlReader.getRequiredInt(prefSizeElement, "width");
/*  91 */         constraints.myPreferredSize.height = LwXmlReader.getRequiredInt(prefSizeElement, "height");
/*     */       } 
/*     */ 
/*     */       
/*  95 */       Element maxSizeElement = LwXmlReader.getChild(gridElement, "maximum-size");
/*  96 */       if (maxSizeElement != null) {
/*  97 */         constraints.myMaximumSize.width = LwXmlReader.getRequiredInt(maxSizeElement, "width");
/*  98 */         constraints.myMaximumSize.height = LwXmlReader.getRequiredInt(maxSizeElement, "height");
/*     */       } 
/*     */       
/* 101 */       component.getConstraints().restore(constraints);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\GridLayoutSerializer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */