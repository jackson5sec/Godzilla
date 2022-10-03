/*     */ package com.intellij.uiDesigner.compiler;
/*     */ 
/*     */ import com.intellij.uiDesigner.core.GridConstraints;
/*     */ import com.intellij.uiDesigner.lw.ComponentVisitor;
/*     */ import com.intellij.uiDesigner.lw.IComponent;
/*     */ import com.intellij.uiDesigner.lw.IContainer;
/*     */ import com.intellij.uiDesigner.lw.LwNestedForm;
/*     */ import com.intellij.uiDesigner.lw.LwRootContainer;
/*     */ import com.intellij.uiDesigner.lw.PropertiesProvider;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.LayoutManager;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.jdom.Document;
/*     */ import org.jdom.input.SAXBuilder;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Utils
/*     */ {
/*     */   public static final String FORM_NAMESPACE = "http://www.intellij.com/uidesigner/form/";
/*  46 */   private static final SAXParser SAX_PARSER = createParser();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SAXParser createParser() {
/*     */     try {
/*  53 */       return SAXParserFactory.newInstance().newSAXParser();
/*     */     }
/*  55 */     catch (Exception e) {
/*  56 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LwRootContainer getRootContainer(String formFileContent, PropertiesProvider provider) throws Exception {
/*  64 */     if (formFileContent.indexOf("http://www.intellij.com/uidesigner/form/") == -1) {
/*  65 */       throw new AlienFormFileException();
/*     */     }
/*     */     
/*  68 */     Document document = (new SAXBuilder()).build(new StringReader(formFileContent), "UTF-8");
/*     */     
/*  70 */     LwRootContainer root = new LwRootContainer();
/*  71 */     root.read(document.getRootElement(), provider);
/*     */     
/*  73 */     return root;
/*     */   }
/*     */   
/*     */   public static LwRootContainer getRootContainer(InputStream stream, PropertiesProvider provider) throws Exception {
/*  77 */     Document document = (new SAXBuilder()).build(stream, "UTF-8");
/*     */     
/*  79 */     LwRootContainer root = new LwRootContainer();
/*  80 */     root.read(document.getRootElement(), provider);
/*     */     
/*  82 */     return root;
/*     */   }
/*     */   
/*     */   public static synchronized String getBoundClassName(String formFileContent) throws Exception {
/*  86 */     if (formFileContent.indexOf("http://www.intellij.com/uidesigner/form/") == -1) {
/*  87 */       throw new AlienFormFileException();
/*     */     }
/*     */     
/*  90 */     String[] className = { null };
/*     */     try {
/*  92 */       SAX_PARSER.parse(new InputSource(new StringReader(formFileContent)), new DefaultHandler(className)
/*     */           {
/*     */             private final String[] val$className;
/*     */ 
/*     */             
/*     */             public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/*  98 */               if ("form".equals(qName)) {
/*  99 */                 this.val$className[0] = attributes.getValue("", "bind-to-class");
/* 100 */                 throw new SAXException("stop parsing");
/*     */               }
/*     */             
/*     */             }
/*     */           });
/* 105 */     } catch (Exception e) {}
/*     */ 
/*     */ 
/*     */     
/* 109 */     return className[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String validateJComponentClass(ClassLoader loader, String className, boolean validateConstructor) {
/*     */     Class aClass;
/* 120 */     if (loader == null) {
/* 121 */       throw new IllegalArgumentException("loader cannot be null");
/*     */     }
/* 123 */     if (className == null) {
/* 124 */       throw new IllegalArgumentException("className cannot be null");
/*     */     }
/*     */ 
/*     */     
/* 128 */     if ("com.intellij.uiDesigner.HSpacer".equals(className) || "com.intellij.uiDesigner.VSpacer".equals(className))
/*     */     {
/*     */ 
/*     */       
/* 132 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 137 */       aClass = Class.forName(className, true, loader);
/*     */     }
/* 139 */     catch (ClassNotFoundException exc) {
/* 140 */       return "Class \"" + className + "\"not found";
/*     */     }
/* 142 */     catch (NoClassDefFoundError exc) {
/* 143 */       return "Cannot load class " + className + ": " + exc.getMessage();
/*     */     }
/* 145 */     catch (ExceptionInInitializerError exc) {
/* 146 */       return "Cannot initialize class " + className + ": " + exc.getMessage();
/*     */     }
/* 148 */     catch (UnsupportedClassVersionError exc) {
/* 149 */       return "Unsupported class version error: " + className;
/*     */     } 
/*     */     
/* 152 */     if (validateConstructor) {
/*     */       try {
/* 154 */         Constructor constructor = aClass.getConstructor(new Class[0]);
/* 155 */         if ((constructor.getModifiers() & 0x1) == 0) {
/* 156 */           return "Class \"" + className + "\" does not have default public constructor";
/*     */         }
/*     */       }
/* 159 */       catch (Exception exc) {
/* 160 */         return "Class \"" + className + "\" does not have default constructor";
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 166 */     if (!JComponent.class.isAssignableFrom(aClass)) {
/* 167 */       return "Class \"" + className + "\" is not an instance of javax.swing.JComponent";
/*     */     }
/*     */     
/* 170 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void validateNestedFormLoop(String formName, NestedFormLoader nestedFormLoader) throws CodeGenerationException, RecursiveFormNestingException {
/* 175 */     validateNestedFormLoop(formName, nestedFormLoader, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void validateNestedFormLoop(String formName, NestedFormLoader nestedFormLoader, String targetForm) throws CodeGenerationException, RecursiveFormNestingException {
/* 180 */     HashSet usedFormNames = new HashSet();
/* 181 */     if (targetForm != null) {
/* 182 */       usedFormNames.add(targetForm);
/*     */     }
/* 184 */     validateNestedFormLoop(usedFormNames, formName, nestedFormLoader);
/*     */   }
/*     */   
/*     */   private static void validateNestedFormLoop(Set usedFormNames, String formName, NestedFormLoader nestedFormLoader) throws CodeGenerationException, RecursiveFormNestingException {
/*     */     LwRootContainer rootContainer;
/* 189 */     if (usedFormNames.contains(formName)) {
/* 190 */       throw new RecursiveFormNestingException();
/*     */     }
/* 192 */     usedFormNames.add(formName);
/*     */     
/*     */     try {
/* 195 */       rootContainer = nestedFormLoader.loadForm(formName);
/*     */     }
/* 197 */     catch (Exception e) {
/* 198 */       throw new CodeGenerationException(null, "Error loading nested form: " + e.getMessage());
/*     */     } 
/* 200 */     Set thisFormNestedForms = new HashSet();
/* 201 */     CodeGenerationException[] validateExceptions = new CodeGenerationException[1];
/* 202 */     RecursiveFormNestingException[] recursiveNestingExceptions = new RecursiveFormNestingException[1];
/* 203 */     rootContainer.accept(new ComponentVisitor(thisFormNestedForms, usedFormNames, nestedFormLoader, recursiveNestingExceptions, validateExceptions) { private final Set val$thisFormNestedForms; private final Set val$usedFormNames;
/*     */           public boolean visit(IComponent component) {
/* 205 */             if (component instanceof LwNestedForm) {
/* 206 */               LwNestedForm nestedForm = (LwNestedForm)component;
/* 207 */               if (!this.val$thisFormNestedForms.contains(nestedForm.getFormFileName())) {
/* 208 */                 this.val$thisFormNestedForms.add(nestedForm.getFormFileName());
/*     */                 try {
/* 210 */                   Utils.validateNestedFormLoop(this.val$usedFormNames, nestedForm.getFormFileName(), this.val$nestedFormLoader);
/*     */                 }
/* 212 */                 catch (RecursiveFormNestingException e) {
/* 213 */                   this.val$recursiveNestingExceptions[0] = e;
/* 214 */                   return false;
/*     */                 }
/* 216 */                 catch (CodeGenerationException e) {
/* 217 */                   this.val$validateExceptions[0] = e;
/* 218 */                   return false;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 222 */             return true;
/*     */           } private final NestedFormLoader val$nestedFormLoader; private final RecursiveFormNestingException[] val$recursiveNestingExceptions; private final CodeGenerationException[] val$validateExceptions; }
/*     */       );
/* 225 */     if (recursiveNestingExceptions[0] != null) {
/* 226 */       throw recursiveNestingExceptions[0];
/*     */     }
/* 228 */     if (validateExceptions[0] != null) {
/* 229 */       throw validateExceptions[0];
/*     */     }
/*     */   }
/*     */   
/*     */   public static String findNotEmptyPanelWithXYLayout(IComponent component) {
/* 234 */     if (!(component instanceof IContainer)) {
/* 235 */       return null;
/*     */     }
/* 237 */     IContainer container = (IContainer)component;
/* 238 */     if (container.getComponentCount() == 0) {
/* 239 */       return null;
/*     */     }
/* 241 */     if (container.isXY()) {
/* 242 */       return container.getId();
/*     */     }
/* 244 */     for (int i = 0; i < container.getComponentCount(); i++) {
/* 245 */       String id = findNotEmptyPanelWithXYLayout(container.getComponent(i));
/* 246 */       if (id != null) {
/* 247 */         return id;
/*     */       }
/*     */     } 
/* 250 */     return null;
/*     */   }
/*     */   
/*     */   public static int getHGap(LayoutManager layout) {
/* 254 */     if (layout instanceof BorderLayout) {
/* 255 */       return ((BorderLayout)layout).getHgap();
/*     */     }
/* 257 */     if (layout instanceof CardLayout) {
/* 258 */       return ((CardLayout)layout).getHgap();
/*     */     }
/* 260 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getVGap(LayoutManager layout) {
/* 264 */     if (layout instanceof BorderLayout) {
/* 265 */       return ((BorderLayout)layout).getVgap();
/*     */     }
/* 267 */     if (layout instanceof CardLayout) {
/* 268 */       return ((CardLayout)layout).getVgap();
/*     */     }
/* 270 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getCustomCreateComponentCount(IContainer container) {
/* 274 */     int[] result = new int[1];
/* 275 */     result[0] = 0;
/* 276 */     container.accept(new ComponentVisitor(result) { private final int[] val$result;
/*     */           public boolean visit(IComponent c) {
/* 278 */             if (c.isCustomCreate()) {
/* 279 */               this.val$result[0] = this.val$result[0] + 1;
/*     */             }
/* 281 */             return true;
/*     */           } }
/*     */       );
/* 284 */     return result[0];
/*     */   }
/*     */   
/*     */   public static Class suggestReplacementClass(Class componentClass) {
/*     */     while (true) {
/* 289 */       componentClass = componentClass.getSuperclass();
/* 290 */       if (componentClass.equals(JComponent.class)) {
/* 291 */         return JPanel.class;
/*     */       }
/* 293 */       if ((componentClass.getModifiers() & 0x402) != 0) {
/*     */         continue;
/*     */       }
/*     */       try {
/* 297 */         componentClass.getConstructor(new Class[0]);
/*     */         break;
/* 299 */       } catch (NoSuchMethodException ex) {}
/*     */     } 
/*     */     
/* 302 */     return componentClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int alignFromConstraints(GridConstraints gc, boolean horizontal) {
/* 307 */     int anchor = gc.getAnchor();
/* 308 */     int fill = gc.getFill();
/* 309 */     int leftMask = horizontal ? 8 : 1;
/* 310 */     int rightMask = horizontal ? 4 : 2;
/* 311 */     int fillMask = horizontal ? 1 : 2;
/* 312 */     if ((fill & fillMask) != 0) return 3; 
/* 313 */     if ((anchor & rightMask) != 0) return 2; 
/* 314 */     if ((anchor & leftMask) != 0) return 0; 
/* 315 */     return 1;
/*     */   }
/*     */   
/*     */   public static boolean isBoundField(IComponent component, String fieldName) {
/* 319 */     if (fieldName.equals(component.getBinding())) {
/* 320 */       return true;
/*     */     }
/* 322 */     if (component instanceof IContainer) {
/* 323 */       IContainer container = (IContainer)component;
/* 324 */       for (int i = 0; i < container.getComponentCount(); i++) {
/* 325 */         if (isBoundField(container.getComponent(i), fieldName)) {
/* 326 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 330 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\Utils.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */