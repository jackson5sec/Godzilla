/*    */ package org.fife.rsta.ac.js.ast.jsType;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import org.fife.rsta.ac.js.Logger;
/*    */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ import org.fife.rsta.ac.js.completion.JSCompletion;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JSR223Type
/*    */   extends JavaScriptType
/*    */ {
/*    */   public JSR223Type(TypeDeclaration type) {
/* 16 */     super(type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected JSCompletion _getCompletion(String completionLookup, SourceCompletionProvider provider) {
/* 28 */     JSCompletion completion = this.methodFieldCompletions.get(completionLookup);
/* 29 */     if (completion != null) {
/* 30 */       return completion;
/*    */     }
/*    */     
/* 33 */     if (completionLookup.indexOf('(') != -1) {
/* 34 */       boolean isJavaScriptType = provider.getTypesFactory().isJavaScriptType(getType());
/*    */ 
/*    */       
/* 37 */       Logger.log("Completion Lookup : " + completionLookup);
/*    */       
/* 39 */       JavaScriptFunctionType javaScriptFunctionType = JavaScriptFunctionType.parseFunction(completionLookup, provider);
/*    */       
/* 41 */       JSCompletion[] matches = getPotentialLookupList(javaScriptFunctionType
/* 42 */           .getName());
/*    */ 
/*    */       
/* 45 */       int bestFitIndex = -1;
/* 46 */       int bestFitWeight = -1;
/* 47 */       Logger.log("Potential matches : " + matches.length);
/* 48 */       for (int i = 0; i < matches.length; i++) {
/* 49 */         Logger.log("Potential match : " + matches[i].getLookupName());
/*    */         
/* 51 */         JavaScriptFunctionType matchFunctionType = JavaScriptFunctionType.parseFunction(matches[i].getLookupName(), provider);
/* 52 */         Logger.log("Matching against completion: " + completionLookup);
/* 53 */         int weight = matchFunctionType.compare(javaScriptFunctionType, provider, isJavaScriptType);
/*    */         
/* 55 */         Logger.log("Weight: " + weight);
/* 56 */         if (weight < JavaScriptFunctionType.CONVERSION_NONE && (weight < bestFitWeight || bestFitIndex == -1)) {
/*    */           
/* 58 */           bestFitIndex = i;
/* 59 */           bestFitWeight = weight;
/*    */         } 
/*    */       } 
/* 62 */       if (bestFitIndex > -1) {
/*    */         
/* 64 */         Logger.log("BEST FIT: " + matches[bestFitIndex]
/* 65 */             .getLookupName());
/* 66 */         return matches[bestFitIndex];
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private JSCompletion[] getPotentialLookupList(String name) {
/* 76 */     HashSet<JSCompletion> completionMatches = new HashSet<>();
/* 77 */     getPotentialLookupList(name, completionMatches, this);
/* 78 */     return (JSCompletion[])completionMatches.toArray((Object[])new JSCompletion[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void getPotentialLookupList(String name, HashSet<JSCompletion> completionMatches, JavaScriptType type) {
/* 85 */     Map<String, JSCompletion> typeCompletions = type.methodFieldCompletions;
/*    */     
/* 87 */     for (String key : typeCompletions.keySet()) {
/* 88 */       if (key.startsWith(name)) {
/* 89 */         JSCompletion completion = typeCompletions.get(key);
/* 90 */         if (completion instanceof org.fife.ui.autocomplete.FunctionCompletion) {
/* 91 */           completionMatches.add(completion);
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 97 */     for (JavaScriptType extendedType : type.getExtendedClasses())
/* 98 */       getPotentialLookupList(name, completionMatches, extendedType); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\jsType\JSR223Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */