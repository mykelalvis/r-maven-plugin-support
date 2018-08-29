package io.cotiviti.r.maven;

import java.nio.charset.Charset;

import org.apache.maven.plugin.AbstractMojo;

abstract public class AbstractRMojo extends AbstractMojo {
  
  private String encoding;
  
  public Charset getEncoding() {
    return Charset.forName(this.encoding);
  }

}
