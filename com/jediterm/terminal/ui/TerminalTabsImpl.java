/*     */ package com.jediterm.terminal.ui;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ContainerEvent;
/*     */ import java.awt.event.ContainerListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ 
/*     */ public class TerminalTabsImpl
/*     */   implements AbstractTabs<JediTermWidget>
/*     */ {
/*  14 */   protected JTabbedPane myTabbedPane = new JTabbedPane();
/*     */ 
/*     */   
/*     */   public int getTabCount() {
/*  18 */     return this.myTabbedPane.getTabCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTab(String name, JediTermWidget terminal) {
/*  23 */     this.myTabbedPane.addTab(name, terminal);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTitleAt(int index) {
/*  28 */     return this.myTabbedPane.getTitleAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSelectedIndex() {
/*  33 */     return this.myTabbedPane.getSelectedIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectedIndex(int index) {
/*  38 */     this.myTabbedPane.setSelectedIndex(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTabComponentAt(int index, Component component) {
/*  43 */     this.myTabbedPane.setTabComponentAt(index, component);
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOfComponent(Component component) {
/*  48 */     return this.myTabbedPane.indexOfComponent(component);
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOfTabComponent(Component component) {
/*  53 */     return this.myTabbedPane.indexOfTabComponent(component);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll() {
/*  58 */     this.myTabbedPane.removeAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(JediTermWidget terminal) {
/*  63 */     this.myTabbedPane.remove(terminal);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTitleAt(int index, String name) {
/*  68 */     this.myTabbedPane.setTitleAt(index, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectedComponent(JediTermWidget terminal) {
/*  73 */     this.myTabbedPane.setSelectedComponent(terminal);
/*     */   }
/*     */ 
/*     */   
/*     */   public JComponent getComponent() {
/*  78 */     return this.myTabbedPane;
/*     */   }
/*     */ 
/*     */   
/*     */   public JediTermWidget getComponentAt(int index) {
/*  83 */     return (JediTermWidget)this.myTabbedPane.getComponentAt(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChangeListener(final AbstractTabs.TabChangeListener listener) {
/*  88 */     this.myTabbedPane.addChangeListener(new ChangeListener()
/*     */         {
/*     */           public void stateChanged(ChangeEvent e) {
/*  91 */             listener.selectionChanged();
/*     */           }
/*     */         });
/*     */     
/*  95 */     this.myTabbedPane.addContainerListener(new ContainerListener()
/*     */         {
/*     */           public void componentAdded(ContainerEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void componentRemoved(ContainerEvent e) {
/* 103 */             if (e.getSource() == TerminalTabsImpl.this.myTabbedPane)
/* 104 */               listener.tabRemoved(); 
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\TerminalTabsImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */