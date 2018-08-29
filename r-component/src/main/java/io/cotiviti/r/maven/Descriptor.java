package io.cotiviti.r.maven;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.cotiviti.util.artifacts.CotivitiVersion;

public class Descriptor {

  private String packageString;
  private CotivitiVersion version;
  private String license;
  private String description;
  private String title;
  private String author;
  private String maintainer;

  // Depends’, ‘Imports’, ‘Suggests’, ‘Enhances’, ‘LinkingTo’ and
  // ‘Additional_repositories
  // SystemRequirements

  // Priority
  private RPriority priority;

  // Collate
  private List<String> collate;

  private boolean generateCollation; // Not a field, but a setter for now

  // ‘Collate.unix’ or ‘Collate.windows’

  // LazyData

  private boolean keepSource;

  private boolean byteCompile;
  private boolean zipData;

  private boolean biarch;
  private boolean buildVignettes;

  private List<String> vignetteBuilder; // "utils" always implicitly appended
  private Charset encoding = Charset.forName("UTF8");

  private boolean needsCompilation;

  private List<String> OS_Type;

  private PackagingType type;
  //
  private Locale language;

  private List<String> RdMacros;

  private Map<RClassification, List<String>> classification;

  
  // GENERATED BEAN FIELDS.  TO BE MANAGED

  public String getPackageString() {
    return packageString;
  }

  public void setPackageString(String packageString) {
    this.packageString = packageString;
  }

  public CotivitiVersion getVersion() {
    return version;
  }

  public void setVersion(CotivitiVersion version) {
    this.version = version;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getMaintainer() {
    return maintainer;
  }

  public void setMaintainer(String maintainer) {
    this.maintainer = maintainer;
  }

  public RPriority getPriority() {
    return priority;
  }

  public void setPriority(RPriority priority) {
    this.priority = priority;
  }

  public List<String> getCollate() {
    return collate;
  }

  public void setCollate(List<String> collate) {
    this.collate = collate;
  }

  public boolean isGenerateCollation() {
    return generateCollation;
  }

  public void setGenerateCollation(boolean generateCollation) {
    this.generateCollation = generateCollation;
  }

  public boolean isKeepSource() {
    return keepSource;
  }

  public void setKeepSource(boolean keepSource) {
    this.keepSource = keepSource;
  }

  public boolean isByteCompile() {
    return byteCompile;
  }

  public void setByteCompile(boolean byteCompile) {
    this.byteCompile = byteCompile;
  }

  public boolean isZipData() {
    return zipData;
  }

  public void setZipData(boolean zipData) {
    this.zipData = zipData;
  }

  public boolean isBiarch() {
    return biarch;
  }

  public void setBiarch(boolean biarch) {
    this.biarch = biarch;
  }

  public boolean isBuildVignettes() {
    return buildVignettes;
  }

  public void setBuildVignettes(boolean buildVignettes) {
    this.buildVignettes = buildVignettes;
  }

  public List<String> getVignetteBuilder() {
    return vignetteBuilder;
  }

  public void setVignetteBuilder(List<String> vignetteBuilder) {
    this.vignetteBuilder = vignetteBuilder;
  }

  public Charset getEncoding() {
    return encoding;
  }

  public void setEncoding(Charset encoding) {
    this.encoding = encoding;
  }

  public boolean isNeedsCompilation() {
    return needsCompilation;
  }

  public void setNeedsCompilation(boolean needsCompilation) {
    this.needsCompilation = needsCompilation;
  }

  public List<String> getOS_Type() {
    return OS_Type;
  }

  public void setOS_Type(List<String> oS_Type) {
    OS_Type = oS_Type;
  }

  public PackagingType getType() {
    return type;
  }

  public void setType(PackagingType type) {
    this.type = type;
  }

  public Locale getLanguage() {
    return language;
  }

  public void setLanguage(Locale language) {
    this.language = language;
  }

  public List<String> getRdMacros() {
    return RdMacros;
  }

  public void setRdMacros(List<String> rdMacros) {
    RdMacros = rdMacros;
  }

  public Map<RClassification, List<String>> getClassification() {
    return classification;
  }

  public void setClassification(Map<RClassification, List<String>> classification) {
    this.classification = classification;
  }
  
  
  
}
