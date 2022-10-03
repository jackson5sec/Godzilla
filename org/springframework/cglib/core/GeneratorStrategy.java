package org.springframework.cglib.core;

public interface GeneratorStrategy {
  byte[] generate(ClassGenerator paramClassGenerator) throws Exception;
  
  boolean equals(Object paramObject);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\GeneratorStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */