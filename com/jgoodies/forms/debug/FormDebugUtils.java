/*     */ package com.jgoodies.forms.debug;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.forms.layout.CellConstraints;
/*     */ import com.jgoodies.forms.layout.ColumnSpec;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import com.jgoodies.forms.layout.RowSpec;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import javax.swing.JLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FormDebugUtils
/*     */ {
/*     */   public static void dumpAll(Container container) {
/*  84 */     if (!(container.getLayout() instanceof FormLayout)) {
/*  85 */       System.out.println("The container's layout is not a FormLayout.");
/*     */       return;
/*     */     } 
/*  88 */     FormLayout layout = (FormLayout)container.getLayout();
/*  89 */     dumpColumnSpecs(layout);
/*  90 */     dumpRowSpecs(layout);
/*  91 */     System.out.println();
/*  92 */     dumpColumnGroups(layout);
/*  93 */     dumpRowGroups(layout);
/*  94 */     System.out.println();
/*  95 */     dumpConstraints(container);
/*  96 */     dumpGridBounds(container);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dumpColumnSpecs(FormLayout layout) {
/* 106 */     System.out.print("COLUMN SPECS:");
/* 107 */     for (int col = 1; col <= layout.getColumnCount(); col++) {
/* 108 */       ColumnSpec colSpec = layout.getColumnSpec(col);
/* 109 */       System.out.print(colSpec.toShortString());
/* 110 */       if (col < layout.getColumnCount()) {
/* 111 */         System.out.print(", ");
/*     */       }
/*     */     } 
/* 114 */     System.out.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dumpRowSpecs(FormLayout layout) {
/* 124 */     System.out.print("ROW SPECS:   ");
/* 125 */     for (int row = 1; row <= layout.getRowCount(); row++) {
/* 126 */       RowSpec rowSpec = layout.getRowSpec(row);
/* 127 */       System.out.print(rowSpec.toShortString());
/* 128 */       if (row < layout.getRowCount()) {
/* 129 */         System.out.print(", ");
/*     */       }
/*     */     } 
/* 132 */     System.out.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dumpColumnGroups(FormLayout layout) {
/* 142 */     dumpGroups("COLUMN GROUPS: ", layout.getColumnGroups());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dumpRowGroups(FormLayout layout) {
/* 152 */     dumpGroups("ROW GROUPS:    ", layout.getRowGroups());
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
/*     */   public static void dumpGridBounds(Container container) {
/* 164 */     System.out.println("GRID BOUNDS");
/* 165 */     dumpGridBounds(getLayoutInfo(container));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dumpGridBounds(FormLayout.LayoutInfo layoutInfo) {
/* 175 */     System.out.print("COLUMN ORIGINS: ");
/* 176 */     for (int columnOrigin : layoutInfo.columnOrigins) {
/* 177 */       System.out.print(columnOrigin + " ");
/*     */     }
/* 179 */     System.out.println();
/*     */     
/* 181 */     System.out.print("ROW ORIGINS:    ");
/* 182 */     for (int rowOrigin : layoutInfo.rowOrigins) {
/* 183 */       System.out.print(rowOrigin + " ");
/*     */     }
/* 185 */     System.out.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void dumpConstraints(Container container) {
/* 195 */     System.out.println("COMPONENT CONSTRAINTS");
/* 196 */     if (!(container.getLayout() instanceof FormLayout)) {
/* 197 */       System.out.println("The container's layout is not a FormLayout.");
/*     */       return;
/*     */     } 
/* 200 */     FormLayout layout = (FormLayout)container.getLayout();
/* 201 */     int childCount = container.getComponentCount();
/* 202 */     for (int i = 0; i < childCount; i++) {
/* 203 */       Component child = container.getComponent(i);
/* 204 */       CellConstraints cc = layout.getConstraints(child);
/* 205 */       String ccString = (cc == null) ? "no constraints" : cc.toShortString(layout);
/*     */ 
/*     */       
/* 208 */       System.out.print(ccString);
/* 209 */       System.out.print("; ");
/* 210 */       String childType = child.getClass().getName();
/* 211 */       System.out.print(childType);
/* 212 */       if (child instanceof JLabel) {
/* 213 */         JLabel label = (JLabel)child;
/* 214 */         System.out.print("      \"" + label.getText() + "\"");
/*     */       } 
/* 216 */       if (child.getName() != null) {
/* 217 */         System.out.print("; name=");
/* 218 */         System.out.print(child.getName());
/*     */       } 
/* 220 */       System.out.println();
/*     */     } 
/* 222 */     System.out.println();
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
/*     */   private static void dumpGroups(String title, int[][] allGroups) {
/* 235 */     System.out.print(title + " {");
/* 236 */     for (int group = 0; group < allGroups.length; group++) {
/* 237 */       int[] groupIndices = allGroups[group];
/* 238 */       System.out.print(" {");
/* 239 */       for (int i = 0; i < groupIndices.length; i++) {
/* 240 */         System.out.print(groupIndices[i]);
/* 241 */         if (i < groupIndices.length - 1) {
/* 242 */           System.out.print(", ");
/*     */         }
/*     */       } 
/* 245 */       System.out.print("} ");
/* 246 */       if (group < allGroups.length - 1) {
/* 247 */         System.out.print(", ");
/*     */       }
/*     */     } 
/* 250 */     System.out.println("}");
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
/*     */   public static FormLayout.LayoutInfo getLayoutInfo(Container container) {
/* 263 */     Preconditions.checkNotNull(container, "The container must not be null.");
/* 264 */     Preconditions.checkArgument(container.getLayout() instanceof FormLayout, "The container must use an instance of FormLayout.");
/*     */     
/* 266 */     FormLayout layout = (FormLayout)container.getLayout();
/* 267 */     return layout.getLayoutInfo(container);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\debug\FormDebugUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */