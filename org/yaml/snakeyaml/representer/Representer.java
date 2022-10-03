/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.annotation.YamlComment;
/*     */ import org.yaml.snakeyaml.comments.CommentLine;
/*     */ import org.yaml.snakeyaml.comments.CommentType;
/*     */ import org.yaml.snakeyaml.events.CommentEvent;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Representer
/*     */   extends SafeRepresenter
/*     */ {
/*  51 */   protected Map<Class<? extends Object>, TypeDescription> typeDefinitions = Collections.emptyMap();
/*     */   
/*     */   public Representer() {
/*  54 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   public Representer(DumperOptions options) {
/*  58 */     super(options);
/*  59 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   public TypeDescription addTypeDescription(TypeDescription td) {
/*  63 */     if (Collections.EMPTY_MAP == this.typeDefinitions) {
/*  64 */       this.typeDefinitions = new HashMap<>();
/*     */     }
/*  66 */     if (td.getTag() != null) {
/*  67 */       addClassTag(td.getType(), td.getTag());
/*     */     }
/*  69 */     td.setPropertyUtils(getPropertyUtils());
/*  70 */     return this.typeDefinitions.put(td.getType(), td);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/*  75 */     super.setPropertyUtils(propertyUtils);
/*  76 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/*  77 */     for (TypeDescription typeDescription : tds)
/*  78 */       typeDescription.setPropertyUtils(propertyUtils); 
/*     */   }
/*     */   
/*     */   protected class RepresentJavaBean
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/*  84 */       return (Node)Representer.this.representJavaBean(Representer.this.getProperties((Class)data.getClass()), data);
/*     */     }
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
/*     */   protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
/* 103 */     List<NodeTuple> value = new ArrayList<>(properties.size());
/*     */     
/* 105 */     Tag customTag = this.classTags.get(javaBean.getClass());
/* 106 */     Tag tag = (customTag != null) ? customTag : new Tag(javaBean.getClass());
/*     */     
/* 108 */     MappingNode node = new MappingNode(tag, value, DumperOptions.FlowStyle.AUTO);
/* 109 */     this.representedObjects.put(javaBean, node);
/* 110 */     DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
/* 111 */     for (Property property : properties) {
/* 112 */       Object memberValue = property.get(javaBean);
/*     */       
/* 114 */       Tag customPropertyTag = (memberValue == null) ? null : this.classTags.get(memberValue.getClass());
/* 115 */       NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
/*     */       
/* 117 */       if (tuple == null) {
/*     */         continue;
/*     */       }
/* 120 */       Node nodeKey = tuple.getKeyNode();
/* 121 */       if (!((ScalarNode)nodeKey).isPlain()) {
/* 122 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 124 */       Node nodeValue = tuple.getValueNode();
/* 125 */       if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain()) {
/* 126 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 128 */       YamlComment comment = (YamlComment)property.getAnnotation(YamlComment.class);
/* 129 */       if (comment != null) {
/* 130 */         if (nodeKey.getBlockComments() == null) {
/* 131 */           nodeKey.setBlockComments(new ArrayList());
/*     */         }
/* 133 */         nodeKey.getBlockComments().add(new CommentLine(new CommentEvent(CommentType.BLOCK, comment.Comment(), null, null)));
/*     */       } 
/* 135 */       value.add(tuple);
/*     */     } 
/* 137 */     if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 138 */       node.setFlowStyle(this.defaultFlowStyle);
/*     */     } else {
/* 140 */       node.setFlowStyle(bestStyle);
/*     */     } 
/* 142 */     return node;
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
/*     */   protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
/* 161 */     ScalarNode nodeKey = (ScalarNode)representData(property.getName());
/*     */     
/* 163 */     boolean hasAlias = this.representedObjects.containsKey(propertyValue);
/*     */     
/* 165 */     Node nodeValue = representData(propertyValue);
/*     */     
/* 167 */     if (propertyValue != null && !hasAlias) {
/* 168 */       NodeId nodeId = nodeValue.getNodeId();
/* 169 */       if (customTag == null) {
/* 170 */         if (nodeId == NodeId.scalar) {
/*     */           
/* 172 */           if (property.getType() != Enum.class && 
/* 173 */             propertyValue instanceof Enum) {
/* 174 */             nodeValue.setTag(Tag.STR);
/*     */           }
/*     */         } else {
/*     */           
/* 178 */           if (nodeId == NodeId.mapping && 
/* 179 */             property.getType() == propertyValue.getClass() && 
/* 180 */             !(propertyValue instanceof Map) && 
/* 181 */             !nodeValue.getTag().equals(Tag.SET)) {
/* 182 */             nodeValue.setTag(Tag.MAP);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 187 */           checkGlobalTag(property, nodeValue, propertyValue);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 192 */     return new NodeTuple((Node)nodeKey, nodeValue);
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
/*     */   protected void checkGlobalTag(Property property, Node node, Object object) {
/* 209 */     if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive()) {
/*     */       return;
/*     */     }
/*     */     
/* 213 */     Class<?>[] arguments = property.getActualTypeArguments();
/* 214 */     if (arguments != null) {
/* 215 */       if (node.getNodeId() == NodeId.sequence) {
/*     */         
/* 217 */         Class<? extends Object> t = (Class)arguments[0];
/* 218 */         SequenceNode snode = (SequenceNode)node;
/* 219 */         Iterable<Object> memberList = Collections.EMPTY_LIST;
/* 220 */         if (object.getClass().isArray()) {
/* 221 */           memberList = Arrays.asList((Object[])object);
/* 222 */         } else if (object instanceof Iterable) {
/*     */           
/* 224 */           memberList = (Iterable<Object>)object;
/*     */         } 
/* 226 */         Iterator<Object> iter = memberList.iterator();
/* 227 */         if (iter.hasNext()) {
/* 228 */           for (Node childNode : snode.getValue()) {
/* 229 */             Object member = iter.next();
/* 230 */             if (member != null && 
/* 231 */               t.equals(member.getClass()) && 
/* 232 */               childNode.getNodeId() == NodeId.mapping) {
/* 233 */               childNode.setTag(Tag.MAP);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/* 238 */       } else if (object instanceof Set) {
/* 239 */         Class<?> t = arguments[0];
/* 240 */         MappingNode mnode = (MappingNode)node;
/* 241 */         Iterator<NodeTuple> iter = mnode.getValue().iterator();
/* 242 */         Set<?> set = (Set)object;
/* 243 */         for (Object member : set) {
/* 244 */           NodeTuple tuple = iter.next();
/* 245 */           Node keyNode = tuple.getKeyNode();
/* 246 */           if (t.equals(member.getClass()) && 
/* 247 */             keyNode.getNodeId() == NodeId.mapping) {
/* 248 */             keyNode.setTag(Tag.MAP);
/*     */           }
/*     */         }
/*     */       
/* 252 */       } else if (object instanceof Map) {
/* 253 */         Class<?> keyType = arguments[0];
/* 254 */         Class<?> valueType = arguments[1];
/* 255 */         MappingNode mnode = (MappingNode)node;
/* 256 */         for (NodeTuple tuple : mnode.getValue()) {
/* 257 */           resetTag((Class)keyType, tuple.getKeyNode());
/* 258 */           resetTag((Class)valueType, tuple.getValueNode());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetTag(Class<? extends Object> type, Node node) {
/* 268 */     Tag tag = node.getTag();
/* 269 */     if (tag.matches(type)) {
/* 270 */       if (Enum.class.isAssignableFrom(type)) {
/* 271 */         node.setTag(Tag.STR);
/*     */       } else {
/* 273 */         node.setTag(Tag.MAP);
/*     */       } 
/*     */     }
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
/*     */   protected Set<Property> getProperties(Class<? extends Object> type) {
/* 287 */     if (this.typeDefinitions.containsKey(type)) {
/* 288 */       return ((TypeDescription)this.typeDefinitions.get(type)).getProperties();
/*     */     }
/* 290 */     return getPropertyUtils().getProperties(type);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\yaml\snakeyaml\representer\Representer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */