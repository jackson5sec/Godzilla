/*     */ package com.intellij.uiDesigner.shared;
/*     */ 
/*     */ import com.intellij.uiDesigner.compiler.UnexpectedFormElementException;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.border.Border;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BorderType
/*     */ {
/*  28 */   public static final BorderType NONE = new BorderType("none", "None", null, null);
/*  29 */   public static final BorderType BEVEL_LOWERED = new BorderType("bevel-lowered", "Bevel Lowered", BorderFactory.createLoweredBevelBorder(), "createLoweredBevelBorder");
/*  30 */   public static final BorderType BEVEL_RAISED = new BorderType("bevel-raised", "Bevel Raised", BorderFactory.createRaisedBevelBorder(), "createRaisedBevelBorder");
/*  31 */   public static final BorderType ETCHED = new BorderType("etched", "Etched", BorderFactory.createEtchedBorder(), "createEtchedBorder");
/*  32 */   public static final BorderType LINE = new BorderType("line", "Line", BorderFactory.createLineBorder(Color.BLACK), "createLineBorder");
/*  33 */   public static final BorderType EMPTY = new BorderType("empty", "Empty", BorderFactory.createEmptyBorder(0, 0, 0, 0), "createEmptyBorder");
/*     */   
/*     */   private final String myId;
/*     */   private final String myName;
/*     */   private final Border myBorder;
/*     */   private final String myBorderFactoryMethodName;
/*     */   
/*     */   private BorderType(String id, String name, Border border, String borderFactoryMethodName) {
/*  41 */     this.myId = id;
/*  42 */     this.myName = name;
/*  43 */     this.myBorder = border;
/*  44 */     this.myBorderFactoryMethodName = borderFactoryMethodName;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  48 */     return this.myId;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  52 */     return this.myName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Border createBorder(String title, int titleJustification, int titlePosition, Font titleFont, Color titleColor, Insets borderSize, Color borderColor) {
/*  62 */     Border baseBorder = this.myBorder;
/*  63 */     if (equals(EMPTY) && borderSize != null) {
/*  64 */       baseBorder = BorderFactory.createEmptyBorder(borderSize.top, borderSize.left, borderSize.bottom, borderSize.right);
/*     */     }
/*  66 */     else if (equals(LINE) && borderColor != null) {
/*  67 */       baseBorder = BorderFactory.createLineBorder(borderColor);
/*     */     } 
/*     */     
/*  70 */     if (title != null) {
/*  71 */       return BorderFactory.createTitledBorder(baseBorder, title, titleJustification, titlePosition, titleFont, titleColor);
/*     */     }
/*     */     
/*  74 */     return baseBorder;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBorderFactoryMethodName() {
/*  79 */     return this.myBorderFactoryMethodName;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  83 */     if (o instanceof BorderType) {
/*  84 */       return this.myId.equals(((BorderType)o).myId);
/*     */     }
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  90 */     return 0;
/*     */   }
/*     */   
/*     */   public static BorderType valueOf(String name) {
/*  94 */     BorderType[] allTypes = getAllTypes();
/*  95 */     for (int i = 0; i < allTypes.length; i++) {
/*  96 */       if (allTypes[i].getId().equals(name)) return allTypes[i]; 
/*     */     } 
/*  98 */     throw new UnexpectedFormElementException("unknown type: " + name);
/*     */   }
/*     */   
/*     */   public static BorderType[] getAllTypes() {
/* 102 */     return new BorderType[] { NONE, EMPTY, BEVEL_LOWERED, BEVEL_RAISED, ETCHED, LINE };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\shared\BorderType.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */