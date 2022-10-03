/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.PopupWindowDecorator;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
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
/*     */ class ParameterizedCompletionDescriptionToolTip
/*     */ {
/*     */   private AutoCompletion ac;
/*     */   private JWindow tooltip;
/*     */   private JLabel descLabel;
/*     */   private ParameterizedCompletion pc;
/*     */   private boolean overflow;
/*     */   
/*     */   ParameterizedCompletionDescriptionToolTip(Window owner, ParameterizedCompletionContext context, AutoCompletion ac, ParameterizedCompletion pc) {
/*  68 */     this.tooltip = new JWindow(owner);
/*     */     
/*  70 */     this.ac = ac;
/*  71 */     this.pc = pc;
/*     */     
/*  73 */     this.descLabel = new JLabel();
/*  74 */     this.descLabel.setBorder(BorderFactory.createCompoundBorder(
/*  75 */           TipUtil.getToolTipBorder(), 
/*  76 */           BorderFactory.createEmptyBorder(2, 5, 2, 5)));
/*  77 */     this.descLabel.setOpaque(true);
/*  78 */     this.descLabel.setBackground(TipUtil.getToolTipBackground());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     JPanel panel = new JPanel(new BorderLayout());
/*  86 */     panel.add(this.descLabel);
/*  87 */     this.tooltip.setContentPane(panel);
/*     */ 
/*     */     
/*  90 */     PopupWindowDecorator decorator = PopupWindowDecorator.get();
/*  91 */     if (decorator != null) {
/*  92 */       decorator.decorate(this.tooltip);
/*     */     }
/*     */     
/*  95 */     updateText(0);
/*     */     
/*  97 */     this.tooltip.setFocusableWindowState(false);
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
/*     */   public boolean isVisible() {
/* 109 */     return this.tooltip.isVisible();
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
/*     */   public void setLocationRelativeTo(Rectangle r) {
/* 125 */     Rectangle screenBounds = Util.getScreenBoundsForPoint(r.x, r.y);
/*     */ 
/*     */ 
/*     */     
/* 129 */     int y = r.y - 5 - this.tooltip.getHeight();
/* 130 */     if (y < 0) {
/* 131 */       y = r.y + r.height + 5;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 136 */     int x = r.x;
/* 137 */     if (x < screenBounds.x) {
/* 138 */       x = screenBounds.x;
/*     */     }
/* 140 */     else if (x + this.tooltip.getWidth() > screenBounds.x + screenBounds.width) {
/* 141 */       x = screenBounds.x + screenBounds.width - this.tooltip.getWidth();
/*     */     } 
/*     */     
/* 144 */     this.tooltip.setLocation(x, y);
/* 145 */     EventQueue.invokeLater(this.tooltip::pack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 156 */     this.tooltip.setVisible(visible);
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
/*     */   public boolean updateText(int selectedParam) {
/* 169 */     StringBuilder sb = new StringBuilder("<html>");
/* 170 */     int paramCount = this.pc.getParamCount();
/*     */     
/* 172 */     if (this.overflow) {
/* 173 */       if (selectedParam < paramCount) {
/* 174 */         String temp = this.pc.getParam(Math.min(paramCount - 1, selectedParam)).toString();
/* 175 */         sb.append("...<b>")
/* 176 */           .append(RSyntaxUtilities.escapeForHtml(temp, "<br>", false))
/* 177 */           .append("</b>...");
/*     */ 
/*     */ 
/*     */         
/* 181 */         if (!isVisible()) {
/* 182 */           setVisible(true);
/*     */         }
/*     */       } else {
/*     */         
/* 186 */         setVisible(false);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 191 */       for (int i = 0; i < paramCount; i++) {
/*     */         
/* 193 */         if (i == selectedParam) {
/* 194 */           sb.append("<b>");
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 200 */         String temp = this.pc.getParam(i).toString();
/* 201 */         sb.append(RSyntaxUtilities.escapeForHtml(temp, "<br>", false));
/*     */         
/* 203 */         if (i == selectedParam) {
/* 204 */           sb.append("</b>");
/*     */         }
/* 206 */         if (i < paramCount - 1) {
/* 207 */           sb.append(this.pc.getProvider().getParameterListSeparator());
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 213 */     if (selectedParam >= 0 && selectedParam < paramCount) {
/*     */       
/* 215 */       ParameterizedCompletion.Parameter param = this.pc.getParam(selectedParam);
/* 216 */       String desc = param.getDescription();
/* 217 */       if (desc != null) {
/* 218 */         sb.append("<br>");
/* 219 */         sb.append(desc);
/*     */       } 
/*     */     } 
/*     */     
/* 223 */     this.descLabel.setText(sb.toString());
/* 224 */     if (!this.overflow && sb.length() > this.ac.getParameterDescriptionTruncateThreshold()) {
/* 225 */       this.overflow = true;
/* 226 */       updateText(selectedParam);
/*     */     } else {
/*     */       
/* 229 */       this.overflow = false;
/* 230 */       this.tooltip.pack();
/*     */     } 
/*     */     
/* 233 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 243 */     SwingUtilities.updateComponentTreeUI(this.tooltip);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\ParameterizedCompletionDescriptionToolTip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */