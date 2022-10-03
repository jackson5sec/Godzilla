/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.util.List;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.DefaultListModel;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.Caret;
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
/*     */ class ClipboardHistoryPopup
/*     */   extends JWindow
/*     */ {
/*     */   private RTextArea textArea;
/*     */   private ChoiceList list;
/*     */   private transient Listener listener;
/*     */   private boolean prevCaretAlwaysVisible;
/*     */   private static final int VERTICAL_SPACE = 1;
/*     */   private static final String MSG = "org.fife.ui.rtextarea.RTextArea";
/*     */   
/*     */   ClipboardHistoryPopup(Window parent, RTextArea textArea) {
/*  78 */     super(parent);
/*  79 */     this.textArea = textArea;
/*     */     
/*  81 */     JPanel cp = new JPanel(new BorderLayout());
/*  82 */     cp.setBorder(BorderFactory.createCompoundBorder(
/*  83 */           TipUtil.getToolTipBorder(), 
/*  84 */           BorderFactory.createEmptyBorder(2, 5, 5, 5)));
/*  85 */     cp.setBackground(TipUtil.getToolTipBackground());
/*  86 */     setContentPane(cp);
/*     */     
/*  88 */     ResourceBundle msg = ResourceBundle.getBundle("org.fife.ui.rtextarea.RTextArea");
/*     */     
/*  90 */     JLabel title = new JLabel(msg.getString("Action.ClipboardHistory.Popup.Label"));
/*  91 */     cp.add(title, "North");
/*     */     
/*  93 */     this.list = new ChoiceList();
/*  94 */     JScrollPane sp = new JScrollPane(this.list);
/*  95 */     sp.setHorizontalScrollBarPolicy(31);
/*  96 */     cp.add(sp);
/*     */     
/*  98 */     installKeyBindings();
/*  99 */     this.listener = new Listener();
/* 100 */     setLocation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 110 */     Dimension size = super.getPreferredSize();
/* 111 */     if (size != null) {
/* 112 */       size.width = Math.min(size.width, 300);
/* 113 */       size.width = Math.max(size.width, 200);
/*     */     } 
/* 115 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void insertSelectedItem() {
/* 123 */     LabelValuePair lvp = this.list.getSelectedValue();
/* 124 */     if (lvp != null) {
/* 125 */       this.listener.uninstallAndHide();
/* 126 */       String text = lvp.value;
/* 127 */       this.textArea.replaceSelection(text);
/* 128 */       ClipboardHistory.get().add(text);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void installKeyBindings() {
/* 138 */     InputMap im = getRootPane().getInputMap(1);
/*     */     
/* 140 */     ActionMap am = getRootPane().getActionMap();
/*     */     
/* 142 */     KeyStroke escapeKS = KeyStroke.getKeyStroke(27, 0);
/* 143 */     im.put(escapeKS, "onEscape");
/* 144 */     am.put("onEscape", new EscapeAction());
/* 145 */     this.list.getInputMap().remove(escapeKS);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContents(List<String> contents) {
/* 150 */     this.list.setContents(contents);
/*     */     
/* 152 */     pack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setLocation() {
/*     */     Rectangle r;
/*     */     try {
/* 164 */       r = this.textArea.modelToView(this.textArea.getCaretPosition());
/* 165 */     } catch (Exception e) {
/* 166 */       e.printStackTrace();
/*     */       return;
/*     */     } 
/* 169 */     Point p = r.getLocation();
/* 170 */     SwingUtilities.convertPointToScreen(p, this.textArea);
/* 171 */     r.x = p.x;
/* 172 */     r.y = p.y;
/*     */     
/* 174 */     Rectangle screenBounds = TipUtil.getScreenBoundsForPoint(r.x, r.y);
/*     */ 
/*     */     
/* 177 */     int totalH = getHeight();
/*     */ 
/*     */ 
/*     */     
/* 181 */     int y = r.y + r.height + 1;
/* 182 */     if (y + totalH > screenBounds.height) {
/* 183 */       y = r.y - 1 - getHeight();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 188 */     int x = r.x;
/* 189 */     if (!this.textArea.getComponentOrientation().isLeftToRight()) {
/* 190 */       x -= getWidth();
/*     */     }
/* 192 */     if (x < screenBounds.x) {
/* 193 */       x = screenBounds.x;
/*     */     }
/* 195 */     else if (x + getWidth() > screenBounds.x + screenBounds.width) {
/* 196 */       x = screenBounds.x + screenBounds.width - getWidth();
/*     */     } 
/*     */     
/* 199 */     setLocation(x, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 206 */     if (this.list.getModel().getSize() == 0) {
/* 207 */       UIManager.getLookAndFeel().provideErrorFeedback(this.textArea);
/*     */       return;
/*     */     } 
/* 210 */     super.setVisible(visible);
/* 211 */     updateTextAreaCaret(visible);
/* 212 */     if (visible) {
/* 213 */       SwingUtilities.invokeLater(() -> {
/*     */             requestFocus();
/*     */             if (this.list.getModel().getSize() > 0) {
/*     */               this.list.setSelectedIndex(0);
/*     */             }
/*     */             this.list.requestFocusInWindow();
/*     */           });
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
/*     */   private void updateTextAreaCaret(boolean visible) {
/* 232 */     Caret caret = this.textArea.getCaret();
/* 233 */     if (caret instanceof ConfigurableCaret) {
/* 234 */       ConfigurableCaret cc = (ConfigurableCaret)caret;
/* 235 */       if (visible) {
/* 236 */         this.prevCaretAlwaysVisible = cc.isAlwaysVisible();
/* 237 */         cc.setAlwaysVisible(true);
/*     */       } else {
/*     */         
/* 240 */         cc.setAlwaysVisible(this.prevCaretAlwaysVisible);
/*     */       } 
/*     */     } 
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
/* 253 */       ClipboardHistoryPopup.this.listener.uninstallAndHide();
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
/* 266 */       ClipboardHistoryPopup.this.addWindowFocusListener(this);
/* 267 */       ClipboardHistoryPopup.this.list.addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mouseClicked(MouseEvent e) {
/* 270 */               if (e.getClickCount() == 2) {
/* 271 */                 ClipboardHistoryPopup.this.insertSelectedItem();
/*     */               }
/*     */             }
/*     */           });
/* 275 */       ClipboardHistoryPopup.this.list.getInputMap().put(
/* 276 */           KeyStroke.getKeyStroke(10, 0), "onEnter");
/* 277 */       ClipboardHistoryPopup.this.list.getActionMap().put("onEnter", new AbstractAction()
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/* 280 */               ClipboardHistoryPopup.this.insertSelectedItem();
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 285 */       Window parent = (Window)ClipboardHistoryPopup.this.getParent();
/* 286 */       parent.addWindowFocusListener(this);
/* 287 */       parent.addWindowListener(this);
/* 288 */       parent.addComponentListener(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void componentResized(ComponentEvent e) {
/* 294 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentMoved(ComponentEvent e) {
/* 299 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentShown(ComponentEvent e) {
/* 304 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentHidden(ComponentEvent e) {
/* 309 */       uninstallAndHide();
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowActivated(WindowEvent e) {
/* 314 */       checkForParentWindowEvent(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowLostFocus(WindowEvent e) {
/* 319 */       if (e.getSource() == ClipboardHistoryPopup.this) {
/* 320 */         uninstallAndHide();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowIconified(WindowEvent e) {
/* 326 */       checkForParentWindowEvent(e);
/*     */     }
/*     */     
/*     */     private boolean checkForParentWindowEvent(WindowEvent e) {
/* 330 */       if (e.getSource() == ClipboardHistoryPopup.this.getParent()) {
/* 331 */         uninstallAndHide();
/* 332 */         return true;
/*     */       } 
/* 334 */       return false;
/*     */     }
/*     */     
/*     */     private void uninstallAndHide() {
/* 338 */       Window parent = (Window)ClipboardHistoryPopup.this.getParent();
/* 339 */       parent.removeWindowFocusListener(this);
/* 340 */       parent.removeWindowListener(this);
/* 341 */       parent.removeComponentListener(this);
/* 342 */       ClipboardHistoryPopup.this.removeWindowFocusListener(this);
/* 343 */       ClipboardHistoryPopup.this.setVisible(false);
/* 344 */       ClipboardHistoryPopup.this.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ChoiceList
/*     */     extends JList<LabelValuePair>
/*     */   {
/*     */     private ChoiceList() {
/* 356 */       super(new DefaultListModel<>());
/* 357 */       setSelectionMode(0);
/* 358 */       installKeyboardActions();
/*     */     }
/*     */ 
/*     */     
/*     */     private void installKeyboardActions() {
/* 363 */       InputMap im = getInputMap();
/* 364 */       ActionMap am = getActionMap();
/*     */       
/* 366 */       im.put(KeyStroke.getKeyStroke(40, 0), "onDown");
/* 367 */       am.put("onDown", new AbstractAction()
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/* 370 */               int index = (ClipboardHistoryPopup.ChoiceList.this.getSelectedIndex() + 1) % ClipboardHistoryPopup.ChoiceList.this.getModel().getSize();
/* 371 */               ClipboardHistoryPopup.ChoiceList.this.ensureIndexIsVisible(index);
/* 372 */               ClipboardHistoryPopup.ChoiceList.this.setSelectedIndex(index);
/*     */             }
/*     */           });
/*     */       
/* 376 */       im.put(KeyStroke.getKeyStroke(38, 0), "onUp");
/* 377 */       am.put("onUp", new AbstractAction()
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/* 380 */               int index = ClipboardHistoryPopup.ChoiceList.this.getSelectedIndex() - 1;
/* 381 */               if (index < 0) {
/* 382 */                 index += ClipboardHistoryPopup.ChoiceList.this.getModel().getSize();
/*     */               }
/* 384 */               ClipboardHistoryPopup.ChoiceList.this.ensureIndexIsVisible(index);
/* 385 */               ClipboardHistoryPopup.ChoiceList.this.setSelectedIndex(index);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     private void setContents(List<String> contents) {
/* 392 */       DefaultListModel<ClipboardHistoryPopup.LabelValuePair> model = (DefaultListModel<ClipboardHistoryPopup.LabelValuePair>)getModel();
/* 393 */       model.clear();
/* 394 */       for (String str : contents) {
/* 395 */         model.addElement(new ClipboardHistoryPopup.LabelValuePair(str));
/*     */       }
/* 397 */       setVisibleRowCount(Math.min(model.getSize(), 8));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LabelValuePair
/*     */   {
/*     */     private String label;
/*     */ 
/*     */     
/*     */     private String value;
/*     */ 
/*     */     
/*     */     private static final int LABEL_MAX_LENGTH = 50;
/*     */ 
/*     */ 
/*     */     
/*     */     LabelValuePair(String value) {
/* 416 */       this.label = this.value = value;
/* 417 */       int newline = this.label.indexOf('\n');
/* 418 */       boolean multiLine = false;
/* 419 */       if (newline > -1) {
/* 420 */         this.label = this.label.substring(0, newline);
/* 421 */         multiLine = true;
/*     */       } 
/* 423 */       if (this.label.length() > 50) {
/* 424 */         this.label = this.label.substring(0, 50) + "...";
/*     */       }
/* 426 */       else if (multiLine) {
/* 427 */         int toRemove = 3 - 50 - this.label.length();
/* 428 */         if (toRemove > 0) {
/* 429 */           this.label = this.label.substring(0, this.label.length() - toRemove);
/*     */         }
/* 431 */         this.label += "...";
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 437 */       return this.label;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\ClipboardHistoryPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */