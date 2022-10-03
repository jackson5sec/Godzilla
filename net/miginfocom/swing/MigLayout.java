/*     */ package net.miginfocom.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.awt.Point;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import net.miginfocom.layout.AC;
/*     */ import net.miginfocom.layout.BoundSize;
/*     */ import net.miginfocom.layout.CC;
/*     */ import net.miginfocom.layout.ComponentWrapper;
/*     */ import net.miginfocom.layout.ConstraintParser;
/*     */ import net.miginfocom.layout.ContainerWrapper;
/*     */ import net.miginfocom.layout.Grid;
/*     */ import net.miginfocom.layout.LC;
/*     */ import net.miginfocom.layout.LayoutCallback;
/*     */ import net.miginfocom.layout.LayoutUtil;
/*     */ import net.miginfocom.layout.PlatformDefaults;
/*     */ import net.miginfocom.layout.UnitValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MigLayout
/*     */   implements LayoutManager2, Externalizable
/*     */ {
/*  56 */   private final Map<Component, Object> scrConstrMap = new IdentityHashMap<>(8);
/*     */ 
/*     */ 
/*     */   
/*  60 */   private Object layoutConstraints = ""; private Object colConstraints = ""; private Object rowConstraints = "";
/*     */ 
/*     */ 
/*     */   
/*  64 */   private transient ContainerWrapper cacheParentW = null;
/*     */   
/*  66 */   private final transient Map<ComponentWrapper, CC> ccMap = new HashMap<>(8);
/*  67 */   private transient Timer debugTimer = null;
/*     */   
/*  69 */   private transient LC lc = null;
/*  70 */   private transient AC colSpecs = null; private transient AC rowSpecs = null;
/*  71 */   private transient Grid grid = null;
/*  72 */   private transient int lastModCount = PlatformDefaults.getModCount();
/*  73 */   private transient int lastHash = -1;
/*  74 */   private transient Dimension lastInvalidSize = null;
/*     */   private transient boolean lastWasInvalid = false;
/*  76 */   private transient Dimension lastParentSize = null;
/*     */   
/*  78 */   private transient ArrayList<LayoutCallback> callbackList = null;
/*     */   
/*     */   private transient boolean dirty = true;
/*     */   
/*     */   private long lastSize;
/*     */ 
/*     */   
/*     */   public MigLayout() {
/*  86 */     this("", "", "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigLayout(String layoutConstraints) {
/*  94 */     this(layoutConstraints, "", "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigLayout(String layoutConstraints, String colConstraints) {
/* 103 */     this(layoutConstraints, colConstraints, "");
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
/*     */   public MigLayout(LC layoutConstraints) {
/* 123 */     this(layoutConstraints, (AC)null, (AC)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MigLayout(LC layoutConstraints, AC colConstraints) {
/* 132 */     this(layoutConstraints, colConstraints, (AC)null);
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
/*     */   public Object getLayoutConstraints() {
/* 154 */     return this.layoutConstraints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLayoutConstraints(Object constr) {
/* 165 */     if (constr == null || constr instanceof String) {
/* 166 */       constr = ConstraintParser.prepare((String)constr);
/* 167 */       this.lc = ConstraintParser.parseLayoutConstraint((String)constr);
/* 168 */     } else if (constr instanceof LC) {
/* 169 */       this.lc = (LC)constr;
/*     */     } else {
/* 171 */       throw new IllegalArgumentException("Illegal constraint type: " + constr.getClass().toString());
/*     */     } 
/* 173 */     this.layoutConstraints = constr;
/* 174 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getColumnConstraints() {
/* 183 */     return this.colConstraints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColumnConstraints(Object constr) {
/* 194 */     if (constr == null || constr instanceof String) {
/* 195 */       constr = ConstraintParser.prepare((String)constr);
/* 196 */       this.colSpecs = ConstraintParser.parseColumnConstraints((String)constr);
/* 197 */     } else if (constr instanceof AC) {
/* 198 */       this.colSpecs = (AC)constr;
/*     */     } else {
/* 200 */       throw new IllegalArgumentException("Illegal constraint type: " + constr.getClass().toString());
/*     */     } 
/* 202 */     this.colConstraints = constr;
/* 203 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getRowConstraints() {
/* 212 */     return this.rowConstraints;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRowConstraints(Object constr) {
/* 223 */     if (constr == null || constr instanceof String) {
/* 224 */       constr = ConstraintParser.prepare((String)constr);
/* 225 */       this.rowSpecs = ConstraintParser.parseRowConstraints((String)constr);
/* 226 */     } else if (constr instanceof AC) {
/* 227 */       this.rowSpecs = (AC)constr;
/*     */     } else {
/* 229 */       throw new IllegalArgumentException("Illegal constraint type: " + constr.getClass().toString());
/*     */     } 
/* 231 */     this.rowConstraints = constr;
/* 232 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Component, Object> getConstraintMap() {
/* 240 */     return new IdentityHashMap<>(this.scrConstrMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConstraintMap(Map<Component, Object> map) {
/* 248 */     this.scrConstrMap.clear();
/* 249 */     this.ccMap.clear();
/* 250 */     for (Map.Entry<Component, Object> e : map.entrySet()) {
/* 251 */       setComponentConstraintsImpl(e.getKey(), e.getValue(), true);
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
/*     */   public Object getComponentConstraints(Component comp) {
/* 265 */     synchronized (comp.getParent().getTreeLock()) {
/* 266 */       return this.scrConstrMap.get(comp);
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
/*     */   public void setComponentConstraints(Component comp, Object constr) {
/* 280 */     setComponentConstraintsImpl(comp, constr, false);
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
/*     */   private void setComponentConstraintsImpl(Component comp, Object constr, boolean noCheck) {
/* 294 */     Container parent = comp.getParent();
/* 295 */     synchronized ((parent != null) ? parent.getTreeLock() : new Object()) {
/* 296 */       if (!noCheck && !this.scrConstrMap.containsKey(comp)) {
/* 297 */         throw new IllegalArgumentException("Component must already be added to parent!");
/*     */       }
/* 299 */       ComponentWrapper cw = new SwingComponentWrapper(comp);
/*     */       
/* 301 */       if (constr == null || constr instanceof String) {
/* 302 */         String cStr = ConstraintParser.prepare((String)constr);
/*     */         
/* 304 */         this.scrConstrMap.put(comp, constr);
/* 305 */         this.ccMap.put(cw, ConstraintParser.parseComponentConstraint(cStr));
/*     */       }
/* 307 */       else if (constr instanceof CC) {
/*     */         
/* 309 */         this.scrConstrMap.put(comp, constr);
/* 310 */         this.ccMap.put(cw, (CC)constr);
/*     */       } else {
/*     */         
/* 313 */         throw new IllegalArgumentException("Constraint must be String or ComponentConstraint: " + constr.getClass().toString());
/*     */       } 
/*     */       
/* 316 */       this.dirty = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isManagingComponent(Component c) {
/* 326 */     return this.scrConstrMap.containsKey(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLayoutCallback(LayoutCallback callback) {
/* 334 */     if (callback == null) {
/* 335 */       throw new NullPointerException();
/*     */     }
/* 337 */     if (this.callbackList == null) {
/* 338 */       this.callbackList = new ArrayList<>(1);
/*     */     }
/* 340 */     this.callbackList.add(callback);
/*     */     
/* 342 */     this.grid = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeLayoutCallback(LayoutCallback callback) {
/* 350 */     if (this.callbackList != null) {
/* 351 */       this.callbackList.remove(callback);
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
/*     */ 
/*     */   
/*     */   private void setDebug(ComponentWrapper parentW, boolean b) {
/* 367 */     if (b && (this.debugTimer == null || this.debugTimer.getDelay() != getDebugMillis())) {
/* 368 */       if (this.debugTimer != null) {
/* 369 */         this.debugTimer.stop();
/*     */       }
/* 371 */       ContainerWrapper pCW = parentW.getParent();
/* 372 */       final Component parent = (pCW != null) ? (Component)pCW.getComponent() : null;
/*     */       
/* 374 */       this.debugTimer = new Timer(getDebugMillis(), new MyDebugRepaintListener());
/*     */       
/* 376 */       if (parent != null) {
/* 377 */         SwingUtilities.invokeLater(new Runnable()
/*     */             {
/*     */               public void run() {
/* 380 */                 Container p = parent.getParent();
/* 381 */                 if (p != null) {
/* 382 */                   if (p instanceof JComponent) {
/* 383 */                     ((JComponent)p).revalidate();
/*     */                   } else {
/* 385 */                     parent.invalidate();
/* 386 */                     p.validate();
/*     */                   } 
/*     */                 }
/*     */               }
/*     */             });
/*     */       }
/*     */       
/* 393 */       this.debugTimer.setInitialDelay(100);
/* 394 */       this.debugTimer.start();
/*     */     }
/* 396 */     else if (!b && this.debugTimer != null) {
/* 397 */       this.debugTimer.stop();
/* 398 */       this.debugTimer = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean getDebug() {
/* 407 */     return (this.debugTimer != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getDebugMillis() {
/* 415 */     int globalDebugMillis = LayoutUtil.getGlobalDebugMillis();
/* 416 */     return (globalDebugMillis > 0) ? globalDebugMillis : this.lc.getDebugMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkCache(Container parent) {
/* 424 */     if (parent == null) {
/*     */       return;
/*     */     }
/* 427 */     if (this.dirty) {
/* 428 */       this.grid = null;
/*     */     }
/* 430 */     cleanConstraintMaps(parent);
/*     */ 
/*     */     
/* 433 */     int mc = PlatformDefaults.getModCount();
/* 434 */     if (this.lastModCount != mc) {
/* 435 */       this.grid = null;
/* 436 */       this.lastModCount = mc;
/*     */     } 
/*     */     
/* 439 */     if (!parent.isValid()) {
/* 440 */       if (!this.lastWasInvalid) {
/* 441 */         this.lastWasInvalid = true;
/*     */         
/* 443 */         int hash = 0;
/* 444 */         boolean resetLastInvalidOnParent = false;
/* 445 */         for (ComponentWrapper wrapper : this.ccMap.keySet()) {
/* 446 */           Object component = wrapper.getComponent();
/* 447 */           if (component instanceof javax.swing.JTextArea || component instanceof javax.swing.JEditorPane) {
/* 448 */             resetLastInvalidOnParent = true;
/*     */           }
/* 450 */           hash ^= wrapper.getLayoutHashCode();
/* 451 */           hash += 285134905;
/*     */         } 
/* 453 */         if (resetLastInvalidOnParent) {
/* 454 */           resetLastInvalidOnParent(parent);
/*     */         }
/* 456 */         if (hash != this.lastHash) {
/* 457 */           this.grid = null;
/* 458 */           this.lastHash = hash;
/*     */         } 
/*     */         
/* 461 */         Dimension ps = parent.getSize();
/* 462 */         if (this.lastInvalidSize == null || !this.lastInvalidSize.equals(ps)) {
/* 463 */           this.grid = null;
/* 464 */           this.lastInvalidSize = ps;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 468 */       this.lastWasInvalid = false;
/*     */     } 
/*     */     
/* 471 */     ContainerWrapper par = checkParent(parent);
/*     */     
/* 473 */     setDebug((ComponentWrapper)par, (getDebugMillis() > 0));
/*     */     
/* 475 */     if (this.grid == null) {
/* 476 */       this.grid = new Grid(par, this.lc, this.rowSpecs, this.colSpecs, this.ccMap, this.callbackList);
/*     */     }
/* 478 */     this.dirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cleanConstraintMaps(Container parent) {
/* 487 */     HashSet<Component> parentCompSet = new HashSet<>(Arrays.asList(parent.getComponents()));
/*     */     
/* 489 */     Iterator<Map.Entry<ComponentWrapper, CC>> it = this.ccMap.entrySet().iterator();
/* 490 */     while (it.hasNext()) {
/* 491 */       Component c = (Component)((ComponentWrapper)((Map.Entry)it.next()).getKey()).getComponent();
/* 492 */       if (!parentCompSet.contains(c)) {
/* 493 */         it.remove();
/* 494 */         this.scrConstrMap.remove(c);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetLastInvalidOnParent(Container parent) {
/* 504 */     while (parent != null) {
/* 505 */       LayoutManager layoutManager = parent.getLayout();
/* 506 */       if (layoutManager instanceof MigLayout) {
/* 507 */         ((MigLayout)layoutManager).lastWasInvalid = false;
/*     */       }
/* 509 */       parent = parent.getParent();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ContainerWrapper checkParent(Container parent) {
/* 515 */     if (parent == null) {
/* 516 */       return null;
/*     */     }
/* 518 */     if (this.cacheParentW == null || this.cacheParentW.getComponent() != parent) {
/* 519 */       this.cacheParentW = new SwingContainerWrapper(parent);
/*     */     }
/* 521 */     return this.cacheParentW;
/*     */   }
/*     */   
/* 524 */   public MigLayout(String layoutConstraints, String colConstraints, String rowConstraints) { this.lastSize = 0L; setLayoutConstraints(layoutConstraints); setColumnConstraints(colConstraints); setRowConstraints(rowConstraints); } public MigLayout(LC layoutConstraints, AC colConstraints, AC rowConstraints) { this.lastSize = 0L;
/*     */     setLayoutConstraints(layoutConstraints);
/*     */     setColumnConstraints(colConstraints);
/*     */     setRowConstraints(rowConstraints); }
/*     */    public void layoutContainer(Container parent) {
/* 529 */     synchronized (parent.getTreeLock()) {
/* 530 */       checkCache(parent);
/*     */       
/* 532 */       Insets i = parent.getInsets();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 537 */       int[] b = { i.left, i.top, parent.getWidth() - i.left - i.right, parent.getHeight() - i.top - i.bottom };
/*     */ 
/*     */       
/* 540 */       if (this.grid.layout(b, this.lc.getAlignX(), this.lc.getAlignY(), getDebug())) {
/* 541 */         this.grid = null;
/* 542 */         checkCache(parent);
/* 543 */         this.grid.layout(b, this.lc.getAlignX(), this.lc.getAlignY(), getDebug());
/*     */       } 
/*     */       
/* 546 */       long newSize = this.grid.getHeight()[1] + (this.grid.getWidth()[1] << 32L);
/* 547 */       if (this.lastSize != newSize) {
/* 548 */         this.lastSize = newSize;
/* 549 */         final ContainerWrapper containerWrapper = checkParent(parent);
/* 550 */         Window win = (Window)SwingUtilities.getAncestorOfClass(Window.class, (Component)containerWrapper.getComponent());
/* 551 */         if (win != null) {
/* 552 */           if (win.isVisible()) {
/* 553 */             SwingUtilities.invokeLater(new Runnable()
/*     */                 {
/*     */                   public void run() {
/* 556 */                     MigLayout.this.adjustWindowSize(containerWrapper);
/*     */                   }
/*     */                 });
/*     */           } else {
/* 560 */             adjustWindowSize(containerWrapper);
/*     */           } 
/*     */         }
/*     */       } 
/* 564 */       this.lastInvalidSize = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void adjustWindowSize(ContainerWrapper parent) {
/* 573 */     BoundSize wBounds = this.lc.getPackWidth();
/* 574 */     BoundSize hBounds = this.lc.getPackHeight();
/*     */     
/* 576 */     if (wBounds == BoundSize.NULL_SIZE && hBounds == BoundSize.NULL_SIZE) {
/*     */       return;
/*     */     }
/* 579 */     Container packable = getPackable((Component)parent.getComponent());
/*     */     
/* 581 */     if (packable != null) {
/*     */       
/* 583 */       Component pc = (Component)parent.getComponent();
/*     */       
/* 585 */       Container c = (pc instanceof Container) ? (Container)pc : pc.getParent();
/* 586 */       for (; c != null; c = c.getParent()) {
/* 587 */         LayoutManager layout = c.getLayout();
/* 588 */         if (layout instanceof javax.swing.BoxLayout || layout instanceof javax.swing.OverlayLayout) {
/* 589 */           ((LayoutManager2)layout).invalidateLayout(c);
/*     */         }
/*     */       } 
/* 592 */       Dimension prefSize = packable.getPreferredSize();
/* 593 */       int targW = constrain(checkParent(packable), packable.getWidth(), prefSize.width, wBounds);
/* 594 */       int targH = constrain(checkParent(packable), packable.getHeight(), prefSize.height, hBounds);
/*     */       
/* 596 */       Point p = packable.isShowing() ? packable.getLocationOnScreen() : packable.getLocation();
/*     */       
/* 598 */       int x = Math.round(p.x - (targW - packable.getWidth()) * (1.0F - this.lc.getPackWidthAlign()));
/* 599 */       int y = Math.round(p.y - (targH - packable.getHeight()) * (1.0F - this.lc.getPackHeightAlign()));
/*     */       
/* 601 */       if (packable instanceof JPopupMenu) {
/* 602 */         JPopupMenu popupMenu = (JPopupMenu)packable;
/* 603 */         popupMenu.setVisible(false);
/* 604 */         popupMenu.setPopupSize(targW, targH);
/* 605 */         Component invoker = popupMenu.getInvoker();
/* 606 */         Point popPoint = new Point(x, y);
/* 607 */         SwingUtilities.convertPointFromScreen(popPoint, invoker);
/* 608 */         ((JPopupMenu)packable).show(invoker, popPoint.x, popPoint.y);
/*     */         
/* 610 */         packable.setPreferredSize(null);
/*     */       } else {
/*     */         
/* 613 */         packable.setBounds(x, y, targW, targH);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Container getPackable(Component comp) {
/* 623 */     JPopupMenu popup = findType(JPopupMenu.class, comp);
/* 624 */     if (popup != null) {
/* 625 */       Container popupComp = popup;
/* 626 */       while (popupComp != null) {
/* 627 */         if (popupComp.getClass().getName().contains("HeavyWeightWindow"))
/* 628 */           return popupComp; 
/* 629 */         popupComp = popupComp.getParent();
/*     */       } 
/* 631 */       return popup;
/*     */     } 
/*     */     
/* 634 */     return findType((Class)Window.class, comp);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <E> E findType(Class<E> clazz, Component comp) {
/* 639 */     while (comp != null && !clazz.isInstance(comp)) {
/* 640 */       comp = comp.getParent();
/*     */     }
/* 642 */     return (E)comp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int constrain(ContainerWrapper parent, int winSize, int prefSize, BoundSize constrain) {
/* 648 */     if (constrain == null) {
/* 649 */       return winSize;
/*     */     }
/* 651 */     int retSize = winSize;
/* 652 */     UnitValue wUV = constrain.getPreferred();
/* 653 */     if (wUV != null) {
/* 654 */       retSize = wUV.getPixels(prefSize, parent, (ComponentWrapper)parent);
/*     */     }
/* 656 */     retSize = constrain.constrain(retSize, prefSize, parent);
/*     */     
/* 658 */     return constrain.getGapPush() ? Math.max(winSize, retSize) : retSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension minimumLayoutSize(Container parent) {
/* 664 */     synchronized (parent.getTreeLock()) {
/* 665 */       return getSizeImpl(parent, 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension preferredLayoutSize(Container parent) {
/* 672 */     synchronized (parent.getTreeLock()) {
/* 673 */       if (this.lastParentSize == null || !parent.getSize().equals(this.lastParentSize)) {
/* 674 */         for (ComponentWrapper wrapper : this.ccMap.keySet()) {
/* 675 */           if (wrapper.getContentBias() != -1) {
/* 676 */             layoutContainer(parent);
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/* 682 */       this.lastParentSize = parent.getSize();
/* 683 */       return getSizeImpl(parent, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension maximumLayoutSize(Container parent) {
/* 690 */     return new Dimension(2147483647, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Dimension getSizeImpl(Container parent, int sizeType) {
/* 696 */     checkCache(parent);
/*     */     
/* 698 */     Insets i = parent.getInsets();
/*     */     
/* 700 */     int w = LayoutUtil.getSizeSafe((this.grid != null) ? this.grid.getWidth() : null, sizeType) + i.left + i.right;
/* 701 */     int h = LayoutUtil.getSizeSafe((this.grid != null) ? this.grid.getHeight() : null, sizeType) + i.top + i.bottom;
/*     */     
/* 703 */     return new Dimension(w, h);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLayoutAlignmentX(Container parent) {
/* 709 */     return (this.lc != null && this.lc.getAlignX() != null) ? this.lc.getAlignX().getPixels(1.0F, checkParent(parent), null) : 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLayoutAlignmentY(Container parent) {
/* 715 */     return (this.lc != null && this.lc.getAlignY() != null) ? this.lc.getAlignY().getPixels(1.0F, checkParent(parent), null) : 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLayoutComponent(String s, Component comp) {
/* 721 */     addLayoutComponent(comp, s);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addLayoutComponent(Component comp, Object constraints) {
/* 727 */     synchronized (comp.getParent().getTreeLock()) {
/* 728 */       setComponentConstraintsImpl(comp, constraints, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeLayoutComponent(Component comp) {
/* 735 */     synchronized (comp.getParent().getTreeLock()) {
/* 736 */       this.scrConstrMap.remove(comp);
/* 737 */       this.ccMap.remove(new SwingComponentWrapper(comp));
/* 738 */       this.grid = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateLayout(Container target) {
/* 745 */     this.dirty = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolve() throws ObjectStreamException {
/* 754 */     return LayoutUtil.getSerializedObject(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 760 */     LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 766 */     if (getClass() == MigLayout.class)
/* 767 */       LayoutUtil.writeAsXML(out, this); 
/*     */   }
/*     */   
/*     */   private class MyDebugRepaintListener
/*     */     implements ActionListener {
/*     */     private MyDebugRepaintListener() {}
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 775 */       if (MigLayout.this.grid != null) {
/* 776 */         Component comp = (Component)MigLayout.this.grid.getContainer().getComponent();
/* 777 */         if (comp.isShowing()) {
/* 778 */           MigLayout.this.grid.paintDebug();
/*     */           return;
/*     */         } 
/*     */       } 
/* 782 */       MigLayout.this.debugTimer.stop();
/* 783 */       MigLayout.this.debugTimer = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\swing\MigLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */