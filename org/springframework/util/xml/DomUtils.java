/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.ContentHandler;
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
/*     */ public abstract class DomUtils
/*     */ {
/*     */   public static List<Element> getChildElementsByTagName(Element ele, String... childEleNames) {
/*  61 */     Assert.notNull(ele, "Element must not be null");
/*  62 */     Assert.notNull(childEleNames, "Element names collection must not be null");
/*  63 */     List<String> childEleNameList = Arrays.asList(childEleNames);
/*  64 */     NodeList nl = ele.getChildNodes();
/*  65 */     List<Element> childEles = new ArrayList<>();
/*  66 */     for (int i = 0; i < nl.getLength(); i++) {
/*  67 */       Node node = nl.item(i);
/*  68 */       if (node instanceof Element && nodeNameMatch(node, childEleNameList)) {
/*  69 */         childEles.add((Element)node);
/*     */       }
/*     */     } 
/*  72 */     return childEles;
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
/*     */   public static List<Element> getChildElementsByTagName(Element ele, String childEleName) {
/*  86 */     return getChildElementsByTagName(ele, new String[] { childEleName });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Element getChildElementByTagName(Element ele, String childEleName) {
/*  97 */     Assert.notNull(ele, "Element must not be null");
/*  98 */     Assert.notNull(childEleName, "Element name must not be null");
/*  99 */     NodeList nl = ele.getChildNodes();
/* 100 */     for (int i = 0; i < nl.getLength(); i++) {
/* 101 */       Node node = nl.item(i);
/* 102 */       if (node instanceof Element && nodeNameMatch(node, childEleName)) {
/* 103 */         return (Element)node;
/*     */       }
/*     */     } 
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static String getChildElementValueByTagName(Element ele, String childEleName) {
/* 117 */     Element child = getChildElementByTagName(ele, childEleName);
/* 118 */     return (child != null) ? getTextValue(child) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Element> getChildElements(Element ele) {
/* 127 */     Assert.notNull(ele, "Element must not be null");
/* 128 */     NodeList nl = ele.getChildNodes();
/* 129 */     List<Element> childEles = new ArrayList<>();
/* 130 */     for (int i = 0; i < nl.getLength(); i++) {
/* 131 */       Node node = nl.item(i);
/* 132 */       if (node instanceof Element) {
/* 133 */         childEles.add((Element)node);
/*     */       }
/*     */     } 
/* 136 */     return childEles;
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
/*     */   public static String getTextValue(Element valueEle) {
/* 149 */     Assert.notNull(valueEle, "Element must not be null");
/* 150 */     StringBuilder sb = new StringBuilder();
/* 151 */     NodeList nl = valueEle.getChildNodes();
/* 152 */     for (int i = 0; i < nl.getLength(); i++) {
/* 153 */       Node item = nl.item(i);
/* 154 */       if ((item instanceof org.w3c.dom.CharacterData && !(item instanceof org.w3c.dom.Comment)) || item instanceof org.w3c.dom.EntityReference) {
/* 155 */         sb.append(item.getNodeValue());
/*     */       }
/*     */     } 
/* 158 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean nodeNameEquals(Node node, String desiredName) {
/* 167 */     Assert.notNull(node, "Node must not be null");
/* 168 */     Assert.notNull(desiredName, "Desired name must not be null");
/* 169 */     return nodeNameMatch(node, desiredName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler createContentHandler(Node node) {
/* 178 */     return new DomContentHandler(node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean nodeNameMatch(Node node, String desiredName) {
/* 185 */     return (desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean nodeNameMatch(Node node, Collection<?> desiredNames) {
/* 192 */     return (desiredNames.contains(node.getNodeName()) || desiredNames.contains(node.getLocalName()));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\xml\DomUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */