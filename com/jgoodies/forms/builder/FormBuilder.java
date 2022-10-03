/*      */ package com.jgoodies.forms.builder;
/*      */ 
/*      */ import com.jgoodies.common.base.Preconditions;
/*      */ import com.jgoodies.common.base.Strings;
/*      */ import com.jgoodies.forms.FormsSetup;
/*      */ import com.jgoodies.forms.debug.FormDebugPanel;
/*      */ import com.jgoodies.forms.factories.CC;
/*      */ import com.jgoodies.forms.factories.ComponentFactory;
/*      */ import com.jgoodies.forms.factories.Forms;
/*      */ import com.jgoodies.forms.factories.Paddings;
/*      */ import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
/*      */ import com.jgoodies.forms.internal.InternalFocusSetupUtils;
/*      */ import com.jgoodies.forms.layout.CellConstraints;
/*      */ import com.jgoodies.forms.layout.ColumnSpec;
/*      */ import com.jgoodies.forms.layout.FormLayout;
/*      */ import com.jgoodies.forms.layout.LayoutMap;
/*      */ import com.jgoodies.forms.layout.RowSpec;
/*      */ import com.jgoodies.forms.util.FocusTraversalType;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.FocusTraversalPolicy;
/*      */ import java.awt.LayoutManager;
/*      */ import java.lang.ref.WeakReference;
/*      */ import javax.swing.AbstractButton;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JCheckBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JRadioButton;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FormBuilder
/*      */ {
/*      */   private static final String LABELED_BY_PROPERTY = "labeledBy";
/*      */   private LayoutMap layoutMap;
/*      */   private ColumnSpec[] columnSpecs;
/*      */   private RowSpec[] rowSpecs;
/*      */   private FormLayout layout;
/*      */   private JPanel panel;
/*      */   private JComponent initialComponent;
/*      */   private FocusTraversalType focusTraversalType;
/*      */   private FocusTraversalPolicy focusTraversalPolicy;
/*      */   private boolean debug;
/*      */   
/*      */   public enum LabelType
/*      */   {
/*  175 */     DEFAULT, READ_ONLY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  195 */   private int offsetX = 0;
/*      */   
/*  197 */   private int offsetY = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean labelForFeatureEnabled;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  208 */   private LabelType defaultLabelType = LabelType.DEFAULT;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ComponentFactory factory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  221 */   private WeakReference mostRecentlyAddedLabelReference = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected FormBuilder() {
/*  227 */     labelForFeatureEnabled(FormsSetup.getLabelForFeatureEnabledDefault());
/*  228 */     this.offsetX = 0;
/*  229 */     this.offsetY = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static FormBuilder create() {
/*  237 */     return new FormBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JPanel build() {
/*  250 */     return getPanel();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder layoutMap(LayoutMap layoutMap) {
/*  272 */     this.layoutMap = layoutMap;
/*  273 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder columns(String encodedColumnSpecs, Object... args) {
/*  297 */     this.columnSpecs = ColumnSpec.decodeSpecs(Strings.get(encodedColumnSpecs, args), getLayoutMap());
/*      */     
/*  299 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder appendColumns(String encodedColumnSpecs, Object... args) {
/*  324 */     ColumnSpec[] newColumnSpecs = ColumnSpec.decodeSpecs(Strings.get(encodedColumnSpecs, args), getLayoutMap());
/*      */     
/*  326 */     for (ColumnSpec columnSpec : newColumnSpecs) {
/*  327 */       getLayout().appendColumn(columnSpec);
/*      */     }
/*  329 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder rows(String encodedRowSpecs, Object... args) {
/*  353 */     this.rowSpecs = RowSpec.decodeSpecs(Strings.get(encodedRowSpecs, args), getLayoutMap());
/*      */     
/*  355 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder appendRows(String encodedRowSpecs, Object... args) {
/*  380 */     RowSpec[] newRowSpecs = RowSpec.decodeSpecs(Strings.get(encodedRowSpecs, args), getLayoutMap());
/*      */     
/*  382 */     for (RowSpec rowSpec : newRowSpecs) {
/*  383 */       getLayout().appendRow(rowSpec);
/*      */     }
/*  385 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder columnGroup(int... columnIndices) {
/*  402 */     getLayout().setColumnGroup(columnIndices);
/*  403 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder columnGroups(int[]... multipleColumnGroups) {
/*  420 */     getLayout().setColumnGroups(multipleColumnGroups);
/*  421 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder rowGroup(int... rowIndices) {
/*  438 */     getLayout().setRowGroup(rowIndices);
/*  439 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder rowGroups(int[]... multipleRowGroups) {
/*  456 */     getLayout().setRowGroups(multipleRowGroups);
/*  457 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder honorsVisibility(boolean b) {
/*  484 */     getLayout().setHonorsVisibility(b);
/*  485 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder honorsVisibility(JComponent c, boolean b) {
/*  501 */     getLayout().setHonorsVisibility(c, Boolean.valueOf(b));
/*  502 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder layout(FormLayout layout) {
/*  513 */     this.layout = (FormLayout)Preconditions.checkNotNull(layout, "The %1$s must not be null.", new Object[] { "layout" });
/*  514 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder panel(JPanel panel) {
/*  525 */     this.panel = (JPanel)Preconditions.checkNotNull(panel, "The %1$s must not be null.", new Object[] { "panel" });
/*  526 */     this.panel.setLayout((LayoutManager)getLayout());
/*  527 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder debug(boolean b) {
/*  542 */     this.debug = b;
/*  543 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder name(String panelName) {
/*  554 */     getPanel().setName(panelName);
/*  555 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder background(Color background) {
/*  570 */     getPanel().setBackground(background);
/*  571 */     opaque(true);
/*  572 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder border(Border border) {
/*  587 */     getPanel().setBorder(border);
/*  588 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public FormBuilder border(String paddingSpec) {
/*  610 */     return padding(paddingSpec, new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder padding(EmptyBorder padding) {
/*  625 */     getPanel().setBorder(padding);
/*  626 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder padding(String paddingSpec, Object... args) {
/*  651 */     padding((EmptyBorder)Paddings.createPadding(paddingSpec, args));
/*  652 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder opaque(boolean b) {
/*  665 */     getPanel().setOpaque(b);
/*  666 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder initialComponent(JComponent initialComponent) {
/*  679 */     Preconditions.checkState((this.initialComponent == null), "The initial component must be set once only.");
/*      */     
/*  681 */     checkValidFocusTraversalSetup();
/*  682 */     this.initialComponent = initialComponent;
/*  683 */     setupFocusTraversalPolicyAndProvider();
/*  684 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder focusTraversalType(FocusTraversalType focusTraversalType) {
/*  704 */     Preconditions.checkNotNull(focusTraversalType, "The %1$s must not be null.", new Object[] { "focus traversal type" });
/*  705 */     Preconditions.checkState((this.focusTraversalType == null), "The focus traversal type must be set once only.");
/*      */     
/*  707 */     checkValidFocusTraversalSetup();
/*  708 */     this.focusTraversalType = focusTraversalType;
/*  709 */     setupFocusTraversalPolicyAndProvider();
/*  710 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder focusTraversalPolicy(FocusTraversalPolicy policy) {
/*  744 */     Preconditions.checkNotNull(policy, "The %1$s must not be null.", new Object[] { "focus traversal policy" });
/*  745 */     Preconditions.checkState((this.focusTraversalPolicy == null), "The focus traversal policy must be set once only.");
/*      */     
/*  747 */     checkValidFocusTraversalSetup();
/*  748 */     this.focusTraversalPolicy = policy;
/*  749 */     setupFocusTraversalPolicyAndProvider();
/*  750 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder focusGroup(AbstractButton... buttons) {
/*  769 */     FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
/*  770 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JPanel getPanel() {
/*  784 */     if (this.panel == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  791 */       this.panel = this.debug ? (JPanel)new FormDebugPanel() : new JPanel(null);
/*  792 */       this.panel.setOpaque(FormsSetup.getOpaqueDefault());
/*      */     } 
/*  794 */     return this.panel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder factory(ComponentFactory factory) {
/*  812 */     this.factory = factory;
/*  813 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder labelForFeatureEnabled(boolean b) {
/*  832 */     this.labelForFeatureEnabled = b;
/*  833 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder offset(int offsetX, int offsetY) {
/*  858 */     this.offsetX = offsetX;
/*  859 */     this.offsetY = offsetY;
/*  860 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder translate(int dX, int dY) {
/*  883 */     this.offsetX += dX;
/*  884 */     this.offsetY += dY;
/*  885 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FormBuilder defaultLabelType(LabelType newValue) {
/*  899 */     this.defaultLabelType = newValue;
/*  900 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder add(Component c) {
/*  934 */     return add(true, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addRaw(Component c) {
/*  965 */     return addRaw(true, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addScrolled(Component c) {
/*  989 */     return addScrolled(true, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addBar(JButton... buttons) {
/* 1015 */     return addBar(true, buttons);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addBar(JCheckBox... checkBoxes) {
/* 1041 */     return addBar(true, checkBoxes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addBar(JRadioButton... radioButtons) {
/* 1068 */     return addBar(true, radioButtons);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addStack(JButton... buttons) {
/* 1094 */     return addStack(true, buttons);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addStack(JCheckBox... checkBoxes) {
/* 1120 */     return addStack(true, checkBoxes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addStack(JRadioButton... radioButtons) {
/* 1148 */     return addStack(true, radioButtons);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ViewAdder add(FormBuildingView view) {
/* 1163 */     return add(true, view);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder add(String markedLabelText, Object... args) {
/* 1197 */     return add(true, markedLabelText, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addLabel(String markedText, Object... args) {
/* 1228 */     return addLabel(true, markedText, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addROLabel(String markedText, Object... args) {
/* 1264 */     return addROLabel(true, markedText, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addTitle(String markedText, Object... args) {
/* 1294 */     return addTitle(true, markedText, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addSeparator(String markedText, Object... args) {
/* 1324 */     return addSeparator(true, markedText, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder add(Icon image) {
/* 1341 */     return add(true, image);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder add(boolean expression, Component c) {
/* 1378 */     if (!expression || c == null) {
/* 1379 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1381 */     if (c instanceof javax.swing.JTable || c instanceof javax.swing.JList || c instanceof javax.swing.JTree) {
/* 1382 */       return addScrolled(expression, c);
/*      */     }
/* 1384 */     return addRaw(expression, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addRaw(boolean expression, Component c) {
/* 1416 */     if (!expression || c == null) {
/* 1417 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1419 */     return addImpl(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addScrolled(boolean expression, Component c) {
/* 1444 */     if (!expression || c == null) {
/* 1445 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1447 */     return addImpl(new JScrollPane(c));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addBar(boolean expression, JButton... buttons) {
/* 1474 */     if (!expression || buttons == null) {
/* 1475 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1477 */     return addImpl(Forms.buttonBar((JComponent[])buttons));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addBar(boolean expression, JCheckBox... checkBoxes) {
/* 1504 */     if (!expression) {
/* 1505 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1507 */     return addImpl(Forms.checkBoxBar(checkBoxes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addBar(boolean expression, JRadioButton... radioButtons) {
/* 1537 */     if (!expression) {
/* 1538 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1540 */     return addImpl(Forms.radioButtonBar(radioButtons));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addStack(boolean expression, JButton... buttons) {
/* 1568 */     if (!expression || buttons == null) {
/* 1569 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1571 */     return addImpl(Forms.buttonStack((JComponent[])buttons));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addStack(boolean expression, JCheckBox... checkBoxes) {
/* 1599 */     if (!expression) {
/* 1600 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1602 */     return addImpl(Forms.checkBoxStack(checkBoxes));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addStack(boolean expression, JRadioButton... radioButtons) {
/* 1632 */     if (!expression || radioButtons == null) {
/* 1633 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1635 */     return addImpl(Forms.radioButtonStack(radioButtons));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ViewAdder add(boolean expression, FormBuildingView view) {
/* 1648 */     return new ViewAdder(this, expression, view);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder add(boolean expression, String markedLabelText, Object... args) {
/* 1685 */     return (this.defaultLabelType == LabelType.DEFAULT) ? addLabel(expression, markedLabelText, args) : addROLabel(expression, markedLabelText, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addLabel(boolean expression, String markedText, Object... args) {
/* 1719 */     return addRaw(expression, getFactory().createLabel(Strings.get(markedText, args)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addROLabel(boolean expression, String markedText, Object... args) {
/* 1758 */     return addRaw(expression, getFactory().createReadOnlyLabel(Strings.get(markedText, args)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addTitle(boolean expression, String markedText, Object... args) {
/* 1791 */     String text = Strings.get(markedText, args);
/* 1792 */     return addRaw(expression, getFactory().createTitle(text));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder addSeparator(boolean expression, String markedText, Object... args) {
/* 1824 */     int alignment = isLeftToRight() ? 2 : 4;
/*      */ 
/*      */     
/* 1827 */     String text = Strings.get(markedText, args);
/* 1828 */     return addRaw(expression, getFactory().createSeparator(text, alignment));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ComponentAdder add(boolean expression, Icon image) {
/* 1847 */     if (!expression || image == null) {
/* 1848 */       return new NoOpComponentAdder(this);
/*      */     }
/* 1850 */     return addImpl(new JLabel(image));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected LayoutMap getLayoutMap() {
/* 1857 */     if (this.layoutMap == null) {
/* 1858 */       this.layoutMap = LayoutMap.getRoot();
/*      */     }
/* 1860 */     return this.layoutMap;
/*      */   }
/*      */ 
/*      */   
/*      */   protected FormLayout getLayout() {
/* 1865 */     if (this.layout != null) {
/* 1866 */       return this.layout;
/*      */     }
/* 1868 */     Preconditions.checkNotNull(this.columnSpecs, "The layout columns must be specified.");
/* 1869 */     Preconditions.checkNotNull(this.rowSpecs, "The layout rows must be specified.");
/* 1870 */     this.layout = new FormLayout(this.columnSpecs, this.rowSpecs);
/* 1871 */     return this.layout;
/*      */   }
/*      */ 
/*      */   
/*      */   protected ComponentFactory getFactory() {
/* 1876 */     if (this.factory == null) {
/* 1877 */       this.factory = FormsSetup.getComponentFactoryDefault();
/*      */     }
/* 1879 */     return this.factory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ComponentAdder addImpl(Component c) {
/* 1887 */     if (getPanel().getLayout() == null) {
/* 1888 */       this.panel.setLayout((LayoutManager)getLayout());
/*      */     }
/* 1890 */     return new ComponentAdder(this, c);
/*      */   }
/*      */ 
/*      */   
/*      */   void addImpl(Component component, CellConstraints rawConstraints) {
/* 1895 */     CellConstraints translatedConstraints = rawConstraints.translate(this.offsetX, this.offsetY);
/* 1896 */     getPanel().add(component, translatedConstraints);
/* 1897 */     manageLabelsAndComponents(component);
/*      */   }
/*      */ 
/*      */   
/*      */   private void manageLabelsAndComponents(Component c) {
/* 1902 */     if (!this.labelForFeatureEnabled) {
/*      */       return;
/*      */     }
/* 1905 */     if (c instanceof JLabel) {
/* 1906 */       JLabel label = (JLabel)c;
/* 1907 */       if (label.getLabelFor() == null) {
/* 1908 */         setMostRecentlyAddedLabel(label);
/*      */       } else {
/* 1910 */         clearMostRecentlyAddedLabel();
/*      */       } 
/*      */       return;
/*      */     } 
/* 1914 */     JLabel mostRecentlyAddedLabel = getMostRecentlyAddedLabel();
/* 1915 */     if (mostRecentlyAddedLabel != null && isLabelForApplicable(mostRecentlyAddedLabel, c)) {
/*      */       
/* 1917 */       setLabelFor(mostRecentlyAddedLabel, c);
/* 1918 */       clearMostRecentlyAddedLabel();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isLabelForApplicable(JLabel label, Component component) {
/* 1938 */     if (label.getLabelFor() != null) {
/* 1939 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1943 */     if (!component.isFocusable()) {
/* 1944 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1948 */     if (!(component instanceof JComponent)) {
/* 1949 */       return true;
/*      */     }
/* 1951 */     JComponent c = (JComponent)component;
/* 1952 */     return (c.getClientProperty("labeledBy") == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setLabelFor(JLabel label, Component component) {
/*      */     Component labeledComponent;
/* 1968 */     if (component instanceof JScrollPane) {
/* 1969 */       JScrollPane scrollPane = (JScrollPane)component;
/* 1970 */       labeledComponent = scrollPane.getViewport().getView();
/*      */     } else {
/* 1972 */       labeledComponent = component;
/*      */     } 
/* 1974 */     label.setLabelFor(labeledComponent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private JLabel getMostRecentlyAddedLabel() {
/* 1991 */     if (this.mostRecentlyAddedLabelReference == null) {
/* 1992 */       return null;
/*      */     }
/* 1994 */     JLabel label = this.mostRecentlyAddedLabelReference.get();
/* 1995 */     if (label == null) {
/* 1996 */       return null;
/*      */     }
/* 1998 */     return label;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setMostRecentlyAddedLabel(JLabel label) {
/* 2008 */     this.mostRecentlyAddedLabelReference = new WeakReference<JLabel>(label);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void clearMostRecentlyAddedLabel() {
/* 2016 */     this.mostRecentlyAddedLabelReference = null;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isLeftToRight() {
/* 2021 */     ComponentOrientation orientation = getPanel().getComponentOrientation();
/* 2022 */     return (orientation.isLeftToRight() || !orientation.isHorizontal());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkValidFocusTraversalSetup() {
/* 2032 */     InternalFocusSetupUtils.checkValidFocusTraversalSetup(this.focusTraversalPolicy, this.focusTraversalType, this.initialComponent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void setupFocusTraversalPolicyAndProvider() {
/* 2038 */     InternalFocusSetupUtils.setupFocusTraversalPolicyAndProvider(getPanel(), this.focusTraversalPolicy, this.focusTraversalType, this.initialComponent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface FormBuildingView
/*      */   {
/*      */     void buildInto(FormBuilder param1FormBuilder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class ViewAdder
/*      */   {
/*      */     private final FormBuilder builder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean expression;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final FormBuilder.FormBuildingView view;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ViewAdder(FormBuilder builder, boolean expression, FormBuilder.FormBuildingView view) {
/* 2079 */       this.builder = builder;
/* 2080 */       this.expression = expression;
/* 2081 */       this.view = view;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public FormBuilder xy(int col, int row) {
/* 2092 */       if (this.expression && this.view != null) {
/* 2093 */         this.builder.translate(col, row);
/* 2094 */         this.view.buildInto(this.builder);
/* 2095 */         this.builder.translate(-col, -row);
/*      */       } 
/* 2097 */       return this.builder;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ComponentAdder
/*      */   {
/*      */     protected final FormBuilder builder;
/*      */ 
/*      */     
/*      */     private final Component component;
/*      */     
/*      */     private boolean labelForSet;
/*      */ 
/*      */     
/*      */     ComponentAdder(FormBuilder builder, Component component) {
/* 2114 */       this.builder = builder;
/* 2115 */       this.component = component;
/* 2116 */       this.labelForSet = false;
/*      */     }
/*      */ 
/*      */     
/*      */     public final ComponentAdder labelFor(Component c) {
/* 2121 */       Preconditions.checkArgument(this.component instanceof JLabel, "#labelFor is applicable only to JLabels");
/* 2122 */       Preconditions.checkArgument(!this.labelForSet, "You must set the label-for-relation only once.");
/* 2123 */       ((JLabel)this.component).setLabelFor(c);
/* 2124 */       this.labelForSet = true;
/* 2125 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder at(CellConstraints constraints) {
/* 2140 */       return add(constraints);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xy(int col, int row) {
/* 2160 */       return at(CC.xy(col, row));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xy(int col, int row, String encodedAlignments) {
/* 2183 */       return at(CC.xy(col, row, encodedAlignments));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xy(int col, int row, CellConstraints.Alignment colAlign, CellConstraints.Alignment rowAlign) {
/* 2204 */       return at(CC.xy(col, row, colAlign, rowAlign));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xyw(int col, int row, int colSpan) {
/* 2223 */       return at(CC.xyw(col, row, colSpan));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xyw(int col, int row, int colSpan, String encodedAlignments) {
/* 2248 */       return at(CC.xyw(col, row, colSpan, encodedAlignments));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xyw(int col, int row, int colSpan, CellConstraints.Alignment colAlign, CellConstraints.Alignment rowAlign) {
/* 2272 */       return at(CC.xyw(col, row, colSpan, colAlign, rowAlign));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xywh(int col, int row, int colSpan, int rowSpan) {
/* 2291 */       return at(CC.xywh(col, row, colSpan, rowSpan));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xywh(int col, int row, int colSpan, int rowSpan, String encodedAlignments) {
/* 2316 */       return at(CC.xywh(col, row, colSpan, rowSpan, encodedAlignments));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder xywh(int col, int row, int colSpan, int rowSpan, CellConstraints.Alignment colAlign, CellConstraints.Alignment rowAlign) {
/* 2340 */       return at(CC.xywh(col, row, colSpan, rowSpan, colAlign, rowAlign));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rc(int row, int col) {
/* 2360 */       return at(CC.rc(row, col));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rc(int row, int col, String encodedAlignments) {
/* 2383 */       return at(CC.rc(row, col, encodedAlignments));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rc(int row, int col, CellConstraints.Alignment rowAlign, CellConstraints.Alignment colAlign) {
/* 2404 */       return at(CC.rc(row, col, rowAlign, colAlign));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rcw(int row, int col, int colSpan) {
/* 2423 */       return at(CC.rcw(row, col, colSpan));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rcw(int row, int col, int colSpan, String encodedAlignments) {
/* 2449 */       return at(CC.rcw(row, col, colSpan, encodedAlignments));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rcw(int row, int col, int colSpan, CellConstraints.Alignment rowAlign, CellConstraints.Alignment colAlign) {
/* 2474 */       return at(CC.rcw(row, col, colSpan, rowAlign, colAlign));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rchw(int row, int col, int rowSpan, int colSpan) {
/* 2493 */       return at(CC.rchw(row, col, rowSpan, colSpan));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rchw(int row, int col, int rowSpan, int colSpan, String encodedAlignments) {
/* 2518 */       return at(CC.rchw(row, col, rowSpan, colSpan, encodedAlignments));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final FormBuilder rchw(int row, int col, int rowSpan, int colSpan, CellConstraints.Alignment rowAlign, CellConstraints.Alignment colAlign) {
/* 2543 */       return at(CC.rchw(col, row, rowSpan, colSpan, colAlign, rowAlign));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected FormBuilder add(CellConstraints constraints) {
/* 2550 */       this.builder.addImpl(this.component, constraints);
/* 2551 */       return this.builder;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class NoOpComponentAdder
/*      */     extends ComponentAdder
/*      */   {
/*      */     NoOpComponentAdder(FormBuilder builder) {
/* 2560 */       super(builder, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected FormBuilder add(CellConstraints constraints) {
/* 2567 */       return this.builder;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\builder\FormBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */