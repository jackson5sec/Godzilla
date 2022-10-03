/*     */ package com.intellij.uiDesigner.lw;
/*     */ 
/*     */ import com.intellij.uiDesigner.compiler.AlienFormFileException;
/*     */ import com.intellij.uiDesigner.compiler.UnexpectedFormElementException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import org.jdom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LwRootContainer
/*     */   extends LwContainer
/*     */   implements IRootContainer
/*     */ {
/*     */   private String myClassToBind;
/*     */   private String myMainComponentBinding;
/*  35 */   private ArrayList myButtonGroups = new ArrayList();
/*  36 */   private ArrayList myInspectionSuppressions = new ArrayList();
/*     */   
/*     */   public LwRootContainer() throws Exception {
/*  39 */     super("javax.swing.JPanel");
/*  40 */     this.myLayoutSerializer = XYLayoutSerializer.INSTANCE;
/*     */   }
/*     */   
/*     */   public String getMainComponentBinding() {
/*  44 */     return this.myMainComponentBinding;
/*     */   }
/*     */   
/*     */   public String getClassToBind() {
/*  48 */     return this.myClassToBind;
/*     */   }
/*     */   
/*     */   public void setClassToBind(String classToBind) {
/*  52 */     this.myClassToBind = classToBind;
/*     */   }
/*     */   
/*     */   public void read(Element element, PropertiesProvider provider) throws Exception {
/*  56 */     if (element == null) {
/*  57 */       throw new IllegalArgumentException("element cannot be null");
/*     */     }
/*  59 */     if (!"form".equals(element.getName())) {
/*  60 */       throw new UnexpectedFormElementException("unexpected element: " + element);
/*     */     }
/*     */     
/*  63 */     if (!"http://www.intellij.com/uidesigner/form/".equals(element.getNamespace().getURI())) {
/*  64 */       throw new AlienFormFileException();
/*     */     }
/*     */     
/*  67 */     setId("root");
/*     */     
/*  69 */     this.myClassToBind = element.getAttributeValue("bind-to-class");
/*     */ 
/*     */     
/*  72 */     for (Iterator i = element.getChildren().iterator(); i.hasNext(); ) {
/*  73 */       Element child = i.next();
/*  74 */       if (child.getName().equals("buttonGroups")) {
/*  75 */         readButtonGroups(child); continue;
/*     */       } 
/*  77 */       if (child.getName().equals("inspectionSuppressions")) {
/*  78 */         readInspectionSuppressions(child);
/*     */         continue;
/*     */       } 
/*  81 */       LwComponent component = createComponentFromTag(child);
/*  82 */       addComponent(component);
/*  83 */       component.read(child, provider);
/*     */     } 
/*     */ 
/*     */     
/*  87 */     this.myMainComponentBinding = element.getAttributeValue("stored-main-component-binding");
/*     */   }
/*     */   
/*     */   private void readButtonGroups(Element element) {
/*  91 */     for (Iterator i = element.getChildren().iterator(); i.hasNext(); ) {
/*  92 */       Element child = i.next();
/*  93 */       LwButtonGroup group = new LwButtonGroup();
/*  94 */       group.read(child);
/*  95 */       this.myButtonGroups.add(group);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void readInspectionSuppressions(Element element) {
/* 100 */     for (Iterator i = element.getChildren().iterator(); i.hasNext(); ) {
/* 101 */       Element child = i.next();
/* 102 */       String inspectionId = LwXmlReader.getRequiredString(child, "inspection");
/* 103 */       String componentId = LwXmlReader.getString(child, "id");
/* 104 */       this.myInspectionSuppressions.add(new LwInspectionSuppression(inspectionId, componentId));
/*     */     } 
/*     */   }
/*     */   
/*     */   public IButtonGroup[] getButtonGroups() {
/* 109 */     return (IButtonGroup[])this.myButtonGroups.toArray((Object[])new LwButtonGroup[this.myButtonGroups.size()]);
/*     */   }
/*     */   
/*     */   public String getButtonGroupName(IComponent component) {
/* 113 */     for (int i = 0; i < this.myButtonGroups.size(); i++) {
/* 114 */       LwButtonGroup group = this.myButtonGroups.get(i);
/* 115 */       String[] ids = group.getComponentIds();
/* 116 */       for (int j = 0; j < ids.length; j++) {
/* 117 */         if (ids[j].equals(component.getId())) {
/* 118 */           return group.getName();
/*     */         }
/*     */       } 
/*     */     } 
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public String[] getButtonGroupComponentIds(String groupName) {
/* 126 */     for (int i = 0; i < this.myButtonGroups.size(); i++) {
/* 127 */       LwButtonGroup group = this.myButtonGroups.get(i);
/* 128 */       if (group.getName().equals(groupName)) {
/* 129 */         return group.getComponentIds();
/*     */       }
/*     */     } 
/* 132 */     throw new IllegalArgumentException("Cannot find group " + groupName);
/*     */   }
/*     */   
/*     */   public boolean isInspectionSuppressed(String inspectionId, String componentId) {
/* 136 */     for (Iterator iterator = this.myInspectionSuppressions.iterator(); iterator.hasNext(); ) {
/* 137 */       LwInspectionSuppression suppression = iterator.next();
/* 138 */       if ((suppression.getComponentId() == null || suppression.getComponentId().equals(componentId)) && suppression.getInspectionId().equals(inspectionId))
/*     */       {
/* 140 */         return true;
/*     */       }
/*     */     } 
/* 143 */     return false;
/*     */   }
/*     */   
/*     */   public LwInspectionSuppression[] getInspectionSuppressions() {
/* 147 */     return (LwInspectionSuppression[])this.myInspectionSuppressions.toArray((Object[])new LwInspectionSuppression[this.myInspectionSuppressions.size()]);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LwRootContainer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */