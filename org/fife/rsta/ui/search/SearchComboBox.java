/*     */ package org.fife.rsta.ui.search;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ui.UIUtil;
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
/*     */ public class SearchComboBox
/*     */   extends RegexAwareComboBox<String>
/*     */ {
/*     */   private FindToolBar toolBar;
/*     */   
/*     */   public SearchComboBox(FindToolBar toolBar, boolean replace) {
/*  43 */     super(replace);
/*  44 */     this.toolBar = toolBar;
/*  45 */     UIUtil.fixComboOrientation((JComboBox)this);
/*  46 */     updateTextFieldKeyMap();
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
/*     */   public void addItem(String item) {
/*  62 */     int curIndex = getIndexOf(item);
/*  63 */     if (curIndex == -1) {
/*  64 */       super.addItem(item);
/*     */     }
/*  66 */     else if (curIndex > 0) {
/*  67 */       removeItem(item);
/*  68 */       insertItemAt(item, 0);
/*     */     } 
/*     */ 
/*     */     
/*  72 */     setSelectedIndex(0);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getIndexOf(String item) {
/*  77 */     for (int i = 0; i < this.dataModel.getSize(); i++) {
/*  78 */       if (((String)this.dataModel.getElementAt(i)).equals(item)) {
/*  79 */         return i;
/*     */       }
/*     */     } 
/*  82 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSelectedString() {
/*  92 */     JTextComponent comp = UIUtil.getTextComponent((JComboBox)this);
/*  93 */     return comp.getText();
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
/*     */   public Vector<String> getSearchStrings() {
/* 109 */     int selectedIndex = getSelectedIndex();
/* 110 */     if (selectedIndex == -1) {
/* 111 */       addItem(getSelectedString());
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 116 */     else if (selectedIndex > 0) {
/* 117 */       String item = (String)getSelectedItem();
/* 118 */       removeItem(item);
/* 119 */       insertItemAt(item, 0);
/* 120 */       setSelectedIndex(0);
/*     */     } 
/*     */     
/* 123 */     int itemCount = getItemCount();
/* 124 */     Vector<String> vector = new Vector<>(itemCount);
/* 125 */     for (int i = 0; i < itemCount; i++) {
/* 126 */       vector.add(getItemAt(i));
/*     */     }
/*     */     
/* 129 */     return vector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateTextFieldKeyMap() {
/* 138 */     JTextComponent comp = UIUtil.getTextComponent((JComboBox)this);
/*     */ 
/*     */     
/* 141 */     InputMap im = comp.getInputMap();
/* 142 */     im.put(KeyStroke.getKeyStroke("ctrl H"), "none");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 148 */     super.updateUI();
/* 149 */     if (this.toolBar != null) {
/* 150 */       this.toolBar.searchComboUpdateUICallback(this);
/*     */     }
/* 152 */     updateTextFieldKeyMap();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\SearchComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */