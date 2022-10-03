/*     */ package com.intellij.uiDesigner.compiler;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.lw.LwComponent;
/*     */ import com.intellij.uiDesigner.lw.LwContainer;
/*     */ import com.jgoodies.forms.layout.CellConstraints;
/*     */ import com.jgoodies.forms.layout.FormLayout;
/*     */ import java.awt.Insets;
/*     */ import org.objectweb.asm.Type;
/*     */ import org.objectweb.asm.commons.GeneratorAdapter;
/*     */ import org.objectweb.asm.commons.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormLayoutCodeGenerator
/*     */   extends LayoutCodeGenerator
/*     */ {
/*  35 */   private static final Type ourFormLayoutType = Type.getType(FormLayout.class);
/*  36 */   private static final Type ourCellConstraintsType = Type.getType(CellConstraints.class);
/*  37 */   private static final Type ourCellAlignmentType = Type.getType(CellConstraints.Alignment.class);
/*  38 */   private static final Method ourFormLayoutConstructor = Method.getMethod("void <init>(java.lang.String,java.lang.String)");
/*  39 */   private static final Method ourCellConstraintsConstructor = Method.getMethod("void <init>(int,int,int,int,com.jgoodies.forms.layout.CellConstraints$Alignment,com.jgoodies.forms.layout.CellConstraints$Alignment,java.awt.Insets)");
/*  40 */   private static final Method ourSetRowGroupsMethod = Method.getMethod("void setRowGroups(int[][])");
/*  41 */   private static final Method ourSetColumnGroupsMethod = Method.getMethod("void setColumnGroups(int[][])");
/*     */   
/*  43 */   public static String[] HORZ_ALIGN_FIELDS = new String[] { "LEFT", "CENTER", "RIGHT", "FILL" };
/*  44 */   public static String[] VERT_ALIGN_FIELDS = new String[] { "TOP", "CENTER", "BOTTOM", "FILL" };
/*     */   
/*     */   public void generateContainerLayout(LwContainer lwContainer, GeneratorAdapter generator, int componentLocal) {
/*  47 */     FormLayout formLayout = (FormLayout)lwContainer.getLayout();
/*     */     
/*  49 */     generator.loadLocal(componentLocal);
/*     */     
/*  51 */     generator.newInstance(ourFormLayoutType);
/*  52 */     generator.dup();
/*  53 */     generator.push(FormLayoutUtils.getEncodedColumnSpecs(formLayout));
/*  54 */     generator.push(FormLayoutUtils.getEncodedRowSpecs(formLayout));
/*     */     
/*  56 */     generator.invokeConstructor(ourFormLayoutType, ourFormLayoutConstructor);
/*     */     
/*  58 */     generateGroups(generator, formLayout.getRowGroups(), ourSetRowGroupsMethod);
/*  59 */     generateGroups(generator, formLayout.getColumnGroups(), ourSetColumnGroupsMethod);
/*     */     
/*  61 */     generator.invokeVirtual(ourContainerType, ourSetLayoutMethod);
/*     */   }
/*     */   
/*     */   private static void generateGroups(GeneratorAdapter generator, int[][] groups, Method setGroupsMethod) {
/*  65 */     if (groups.length == 0)
/*  66 */       return;  int groupLocal = generator.newLocal(Type.getType("[I"));
/*  67 */     generator.dup();
/*  68 */     generator.push(groups.length);
/*  69 */     generator.newArray(Type.getType("[I"));
/*  70 */     for (int i = 0; i < groups.length; i++) {
/*  71 */       generator.dup();
/*  72 */       generator.push((groups[i]).length);
/*  73 */       generator.newArray(Type.INT_TYPE);
/*  74 */       generator.storeLocal(groupLocal);
/*  75 */       for (int j = 0; j < (groups[i]).length; j++) {
/*  76 */         generator.loadLocal(groupLocal);
/*  77 */         generator.push(j);
/*  78 */         generator.push(groups[i][j]);
/*  79 */         generator.visitInsn(79);
/*     */       } 
/*  81 */       generator.push(i);
/*  82 */       generator.loadLocal(groupLocal);
/*  83 */       generator.visitInsn(83);
/*     */     } 
/*  85 */     generator.invokeVirtual(ourFormLayoutType, setGroupsMethod);
/*     */   }
/*     */   
/*     */   public void generateComponentLayout(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal, int parentLocal) {
/*  89 */     generator.loadLocal(parentLocal);
/*  90 */     generator.loadLocal(componentLocal);
/*  91 */     addNewCellConstraints(generator, lwComponent);
/*  92 */     generator.invokeVirtual(ourContainerType, ourAddMethod);
/*     */   }
/*     */   
/*     */   private static void addNewCellConstraints(GeneratorAdapter generator, LwComponent lwComponent) {
/*  96 */     GridConstraints constraints = lwComponent.getConstraints();
/*  97 */     CellConstraints cc = (CellConstraints)lwComponent.getCustomLayoutConstraints();
/*     */     
/*  99 */     generator.newInstance(ourCellConstraintsType);
/* 100 */     generator.dup();
/* 101 */     generator.push(constraints.getColumn() + 1);
/* 102 */     generator.push(constraints.getRow() + 1);
/* 103 */     generator.push(constraints.getColSpan());
/* 104 */     generator.push(constraints.getRowSpan());
/*     */     
/* 106 */     if (cc.hAlign == CellConstraints.DEFAULT) {
/* 107 */       generator.getStatic(ourCellConstraintsType, "DEFAULT", ourCellAlignmentType);
/*     */     } else {
/*     */       
/* 110 */       int hAlign = Utils.alignFromConstraints(constraints, true);
/* 111 */       generator.getStatic(ourCellConstraintsType, HORZ_ALIGN_FIELDS[hAlign], ourCellAlignmentType);
/*     */     } 
/* 113 */     if (cc.vAlign == CellConstraints.DEFAULT) {
/* 114 */       generator.getStatic(ourCellConstraintsType, "DEFAULT", ourCellAlignmentType);
/*     */     } else {
/*     */       
/* 117 */       int vAlign = Utils.alignFromConstraints(constraints, false);
/* 118 */       generator.getStatic(ourCellConstraintsType, VERT_ALIGN_FIELDS[vAlign], ourCellAlignmentType);
/*     */     } 
/*     */     
/* 121 */     AsmCodeGenerator.pushPropValue(generator, Insets.class.getName(), cc.insets);
/*     */     
/* 123 */     generator.invokeConstructor(ourCellConstraintsType, ourCellConstraintsConstructor);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\FormLayoutCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */