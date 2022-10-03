/*     */ package com.formdev.flatlaf.extras.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.ItemEvent;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
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
/*     */ public class FlatTriStateCheckBox
/*     */   extends JCheckBox
/*     */ {
/*     */   private State state;
/*     */   
/*     */   public enum State
/*     */   {
/*  63 */     UNSELECTED, INDETERMINATE, SELECTED;
/*     */   }
/*     */   
/*     */   private boolean allowIndeterminate = true;
/*  67 */   private boolean altStateCycleOrder = UIManager.getBoolean("FlatTriStateCheckBox.altStateCycleOrder");
/*     */   
/*     */   public FlatTriStateCheckBox() {
/*  70 */     this((String)null);
/*     */   }
/*     */   
/*     */   public FlatTriStateCheckBox(String text) {
/*  74 */     this(text, State.INDETERMINATE);
/*     */   }
/*     */   
/*     */   public FlatTriStateCheckBox(String text, State initialState) {
/*  78 */     super(text);
/*     */     
/*  80 */     setModel(new JToggleButton.ToggleButtonModel()
/*     */         {
/*     */           public boolean isSelected() {
/*  83 */             return (FlatTriStateCheckBox.this.state != FlatTriStateCheckBox.State.UNSELECTED);
/*     */           }
/*     */ 
/*     */           
/*     */           public void setSelected(boolean b) {
/*  88 */             FlatTriStateCheckBox.this.setState(FlatTriStateCheckBox.this.nextState(FlatTriStateCheckBox.this.state));
/*     */             
/*  90 */             fireStateChanged();
/*  91 */             fireItemStateChanged(new ItemEvent(this, 701, this, 
/*  92 */                   isSelected() ? 1 : 2));
/*     */           }
/*     */         });
/*     */     
/*  96 */     setState(initialState);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public State getState() {
/* 106 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setState(State state) {
/* 113 */     if (this.state == state) {
/*     */       return;
/*     */     }
/* 116 */     State oldState = this.state;
/* 117 */     this.state = state;
/*     */     
/* 119 */     putClientProperty("JButton.selectedState", (state == State.INDETERMINATE) ? "indeterminate" : null);
/*     */     
/* 121 */     firePropertyChange("state", oldState, state);
/* 122 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected State nextState(State state) {
/* 130 */     if (!this.altStateCycleOrder) {
/*     */       
/* 132 */       switch (state)
/*     */       { default:
/* 134 */           return this.allowIndeterminate ? State.INDETERMINATE : State.SELECTED;
/* 135 */         case INDETERMINATE: return State.SELECTED;
/* 136 */         case SELECTED: break; }  return State.UNSELECTED;
/*     */     } 
/*     */ 
/*     */     
/* 140 */     switch (state)
/*     */     { default:
/* 142 */         return State.SELECTED;
/* 143 */       case INDETERMINATE: return State.UNSELECTED;
/* 144 */       case SELECTED: break; }  return this.allowIndeterminate ? State.INDETERMINATE : State.UNSELECTED;
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
/*     */   public Boolean getChecked() {
/* 157 */     switch (this.state)
/*     */     { default:
/* 159 */         return Boolean.valueOf(false);
/* 160 */       case INDETERMINATE: return null;
/* 161 */       case SELECTED: break; }  return Boolean.valueOf(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChecked(Boolean value) {
/* 170 */     setState((value == null) ? State.INDETERMINATE : (value.booleanValue() ? State.SELECTED : State.UNSELECTED));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelected(boolean b) {
/* 175 */     setState(b ? State.SELECTED : State.UNSELECTED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIndeterminate() {
/* 182 */     return (this.state == State.INDETERMINATE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndeterminate(boolean indeterminate) {
/* 189 */     if (indeterminate) {
/* 190 */       setState(State.INDETERMINATE);
/* 191 */     } else if (this.state == State.INDETERMINATE) {
/* 192 */       setState(State.UNSELECTED);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAllowIndeterminate() {
/* 202 */     return this.allowIndeterminate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowIndeterminate(boolean allowIndeterminate) {
/* 212 */     this.allowIndeterminate = allowIndeterminate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAltStateCycleOrder() {
/* 219 */     return this.altStateCycleOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAltStateCycleOrder(boolean altStateCycleOrder) {
/* 226 */     this.altStateCycleOrder = altStateCycleOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/* 231 */     super.paintComponent(g);
/*     */     
/* 233 */     if (this.state == State.INDETERMINATE && !isIndeterminateStateSupported()) {
/* 234 */       paintIndeterminateState(g);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintIndeterminateState(Graphics g) {
/* 243 */     g.setColor(Color.magenta);
/* 244 */     g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isIndeterminateStateSupported() {
/* 252 */     LookAndFeel laf = UIManager.getLookAndFeel();
/* 253 */     return (laf instanceof com.formdev.flatlaf.FlatLaf || laf.getClass().getName().equals("com.apple.laf.AquaLookAndFeel"));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\components\FlatTriStateCheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */