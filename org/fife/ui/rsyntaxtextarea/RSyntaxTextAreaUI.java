/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.InputMapUIResource;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Highlighter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.View;
/*     */ import org.fife.ui.rtextarea.RTextArea;
/*     */ import org.fife.ui.rtextarea.RTextAreaUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RSyntaxTextAreaUI
/*     */   extends RTextAreaUI
/*     */ {
/*     */   private static final String SHARED_ACTION_MAP_NAME = "RSyntaxTextAreaUI.actionMap";
/*     */   private static final String SHARED_INPUT_MAP_NAME = "RSyntaxTextAreaUI.inputMap";
/*  43 */   private static final EditorKit DEFAULT_KIT = (EditorKit)new RSyntaxTextAreaEditorKit();
/*     */ 
/*     */   
/*     */   public static ComponentUI createUI(JComponent ta) {
/*  47 */     return (ComponentUI)new RSyntaxTextAreaUI(ta);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RSyntaxTextAreaUI(JComponent rSyntaxTextArea) {
/*  55 */     super(rSyntaxTextArea);
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
/*     */   public View create(Element elem) {
/*  67 */     RTextArea c = getRTextArea();
/*  68 */     if (c instanceof RSyntaxTextArea) {
/*  69 */       View v; RSyntaxTextArea area = (RSyntaxTextArea)c;
/*     */       
/*  71 */       if (area.getLineWrap()) {
/*  72 */         v = new WrappedSyntaxView(elem);
/*     */       } else {
/*     */         
/*  75 */         v = new SyntaxView(elem);
/*     */       } 
/*  77 */       return v;
/*     */     } 
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Highlighter createHighlighter() {
/*  90 */     return (Highlighter)new RSyntaxTextAreaHighlighter();
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
/*     */   protected String getActionMapName() {
/* 103 */     return "RSyntaxTextAreaUI.actionMap";
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
/*     */   public EditorKit getEditorKit(JTextComponent tc) {
/* 116 */     return DEFAULT_KIT;
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
/*     */   protected InputMap getRTextAreaInputMap() {
/*     */     RSyntaxTextAreaDefaultInputMap rSyntaxTextAreaDefaultInputMap;
/* 131 */     InputMap map = new InputMapUIResource();
/* 132 */     InputMap shared = (InputMap)UIManager.get("RSyntaxTextAreaUI.inputMap");
/* 133 */     if (shared == null) {
/* 134 */       rSyntaxTextAreaDefaultInputMap = new RSyntaxTextAreaDefaultInputMap();
/* 135 */       UIManager.put("RSyntaxTextAreaUI.inputMap", rSyntaxTextAreaDefaultInputMap);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 140 */     map.setParent((InputMap)rSyntaxTextAreaDefaultInputMap);
/* 141 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintEditorAugmentations(Graphics g) {
/* 147 */     super.paintEditorAugmentations(g);
/* 148 */     paintMatchedBracket(g);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintMatchedBracket(Graphics g) {
/* 158 */     RSyntaxTextArea rsta = (RSyntaxTextArea)this.textArea;
/* 159 */     if (rsta.isBracketMatchingEnabled()) {
/* 160 */       Rectangle match = rsta.getMatchRectangle();
/* 161 */       if (match != null) {
/* 162 */         paintMatchedBracketImpl(g, rsta, match);
/*     */       }
/* 164 */       if (rsta.getPaintMatchedBracketPair()) {
/* 165 */         Rectangle dotRect = rsta.getDotRectangle();
/* 166 */         if (dotRect != null) {
/* 167 */           paintMatchedBracketImpl(g, rsta, dotRect);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintMatchedBracketImpl(Graphics g, RSyntaxTextArea rsta, Rectangle r) {
/* 178 */     if (rsta.getAnimateBracketMatching()) {
/* 179 */       Color bg = rsta.getMatchedBracketBGColor();
/* 180 */       int arcWH = 5;
/* 181 */       if (bg != null) {
/* 182 */         g.setColor(bg);
/* 183 */         g.fillRoundRect(r.x, r.y, r.width, r.height - 1, 5, 5);
/*     */       } 
/* 185 */       g.setColor(rsta.getMatchedBracketBorderColor());
/* 186 */       g.drawRoundRect(r.x, r.y, r.width, r.height - 1, 5, 5);
/*     */     } else {
/*     */       
/* 189 */       Color bg = rsta.getMatchedBracketBGColor();
/* 190 */       if (bg != null) {
/* 191 */         g.setColor(bg);
/* 192 */         g.fillRect(r.x, r.y, r.width, r.height - 1);
/*     */       } 
/* 194 */       g.setColor(rsta.getMatchedBracketBorderColor());
/* 195 */       g.drawRect(r.x, r.y, r.width, r.height - 1);
/*     */     } 
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
/*     */   protected void propertyChange(PropertyChangeEvent e) {
/* 209 */     String name = e.getPropertyName();
/*     */ 
/*     */ 
/*     */     
/* 213 */     if (name.equals("RSTA.syntaxScheme")) {
/* 214 */       modelChanged();
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 219 */       super.propertyChange(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void refreshSyntaxHighlighting() {
/* 230 */     modelChanged();
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
/*     */   public int yForLine(int line) throws BadLocationException {
/* 243 */     Rectangle alloc = getVisibleEditorRect();
/* 244 */     if (alloc != null) {
/* 245 */       RSTAView view = (RSTAView)getRootView((JTextComponent)this.textArea).getView(0);
/* 246 */       return view.yForLine(alloc, line);
/*     */     } 
/* 248 */     return -1;
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
/*     */   public int yForLineContaining(int offs) throws BadLocationException {
/* 260 */     Rectangle alloc = getVisibleEditorRect();
/* 261 */     if (alloc != null) {
/* 262 */       RSTAView view = (RSTAView)getRootView((JTextComponent)this.textArea).getView(0);
/* 263 */       return view.yForLineContaining(alloc, offs);
/*     */     } 
/* 265 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RSyntaxTextAreaUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */