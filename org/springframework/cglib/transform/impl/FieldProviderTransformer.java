/*     */ package org.springframework.cglib.transform.impl;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.cglib.core.CodeEmitter;
/*     */ import org.springframework.cglib.core.CodeGenerationException;
/*     */ import org.springframework.cglib.core.Constants;
/*     */ import org.springframework.cglib.core.EmitUtils;
/*     */ import org.springframework.cglib.core.ObjectSwitchCallback;
/*     */ import org.springframework.cglib.core.ProcessSwitchCallback;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.core.TypeUtils;
/*     */ import org.springframework.cglib.transform.ClassEmitterTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FieldProviderTransformer
/*     */   extends ClassEmitterTransformer
/*     */ {
/*     */   private static final String FIELD_NAMES = "CGLIB$FIELD_NAMES";
/*     */   private static final String FIELD_TYPES = "CGLIB$FIELD_TYPES";
/*  31 */   private static final Type FIELD_PROVIDER = TypeUtils.parseType("org.springframework.cglib.transform.impl.FieldProvider");
/*     */   
/*  33 */   private static final Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
/*     */   
/*  35 */   private static final Signature PROVIDER_GET = TypeUtils.parseSignature("Object getField(String)");
/*     */   
/*  37 */   private static final Signature PROVIDER_SET = TypeUtils.parseSignature("void setField(String, Object)");
/*     */   
/*  39 */   private static final Signature PROVIDER_SET_BY_INDEX = TypeUtils.parseSignature("void setField(int, Object)");
/*     */   
/*  41 */   private static final Signature PROVIDER_GET_BY_INDEX = TypeUtils.parseSignature("Object getField(int)");
/*     */   
/*  43 */   private static final Signature PROVIDER_GET_TYPES = TypeUtils.parseSignature("Class[] getFieldTypes()");
/*     */   
/*  45 */   private static final Signature PROVIDER_GET_NAMES = TypeUtils.parseSignature("String[] getFieldNames()");
/*     */   
/*     */   private int access;
/*     */   private Map fields;
/*     */   
/*     */   public void begin_class(int version, int access, String className, Type superType, Type[] interfaces, String sourceFile) {
/*  51 */     if (!TypeUtils.isAbstract(access)) {
/*  52 */       interfaces = TypeUtils.add(interfaces, FIELD_PROVIDER);
/*     */     }
/*  54 */     this.access = access;
/*  55 */     this.fields = new HashMap<Object, Object>();
/*  56 */     super.begin_class(version, access, className, superType, interfaces, sourceFile);
/*     */   }
/*     */   
/*     */   public void declare_field(int access, String name, Type type, Object value) {
/*  60 */     super.declare_field(access, name, type, value);
/*     */     
/*  62 */     if (!TypeUtils.isStatic(access)) {
/*  63 */       this.fields.put(name, type);
/*     */     }
/*     */   }
/*     */   
/*     */   public void end_class() {
/*  68 */     if (!TypeUtils.isInterface(this.access)) {
/*     */       try {
/*  70 */         generate();
/*  71 */       } catch (RuntimeException e) {
/*  72 */         throw e;
/*  73 */       } catch (Exception e) {
/*  74 */         throw new CodeGenerationException(e);
/*     */       } 
/*     */     }
/*  77 */     super.end_class();
/*     */   }
/*     */   
/*     */   private void generate() throws Exception {
/*  81 */     String[] names = (String[])this.fields.keySet().toArray((Object[])new String[this.fields.size()]);
/*     */     
/*  83 */     int[] indexes = new int[names.length];
/*  84 */     for (int i = 0; i < indexes.length; i++) {
/*  85 */       indexes[i] = i;
/*     */     }
/*     */     
/*  88 */     super.declare_field(26, "CGLIB$FIELD_NAMES", Constants.TYPE_STRING_ARRAY, null);
/*  89 */     super.declare_field(26, "CGLIB$FIELD_TYPES", Constants.TYPE_CLASS_ARRAY, null);
/*     */ 
/*     */     
/*  92 */     initFieldProvider(names);
/*  93 */     getNames();
/*  94 */     getTypes();
/*  95 */     getField(names);
/*  96 */     setField(names);
/*  97 */     setByIndex(names, indexes);
/*  98 */     getByIndex(names, indexes);
/*     */   }
/*     */   
/*     */   private void initFieldProvider(String[] names) {
/* 102 */     CodeEmitter e = getStaticHook();
/* 103 */     EmitUtils.push_object(e, names);
/* 104 */     e.putstatic(getClassType(), "CGLIB$FIELD_NAMES", Constants.TYPE_STRING_ARRAY);
/*     */     
/* 106 */     e.push(names.length);
/* 107 */     e.newarray(Constants.TYPE_CLASS);
/* 108 */     e.dup();
/* 109 */     for (int i = 0; i < names.length; i++) {
/* 110 */       e.dup();
/* 111 */       e.push(i);
/* 112 */       Type type = (Type)this.fields.get(names[i]);
/* 113 */       EmitUtils.load_class(e, type);
/* 114 */       e.aastore();
/*     */     } 
/* 116 */     e.putstatic(getClassType(), "CGLIB$FIELD_TYPES", Constants.TYPE_CLASS_ARRAY);
/*     */   }
/*     */   
/*     */   private void getNames() {
/* 120 */     CodeEmitter e = begin_method(1, PROVIDER_GET_NAMES, null);
/* 121 */     e.getstatic(getClassType(), "CGLIB$FIELD_NAMES", Constants.TYPE_STRING_ARRAY);
/* 122 */     e.return_value();
/* 123 */     e.end_method();
/*     */   }
/*     */   
/*     */   private void getTypes() {
/* 127 */     CodeEmitter e = begin_method(1, PROVIDER_GET_TYPES, null);
/* 128 */     e.getstatic(getClassType(), "CGLIB$FIELD_TYPES", Constants.TYPE_CLASS_ARRAY);
/* 129 */     e.return_value();
/* 130 */     e.end_method();
/*     */   }
/*     */   
/*     */   private void setByIndex(final String[] names, int[] indexes) throws Exception {
/* 134 */     final CodeEmitter e = begin_method(1, PROVIDER_SET_BY_INDEX, null);
/* 135 */     e.load_this();
/* 136 */     e.load_arg(1);
/* 137 */     e.load_arg(0);
/* 138 */     e.process_switch(indexes, new ProcessSwitchCallback() {
/*     */           public void processCase(int key, Label end) throws Exception {
/* 140 */             Type type = (Type)FieldProviderTransformer.this.fields.get(names[key]);
/* 141 */             e.unbox(type);
/* 142 */             e.putfield(names[key]);
/* 143 */             e.return_value();
/*     */           }
/*     */           public void processDefault() throws Exception {
/* 146 */             e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field index");
/*     */           }
/*     */         });
/* 149 */     e.end_method();
/*     */   }
/*     */   
/*     */   private void getByIndex(final String[] names, int[] indexes) throws Exception {
/* 153 */     final CodeEmitter e = begin_method(1, PROVIDER_GET_BY_INDEX, null);
/* 154 */     e.load_this();
/* 155 */     e.load_arg(0);
/* 156 */     e.process_switch(indexes, new ProcessSwitchCallback() {
/*     */           public void processCase(int key, Label end) throws Exception {
/* 158 */             Type type = (Type)FieldProviderTransformer.this.fields.get(names[key]);
/* 159 */             e.getfield(names[key]);
/* 160 */             e.box(type);
/* 161 */             e.return_value();
/*     */           }
/*     */           public void processDefault() throws Exception {
/* 164 */             e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field index");
/*     */           }
/*     */         });
/* 167 */     e.end_method();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void getField(String[] names) throws Exception {
/* 173 */     final CodeEmitter e = begin_method(1, PROVIDER_GET, null);
/* 174 */     e.load_this();
/* 175 */     e.load_arg(0);
/* 176 */     EmitUtils.string_switch(e, names, 1, new ObjectSwitchCallback() {
/*     */           public void processCase(Object key, Label end) {
/* 178 */             Type type = (Type)FieldProviderTransformer.this.fields.get(key);
/* 179 */             e.getfield((String)key);
/* 180 */             e.box(type);
/* 181 */             e.return_value();
/*     */           }
/*     */           public void processDefault() {
/* 184 */             e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field name");
/*     */           }
/*     */         });
/* 187 */     e.end_method();
/*     */   }
/*     */   
/*     */   private void setField(String[] names) throws Exception {
/* 191 */     final CodeEmitter e = begin_method(1, PROVIDER_SET, null);
/* 192 */     e.load_this();
/* 193 */     e.load_arg(1);
/* 194 */     e.load_arg(0);
/* 195 */     EmitUtils.string_switch(e, names, 1, new ObjectSwitchCallback() {
/*     */           public void processCase(Object key, Label end) {
/* 197 */             Type type = (Type)FieldProviderTransformer.this.fields.get(key);
/* 198 */             e.unbox(type);
/* 199 */             e.putfield((String)key);
/* 200 */             e.return_value();
/*     */           }
/*     */           public void processDefault() {
/* 203 */             e.throw_exception(FieldProviderTransformer.ILLEGAL_ARGUMENT_EXCEPTION, "Unknown field name");
/*     */           }
/*     */         });
/* 206 */     e.end_method();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\transform\impl\FieldProviderTransformer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */