/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Point;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.ui.rsyntaxtextarea.focusabletip.TipUtil;
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
/*     */ public class MatchedBracketPopup
/*     */   extends JWindow
/*     */ {
/*     */   public static final String PROPERTY_CONSIDER_TEXTAREA_BACKGROUND = "rsta.matchedBracket.considerTextAreaBackground";
/*     */   private RSyntaxTextArea textArea;
/*     */   private transient Listener listener;
/*     */   private static final int LEFT_EMPTY_BORDER = 5;
/*  74 */   private static final boolean CONSIDER_TEXTAREA_BG = Boolean.getBoolean("rsta.matchedBracket.considerTextAreaBackground");
/*     */ 
/*     */ 
/*     */   
/*     */   MatchedBracketPopup(Window parent, RSyntaxTextArea textArea, int offsToRender) {
/*  79 */     super(parent);
/*  80 */     this.textArea = textArea;
/*  81 */     JPanel cp = new JPanel(new BorderLayout());
/*  82 */     RSyntaxTextArea toolTipParam = CONSIDER_TEXTAREA_BG ? textArea : null;
/*  83 */     cp.setBorder(BorderFactory.createCompoundBorder(
/*  84 */           TipUtil.getToolTipBorder(toolTipParam), 
/*  85 */           BorderFactory.createEmptyBorder(2, 5, 5, 5)));
/*  86 */     cp.setBackground(TipUtil.getToolTipBackground(toolTipParam));
/*  87 */     setContentPane(cp);
/*     */     
/*  89 */     cp.add(new JLabel(getText(offsToRender)));
/*     */     
/*  91 */     installKeyBindings();
/*  92 */     this.listener = new Listener();
/*  93 */     setLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 103 */     Dimension size = super.getPreferredSize();
/* 104 */     if (size != null) {
/* 105 */       size.width = Math.min(size.width, 800);
/*     */     }
/* 107 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getText(int offsToRender) {
/* 113 */     int line = 0;
/*     */     try {
/* 115 */       line = this.textArea.getLineOfOffset(offsToRender);
/* 116 */     } catch (BadLocationException ble) {
/* 117 */       ble.printStackTrace();
/* 118 */       return null;
/*     */     } 
/*     */     
/* 121 */     int lastLine = line + 1;
/*     */ 
/*     */     
/* 124 */     if (line > 0) {
/*     */       try {
/* 126 */         int startOffs = this.textArea.getLineStartOffset(line);
/* 127 */         int length = this.textArea.getLineEndOffset(line) - startOffs;
/* 128 */         String text = this.textArea.getText(startOffs, length);
/* 129 */         if (text.trim().length() == 1) {
/* 130 */           line--;
/*     */         }
/* 132 */       } catch (BadLocationException ble) {
/* 133 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)this.textArea);
/* 134 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 138 */     Font font = this.textArea.getFontForTokenType(20);
/* 139 */     StringBuilder sb = new StringBuilder("<html>");
/* 140 */     sb.append("<style>body { font-size:\"").append(font.getSize());
/* 141 */     sb.append("pt\" }</style><nobr>");
/* 142 */     while (line < lastLine) {
/* 143 */       Token t = this.textArea.getTokenListForLine(line);
/* 144 */       while (t != null && t.isPaintable()) {
/* 145 */         t.appendHTMLRepresentation(sb, this.textArea, true, true);
/* 146 */         t = t.getNextToken();
/*     */       } 
/* 148 */       sb.append("<br>");
/* 149 */       line++;
/*     */     } 
/*     */     
/* 152 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void installKeyBindings() {
/* 162 */     InputMap im = getRootPane().getInputMap(1);
/*     */     
/* 164 */     ActionMap am = getRootPane().getActionMap();
/*     */     
/* 166 */     KeyStroke escapeKS = KeyStroke.getKeyStroke(27, 0);
/* 167 */     im.put(escapeKS, "onEscape");
/* 168 */     am.put("onEscape", new EscapeAction());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setLocation() {
/* 177 */     Point topLeft = this.textArea.getVisibleRect().getLocation();
/* 178 */     SwingUtilities.convertPointToScreen(topLeft, (Component)this.textArea);
/* 179 */     topLeft.y = Math.max(topLeft.y - 24, 0);
/* 180 */     setLocation(topLeft.x - 5, topLeft.y);
/*     */   }
/*     */ 
/*     */   
/*     */   private class EscapeAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private EscapeAction() {}
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 191 */       MatchedBracketPopup.this.listener.uninstallAndHide();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class Listener
/*     */     extends WindowAdapter
/*     */     implements ComponentListener
/*     */   {
/*     */     Listener() {
/* 204 */       MatchedBracketPopup.this.addWindowFocusListener(this);
/*     */ 
/*     */       
/* 207 */       Window parent = (Window)MatchedBracketPopup.this.getParent();
/* 208 */       parent.addWindowFocusListener(this);
/* 209 */       parent.addWindowListener(this);
/* 210 */       parent.addComponentListener(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void componentResized(ComponentEvent e) {
/* 216 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentMoved(ComponentEvent e) {
/* 221 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentShown(ComponentEvent e) {
/* 226 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentHidden(ComponentEvent e) {
/* 231 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowActivated(WindowEvent e) {
/* 236 */       checkForParentWindowEvent(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowLostFocus(WindowEvent e) {
/* 241 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowIconified(WindowEvent e) {
/* 246 */       checkForParentWindowEvent(e);
/*     */     }
/*     */     
/*     */     private boolean checkForParentWindowEvent(WindowEvent e) {
/* 250 */       if (e.getSource() == MatchedBracketPopup.this.getParent()) {
/* 251 */         uninstallAndHide();
/* 252 */         return true;
/*     */       } 
/* 254 */       return false;
/*     */     }
/*     */     
/*     */     private void uninstallAndHide() {
/* 258 */       Window parent = (Window)MatchedBracketPopup.this.getParent();
/* 259 */       parent.removeWindowFocusListener(this);
/* 260 */       parent.removeWindowListener(this);
/* 261 */       parent.removeComponentListener(this);
/* 262 */       MatchedBracketPopup.this.removeWindowFocusListener(this);
/* 263 */       MatchedBracketPopup.this.setVisible(false);
/* 264 */       MatchedBracketPopup.this.dispose();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\MatchedBracketPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */