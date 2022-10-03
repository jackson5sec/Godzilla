/*     */ package com.jgoodies.forms.builder;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.common.base.Strings;
/*     */ import com.jgoodies.forms.FormsSetup;
/*     */ import com.jgoodies.forms.factories.ComponentFactory;
/*     */ import com.jgoodies.forms.factories.Forms;
/*     */ import com.jgoodies.forms.factories.Paddings;
/*     */ import com.jgoodies.forms.internal.InternalFocusSetupUtils;
/*     */ import com.jgoodies.forms.util.FocusTraversalType;
/*     */ import java.awt.Component;
/*     */ import java.awt.FocusTraversalPolicy;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ListViewBuilder
/*     */ {
/*     */   private ComponentFactory factory;
/*     */   private JComponent label;
/*     */   private JComponent filterView;
/*     */   private JComponent listView;
/*     */   private JComponent listBarView;
/*     */   private JComponent listExtrasView;
/*     */   private JComponent detailsView;
/*     */   private JComponent listStackView;
/*     */   private Border border;
/*     */   private boolean honorsVisibility = true;
/*     */   private Component initialComponent;
/*     */   private FocusTraversalType focusTraversalType;
/*     */   private FocusTraversalPolicy focusTraversalPolicy;
/* 104 */   private String namePrefix = "ListView";
/* 105 */   private String filterViewColSpec = "[100dlu, p]";
/* 106 */   private String listViewRowSpec = "fill:[100dlu, d]:grow";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JComponent panel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ListViewBuilder create() {
/* 135 */     return new ListViewBuilder();
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
/*     */   public ListViewBuilder border(Border border) {
/* 148 */     this.border = border;
/* 149 */     invalidatePanel();
/* 150 */     return this;
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
/*     */   public ListViewBuilder padding(EmptyBorder padding) {
/* 163 */     border(padding);
/* 164 */     return this;
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
/*     */   public ListViewBuilder padding(String paddingSpec, Object... args) {
/* 189 */     padding((EmptyBorder)Paddings.createPadding(paddingSpec, args));
/* 190 */     return this;
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
/*     */   public ListViewBuilder initialComponent(JComponent initialComponent) {
/* 205 */     Preconditions.checkNotNull(initialComponent, "The %1$s must not be null.", new Object[] { "initial component" });
/* 206 */     Preconditions.checkState((this.initialComponent == null), "The initial component must be set once only.");
/*     */     
/* 208 */     checkValidFocusTraversalSetup();
/* 209 */     this.initialComponent = initialComponent;
/* 210 */     return this;
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
/*     */   public ListViewBuilder focusTraversalType(FocusTraversalType focusTraversalType) {
/* 222 */     Preconditions.checkNotNull(focusTraversalType, "The %1$s must not be null.", new Object[] { "focus traversal type" });
/* 223 */     Preconditions.checkState((this.focusTraversalType == null), "The focus traversal type must be set once only.");
/*     */     
/* 225 */     checkValidFocusTraversalSetup();
/* 226 */     this.focusTraversalType = focusTraversalType;
/* 227 */     return this;
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
/*     */   public ListViewBuilder focusTraversalPolicy(FocusTraversalPolicy policy) {
/* 247 */     Preconditions.checkNotNull(policy, "The %1$s must not be null.", new Object[] { "focus traversal policy" });
/* 248 */     Preconditions.checkState((this.focusTraversalPolicy == null), "The focus traversal policy must be set once only.");
/*     */     
/* 250 */     checkValidFocusTraversalSetup();
/* 251 */     this.focusTraversalPolicy = policy;
/* 252 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListViewBuilder honorVisibility(boolean b) {
/* 282 */     this.honorsVisibility = b;
/* 283 */     invalidatePanel();
/* 284 */     return this;
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
/*     */   public ListViewBuilder namePrefix(String namePrefix) {
/* 297 */     this.namePrefix = namePrefix;
/* 298 */     return this;
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
/*     */   public ListViewBuilder factory(ComponentFactory factory) {
/* 313 */     this.factory = factory;
/* 314 */     return this;
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
/*     */   public ListViewBuilder label(JComponent labelView) {
/* 328 */     this.label = labelView;
/* 329 */     overrideNameIfBlank(labelView, "label");
/* 330 */     invalidatePanel();
/* 331 */     return this;
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
/*     */   public ListViewBuilder labelText(String markedText, Object... args) {
/* 352 */     label(getFactory().createLabel(Strings.get(markedText, args)));
/* 353 */     return this;
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
/*     */   public ListViewBuilder headerText(String markedText, Object... args) {
/* 374 */     label(getFactory().createHeaderLabel(Strings.get(markedText, args)));
/* 375 */     return this;
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
/*     */   public ListViewBuilder filterView(JComponent filterView) {
/* 388 */     this.filterView = filterView;
/* 389 */     overrideNameIfBlank(filterView, "filter");
/* 390 */     invalidatePanel();
/* 391 */     return this;
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
/*     */   public ListViewBuilder filterViewColumn(String colSpec, Object... args) {
/* 411 */     Preconditions.checkNotNull(colSpec, "The %1$s must not be null, empty, or whitespace.", new Object[] { "filter view column specification" });
/* 412 */     this.filterViewColSpec = Strings.get(colSpec, args);
/* 413 */     invalidatePanel();
/* 414 */     return this;
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
/*     */   public ListViewBuilder listView(JComponent listView) {
/* 430 */     Preconditions.checkNotNull(listView, "The %1$s must not be null, empty, or whitespace.", new Object[] { "list view" });
/* 431 */     if (listView instanceof javax.swing.JTable || listView instanceof javax.swing.JList || listView instanceof javax.swing.JTree) {
/* 432 */       this.listView = new JScrollPane(listView);
/*     */     } else {
/* 434 */       this.listView = listView;
/*     */     } 
/* 436 */     overrideNameIfBlank(listView, "listView");
/* 437 */     invalidatePanel();
/* 438 */     return this;
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
/*     */ 
/*     */   
/*     */   public ListViewBuilder listViewRow(String rowSpec, Object... args) {
/* 465 */     Preconditions.checkNotNull(rowSpec, "The %1$s must not be null, empty, or whitespace.", new Object[] { "list view row specification" });
/* 466 */     this.listViewRowSpec = Strings.get(rowSpec, args);
/* 467 */     invalidatePanel();
/* 468 */     return this;
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
/*     */   public ListViewBuilder listBarView(JComponent listBarView) {
/* 482 */     this.listBarView = listBarView;
/* 483 */     overrideNameIfBlank(listBarView, "listBarView");
/* 484 */     invalidatePanel();
/* 485 */     return this;
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
/*     */   public ListViewBuilder listBar(JComponent... buttons) {
/* 505 */     listBarView(Forms.buttonBar(buttons));
/* 506 */     return this;
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
/*     */   public ListViewBuilder listStackView(JComponent listStackView) {
/* 520 */     this.listStackView = listStackView;
/* 521 */     overrideNameIfBlank(listStackView, "listStackView");
/* 522 */     invalidatePanel();
/* 523 */     return this;
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
/*     */   public ListViewBuilder listStack(JComponent... buttons) {
/* 543 */     listStackView(Forms.buttonStack(buttons));
/* 544 */     return this;
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
/*     */   public ListViewBuilder listExtrasView(JComponent listExtrasView) {
/* 556 */     this.listExtrasView = listExtrasView;
/* 557 */     overrideNameIfBlank(listExtrasView, "listExtrasView");
/* 558 */     invalidatePanel();
/* 559 */     return this;
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
/*     */   public ListViewBuilder detailsView(JComponent detailsView) {
/* 571 */     this.detailsView = detailsView;
/* 572 */     overrideNameIfBlank(detailsView, "detailsView");
/* 573 */     invalidatePanel();
/* 574 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JComponent build() {
/* 584 */     if (this.panel == null) {
/* 585 */       this.panel = buildPanel();
/*     */     }
/* 587 */     return this.panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ComponentFactory getFactory() {
/* 594 */     if (this.factory == null) {
/* 595 */       this.factory = FormsSetup.getComponentFactoryDefault();
/*     */     }
/* 597 */     return this.factory;
/*     */   }
/*     */ 
/*     */   
/*     */   private void invalidatePanel() {
/* 602 */     this.panel = null;
/*     */   }
/*     */ 
/*     */   
/*     */   private JComponent buildPanel() {
/* 607 */     Preconditions.checkNotNull(this.listView, "The list view must be set before #build is invoked.");
/* 608 */     String stackGap = hasStack() ? "$rg" : "0";
/* 609 */     String detailsGap = hasDetails() ? "14dlu" : "0";
/* 610 */     FormBuilder builder = FormBuilder.create().columns("fill:default:grow, %s, p", new Object[] { stackGap }).rows("p, %1$s, p, %2$s, p", new Object[] { this.listViewRowSpec, detailsGap }).honorsVisibility(this.honorsVisibility).border(this.border).add(hasHeader(), buildHeader()).xy(1, 1).add(true, this.listView).xy(1, 2).add(hasOperations(), buildOperations()).xy(1, 3).add(hasStack(), this.listStackView).xy(3, 2).add(hasDetails(), this.detailsView).xy(1, 5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 622 */     if (this.label instanceof JLabel) {
/* 623 */       JLabel theLabel = (JLabel)this.label;
/* 624 */       if (theLabel.getLabelFor() == null) {
/* 625 */         theLabel.setLabelFor(this.listView);
/*     */       }
/*     */     } 
/* 628 */     InternalFocusSetupUtils.setupFocusTraversalPolicyAndProvider(builder.getPanel(), this.focusTraversalPolicy, this.focusTraversalType, this.initialComponent);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 633 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   private JComponent buildHeader() {
/* 638 */     if (!hasHeader()) {
/* 639 */       return null;
/*     */     }
/* 641 */     String columnSpec = hasFilter() ? "default:grow, 9dlu, %s" : "default:grow, 0,    0";
/*     */ 
/*     */     
/* 644 */     return FormBuilder.create().columns(columnSpec, new Object[] { this.filterViewColSpec }).rows("[14dlu, p], $lcg", new Object[0]).labelForFeatureEnabled(false).add(hasLabel(), this.label).xy(1, 1).add(hasFilter(), this.filterView).xy(3, 1).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JComponent buildOperations() {
/* 655 */     if (!hasOperations()) {
/* 656 */       return null;
/*     */     }
/* 658 */     String gap = hasListExtras() ? "9dlu" : "0";
/* 659 */     return FormBuilder.create().columns("left:default, %s:grow, right:pref", new Object[] { gap }).rows("$rgap, p", new Object[0]).honorsVisibility(this.honorsVisibility).add(hasListBar(), this.listBarView).xy(1, 2).add(hasListExtras(), this.listExtrasView).xy(3, 2).build();
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
/*     */   private boolean hasLabel() {
/* 672 */     return (this.label != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasFilter() {
/* 677 */     return (this.filterView != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasHeader() {
/* 682 */     return (hasLabel() || hasFilter());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasListBar() {
/* 687 */     return (this.listBarView != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasListExtras() {
/* 692 */     return (this.listExtrasView != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasOperations() {
/* 697 */     return (hasListBar() || hasListExtras());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasStack() {
/* 702 */     return (this.listStackView != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasDetails() {
/* 707 */     return (this.detailsView != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private void overrideNameIfBlank(JComponent component, String suffix) {
/* 712 */     if (component != null && Strings.isBlank(component.getName())) {
/* 713 */       component.setName(this.namePrefix + '.' + suffix);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkValidFocusTraversalSetup() {
/* 723 */     InternalFocusSetupUtils.checkValidFocusTraversalSetup(this.focusTraversalPolicy, this.focusTraversalType, this.initialComponent);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\builder\ListViewBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */