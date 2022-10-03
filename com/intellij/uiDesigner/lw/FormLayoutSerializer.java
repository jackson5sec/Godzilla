/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import com.intellij.uiDesigner.compiler.Utils;
/*     */ import com.jgoodies.forms.layout.CellConstraints;
/*     */ import com.jgoodies.forms.layout.ColumnSpec;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import com.jgoodies.forms.layout.RowSpec;
/*     */ import java.awt.LayoutManager;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class FormLayoutSerializer
/*     */   extends GridLayoutSerializer
/*     */ {
/*  37 */   public static FormLayoutSerializer INSTANCE = new FormLayoutSerializer();
/*     */   
/*  39 */   public static CellConstraints.Alignment[] ourHorizontalAlignments = new CellConstraints.Alignment[] { CellConstraints.LEFT, CellConstraints.CENTER, CellConstraints.RIGHT, CellConstraints.FILL };
/*     */ 
/*     */   
/*  42 */   public static CellConstraints.Alignment[] ourVerticalAlignments = new CellConstraints.Alignment[] { CellConstraints.TOP, CellConstraints.CENTER, CellConstraints.BOTTOM, CellConstraints.FILL };
/*     */ 
/*     */ 
/*     */   
/*     */   void readLayout(Element element, LwContainer container) {
/*  47 */     FormLayout layout = new FormLayout();
/*  48 */     List rowSpecs = element.getChildren("rowspec", element.getNamespace());
/*  49 */     for (Iterator iterator = rowSpecs.iterator(); iterator.hasNext(); ) {
/*  50 */       Element rowSpecElement = iterator.next();
/*  51 */       String spec = LwXmlReader.getRequiredString(rowSpecElement, "value");
/*  52 */       layout.appendRow(new RowSpec(spec));
/*     */     } 
/*     */     
/*  55 */     List colSpecs = element.getChildren("colspec", element.getNamespace());
/*  56 */     for (Iterator iterator1 = colSpecs.iterator(); iterator1.hasNext(); ) {
/*  57 */       Element colSpecElement = iterator1.next();
/*  58 */       String spec = LwXmlReader.getRequiredString(colSpecElement, "value");
/*  59 */       layout.appendColumn(new ColumnSpec(spec));
/*     */     } 
/*     */     
/*  62 */     int[][] rowGroups = readGroups(element, "rowgroup");
/*  63 */     int[][] colGroups = readGroups(element, "colgroup");
/*  64 */     if (rowGroups != null) {
/*  65 */       layout.setRowGroups(rowGroups);
/*     */     }
/*  67 */     if (colGroups != null) {
/*  68 */       layout.setColumnGroups(colGroups);
/*     */     }
/*  70 */     container.setLayout((LayoutManager)layout);
/*     */   }
/*     */   
/*     */   private static int[][] readGroups(Element element, String elementName) {
/*  74 */     List groupElements = element.getChildren(elementName, element.getNamespace());
/*  75 */     if (groupElements.size() == 0) return (int[][])null; 
/*  76 */     int[][] groups = new int[groupElements.size()][];
/*  77 */     for (int i = 0; i < groupElements.size(); i++) {
/*  78 */       Element groupElement = groupElements.get(i);
/*  79 */       List groupMembers = groupElement.getChildren("member", element.getNamespace());
/*  80 */       groups[i] = new int[groupMembers.size()];
/*  81 */       for (int j = 0; j < groupMembers.size(); j++) {
/*  82 */         groups[i][j] = LwXmlReader.getRequiredInt((Element)groupMembers.get(j), "index");
/*     */       }
/*     */     } 
/*  85 */     return groups;
/*     */   }
/*     */   
/*     */   void readChildConstraints(Element constraintsElement, LwComponent component) {
/*  89 */     super.readChildConstraints(constraintsElement, component);
/*  90 */     CellConstraints cc = new CellConstraints();
/*  91 */     Element formsElement = LwXmlReader.getChild(constraintsElement, "forms");
/*  92 */     if (formsElement != null) {
/*  93 */       if (formsElement.getAttributeValue("top") != null) {
/*  94 */         cc.insets = LwXmlReader.readInsets(formsElement);
/*     */       }
/*  96 */       if (!LwXmlReader.getOptionalBoolean(formsElement, "defaultalign-horz", true)) {
/*  97 */         cc.hAlign = ourHorizontalAlignments[Utils.alignFromConstraints(component.getConstraints(), true)];
/*     */       }
/*  99 */       if (!LwXmlReader.getOptionalBoolean(formsElement, "defaultalign-vert", true)) {
/* 100 */         cc.vAlign = ourVerticalAlignments[Utils.alignFromConstraints(component.getConstraints(), false)];
/*     */       }
/*     */     } 
/* 103 */     component.setCustomLayoutConstraints(cc);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\FormLayoutSerializer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */