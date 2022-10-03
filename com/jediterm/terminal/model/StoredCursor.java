/*    */ package com.jediterm.terminal.model;
/*    */ 
/*    */ import com.jediterm.terminal.TextStyle;
/*    */ import com.jediterm.terminal.emulator.charset.CharacterSet;
/*    */ import com.jediterm.terminal.emulator.charset.GraphicSetState;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StoredCursor
/*    */ {
/*    */   private final int myCursorX;
/*    */   private final int myCursorY;
/*    */   private final TextStyle myTextStyle;
/*    */   private final int myGLMapping;
/*    */   private final int myGRMapping;
/*    */   private final boolean myAutoWrap;
/*    */   private final boolean myOriginMode;
/*    */   private final int myGLOverride;
/* 37 */   private final CharacterSet[] myDesignations = new CharacterSet[4];
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StoredCursor(int cursorX, int cursorY, @NotNull TextStyle textStyle, boolean autoWrap, boolean originMode, GraphicSetState graphicSetState) {
/* 45 */     this.myCursorX = cursorX;
/* 46 */     this.myCursorY = cursorY;
/* 47 */     this.myTextStyle = textStyle;
/* 48 */     this.myAutoWrap = autoWrap;
/* 49 */     this.myOriginMode = originMode;
/* 50 */     this.myGLMapping = graphicSetState.getGL().getIndex();
/* 51 */     this.myGRMapping = graphicSetState.getGR().getIndex();
/* 52 */     this.myGLOverride = graphicSetState.getGLOverrideIndex();
/* 53 */     for (int i = 0; i < 4; i++) {
/* 54 */       this.myDesignations[i] = graphicSetState.getGraphicSet(i).getDesignation();
/*    */     }
/*    */   }
/*    */   
/*    */   public int getCursorX() {
/* 59 */     return this.myCursorX;
/*    */   }
/*    */   
/*    */   public int getCursorY() {
/* 63 */     return this.myCursorY;
/*    */   }
/*    */   
/*    */   public TextStyle getTextStyle() {
/* 67 */     return this.myTextStyle;
/*    */   }
/*    */   
/*    */   public int getGLMapping() {
/* 71 */     return this.myGLMapping;
/*    */   }
/*    */   
/*    */   public int getGRMapping() {
/* 75 */     return this.myGRMapping;
/*    */   }
/*    */   
/*    */   public boolean isAutoWrap() {
/* 79 */     return this.myAutoWrap;
/*    */   }
/*    */   
/*    */   public boolean isOriginMode() {
/* 83 */     return this.myOriginMode;
/*    */   }
/*    */   
/*    */   public int getGLOverride() {
/* 87 */     return this.myGLOverride;
/*    */   }
/*    */   
/*    */   public CharacterSet[] getDesignations() {
/* 91 */     return this.myDesignations;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\StoredCursor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */