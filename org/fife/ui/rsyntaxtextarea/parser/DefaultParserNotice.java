/*     */ package org.fife.ui.rsyntaxtextarea.parser;
/*     */ 
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultParserNotice
/*     */   implements ParserNotice
/*     */ {
/*     */   private Parser parser;
/*     */   private ParserNotice.Level level;
/*     */   private int line;
/*     */   private int offset;
/*     */   private int length;
/*     */   private boolean showInEditor;
/*     */   private Color color;
/*     */   private String message;
/*     */   private String toolTipText;
/*  35 */   private static final Color[] DEFAULT_COLORS = new Color[] { new Color(255, 0, 128), new Color(244, 200, 45), Color.gray };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultParserNotice(Parser parser, String msg, int line) {
/*  50 */     this(parser, msg, line, -1, -1);
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
/*     */   public DefaultParserNotice(Parser parser, String message, int line, int offset, int length) {
/*  67 */     this.parser = parser;
/*  68 */     this.message = message;
/*  69 */     this.line = line;
/*  70 */     this.offset = offset;
/*  71 */     this.length = length;
/*  72 */     setLevel(ParserNotice.Level.ERROR);
/*  73 */     setShowInEditor(true);
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
/*     */   public int compareTo(ParserNotice other) {
/*  86 */     int diff = -1;
/*  87 */     if (other != null) {
/*  88 */       diff = this.level.getNumericValue() - other.getLevel().getNumericValue();
/*  89 */       if (diff == 0) {
/*  90 */         diff = this.line - other.getLine();
/*  91 */         if (diff == 0) {
/*  92 */           diff = this.message.compareTo(other.getMessage());
/*     */         }
/*     */       } 
/*     */     } 
/*  96 */     return diff;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsPosition(int pos) {
/* 102 */     return (this.offset <= pos && pos < this.offset + this.length);
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
/*     */   public boolean equals(Object obj) {
/* 114 */     if (!(obj instanceof ParserNotice)) {
/* 115 */       return false;
/*     */     }
/* 117 */     return (compareTo((ParserNotice)obj) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 123 */     Color c = this.color;
/* 124 */     if (c == null) {
/* 125 */       c = DEFAULT_COLORS[getLevel().getNumericValue()];
/*     */     }
/* 127 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getKnowsOffsetAndLength() {
/* 133 */     return (this.offset >= 0 && this.length >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLength() {
/* 139 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParserNotice.Level getLevel() {
/* 145 */     return this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/* 151 */     return this.line;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMessage() {
/* 157 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset() {
/* 163 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser getParser() {
/* 169 */     return this.parser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShowInEditor() {
/* 175 */     return this.showInEditor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToolTipText() {
/* 181 */     return (this.toolTipText != null) ? this.toolTipText : getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 192 */     return this.line << 16 | this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 203 */     this.color = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLevel(ParserNotice.Level level) {
/* 214 */     if (level == null) {
/* 215 */       level = ParserNotice.Level.ERROR;
/*     */     }
/* 217 */     this.level = level;
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
/*     */   public void setShowInEditor(boolean show) {
/* 229 */     this.showInEditor = show;
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
/*     */   public void setToolTipText(String text) {
/* 242 */     this.toolTipText = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 253 */     return "Line " + getLine() + ": " + getMessage();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\parser\DefaultParserNotice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */