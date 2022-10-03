/*    */ package org.springframework.core.type.filter;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.aspectj.bridge.IMessageHandler;
/*    */ import org.aspectj.weaver.ResolvedType;
/*    */ import org.aspectj.weaver.World;
/*    */ import org.aspectj.weaver.bcel.BcelWorld;
/*    */ import org.aspectj.weaver.patterns.Bindings;
/*    */ import org.aspectj.weaver.patterns.IScope;
/*    */ import org.aspectj.weaver.patterns.PatternParser;
/*    */ import org.aspectj.weaver.patterns.SimpleScope;
/*    */ import org.aspectj.weaver.patterns.TypePattern;
/*    */ import org.springframework.core.type.classreading.MetadataReader;
/*    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AspectJTypeFilter
/*    */   implements TypeFilter
/*    */ {
/*    */   private final World world;
/*    */   private final TypePattern typePattern;
/*    */   
/*    */   public AspectJTypeFilter(String typePatternExpression, @Nullable ClassLoader classLoader) {
/* 54 */     this.world = (World)new BcelWorld(classLoader, IMessageHandler.THROW, null);
/* 55 */     this.world.setBehaveInJava5Way(true);
/* 56 */     PatternParser patternParser = new PatternParser(typePatternExpression);
/* 57 */     TypePattern typePattern = patternParser.parseTypePattern();
/* 58 */     typePattern.resolve(this.world);
/* 59 */     SimpleScope simpleScope = new SimpleScope(this.world, new org.aspectj.weaver.patterns.FormalBinding[0]);
/* 60 */     this.typePattern = typePattern.resolveBindings((IScope)simpleScope, Bindings.NONE, false, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
/* 68 */     String className = metadataReader.getClassMetadata().getClassName();
/* 69 */     ResolvedType resolvedType = this.world.resolve(className);
/* 70 */     return this.typePattern.matchesStatically(resolvedType);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\type\filter\AspectJTypeFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */