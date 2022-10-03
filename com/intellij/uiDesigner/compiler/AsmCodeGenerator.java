/*     */ package com.intellij.uiDesigner.compiler;
/*     */ import com.intellij.uiDesigner.lw.FontDescriptor;
/*     */ import com.intellij.uiDesigner.lw.IButtonGroup;
/*     */ import com.intellij.uiDesigner.lw.LwComponent;
/*     */ import com.intellij.uiDesigner.lw.LwContainer;
/*     */ import com.intellij.uiDesigner.lw.LwIntrospectedProperty;
/*     */ import com.intellij.uiDesigner.lw.LwNestedForm;
/*     */ import com.intellij.uiDesigner.lw.LwRootContainer;
/*     */ import com.intellij.uiDesigner.lw.StringDescriptor;
/*     */ import com.intellij.uiDesigner.shared.BorderType;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.border.Border;
/*     */ import org.objectweb.asm.ClassAdapter;
/*     */ import org.objectweb.asm.ClassReader;
/*     */ import org.objectweb.asm.ClassVisitor;
/*     */ import org.objectweb.asm.ClassWriter;
/*     */ import org.objectweb.asm.FieldVisitor;
/*     */ import org.objectweb.asm.Label;
/*     */ import org.objectweb.asm.MethodAdapter;
/*     */ import org.objectweb.asm.MethodVisitor;
/*     */ import org.objectweb.asm.Type;
/*     */ import org.objectweb.asm.commons.EmptyVisitor;
/*     */ import org.objectweb.asm.commons.GeneratorAdapter;
/*     */ import org.objectweb.asm.commons.Method;
/*     */ 
/*     */ public class AsmCodeGenerator {
/*     */   private LwRootContainer myRootContainer;
/*  44 */   private Map myIdToLocalMap = new HashMap(); private ClassLoader myLoader;
/*     */   private ArrayList myErrors;
/*     */   private ArrayList myWarnings;
/*     */   private static final String CONSTRUCTOR_NAME = "<init>";
/*     */   private String myClassToBind;
/*     */   private byte[] myPatchedData;
/*  50 */   private static Map myContainerLayoutCodeGenerators = new HashMap();
/*  51 */   private static Map myComponentLayoutCodeGenerators = new HashMap();
/*  52 */   private static Map myPropertyCodeGenerators = new HashMap();
/*     */   
/*     */   public static final String SETUP_METHOD_NAME = "$$$setupUI$$$";
/*     */   public static final String GET_ROOT_COMPONENT_METHOD_NAME = "$$$getRootComponent$$$";
/*     */   public static final String CREATE_COMPONENTS_METHOD_NAME = "createUIComponents";
/*     */   public static final String LOAD_LABEL_TEXT_METHOD = "$$$loadLabelText$$$";
/*     */   public static final String LOAD_BUTTON_TEXT_METHOD = "$$$loadButtonText$$$";
/*  59 */   private static final Type ourButtonGroupType = Type.getType(ButtonGroup.class);
/*  60 */   private static final Type ourBorderFactoryType = Type.getType(BorderFactory.class);
/*  61 */   private static final Type ourBorderType = Type.getType(Border.class);
/*  62 */   private static final Method ourCreateTitledBorderMethod = Method.getMethod("javax.swing.border.TitledBorder createTitledBorder(javax.swing.border.Border,java.lang.String,int,int,java.awt.Font,java.awt.Color)"); private NestedFormLoader myFormLoader; private final boolean myIgnoreCustomCreation; private final ClassWriter myClassWriter; static Class class$javax$swing$JComponent; static Class class$java$lang$Integer; static Class class$java$lang$Boolean; static Class class$java$lang$Double; static Class class$java$lang$Float;
/*     */   static Class class$java$lang$Long;
/*     */   static Class class$java$lang$Byte;
/*     */   static Class class$java$lang$Short;
/*     */   static Class class$java$lang$Character;
/*     */   static Class class$java$lang$Object;
/*     */   
/*     */   static {
/*  70 */     myContainerLayoutCodeGenerators.put("GridLayoutManager", new GridLayoutCodeGenerator());
/*  71 */     myContainerLayoutCodeGenerators.put("GridBagLayout", new GridBagLayoutCodeGenerator());
/*  72 */     myContainerLayoutCodeGenerators.put("BorderLayout", new SimpleLayoutCodeGenerator(Type.getType(BorderLayout.class)));
/*  73 */     myContainerLayoutCodeGenerators.put("CardLayout", new SimpleLayoutCodeGenerator(Type.getType(CardLayout.class)));
/*  74 */     myContainerLayoutCodeGenerators.put("FlowLayout", new FlowLayoutCodeGenerator());
/*     */     
/*  76 */     myComponentLayoutCodeGenerators.put(LwSplitPane.class, new SplitPaneLayoutCodeGenerator());
/*  77 */     myComponentLayoutCodeGenerators.put(LwTabbedPane.class, new TabbedPaneLayoutCodeGenerator());
/*  78 */     myComponentLayoutCodeGenerators.put(LwScrollPane.class, new ScrollPaneLayoutCodeGenerator());
/*  79 */     myComponentLayoutCodeGenerators.put(LwToolBar.class, new ToolBarLayoutCodeGenerator());
/*     */     
/*  81 */     myPropertyCodeGenerators.put(String.class.getName(), new StringPropertyCodeGenerator());
/*  82 */     myPropertyCodeGenerators.put(Dimension.class.getName(), new DimensionPropertyCodeGenerator());
/*  83 */     myPropertyCodeGenerators.put(Insets.class.getName(), new InsetsPropertyCodeGenerator());
/*  84 */     myPropertyCodeGenerators.put(Rectangle.class.getName(), new RectanglePropertyCodeGenerator());
/*  85 */     myPropertyCodeGenerators.put(Color.class.getName(), new ColorPropertyCodeGenerator());
/*  86 */     myPropertyCodeGenerators.put(Font.class.getName(), new FontPropertyCodeGenerator());
/*  87 */     myPropertyCodeGenerators.put(Icon.class.getName(), new IconPropertyCodeGenerator());
/*  88 */     myPropertyCodeGenerators.put(ListModel.class.getName(), new ListModelPropertyCodeGenerator(DefaultListModel.class));
/*  89 */     myPropertyCodeGenerators.put(ComboBoxModel.class.getName(), new ListModelPropertyCodeGenerator(DefaultComboBoxModel.class));
/*  90 */     myPropertyCodeGenerators.put("java.lang.Enum", new EnumPropertyCodeGenerator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsmCodeGenerator(LwRootContainer rootContainer, ClassLoader loader, NestedFormLoader formLoader, boolean ignoreCustomCreation, ClassWriter classWriter) {
/*  98 */     this.myFormLoader = formLoader;
/*  99 */     this.myIgnoreCustomCreation = ignoreCustomCreation;
/* 100 */     if (loader == null) {
/* 101 */       throw new IllegalArgumentException("loader cannot be null");
/*     */     }
/* 103 */     if (rootContainer == null) {
/* 104 */       throw new IllegalArgumentException("rootContainer cannot be null");
/*     */     }
/* 106 */     this.myRootContainer = rootContainer;
/* 107 */     this.myLoader = loader;
/*     */     
/* 109 */     this.myErrors = new ArrayList();
/* 110 */     this.myWarnings = new ArrayList();
/* 111 */     this.myClassWriter = classWriter;
/*     */   }
/*     */   
/*     */   public void patchFile(File classFile) {
/* 115 */     if (!classFile.exists()) {
/* 116 */       this.myErrors.add(new FormErrorInfo(null, "Class to bind does not exist: " + this.myRootContainer.getClassToBind()));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*     */       byte[] patchedData;
/* 123 */       FileInputStream fis = new FileInputStream(classFile);
/*     */       try {
/* 125 */         patchedData = patchClass(fis);
/* 126 */         if (patchedData == null) {
/*     */           return;
/*     */         }
/*     */       } finally {
/*     */         
/* 131 */         fis.close();
/*     */       } 
/*     */       
/* 134 */       FileOutputStream fos = new FileOutputStream(classFile);
/*     */       try {
/* 136 */         fos.write(patchedData);
/*     */       } finally {
/*     */         
/* 139 */         fos.close();
/*     */       }
/*     */     
/* 142 */     } catch (IOException e) {
/* 143 */       this.myErrors.add(new FormErrorInfo(null, "Cannot read or write class file " + classFile.getPath() + ": " + e.toString()));
/*     */     }
/* 145 */     catch (IllegalStateException e) {
/* 146 */       this.myErrors.add(new FormErrorInfo(null, "Unexpected data in form file when patching class " + classFile.getPath() + ": " + e.toString()));
/*     */     } 
/*     */   }
/*     */   public byte[] patchClass(InputStream classStream) {
/*     */     ClassReader reader;
/* 151 */     this.myClassToBind = this.myRootContainer.getClassToBind();
/* 152 */     if (this.myClassToBind == null) {
/* 153 */       this.myWarnings.add(new FormErrorInfo(null, "No class to bind specified"));
/* 154 */       return null;
/*     */     } 
/*     */     
/* 157 */     if (this.myRootContainer.getComponentCount() != 1) {
/* 158 */       this.myErrors.add(new FormErrorInfo(null, "There should be only one component at the top level"));
/* 159 */       return null;
/*     */     } 
/*     */     
/* 162 */     String nonEmptyPanel = Utils.findNotEmptyPanelWithXYLayout(this.myRootContainer.getComponent(0));
/* 163 */     if (nonEmptyPanel != null) {
/* 164 */       this.myErrors.add(new FormErrorInfo(nonEmptyPanel, "There are non empty panels with XY layout. Please lay them out in a grid."));
/*     */       
/* 166 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 171 */       reader = new ClassReader(classStream);
/*     */     }
/* 173 */     catch (IOException e) {
/* 174 */       this.myErrors.add(new FormErrorInfo(null, "Error reading class data stream"));
/* 175 */       return null;
/*     */     } 
/*     */     
/* 178 */     FirstPassClassVisitor visitor = new FirstPassClassVisitor();
/* 179 */     reader.accept((ClassVisitor)visitor, 0);
/*     */     
/* 181 */     reader.accept((ClassVisitor)new FormClassVisitor(this, (ClassVisitor)this.myClassWriter, visitor.isExplicitSetupCall()), 0);
/* 182 */     this.myPatchedData = this.myClassWriter.toByteArray();
/* 183 */     return this.myPatchedData;
/*     */   }
/*     */   
/*     */   public FormErrorInfo[] getErrors() {
/* 187 */     return (FormErrorInfo[])this.myErrors.toArray((Object[])new FormErrorInfo[this.myErrors.size()]);
/*     */   }
/*     */   
/*     */   public FormErrorInfo[] getWarnings() {
/* 191 */     return (FormErrorInfo[])this.myWarnings.toArray((Object[])new FormErrorInfo[this.myWarnings.size()]);
/*     */   }
/*     */   
/*     */   public byte[] getPatchedData() {
/* 195 */     return this.myPatchedData;
/*     */   }
/*     */   
/*     */   static void pushPropValue(GeneratorAdapter generator, String propertyClass, Object value) {
/* 199 */     PropertyCodeGenerator codeGen = (PropertyCodeGenerator)myPropertyCodeGenerators.get(propertyClass);
/* 200 */     if (codeGen == null) {
/* 201 */       throw new RuntimeException("Unknown property class " + propertyClass);
/*     */     }
/* 203 */     codeGen.generatePushValue(generator, value);
/*     */   }
/*     */   
/*     */   static Class getComponentClass(String className, ClassLoader classLoader) throws CodeGenerationException {
/*     */     try {
/* 208 */       return Class.forName(className, false, classLoader);
/*     */     }
/* 210 */     catch (ClassNotFoundException e) {
/* 211 */       throw new CodeGenerationException(null, "Class not found: " + className);
/*     */     }
/* 213 */     catch (UnsupportedClassVersionError e) {
/* 214 */       throw new CodeGenerationException(null, "Unsupported class version error: " + className);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Type typeFromClassName(String className) {
/* 219 */     return Type.getType("L" + className.replace('.', '/') + ";");
/*     */   }
/*     */   
/*     */   class FormClassVisitor extends ClassAdapter { private String myClassName;
/*     */     private String mySuperName;
/*     */     private Map myFieldDescMap;
/*     */     private Map myFieldAccessMap;
/*     */     private boolean myHaveCreateComponentsMethod;
/*     */     private int myCreateComponentsAccess;
/*     */     private final boolean myExplicitSetupCall;
/*     */     private final AsmCodeGenerator this$0;
/*     */     
/*     */     public FormClassVisitor(AsmCodeGenerator this$0, ClassVisitor cv, boolean explicitSetupCall) {
/* 232 */       super(cv); this.this$0 = this$0; this.myFieldDescMap = new HashMap(); this.myFieldAccessMap = new HashMap(); this.myHaveCreateComponentsMethod = false;
/* 233 */       this.myExplicitSetupCall = explicitSetupCall;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
/* 242 */       super.visit(version, access, name, signature, superName, interfaces);
/* 243 */       this.myClassName = name;
/* 244 */       this.mySuperName = superName;
/*     */       
/* 246 */       for (Iterator iterator = AsmCodeGenerator.myPropertyCodeGenerators.values().iterator(); iterator.hasNext(); ) {
/* 247 */         PropertyCodeGenerator propertyCodeGenerator = iterator.next();
/* 248 */         propertyCodeGenerator.generateClassStart(this, name, this.this$0.myLoader);
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getClassName() {
/* 253 */       return this.myClassName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 262 */       if (name.equals("$$$setupUI$$$") || name.equals("$$$getRootComponent$$$") || name.equals("$$$loadButtonText$$$") || name.equals("$$$loadLabelText$$$"))
/*     */       {
/* 264 */         return null;
/*     */       }
/* 266 */       if (name.equals("createUIComponents") && desc.equals("()V")) {
/* 267 */         this.myHaveCreateComponentsMethod = true;
/* 268 */         this.myCreateComponentsAccess = access;
/*     */       } 
/*     */       
/* 271 */       MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
/* 272 */       if (name.equals("<init>") && !this.myExplicitSetupCall) {
/* 273 */         return (MethodVisitor)new AsmCodeGenerator.FormConstructorVisitor(this.this$0, methodVisitor, this.myClassName, this.mySuperName);
/*     */       }
/* 275 */       return methodVisitor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     MethodVisitor visitNewMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 283 */       return super.visitMethod(access, name, desc, signature, exceptions);
/*     */     }
/*     */     
/*     */     public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
/* 287 */       this.myFieldDescMap.put(name, desc);
/* 288 */       this.myFieldAccessMap.put(name, new Integer(access));
/* 289 */       return super.visitField(access, name, desc, signature, value);
/*     */     }
/*     */     
/*     */     public void visitEnd() {
/* 293 */       boolean haveCustomCreateComponents = (Utils.getCustomCreateComponentCount((IContainer)this.this$0.myRootContainer) > 0 && !this.this$0.myIgnoreCustomCreation);
/*     */       
/* 295 */       if (haveCustomCreateComponents && !this.myHaveCreateComponentsMethod) {
/* 296 */         this.this$0.myErrors.add(new FormErrorInfo(null, "Form contains components with Custom Create option but no createUIComponents() method"));
/*     */       }
/*     */       
/* 299 */       Method method = Method.getMethod("void $$$setupUI$$$ ()");
/* 300 */       GeneratorAdapter generator = new GeneratorAdapter(4098, method, null, null, this.cv);
/* 301 */       if (haveCustomCreateComponents && this.myHaveCreateComponentsMethod) {
/* 302 */         generator.visitVarInsn(25, 0);
/* 303 */         int opcode = (this.myCreateComponentsAccess == 2) ? 183 : 182;
/* 304 */         generator.visitMethodInsn(opcode, this.myClassName, "createUIComponents", "()V");
/*     */       } 
/* 306 */       buildSetupMethod(generator);
/*     */       
/* 308 */       String rootBinding = this.this$0.myRootContainer.getComponent(0).getBinding();
/* 309 */       if (rootBinding != null && this.myFieldDescMap.containsKey(rootBinding)) {
/* 310 */         buildGetRootComponenMethod();
/*     */       }
/*     */       
/* 313 */       for (Iterator iterator = AsmCodeGenerator.myPropertyCodeGenerators.values().iterator(); iterator.hasNext(); ) {
/* 314 */         PropertyCodeGenerator propertyCodeGenerator = iterator.next();
/* 315 */         propertyCodeGenerator.generateClassEnd(this);
/*     */       } 
/*     */       
/* 318 */       super.visitEnd();
/*     */     }
/*     */     
/*     */     private void buildGetRootComponenMethod() {
/* 322 */       Type componentType = Type.getType((AsmCodeGenerator.class$javax$swing$JComponent == null) ? (AsmCodeGenerator.class$javax$swing$JComponent = AsmCodeGenerator.class$("javax.swing.JComponent")) : AsmCodeGenerator.class$javax$swing$JComponent);
/* 323 */       Method method = new Method("$$$getRootComponent$$$", componentType, new Type[0]);
/* 324 */       GeneratorAdapter generator = new GeneratorAdapter(4097, method, null, null, this.cv);
/*     */       
/* 326 */       LwComponent topComponent = (LwComponent)this.this$0.myRootContainer.getComponent(0);
/* 327 */       String binding = topComponent.getBinding();
/*     */       
/* 329 */       generator.loadThis();
/* 330 */       generator.getField(AsmCodeGenerator.typeFromClassName(this.myClassName), binding, Type.getType((String)this.myFieldDescMap.get(binding)));
/*     */       
/* 332 */       generator.returnValue();
/* 333 */       generator.endMethod();
/*     */     }
/*     */     
/*     */     private void buildSetupMethod(GeneratorAdapter generator) {
/*     */       try {
/* 338 */         LwComponent topComponent = (LwComponent)this.this$0.myRootContainer.getComponent(0);
/* 339 */         generateSetupCodeForComponent(topComponent, generator, -1);
/* 340 */         generateComponentReferenceProperties(topComponent, generator);
/* 341 */         generateButtonGroups(this.this$0.myRootContainer, generator);
/*     */       }
/* 343 */       catch (CodeGenerationException e) {
/* 344 */         this.this$0.myErrors.add(new FormErrorInfo(e.getComponentId(), e.getMessage()));
/*     */       } 
/* 346 */       generator.returnValue();
/* 347 */       generator.endMethod();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void generateSetupCodeForComponent(LwComponent lwComponent, GeneratorAdapter generator, int parentLocal) throws CodeGenerationException {
/*     */       String className;
/* 355 */       if (lwComponent instanceof LwNestedForm) {
/*     */         LwRootContainer nestedFormContainer;
/* 357 */         LwNestedForm nestedForm = (LwNestedForm)lwComponent;
/* 358 */         if (this.this$0.myFormLoader == null) {
/* 359 */           throw new CodeGenerationException(null, "Attempt to compile nested form with no nested form loader specified");
/*     */         }
/*     */         try {
/* 362 */           nestedFormContainer = this.this$0.myFormLoader.loadForm(nestedForm.getFormFileName());
/*     */         }
/* 364 */         catch (Exception e) {
/* 365 */           throw new CodeGenerationException(lwComponent.getId(), e.getMessage());
/*     */         } 
/*     */         
/* 368 */         if (nestedFormContainer.getComponentCount() == 0) {
/*     */           return;
/*     */         }
/* 371 */         if (nestedFormContainer.getComponent(0).getBinding() == null) {
/* 372 */           throw new CodeGenerationException(lwComponent.getId(), "No binding on root component of nested form " + nestedForm.getFormFileName());
/*     */         }
/*     */         try {
/* 375 */           Utils.validateNestedFormLoop(nestedForm.getFormFileName(), this.this$0.myFormLoader);
/*     */         }
/* 377 */         catch (RecursiveFormNestingException e) {
/* 378 */           throw new CodeGenerationException(lwComponent.getId(), "Recursive form nesting is not allowed");
/*     */         } 
/* 380 */         className = this.this$0.myFormLoader.getClassToBindName(nestedFormContainer);
/*     */       } else {
/*     */         
/* 383 */         className = getComponentCodeGenerator(lwComponent.getParent()).mapComponentClass(lwComponent.getComponentClassName());
/*     */       } 
/* 385 */       Type componentType = AsmCodeGenerator.typeFromClassName(className);
/* 386 */       int componentLocal = generator.newLocal(componentType);
/*     */       
/* 388 */       this.this$0.myIdToLocalMap.put(lwComponent.getId(), new Integer(componentLocal));
/*     */       
/* 390 */       Class componentClass = AsmCodeGenerator.getComponentClass(className, this.this$0.myLoader);
/* 391 */       validateFieldBinding(lwComponent, componentClass);
/*     */       
/* 393 */       if (this.this$0.myIgnoreCustomCreation) {
/* 394 */         boolean creatable = true;
/* 395 */         if ((componentClass.getModifiers() & 0x402) != 0) {
/* 396 */           creatable = false;
/*     */         } else {
/*     */           
/*     */           try {
/* 400 */             Constructor constructor = componentClass.getConstructor(new Class[0]);
/* 401 */             if ((constructor.getModifiers() & 0x1) == 0) {
/* 402 */               creatable = false;
/*     */             }
/*     */           }
/* 405 */           catch (NoSuchMethodException ex) {
/* 406 */             creatable = false;
/*     */           } 
/*     */         } 
/* 409 */         if (!creatable) {
/* 410 */           componentClass = Utils.suggestReplacementClass(componentClass);
/* 411 */           componentType = Type.getType(componentClass);
/*     */         } 
/*     */       } 
/*     */       
/* 415 */       if (!lwComponent.isCustomCreate() || this.this$0.myIgnoreCustomCreation) {
/* 416 */         generator.newInstance(componentType);
/* 417 */         generator.dup();
/* 418 */         generator.invokeConstructor(componentType, Method.getMethod("void <init>()"));
/* 419 */         generator.storeLocal(componentLocal);
/*     */         
/* 421 */         generateFieldBinding(lwComponent, generator, componentLocal);
/*     */       } else {
/*     */         
/* 424 */         String binding = lwComponent.getBinding();
/* 425 */         if (binding == null) {
/* 426 */           throw new CodeGenerationException(lwComponent.getId(), "Only components bound to fields can have custom creation code");
/*     */         }
/*     */         
/* 429 */         generator.loadThis();
/* 430 */         generator.getField(getMainClassType(), binding, Type.getType((String)this.myFieldDescMap.get(binding)));
/*     */         
/* 432 */         generator.storeLocal(componentLocal);
/*     */       } 
/*     */       
/* 435 */       if (lwComponent instanceof LwContainer) {
/* 436 */         LwContainer lwContainer = (LwContainer)lwComponent;
/* 437 */         if (!lwContainer.isCustomCreate() || lwContainer.getComponentCount() > 0) {
/* 438 */           getComponentCodeGenerator(lwContainer).generateContainerLayout(lwContainer, generator, componentLocal);
/*     */         }
/*     */       } 
/*     */       
/* 442 */       generateComponentProperties(lwComponent, componentClass, generator, componentLocal);
/*     */ 
/*     */       
/* 445 */       if (!(lwComponent.getParent() instanceof LwRootContainer)) {
/* 446 */         LayoutCodeGenerator parentCodeGenerator = getComponentCodeGenerator(lwComponent.getParent());
/* 447 */         if (lwComponent instanceof LwNestedForm) {
/* 448 */           componentLocal = getNestedFormComponent(generator, componentClass, componentLocal);
/*     */         }
/* 450 */         parentCodeGenerator.generateComponentLayout(lwComponent, generator, componentLocal, parentLocal);
/*     */       } 
/*     */       
/* 453 */       if (lwComponent instanceof LwContainer) {
/* 454 */         LwContainer container = (LwContainer)lwComponent;
/*     */         
/* 456 */         generateBorder(container, generator, componentLocal);
/*     */         
/* 458 */         for (int i = 0; i < container.getComponentCount(); i++) {
/* 459 */           generateSetupCodeForComponent((LwComponent)container.getComponent(i), generator, componentLocal);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private int getNestedFormComponent(GeneratorAdapter generator, Class componentClass, int formLocal) throws CodeGenerationException {
/* 465 */       Type componentType = Type.getType((AsmCodeGenerator.class$javax$swing$JComponent == null) ? (AsmCodeGenerator.class$javax$swing$JComponent = AsmCodeGenerator.class$("javax.swing.JComponent")) : AsmCodeGenerator.class$javax$swing$JComponent);
/* 466 */       int componentLocal = generator.newLocal(componentType);
/* 467 */       generator.loadLocal(formLocal);
/* 468 */       generator.invokeVirtual(Type.getType(componentClass), new Method("$$$getRootComponent$$$", componentType, new Type[0]));
/*     */       
/* 470 */       generator.storeLocal(componentLocal);
/* 471 */       return componentLocal;
/*     */     }
/*     */     
/*     */     private LayoutCodeGenerator getComponentCodeGenerator(LwContainer container) {
/* 475 */       LayoutCodeGenerator generator = (LayoutCodeGenerator)AsmCodeGenerator.myComponentLayoutCodeGenerators.get(container.getClass());
/* 476 */       if (generator != null) {
/* 477 */         return generator;
/*     */       }
/* 479 */       LwContainer parent = container;
/* 480 */       while (parent != null) {
/* 481 */         String layoutManager = parent.getLayoutManager();
/* 482 */         if (layoutManager != null && layoutManager.length() > 0) {
/* 483 */           if (layoutManager.equals("FormLayout") && !AsmCodeGenerator.myContainerLayoutCodeGenerators.containsKey("FormLayout"))
/*     */           {
/* 485 */             AsmCodeGenerator.myContainerLayoutCodeGenerators.put("FormLayout", new FormLayoutCodeGenerator());
/*     */           }
/* 487 */           generator = (LayoutCodeGenerator)AsmCodeGenerator.myContainerLayoutCodeGenerators.get(layoutManager);
/* 488 */           if (generator != null) {
/* 489 */             return generator;
/*     */           }
/*     */         } 
/* 492 */         parent = parent.getParent();
/*     */       } 
/* 494 */       return GridLayoutCodeGenerator.INSTANCE;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void generateComponentProperties(LwComponent lwComponent, Class componentClass, GeneratorAdapter generator, int componentLocal) throws CodeGenerationException {
/* 502 */       LwIntrospectedProperty[] introspectedProperties = lwComponent.getAssignedIntrospectedProperties();
/* 503 */       for (int i = 0; i < introspectedProperties.length; i++) {
/* 504 */         Type setterArgType; LwIntrospectedProperty property = introspectedProperties[i];
/* 505 */         if (property instanceof com.intellij.uiDesigner.lw.LwIntroComponentProperty) {
/*     */           continue;
/*     */         }
/* 508 */         String propertyClass = property.getCodeGenPropertyClassName();
/* 509 */         if (this.this$0.myIgnoreCustomCreation) {
/*     */           try {
/*     */             Class setterClass;
/* 512 */             if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Integer == null) ? (AsmCodeGenerator.class$java$lang$Integer = AsmCodeGenerator.class$("java.lang.Integer")) : AsmCodeGenerator.class$java$lang$Integer).getName())) {
/* 513 */               setterClass = int.class;
/*     */             }
/* 515 */             else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Boolean == null) ? (AsmCodeGenerator.class$java$lang$Boolean = AsmCodeGenerator.class$("java.lang.Boolean")) : AsmCodeGenerator.class$java$lang$Boolean).getName())) {
/* 516 */               setterClass = boolean.class;
/*     */             }
/* 518 */             else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Double == null) ? (AsmCodeGenerator.class$java$lang$Double = AsmCodeGenerator.class$("java.lang.Double")) : AsmCodeGenerator.class$java$lang$Double).getName())) {
/* 519 */               setterClass = double.class;
/*     */             }
/* 521 */             else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Float == null) ? (AsmCodeGenerator.class$java$lang$Float = AsmCodeGenerator.class$("java.lang.Float")) : AsmCodeGenerator.class$java$lang$Float).getName())) {
/* 522 */               setterClass = float.class;
/*     */             }
/* 524 */             else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Long == null) ? (AsmCodeGenerator.class$java$lang$Long = AsmCodeGenerator.class$("java.lang.Long")) : AsmCodeGenerator.class$java$lang$Long).getName())) {
/* 525 */               setterClass = long.class;
/*     */             }
/* 527 */             else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Byte == null) ? (AsmCodeGenerator.class$java$lang$Byte = AsmCodeGenerator.class$("java.lang.Byte")) : AsmCodeGenerator.class$java$lang$Byte).getName())) {
/* 528 */               setterClass = byte.class;
/*     */             }
/* 530 */             else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Short == null) ? (AsmCodeGenerator.class$java$lang$Short = AsmCodeGenerator.class$("java.lang.Short")) : AsmCodeGenerator.class$java$lang$Short).getName())) {
/* 531 */               setterClass = short.class;
/*     */             }
/* 533 */             else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Character == null) ? (AsmCodeGenerator.class$java$lang$Character = AsmCodeGenerator.class$("java.lang.Character")) : AsmCodeGenerator.class$java$lang$Character).getName())) {
/* 534 */               setterClass = char.class;
/*     */             } else {
/*     */               
/* 537 */               setterClass = Class.forName(propertyClass);
/*     */             } 
/* 539 */             componentClass.getMethod(property.getWriteMethodName(), new Class[] { setterClass });
/*     */           }
/* 541 */           catch (Exception e) {
/*     */             continue;
/*     */           } 
/*     */         }
/* 545 */         PropertyCodeGenerator propGen = (PropertyCodeGenerator)AsmCodeGenerator.myPropertyCodeGenerators.get(propertyClass);
/*     */         
/* 547 */         if (propGen != null && propGen.generateCustomSetValue(lwComponent, componentClass, property, generator, componentLocal, this.myClassName)) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 552 */         generator.loadLocal(componentLocal);
/*     */         
/* 554 */         Object value = lwComponent.getPropertyValue(property);
/*     */         
/* 556 */         if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Integer == null) ? (AsmCodeGenerator.class$java$lang$Integer = AsmCodeGenerator.class$("java.lang.Integer")) : AsmCodeGenerator.class$java$lang$Integer).getName())) {
/* 557 */           generator.push(((Integer)value).intValue());
/* 558 */           setterArgType = Type.INT_TYPE;
/*     */         }
/* 560 */         else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Boolean == null) ? (AsmCodeGenerator.class$java$lang$Boolean = AsmCodeGenerator.class$("java.lang.Boolean")) : AsmCodeGenerator.class$java$lang$Boolean).getName())) {
/* 561 */           generator.push(((Boolean)value).booleanValue());
/* 562 */           setterArgType = Type.BOOLEAN_TYPE;
/*     */         }
/* 564 */         else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Double == null) ? (AsmCodeGenerator.class$java$lang$Double = AsmCodeGenerator.class$("java.lang.Double")) : AsmCodeGenerator.class$java$lang$Double).getName())) {
/* 565 */           generator.push(((Double)value).doubleValue());
/* 566 */           setterArgType = Type.DOUBLE_TYPE;
/*     */         }
/* 568 */         else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Float == null) ? (AsmCodeGenerator.class$java$lang$Float = AsmCodeGenerator.class$("java.lang.Float")) : AsmCodeGenerator.class$java$lang$Float).getName())) {
/* 569 */           generator.push(((Float)value).floatValue());
/* 570 */           setterArgType = Type.FLOAT_TYPE;
/*     */         }
/* 572 */         else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Long == null) ? (AsmCodeGenerator.class$java$lang$Long = AsmCodeGenerator.class$("java.lang.Long")) : AsmCodeGenerator.class$java$lang$Long).getName())) {
/* 573 */           generator.push(((Long)value).longValue());
/* 574 */           setterArgType = Type.LONG_TYPE;
/*     */         }
/* 576 */         else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Short == null) ? (AsmCodeGenerator.class$java$lang$Short = AsmCodeGenerator.class$("java.lang.Short")) : AsmCodeGenerator.class$java$lang$Short).getName())) {
/* 577 */           generator.push(((Short)value).intValue());
/* 578 */           setterArgType = Type.SHORT_TYPE;
/*     */         }
/* 580 */         else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Byte == null) ? (AsmCodeGenerator.class$java$lang$Byte = AsmCodeGenerator.class$("java.lang.Byte")) : AsmCodeGenerator.class$java$lang$Byte).getName())) {
/* 581 */           generator.push(((Byte)value).intValue());
/* 582 */           setterArgType = Type.BYTE_TYPE;
/*     */         }
/* 584 */         else if (propertyClass.equals(((AsmCodeGenerator.class$java$lang$Character == null) ? (AsmCodeGenerator.class$java$lang$Character = AsmCodeGenerator.class$("java.lang.Character")) : AsmCodeGenerator.class$java$lang$Character).getName())) {
/* 585 */           generator.push(((Character)value).charValue());
/* 586 */           setterArgType = Type.CHAR_TYPE;
/*     */         } else {
/*     */           
/* 589 */           if (propGen == null) {
/*     */             continue;
/*     */           }
/* 592 */           propGen.generatePushValue(generator, value);
/* 593 */           setterArgType = AsmCodeGenerator.typeFromClassName(property.getPropertyClassName());
/*     */         } 
/*     */         
/* 596 */         Type declaringType = (property.getDeclaringClassName() != null) ? AsmCodeGenerator.typeFromClassName(property.getDeclaringClassName()) : Type.getType(componentClass);
/*     */ 
/*     */         
/* 599 */         generator.invokeVirtual(declaringType, new Method(property.getWriteMethodName(), Type.VOID_TYPE, new Type[] { setterArgType }));
/*     */         
/*     */         continue;
/*     */       } 
/* 603 */       generateClientProperties(lwComponent, componentClass, generator, componentLocal);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void generateClientProperties(LwComponent lwComponent, Class componentClass, GeneratorAdapter generator, int componentLocal) throws CodeGenerationException {
/* 610 */       HashMap props = lwComponent.getDelegeeClientProperties();
/* 611 */       for (Iterator iterator = props.entrySet().iterator(); iterator.hasNext(); ) {
/* 612 */         Map.Entry e = iterator.next();
/* 613 */         generator.loadLocal(componentLocal);
/*     */         
/* 615 */         generator.push((String)e.getKey());
/*     */         
/* 617 */         Object value = e.getValue();
/* 618 */         if (value instanceof StringDescriptor) {
/* 619 */           generator.push(((StringDescriptor)value).getValue());
/*     */         } else {
/*     */           
/* 622 */           Type valueType = Type.getType(value.getClass());
/* 623 */           generator.newInstance(valueType);
/* 624 */           generator.dup();
/* 625 */           if (value instanceof Boolean) {
/* 626 */             generator.push(((Boolean)value).booleanValue());
/* 627 */             generator.invokeConstructor(valueType, Method.getMethod("void <init>(boolean)"));
/*     */           }
/* 629 */           else if (value instanceof Integer) {
/* 630 */             generator.push(((Integer)value).intValue());
/* 631 */             generator.invokeConstructor(valueType, Method.getMethod("void <init>(int)"));
/*     */           }
/* 633 */           else if (value instanceof Double) {
/* 634 */             generator.push(((Double)value).doubleValue());
/* 635 */             generator.invokeConstructor(valueType, Method.getMethod("void <init>(double)"));
/*     */           } else {
/*     */             
/* 638 */             throw new CodeGenerationException(lwComponent.getId(), "Unknown client property value type");
/*     */           } 
/*     */         } 
/*     */         
/* 642 */         Type componentType = Type.getType(componentClass);
/* 643 */         Type objectType = Type.getType((AsmCodeGenerator.class$java$lang$Object == null) ? (AsmCodeGenerator.class$java$lang$Object = AsmCodeGenerator.class$("java.lang.Object")) : AsmCodeGenerator.class$java$lang$Object);
/* 644 */         generator.invokeVirtual(componentType, new Method("putClientProperty", Type.VOID_TYPE, new Type[] { objectType, objectType }));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void generateComponentReferenceProperties(LwComponent component, GeneratorAdapter generator) throws CodeGenerationException {
/* 651 */       if (component instanceof LwNestedForm)
/* 652 */         return;  int componentLocal = ((Integer)this.this$0.myIdToLocalMap.get(component.getId())).intValue();
/* 653 */       LayoutCodeGenerator layoutCodeGenerator = getComponentCodeGenerator(component.getParent());
/* 654 */       Class componentClass = AsmCodeGenerator.getComponentClass(layoutCodeGenerator.mapComponentClass(component.getComponentClassName()), this.this$0.myLoader);
/*     */       
/* 656 */       LwIntrospectedProperty[] introspectedProperties = component.getAssignedIntrospectedProperties();
/* 657 */       for (int i = 0; i < introspectedProperties.length; i++) {
/* 658 */         LwIntrospectedProperty property = introspectedProperties[i];
/* 659 */         if (property instanceof com.intellij.uiDesigner.lw.LwIntroComponentProperty) {
/* 660 */           String targetId = (String)component.getPropertyValue(property);
/* 661 */           if (targetId != null && targetId.length() > 0) {
/*     */ 
/*     */             
/* 664 */             Integer targetLocalInt = (Integer)this.this$0.myIdToLocalMap.get(targetId);
/* 665 */             if (targetLocalInt != null) {
/* 666 */               int targetLocal = targetLocalInt.intValue();
/* 667 */               generator.loadLocal(componentLocal);
/* 668 */               generator.loadLocal(targetLocal);
/* 669 */               Type declaringType = (property.getDeclaringClassName() != null) ? AsmCodeGenerator.typeFromClassName(property.getDeclaringClassName()) : Type.getType(componentClass);
/*     */ 
/*     */               
/* 672 */               generator.invokeVirtual(declaringType, new Method(property.getWriteMethodName(), Type.VOID_TYPE, new Type[] { AsmCodeGenerator.typeFromClassName(property.getPropertyClassName()) }));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 680 */       if (component instanceof LwContainer) {
/* 681 */         LwContainer container = (LwContainer)component;
/*     */         
/* 683 */         for (int j = 0; j < container.getComponentCount(); j++) {
/* 684 */           generateComponentReferenceProperties((LwComponent)container.getComponent(j), generator);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private void generateButtonGroups(LwRootContainer rootContainer, GeneratorAdapter generator) throws CodeGenerationException {
/* 690 */       IButtonGroup[] groups = rootContainer.getButtonGroups();
/* 691 */       if (groups.length > 0) {
/* 692 */         int groupLocal = generator.newLocal(AsmCodeGenerator.ourButtonGroupType);
/* 693 */         for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {
/* 694 */           String[] ids = groups[groupIndex].getComponentIds();
/*     */           
/* 696 */           if (ids.length > 0) {
/* 697 */             generator.newInstance(AsmCodeGenerator.ourButtonGroupType);
/* 698 */             generator.dup();
/* 699 */             generator.invokeConstructor(AsmCodeGenerator.ourButtonGroupType, Method.getMethod("void <init>()"));
/* 700 */             generator.storeLocal(groupLocal);
/*     */             
/* 702 */             if (groups[groupIndex].isBound() && !this.this$0.myIgnoreCustomCreation) {
/* 703 */               validateFieldClass(groups[groupIndex].getName(), (AsmCodeGenerator.class$javax$swing$ButtonGroup == null) ? (AsmCodeGenerator.class$javax$swing$ButtonGroup = AsmCodeGenerator.class$("javax.swing.ButtonGroup")) : AsmCodeGenerator.class$javax$swing$ButtonGroup, null);
/* 704 */               generator.loadThis();
/* 705 */               generator.loadLocal(groupLocal);
/* 706 */               generator.putField(getMainClassType(), groups[groupIndex].getName(), AsmCodeGenerator.ourButtonGroupType);
/*     */             } 
/*     */             
/* 709 */             for (int i = 0; i < ids.length; i++) {
/* 710 */               Integer localInt = (Integer)this.this$0.myIdToLocalMap.get(ids[i]);
/* 711 */               if (localInt != null) {
/* 712 */                 generator.loadLocal(groupLocal);
/* 713 */                 generator.loadLocal(localInt.intValue());
/* 714 */                 generator.invokeVirtual(AsmCodeGenerator.ourButtonGroupType, Method.getMethod("void add(javax.swing.AbstractButton)"));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void generateFieldBinding(LwComponent lwComponent, GeneratorAdapter generator, int componentLocal) throws CodeGenerationException {
/* 725 */       String binding = lwComponent.getBinding();
/* 726 */       if (binding != null) {
/* 727 */         Integer access = (Integer)this.myFieldAccessMap.get(binding);
/* 728 */         if ((access.intValue() & 0x8) != 0) {
/* 729 */           throw new CodeGenerationException(lwComponent.getId(), "Cannot bind: field is static: " + this.this$0.myClassToBind + "." + binding);
/*     */         }
/* 731 */         if ((access.intValue() & 0x10) != 0) {
/* 732 */           throw new CodeGenerationException(lwComponent.getId(), "Cannot bind: field is final: " + this.this$0.myClassToBind + "." + binding);
/*     */         }
/*     */         
/* 735 */         generator.loadThis();
/* 736 */         generator.loadLocal(componentLocal);
/* 737 */         generator.putField(getMainClassType(), binding, Type.getType((String)this.myFieldDescMap.get(binding)));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private Type getMainClassType() {
/* 743 */       return Type.getType("L" + this.myClassName + ";");
/*     */     }
/*     */     
/*     */     private void validateFieldBinding(LwComponent component, Class componentClass) throws CodeGenerationException {
/* 747 */       String binding = component.getBinding();
/* 748 */       if (binding == null)
/*     */         return; 
/* 750 */       validateFieldClass(binding, componentClass, component.getId());
/*     */     }
/*     */     private void validateFieldClass(String binding, Class componentClass, String componentId) throws CodeGenerationException {
/*     */       Class fieldClass;
/* 754 */       if (!this.myFieldDescMap.containsKey(binding)) {
/* 755 */         throw new CodeGenerationException(componentId, "Cannot bind: field does not exist: " + this.this$0.myClassToBind + "." + binding);
/*     */       }
/*     */       
/* 758 */       Type fieldType = Type.getType((String)this.myFieldDescMap.get(binding));
/* 759 */       if (fieldType.getSort() != 10) {
/* 760 */         throw new CodeGenerationException(componentId, "Cannot bind: field is of primitive type: " + this.this$0.myClassToBind + "." + binding);
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 765 */         fieldClass = this.this$0.myLoader.loadClass(fieldType.getClassName());
/*     */       }
/* 767 */       catch (ClassNotFoundException e) {
/* 768 */         throw new CodeGenerationException(componentId, "Class not found: " + fieldType.getClassName());
/*     */       } 
/* 770 */       if (!fieldClass.isAssignableFrom(componentClass)) {
/* 771 */         throw new CodeGenerationException(componentId, "Cannot bind: Incompatible types. Cannot assign " + componentClass.getName() + " to field " + this.this$0.myClassToBind + "." + binding);
/*     */       }
/*     */     }
/*     */     
/*     */     private void generateBorder(LwContainer container, GeneratorAdapter generator, int componentLocal) {
/* 776 */       BorderType borderType = container.getBorderType();
/* 777 */       StringDescriptor borderTitle = container.getBorderTitle();
/* 778 */       String borderFactoryMethodName = borderType.getBorderFactoryMethodName();
/*     */       
/* 780 */       boolean borderNone = borderType.equals(BorderType.NONE);
/* 781 */       if (!borderNone || borderTitle != null) {
/*     */         
/* 783 */         generator.loadLocal(componentLocal);
/*     */         
/* 785 */         if (!borderNone) {
/* 786 */           if (borderType.equals(BorderType.LINE)) {
/* 787 */             if (container.getBorderColor() == null) {
/* 788 */               Type colorType = Type.getType((AsmCodeGenerator.class$java$awt$Color == null) ? (AsmCodeGenerator.class$java$awt$Color = AsmCodeGenerator.class$("java.awt.Color")) : AsmCodeGenerator.class$java$awt$Color);
/* 789 */               generator.getStatic(colorType, "black", colorType);
/*     */             } else {
/*     */               
/* 792 */               AsmCodeGenerator.pushPropValue(generator, ((AsmCodeGenerator.class$java$awt$Color == null) ? (AsmCodeGenerator.class$java$awt$Color = AsmCodeGenerator.class$("java.awt.Color")) : AsmCodeGenerator.class$java$awt$Color).getName(), container.getBorderColor());
/*     */             } 
/* 794 */             generator.invokeStatic(AsmCodeGenerator.ourBorderFactoryType, new Method(borderFactoryMethodName, AsmCodeGenerator.ourBorderType, new Type[] { Type.getType((AsmCodeGenerator.class$java$awt$Color == null) ? (AsmCodeGenerator.class$java$awt$Color = AsmCodeGenerator.class$("java.awt.Color")) : AsmCodeGenerator.class$java$awt$Color) }));
/*     */ 
/*     */           
/*     */           }
/* 798 */           else if (borderType.equals(BorderType.EMPTY) && container.getBorderSize() != null) {
/* 799 */             Insets size = container.getBorderSize();
/* 800 */             generator.push(size.top);
/* 801 */             generator.push(size.left);
/* 802 */             generator.push(size.bottom);
/* 803 */             generator.push(size.right);
/* 804 */             generator.invokeStatic(AsmCodeGenerator.ourBorderFactoryType, new Method(borderFactoryMethodName, AsmCodeGenerator.ourBorderType, new Type[] { Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE }));
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 809 */             generator.invokeStatic(AsmCodeGenerator.ourBorderFactoryType, new Method(borderFactoryMethodName, AsmCodeGenerator.ourBorderType, new Type[0]));
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 814 */           generator.push((String)null);
/*     */         } 
/* 816 */         pushBorderProperties(container, generator, borderTitle, componentLocal);
/* 817 */         generator.invokeStatic(AsmCodeGenerator.ourBorderFactoryType, AsmCodeGenerator.ourCreateTitledBorderMethod);
/*     */ 
/*     */         
/* 820 */         generator.invokeVirtual(Type.getType((AsmCodeGenerator.class$javax$swing$JComponent == null) ? (AsmCodeGenerator.class$javax$swing$JComponent = AsmCodeGenerator.class$("javax.swing.JComponent")) : AsmCodeGenerator.class$javax$swing$JComponent), Method.getMethod("void setBorder(javax.swing.border.Border)"));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void pushBorderProperties(LwContainer container, GeneratorAdapter generator, StringDescriptor borderTitle, int componentLocal) {
/* 827 */       AsmCodeGenerator.pushPropValue(generator, "java.lang.String", borderTitle);
/* 828 */       generator.push(container.getBorderTitleJustification());
/* 829 */       generator.push(container.getBorderTitlePosition());
/* 830 */       FontDescriptor font = container.getBorderTitleFont();
/* 831 */       if (font == null) {
/* 832 */         generator.push((String)null);
/*     */       } else {
/*     */         
/* 835 */         FontPropertyCodeGenerator.generatePushFont(generator, componentLocal, (LwComponent)container, font, "getFont");
/*     */       } 
/* 837 */       if (container.getBorderTitleColor() == null) {
/* 838 */         generator.push((String)null);
/*     */       } else {
/*     */         
/* 841 */         AsmCodeGenerator.pushPropValue(generator, ((AsmCodeGenerator.class$java$awt$Color == null) ? (AsmCodeGenerator.class$java$awt$Color = AsmCodeGenerator.class$("java.awt.Color")) : AsmCodeGenerator.class$java$awt$Color).getName(), container.getBorderTitleColor());
/*     */       } 
/*     */     } }
/*     */   
/*     */   private class FormConstructorVisitor extends MethodAdapter {
/*     */     private final String myClassName;
/*     */     private final String mySuperName;
/*     */     private boolean callsSelfConstructor;
/*     */     private boolean mySetupCalled;
/*     */     private boolean mySuperCalled;
/*     */     private final AsmCodeGenerator this$0;
/*     */     
/*     */     public FormConstructorVisitor(AsmCodeGenerator this$0, MethodVisitor mv, String className, String superName) {
/* 854 */       super(mv); this.this$0 = this$0; this.callsSelfConstructor = false; this.mySetupCalled = false; this.mySuperCalled = false;
/* 855 */       this.myClassName = className;
/* 856 */       this.mySuperName = superName;
/*     */     }
/*     */     
/*     */     public void visitFieldInsn(int opcode, String owner, String name, String desc) {
/* 860 */       if (opcode == 180 && !this.mySetupCalled && !this.callsSelfConstructor && Utils.isBoundField((IComponent)this.this$0.myRootContainer, name)) {
/* 861 */         callSetupUI();
/*     */       }
/* 863 */       super.visitFieldInsn(opcode, owner, name, desc);
/*     */     }
/*     */     
/*     */     public void visitMethodInsn(int opcode, String owner, String name, String desc) {
/* 867 */       if (opcode == 183 && name.equals("<init>")) {
/* 868 */         if (owner.equals(this.myClassName)) {
/* 869 */           this.callsSelfConstructor = true;
/*     */         }
/* 871 */         else if (owner.equals(this.mySuperName)) {
/* 872 */           this.mySuperCalled = true;
/*     */         }
/* 874 */         else if (this.mySuperCalled) {
/* 875 */           callSetupUI();
/*     */         }
/*     */       
/* 878 */       } else if (this.mySuperCalled) {
/* 879 */         callSetupUI();
/*     */       } 
/* 881 */       super.visitMethodInsn(opcode, owner, name, desc);
/*     */     }
/*     */     
/*     */     public void visitJumpInsn(int opcode, Label label) {
/* 885 */       if (this.mySuperCalled) {
/* 886 */         callSetupUI();
/*     */       }
/* 888 */       super.visitJumpInsn(opcode, label);
/*     */     }
/*     */     
/*     */     private void callSetupUI() {
/* 892 */       if (!this.mySetupCalled) {
/* 893 */         this.mv.visitVarInsn(25, 0);
/* 894 */         this.mv.visitMethodInsn(183, this.myClassName, "$$$setupUI$$$", "()V");
/* 895 */         this.mySetupCalled = true;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void visitInsn(int opcode) {
/* 900 */       if (opcode == 177 && !this.mySetupCalled && !this.callsSelfConstructor) {
/* 901 */         callSetupUI();
/*     */       }
/* 903 */       super.visitInsn(opcode);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FirstPassClassVisitor extends ClassAdapter {
/*     */     private boolean myExplicitSetupCall = false;
/*     */     
/*     */     public FirstPassClassVisitor() {
/* 911 */       super((ClassVisitor)new EmptyVisitor());
/*     */     }
/*     */     
/*     */     public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
/* 915 */       if (name.equals("<init>")) {
/* 916 */         return (MethodVisitor)new FirstPassConstructorVisitor(this);
/*     */       }
/* 918 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isExplicitSetupCall() {
/* 922 */       return this.myExplicitSetupCall;
/*     */     }
/*     */     private class FirstPassConstructorVisitor extends MethodAdapter { private final AsmCodeGenerator.FirstPassClassVisitor this$0;
/*     */       
/*     */       public FirstPassConstructorVisitor(AsmCodeGenerator.FirstPassClassVisitor this$0) {
/* 927 */         super((MethodVisitor)new EmptyVisitor());
/*     */         this.this$0 = this$0;
/*     */       }
/*     */       public void visitMethodInsn(int opcode, String owner, String name, String desc) {
/* 931 */         if (name.equals("$$$setupUI$$$"))
/* 932 */           this.this$0.myExplicitSetupCall = true; 
/*     */       } }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\AsmCodeGenerator.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */