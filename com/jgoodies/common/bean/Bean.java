/*     */ package com.jgoodies.common.bean;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.beans.VetoableChangeSupport;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ public abstract class Bean
/*     */   implements Serializable, ObservableBean2
/*     */ {
/*     */   protected transient PropertyChangeSupport changeSupport;
/*     */   private transient VetoableChangeSupport vetoSupport;
/*     */   
/*     */   public final synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
/* 110 */     if (listener == null) {
/*     */       return;
/*     */     }
/* 113 */     if (this.changeSupport == null) {
/* 114 */       this.changeSupport = createPropertyChangeSupport(this);
/*     */     }
/* 116 */     this.changeSupport.addPropertyChangeListener(listener);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
/* 136 */     if (listener == null || this.changeSupport == null) {
/*     */       return;
/*     */     }
/* 139 */     this.changeSupport.removePropertyChangeListener(listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
/* 163 */     if (listener == null) {
/*     */       return;
/*     */     }
/* 166 */     if (this.changeSupport == null) {
/* 167 */       this.changeSupport = createPropertyChangeSupport(this);
/*     */     }
/* 169 */     this.changeSupport.addPropertyChangeListener(propertyName, listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
/* 191 */     if (listener == null || this.changeSupport == null) {
/*     */       return;
/*     */     }
/* 194 */     this.changeSupport.removePropertyChangeListener(propertyName, listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void addVetoableChangeListener(VetoableChangeListener listener) {
/* 215 */     if (listener == null) {
/*     */       return;
/*     */     }
/* 218 */     if (this.vetoSupport == null) {
/* 219 */       this.vetoSupport = new VetoableChangeSupport(this);
/*     */     }
/* 221 */     this.vetoSupport.addVetoableChangeListener(listener);
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
/*     */ 
/*     */   
/*     */   public final synchronized void removeVetoableChangeListener(VetoableChangeListener listener) {
/* 240 */     if (listener == null || this.vetoSupport == null) {
/*     */       return;
/*     */     }
/* 243 */     this.vetoSupport.removeVetoableChangeListener(listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
/* 266 */     if (listener == null) {
/*     */       return;
/*     */     }
/* 269 */     if (this.vetoSupport == null) {
/* 270 */       this.vetoSupport = new VetoableChangeSupport(this);
/*     */     }
/* 272 */     this.vetoSupport.addVetoableChangeListener(propertyName, listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
/* 293 */     if (listener == null || this.vetoSupport == null) {
/*     */       return;
/*     */     }
/* 296 */     this.vetoSupport.removeVetoableChangeListener(propertyName, listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized PropertyChangeListener[] getPropertyChangeListeners() {
/* 317 */     if (this.changeSupport == null) {
/* 318 */       return new PropertyChangeListener[0];
/*     */     }
/* 320 */     return this.changeSupport.getPropertyChangeListeners();
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
/*     */ 
/*     */   
/*     */   public final synchronized PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
/* 339 */     if (this.changeSupport == null) {
/* 340 */       return new PropertyChangeListener[0];
/*     */     }
/* 342 */     return this.changeSupport.getPropertyChangeListeners(propertyName);
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
/*     */   
/*     */   public final synchronized VetoableChangeListener[] getVetoableChangeListeners() {
/* 360 */     if (this.vetoSupport == null) {
/* 361 */       return new VetoableChangeListener[0];
/*     */     }
/* 363 */     return this.vetoSupport.getVetoableChangeListeners();
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
/*     */   
/*     */   public final synchronized VetoableChangeListener[] getVetoableChangeListeners(String propertyName) {
/* 381 */     if (this.vetoSupport == null) {
/* 382 */       return new VetoableChangeListener[0];
/*     */     }
/* 384 */     return this.vetoSupport.getVetoableChangeListeners(propertyName);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PropertyChangeSupport createPropertyChangeSupport(Object bean) {
/* 406 */     return new PropertyChangeSupport(bean);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void firePropertyChange(PropertyChangeEvent event) {
/* 427 */     PropertyChangeSupport aChangeSupport = this.changeSupport;
/* 428 */     if (aChangeSupport == null) {
/*     */       return;
/*     */     }
/* 431 */     aChangeSupport.firePropertyChange(event);
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
/*     */   protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
/* 448 */     PropertyChangeSupport aChangeSupport = this.changeSupport;
/* 449 */     if (aChangeSupport == null) {
/*     */       return;
/*     */     }
/* 452 */     aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
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
/*     */   protected final void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
/* 469 */     PropertyChangeSupport aChangeSupport = this.changeSupport;
/* 470 */     if (aChangeSupport == null) {
/*     */       return;
/*     */     }
/* 473 */     aChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
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
/*     */   protected final void firePropertyChange(String propertyName, double oldValue, double newValue) {
/* 490 */     firePropertyChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
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
/*     */   protected final void firePropertyChange(String propertyName, float oldValue, float newValue) {
/* 507 */     firePropertyChange(propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
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
/*     */   protected final void firePropertyChange(String propertyName, int oldValue, int newValue) {
/* 524 */     PropertyChangeSupport aChangeSupport = this.changeSupport;
/* 525 */     if (aChangeSupport == null) {
/*     */       return;
/*     */     }
/* 528 */     aChangeSupport.firePropertyChange(propertyName, Integer.valueOf(oldValue), Integer.valueOf(newValue));
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
/*     */   
/*     */   protected final void firePropertyChange(String propertyName, long oldValue, long newValue) {
/* 546 */     firePropertyChange(propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
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
/*     */   protected final void fireMultiplePropertiesChanged() {
/* 559 */     firePropertyChange((String)null, (Object)null, (Object)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
/* 580 */     PropertyChangeSupport aChangeSupport = this.changeSupport;
/* 581 */     if (aChangeSupport == null) {
/*     */       return;
/*     */     }
/* 584 */     aChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue) {
/* 608 */     if (oldValue == newValue) {
/*     */       return;
/*     */     }
/* 611 */     fireIndexedPropertyChange(propertyName, index, Integer.valueOf(oldValue), Integer.valueOf(newValue));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue) {
/* 636 */     if (oldValue == newValue) {
/*     */       return;
/*     */     }
/* 639 */     fireIndexedPropertyChange(propertyName, index, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fireVetoableChange(PropertyChangeEvent event) throws PropertyVetoException {
/* 663 */     VetoableChangeSupport aVetoSupport = this.vetoSupport;
/* 664 */     if (aVetoSupport == null) {
/*     */       return;
/*     */     }
/* 667 */     aVetoSupport.fireVetoableChange(event);
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
/*     */ 
/*     */   
/*     */   protected final void fireVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
/* 686 */     VetoableChangeSupport aVetoSupport = this.vetoSupport;
/* 687 */     if (aVetoSupport == null) {
/*     */       return;
/*     */     }
/* 690 */     aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
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
/*     */ 
/*     */   
/*     */   protected final void fireVetoableChange(String propertyName, boolean oldValue, boolean newValue) throws PropertyVetoException {
/* 709 */     VetoableChangeSupport aVetoSupport = this.vetoSupport;
/* 710 */     if (aVetoSupport == null) {
/*     */       return;
/*     */     }
/* 713 */     aVetoSupport.fireVetoableChange(propertyName, oldValue, newValue);
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
/*     */ 
/*     */   
/*     */   protected final void fireVetoableChange(String propertyName, double oldValue, double newValue) throws PropertyVetoException {
/* 732 */     fireVetoableChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
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
/*     */ 
/*     */   
/*     */   protected final void fireVetoableChange(String propertyName, int oldValue, int newValue) throws PropertyVetoException {
/* 751 */     VetoableChangeSupport aVetoSupport = this.vetoSupport;
/* 752 */     if (aVetoSupport == null) {
/*     */       return;
/*     */     }
/* 755 */     aVetoSupport.fireVetoableChange(propertyName, Integer.valueOf(oldValue), Integer.valueOf(newValue));
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fireVetoableChange(String propertyName, float oldValue, float newValue) throws PropertyVetoException {
/* 775 */     fireVetoableChange(propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
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
/*     */ 
/*     */   
/*     */   protected final void fireVetoableChange(String propertyName, long oldValue, long newValue) throws PropertyVetoException {
/* 794 */     fireVetoableChange(propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\bean\Bean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */