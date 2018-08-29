///**
// * Copyright (C) 2015 Cotiviti Labs (nexgen.admin@cotiviti.io)
// *
// * The software code contained herein is the property of Cotiviti Corporation
// * and its subsidiaries and affiliates (collectively, “Cotiviti”).
// * Access to this software code is being provided to you in the course of your
// * employment or affiliation with Cotiviti and may be used solely in the scope
// * and course of your work for Cotiviti, and is for internal Cotiviti use only.
// * Any unauthorized use, disclosure, copying, distribution, destruction of this
// * software code, or the taking of any unauthorized action in reliance on this
// * software code, is strictly prohibited.
// * If this information is viewed in error, immediately discontinue use of the
// * application.  Anyone using this software code and the applications will be
// * subject to monitoring for improper use, system maintenance and security
// * purposes, and is advised that if such monitoring reveals possible criminal
// * activity or policy violation, Cotiviti personnel may provide the evidence of
// * such monitoring to law enforcement or other officials, and the user may be
// * subject to disciplinary action by Cotiviti, up to and including termination
// * of employment.
// *
// * Use of this software code and any applications and information therein
// * constitutes acknowledgement of and consent to this notice
// */
//package io.cotiviti.packer.maven;
//
//import static io.cotiviti.configuration.management.CMConstants.CMAR_METADATA_JSON_FILENAME;
//import static io.cotiviti.packer.PackerConstantsV1.PACKER;
//import static io.cotiviti.packer.PackerException.et;
//import static io.cotiviti.util.CotivitiIOUtilitiesLW.copy;
//import static io.cotiviti.util.CotivitiIOUtilitiesLW.readJsonObject;
//
//import java.io.File;
//import java.io.PrintStream;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.Properties;
//import java.util.Set;
//import java.util.UUID;
//
//import org.apache.maven.artifact.Artifact;
//import org.apache.maven.artifact.handler.ArtifactHandler;
//import org.apache.maven.plugin.AbstractMojo;
//import org.apache.maven.plugin.MojoExecution;
//import org.apache.maven.plugin.MojoExecutionException;
//import org.apache.maven.plugin.MojoFailureException;
//import org.apache.maven.plugins.annotations.Component;
//import org.apache.maven.plugins.annotations.LifecyclePhase;
//import org.apache.maven.plugins.annotations.Mojo;
//import org.apache.maven.plugins.annotations.Parameter;
//import org.apache.maven.plugins.annotations.ResolutionScope;
//import org.apache.maven.project.MavenProject;
//import org.apache.maven.project.MavenProjectHelper;
//import org.apache.maven.settings.Server;
//import org.apache.maven.settings.Settings;
//import org.codehaus.plexus.PlexusConstants;
//import org.codehaus.plexus.PlexusContainer;
//import org.codehaus.plexus.archiver.UnArchiver;
//import org.codehaus.plexus.archiver.manager.ArchiverManager;
//import org.codehaus.plexus.context.Context;
//import org.codehaus.plexus.context.ContextException;
//import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
//import org.codehaus.plexus.personality.plexus.lifecycle.phase.Startable;
//import org.codehaus.plexus.personality.plexus.lifecycle.phase.StartingException;
//import org.codehaus.plexus.personality.plexus.lifecycle.phase.StoppingException;
//import org.json.JSONObject;
//
//import io.cotiviti.configuration.management.CMARArchive;
//import io.cotiviti.packer.InternalDependency;
//import io.cotiviti.packer.PackerException;
//import io.cotiviti.packer.cmar.PackerGenericCMARArchiveProvisioner;
//import io.cotiviti.util.artifacts.impl.CotivitiGAVImpl;
//import io.cotiviti.util.artifacts.impl.CotivitiGAVLWImpl;
//import io.cotiviti.util.auth.AuthenticationBean;
//import io.cotiviti.util.auth.AuthenticationConfigBean;
//import io.cotiviti.util.logging.SLF4JFromMavenLogger;
//
///**
// * Executes `packer`'s `build` command against the noted targets, collecting the
// * results as a datamap.
// *
// * @author mykelalvis
// *
// */
//
//@Mojo(name = "packer", requiresProject = true, threadSafe = false, requiresDependencyResolution = ResolutionScope.RUNTIME, defaultPhase = LifecyclePhase.PACKAGE)
//public final class PackerBuildMojo extends AbstractMojo implements Contextualizable, PackerExecutionConfig, Startable {
//  protected final static String PACKER_ARCHIVE_FINALIZER_CONFIG = "_PACKER_ARCHIVE_DESCRIPTOR";
//
//  @Parameter
//  private Map<String, String> additionalEnvironment = new HashMap<>();
//
//  @Component
//  private ArchiverManager archiverManager;
//
//  @Component(hint = "packer")
//  private ArtifactHandler artifactHandler;
//
//  /**
//   * Local authentication for mapping of provider names to serverId, and thus
//   * username/password combinations for authentications
//   */
//  @Parameter
//  protected AuthenticationConfigBean authConfig = new AuthenticationConfigBean();
//
//  /**
//   * Base authentications for mapping of provider names to serverId, and thus
//   * username/password combinations for authentications. This allows us to set
//   * defaults in pluginManagement and then override in lower executions.
//   */
//  @Parameter(property = "packer.baseAuthentications")
//  private List<AuthenticationBean> baseAuthentications = new ArrayList<>();
//
//  /**
//   * Classifier to add to the artifact generated. If given, the artifact will be
//   * attached as a supplemental artifact. If not given this will create the main
//   * artifact which is the default behavior. If you try to do that a second time
//   * without using a classifier the build will fail.
//   */
//  @Parameter(property = "packer.classifier")
//  private String classifier;
//
//  /**
//   * This property forces packer to execute as `-on-error=cleanup`. If set to
//   * false, packer will execute with `-on-error=abort`
//   */
//  @Parameter(property = "packer.cleanupOnError", defaultValue = "true")
//  private boolean cleanupOnError;
//
//  // ---------------------- REAL Params -----------------------------------------
//
//  private List<CMARArchive> cmarArtifactData;
//
//  /**
//   * The hint of the PackerCMARProvisioner that will be used to process CMAR
//   * archive
//   */
//  @Parameter(defaultValue = PackerGenericCMARArchiveProvisioner.GENERIC_CMAR)
//  private String cmarHandler;
//
//  // Local vars
//  private PackerBean data = new PackerBean();
//
//  /**
//   * The description of the ingested data
//   *
//   * @since 1.0.0
//   */
//  @Parameter(property = "packer.description", defaultValue = "${project.description}")
//  private String description;
//
//  @Parameter(property = "packer.encoding", defaultValue = "${project.build.sourceEncoding}")
//  private String encoding;
//
//  /**
//   * Comma delimited list of builders to not run. Passsed in as -except
//   */
//  @Parameter(property = "packer.except")
//  private String except;
//
//  /**
//   * Name of the generated artifact.
//   */
//  @Parameter(property = "packer.finalName", required = true, defaultValue = "${project.build.finalName}")
//  private String finalName;
//
//  /**
//   * Data for the -force parameter
//   */
//  @Parameter(property = "packer.force", required = false)
//  private boolean force = false;
//
//  /**
//   * The PackerImageBuilder that configures the build
//   */
//  @Parameter(required = true)
//  private PackerImageBuilder image;
//
//  @Component
//  private MavenProjectHelper mavenProjectHelper;
//
//  @Parameter(defaultValue = "${mojoExecution}", readonly = true)
//  private MojoExecution mojo;
//
//  /**
//   * Comma delimited list of builders to exclusively run. Passed to packer as
//   * -only
//   */
//  @Parameter(property = "packer.only")
//  private String only;
//  /**
//   * Output directory to write output finalized artifacts
//   *
//   * @since 1.0.0
//   */
//  @Parameter(property = "packer.outputDirectory", defaultValue = "${project.build.directory}", required = true)
//  private File outputDirectory;
//
//  /**
//   * Set true to force overwrite of existing files
//   */
//  @Parameter(property = "packer.overwrite", defaultValue = "false")
//  private boolean overwrite;
//
//  /**
//   * These are PackerManifest that are harvested from the InternalDependencies
//   */
//  private List<PackerManifest> packerArtifactData;
//
//  /**
//   * Absolute path to executable packer. In our environment, it is expected to be
//   * delivered via wget from google download plugin
//   *
//   * Whatever the case, this must be an absolute file path to the packer program
//   */
//  @Parameter(property = "packer.executable", required = true, defaultValue = "${project.build.directory}/packer")
//  private File packerExecutable;
//
//  /**
//   * Data for the -parallel= parameter
//   */
//  @Parameter(property = "packer.parallel", defaultValue = "true", required = false)
//  private boolean parallel;
//
//  private PlexusContainer plexusContainer; // Set by impl of Contextualizable.contextualize()
//
//  /**
//   * Maven project to interact with.
//   */
//  @Parameter(property = "project", readonly = true, required = true)
//  private MavenProject project;
//
//  @Parameter(defaultValue = "${project.build.directory}", readonly = true)
//  private File projectBuildDirectory;
//
//  /**
//   * Requirements are artifact identifiers that are individually processed by the
//   * plugin. These MUST be available as resolved artifacts for the project, so if
//   * they are not already available in the dependency tree, processing will fail.
//   */
//  @Parameter
//  private List<InternalDependency> requirements = new ArrayList<>();
//
//  @Parameter(defaultValue = "${settings}", readonly = true, required = true)
//  private Settings settings;
//
//  private JSONObject settingsJSON;
//
//  /**
//   * If true, skip entire execution of this plugin
//   */
//  @Parameter(property = "packer.skip", defaultValue = "false")
//  private boolean skip;
//
//  /**
//   * Set true to skip execution of the packer process itself. This will not fail
//   * packaging, but there will be no primary artifact. The generated packer file
//   * is still produced.
//   */
//  @Parameter(property = "packer.skipActualPackerRun", defaultValue = "false")
//  private boolean skipActualPackerRun;
//
//  /**
//   * Skip creating empty archives.
//   */
//  @Parameter(property = "packer.skipIfEmpty", defaultValue = "true")
//  private boolean skipIfEmpty;
//
//  private SLF4JFromMavenLogger slf4JLogger;
//
//  /**
//   * this is a Duration.parse() capable string describing how long this process
//   * should wait before aborting
//   *
//   * @since 1.0.0
//   */
//  @Parameter(property = "packer.timeout")
//  private String timeout;
//
//  /**
//   * Temporary directory for processing. Volatile and dangerous data may be
//   * written here.
//   *
//   * If at all possible, this should be a RAMDISK, as we will write authentication
//   * data to this during execution and it'd be nice if this disappeared cleanly
//   * afterwards
//   *
//   * @since 1.0.0
//   */
//  @Parameter(property = "packer.tmpdir", defaultValue = "${project.build.directory}/packer-tmp")
//  private File tmpDir;
//
//  /**
//   * Varfile to pass to the packer execution as -varfile
//   */
//  @Parameter(property = "packer.var-file", required = false)
//  private File varFile = null;
//
//  /**
//   * Individual vars passed to packer. Currently, this is not working and is
//   * ignored.
//   */
//  @Parameter(property = "packer.vars", required = false)
//  private Map<String, String> vars = new HashMap<>();
//
//  /**
//   * The directory where all the packer work will be done
//   */
//  @Parameter(property = "packer.working.dir", required = true, defaultValue = "${project.build.directory}/packer-work")
//  private File workingDir;
//
//  private Map<CMARArchive, InternalDependency> cmarMapping = new HashMap<>();
//
//  private void _validatePackerExecutable() {
//    if (this.packerExecutable == null)
//      throw new PackerException("No packer executable is available");
//    if (!this.packerExecutable.isAbsolute())
//      throw new PackerException("The path to the packer executable must be absolute");
//    if (!this.packerExecutable.canExecute())
//      throw new PackerException("The packer executable must be executable");
//  }
//
//  @Override
//  public Map<CMARArchive, InternalDependency> getCMARMapping() {
//    return this.cmarMapping;
//  }
//
//  @Override
//  public void contextualize(Context context) throws ContextException {
//    this.plexusContainer = (PlexusContainer) context.get(PlexusConstants.PLEXUS_KEY);
//  }
//
//  /**
//   *
//   */
//  @Override
//  public void execute() throws MojoExecutionException, MojoFailureException {
//
//    String executionId = mojo.getExecutionId();
//    if (executionId.contains("executionId"))
//      throw new MojoExecutionException("Execution Id cannot contain the string 'executionId'");
//    getLog().info("ExecutionId : " + executionId);
//    try {
//      if (isSkip()) {
//        getLog().info("Skipping plugin execution");
//        return;
//      }
//
//      this.packerArtifactData = resolve(project.getArtifacts());
//      getLog()
//          .info("For execution " + executionId + " we have acquired " + this.packerArtifactData.size() + " elements");
//      this.cmarArtifactData = extract(getRequirements(), project.getArtifacts());
//      this.data.setExecutionConfigFrom(this).executePacker(executionId).ifPresent(out -> {
//        Optional.ofNullable(out.get(PACKER)).ifPresent(finalPath -> {
//          et.withTranslation(() -> {
//            if (skipIfEmpty && Files.size(finalPath) == 0) {
//              getLog().info("Skipping packaging of the " + PACKER);
//            } else {
//              if (getClassifier() != null) {
//                mavenProjectHelper.attachArtifact(project, PACKER, getClassifier(), finalPath.toFile());
//              } else {
//                if (project.getArtifact().getFile() != null && project.getArtifact().getFile().isFile()) {
//                  throw new PackerException("You have to use a classifier "
//                      + "to attach supplemental artifacts to the project instead of replacing them.");
//                }
//                project.getArtifact().setFile(finalPath.toFile());
//              }
//            }
//          });
//        });
//        out.keySet().stream().filter(k -> !PACKER.equals(k)).filter(key -> Files.exists(out.get(key)))
//            .forEach(key -> mavenProjectHelper.attachArtifact(project, key, null, out.get(key).toFile()));
//      });
//    } catch (Exception e) {
//      throw new MojoFailureException("Packer processing exception", e);
//    }
//  }
//
//  /**
//   * Isolated as much as is currently convenient, this method returns a list of
//   * "relevant" artifacts from the dependency tree
//   *
//   * Basically, since packer artifacts aren't part of the classpath, this List of
//   * PackerArtifactData is how we extract previous packer run information from the
//   * artifact repository. It's a bit of going around your elbow to get to your
//   * nose, but it's a very odd piece of code to be running inside a maven run. I
//   * isolated this to enable us to debug and modify it more cleanly, and hopefully
//   * one day I'll actually be able to test it properly.
//   *
//   * @param log
//   * @param requirements
//   * @param resolvedArtifacts
//   * @param extractDir
//   * @param isOverwrite
//   * @param manager
//   * @return
//   * @throws MojoExecutionException
//   * @throws PackerException
//   */
//  public List<CMARArchive> extract(List<InternalDependency> requirements, Set<Artifact> resolvedArtifacts)
//      throws MojoExecutionException {
//
//    /**
//     * A collection of resolved artifacts as dependencies of a given artifact This
//     * allows us to use different versions as dependendencies, but DOES introduce a
//     * weird, possibly cyclic, tree walk.
//     *
//     * Use with caution
//     */
//
//    // Create the extractDir, if needed
//    if (!Files.isDirectory(getWorkingDirectory()))
//      et.withTranslation(() -> Files.createDirectories(getWorkingDirectory()));
//
//    List<CMARArchive> cmarArchives = new ArrayList<>();
//    // InternalDependency elements are handled slightly differently than regular dependencies in Packer runs
//    for (InternalDependency a : requirements) {
//      getLog().info("Dependency checking is " + a);
//      List<Artifact> list1 = new ArrayList<>();
//      //           resolvedArtifacts.stream().filter(art -> PackerBuildMojo.matchArtifact(a,art)).collect(Collectors.toList());
//      for (Artifact a1 : resolvedArtifacts) {
//        getLog().debug("  vs. " + a1);
//        if (PackerBuildMojo.matchArtifact(a, a1)) {
//          list1.add(a1);
//        }
//      }
//      if (list1.size() == 0)
//        throw new MojoExecutionException("There was no matching artifact for " + a);
//      
//      for (Artifact xx : list1) {
//        Path instanceWorkingDir = CMARArchive.CMAR.equals(xx.getType()) ? 
//            getWorkingDirectory().toAbsolutePath().resolve(UUID.randomUUID().toString()) 
//            : getWorkingDirectory().toAbsolutePath();
//        getLog().info("Writing " + xx + " to " + instanceWorkingDir);
//        if (a.isUnpack().orElse(false)) {
//          UnArchiver aa = et.withReturningTranslation(() -> getArchiverManager().getUnArchiver(xx.getFile()));
//          aa.setDestDirectory(instanceWorkingDir.toFile());
//          aa.setOverwrite(a.isOverwrite());
//          et.withTranslation(() -> Files.createDirectories(aa.getDestDirectory().toPath()));
//          aa.setSourceFile(xx.getFile());
//          getLog().info("Unpacking " + aa.getSourceFile() + " to " + instanceWorkingDir);
//          aa.extract();
//          a.setFile(instanceWorkingDir);
//        } else {
//          Path targetPath = instanceWorkingDir.resolve(xx.getFile().getName());
//          getLog().info("Copying " + xx.getFile().toPath() + " to " + targetPath);
//          et.withTranslation(() -> copy(xx.getFile().toPath(), targetPath));
//          a.setFile(targetPath);
//        }
//        a.applyTargetDir(instanceWorkingDir); // !!!! SIDE EFFECT!
//        if (CMARArchive.CMAR.equals(xx.getType())) {
//          getLog().info("Reading CMAR Manifest from " + xx.getFile().toPath());
//          CMARArchive archive = et.withReturningTranslation(() -> new CMARArchive(
//          readJsonObject(instanceWorkingDir.resolve(CMAR_METADATA_JSON_FILENAME)), instanceWorkingDir));
//          // FIXME SIDE EFFECT!
//          this.cmarMapping.put(archive, a);
//          cmarArchives.add(archive);
//        }
//      }
//    }
//    return cmarArchives;
//  }
//
//  public final static boolean matchArtifact(InternalDependency d, Artifact a) {
//    return d.matches(
//        new CotivitiGAVLWImpl(a.getGroupId(), a.getArtifactId(), a.getClassifier(), a.getVersion(), a.getType()));
//  }
//
//  @Override
//  public Map<String, String> getAdditionalEnvironment() {
//    return this.additionalEnvironment;
//  }
//
//  @Override
//  public PrintStream getAdditionalPrintStream() {
//    return System.out;
//  }
//
//  @Override
//  public ArchiverManager getArchiverManager() {
//    return this.archiverManager;
//  }
//
//  @Override
//  public AuthenticationConfigBean getAuthFileWriter() {
//    return this.authConfig;
//  }
//
//  @Override
//  public List<AuthenticationBean> getBaseAuthentications() {
//    return this.baseAuthentications;
//  }
//
//  // Classifier string or null
//  @Override
//  public String getClassifier() {
//    return this.classifier;
//  }
//
//  @Override
//  public List<CMARArchive> getCMARArchives() {
//    return this.cmarArtifactData;
//  }
//
//  @Override
//  public String getCMARHandler() {
//    return cmarHandler;
//  }
//
//  @Override
//  public CotivitiGAVImpl getCoordinates() {
//    org.apache.maven.artifact.Artifact art = this.project.getArtifact();
//    CotivitiGAVImpl bbb = new CotivitiGAVImpl(art.getGroupId(), art.getArtifactId(), getClassifier(), art.getVersion(),
//        PACKER);
//    return bbb;
//  }
//
//  @Override
//  public String getDescription() {
//    return description;
//  }
//
//  @Override
//  public Charset getEncoding() {
//    return this.encoding != null ? Charset.forName(this.encoding) : Charset.defaultCharset();
//  }
//
//  @Override
//  public String getExcept() {
//    return except;
//  }
//
//  @Override
//  public String getFinalName() {
//    return finalName;
//  }
//
//  @Override
//  public PackerImageBuilder getImage() {
//    return image;
//  }
//
//  @Override
//  public String getName() {
//    return this.project.getName();
//  }
//
//  @Override
//  public String getOnly() {
//    return only;
//  }
//
//  @Override
//  public Path getOutputDirectory() {
//    return this.outputDirectory.toPath();
//  }
//
//  @Override
//  public Path getPackerExecutable() {
//    _validatePackerExecutable();
//    return this.packerExecutable.toPath();
//  }
//
//  @Override
//  public List<PackerManifest> getPackerManifests() {
//    return packerArtifactData;
//  }
//
//  @Override
//  public PlexusContainer getPlexusContainer() {
//    return plexusContainer;
//  }
//
//  @Override
//  public File getProjectBuildDirectory() {
//    return this.projectBuildDirectory;
//  }
//
//  @Override
//  public Properties getProperties() {
//    return this.project.getProperties();
//  }
//
//  /**
//   * Gets list of internal dependencies, and sets defaults for those, as well
//   *
//   * @return
//   */
//  @Override
//  public List<InternalDependency> getRequirements() {
//    for (InternalDependency i : this.requirements) {
//      if (!i.isUnpack().isPresent())
//        i.setUnpack(this.isUnpackByDefault());
//    }
//    return requirements;
//  }
//
//  @Override
//  public JSONObject getSettingsJSON() {
//    return this.settingsJSON;
//  }
//
//  @Override
//  public String getTimeout() {
//    return this.timeout;
//  }
//
//  @Override
//  public Path getTmpDir() {
//    return tmpDir.toPath();
//  }
//
//  @Override
//  public File getVarFile() {
//    return varFile;
//  }
//
//  @Override
//  public Map<String, String> getVars() {
//    return vars;
//  }
//
//  @Override
//  public Path getWorkingDirectory() {
//    return this.workingDir.toPath();
//  }
//
//  @Override
//  public boolean isCleanupOnError() {
//    return cleanupOnError;
//  }
//
//  @Override
//  public boolean isForce() {
//    return force;
//  }
//
//  @Override
//  public boolean isOverwrite() {
//    return overwrite;
//  }
//
//  @Override
//  public boolean isParallel() {
//    return parallel;
//  }
//
//  @Override
//  public boolean isSkip() {
//    return skip;
//  }
//
//  @Override
//  public boolean isSkipActualPackerRun() {
//    return skipActualPackerRun;
//  }
//
//  @Override
//  public boolean isSkipIfEmpty() {
//    return skipIfEmpty;
//  }
//
//  @Override
//  public boolean isUnpackByDefault() {
//    return true;
//  }
//
//  /**
//   * Isolated as much as is currently convenient, this method returns a list of
//   * "relevant" artifacts from the dependency tree
//   *
//   * Basically, since packer artifacts aren't part of the classpath, this List of
//   * PackerArtifactData is how we extract previous packer run information from the
//   * artifact repository. It's a bit of going around your elbow to get to your
//   * nose, but it's a very odd piece of code to be running inside a maven run. I
//   * isolated this to enable us to debug and modify it more cleanly, and hopefully
//   * one day I'll actually be able to test it properly.
//   *
//   * @param log
//   * @param requirements
//   * @param resolvedArtifacts
//   * @param extractDir
//   * @param isOverwrite
//   * @param manager
//   * @return
//   * @throws PackerException
//   */
//  public List<PackerManifest> resolve(Set<Artifact> resolvedArtifacts) {
//
//    /**
//     * A collection of resolved artifacts as dependencies of a given artifact This
//     * allows us to use different versions as dependendencies, but DOES introduce a
//     * weird, possibly cyclic, tree walk.
//     *
//     * Use with caution
//     */
//
//    List<PackerManifest> psm = new ArrayList<>();
//    for (org.apache.maven.artifact.Artifact d : resolvedArtifacts) {
//      if (PACKER.equals(d.getType())) {
//        getLog().info("Reading manifest from " + d.getFile().toPath());
//        et.withTranslation(() -> psm
//            .add(new PackerManifest(readJsonObject(d.getFile().toPath().toAbsolutePath()), this.plexusContainer)));
//      }
//    }
//
//    getLog().info("Got \n" + psm.toString());
//
//    return psm;
//  }
//
//  public void setAdditionalEnvironment(Map<String, String> additionalEnvironment) {
//    this.additionalEnvironment = additionalEnvironment;
//  }
//
//  public void setAuthConfig(AuthenticationConfigBean authConfig) {
//    this.authConfig = authConfig;
//  }
//
//  public void setBaseAuthentications(List<AuthenticationBean> baseAuthentications) {
//    this.baseAuthentications = baseAuthentications;
//  }
//
//  public void setClassifier(String classifier) {
//    this.classifier = classifier;
//  }
//
//  public void setDescription(String description) {
//    this.description = description;
//  }
//
//  public void setEncoding(String encoding) {
//    this.encoding = Objects.requireNonNull(encoding);
//  }
//
//  public void setFinalName(String finalName) {
//    this.finalName = finalName;
//  }
//
//  public void setImage(PackerImageBuilder image) {
//    this.image = Objects.requireNonNull(image);
//  }
//
//  public void setOutputDirectory(File outputDirectory) {
//    this.outputDirectory = outputDirectory;
//  }
//
//  public void setOverwrite(boolean overwrite) {
//    this.overwrite = overwrite;
//  }
//
//  public void setPackerExecutable(File packerExecutable) {
//    this.packerExecutable = Objects.requireNonNull(packerExecutable);
//    _validatePackerExecutable();
//
//  }
//
//  public void setProject(MavenProject project) {
//    this.project = Objects.requireNonNull(project);
//  }
//
//  public void setRequirements(List<InternalDependency> requirements) {
//    this.requirements = Objects.requireNonNull(requirements);
//  }
//
//  public void setSettings(Settings settings) {
//    this.settings = Objects.requireNonNull(settings);
//    JSONObject retval = new JSONObject();
//    for (Server s : this.settings.getServers()) {
//      retval.put(s.getId(), new JSONObject(s));
//    }
//    this.settingsJSON = retval;
//  }
//
//  public void setSettingsJSON(JSONObject settingsJSON) {
//    this.settingsJSON = settingsJSON;
//  }
//
//  public void setSkip(boolean skip) {
//    this.skip = skip;
//  }
//
//  public void setSkipActualPackerRun(boolean skipActualPackerRun) {
//    this.skipActualPackerRun = skipActualPackerRun;
//  }
//
//  public void setTimeout(String timeout) {
//    this.timeout = timeout;
//  }
//
//  public void setTmpDir(File tmpDir) {
//    this.tmpDir = tmpDir;
//  }
//
//  public void setVarFile(File varFile) {
//    this.varFile = Objects.requireNonNull(varFile);
//  }
//
//  public void setVars(Map<String, String> vars) {
//    this.vars = Objects.requireNonNull(vars);
//  }
//
//  public void setWorkingDir(File workingDir) {
//    this.workingDir = workingDir;
//  }
//
//  @Override
//  public void start() throws StartingException {
//    getLog().info("Starting " + this.getClass().getName() + " component");
//    slf4JLogger = new SLF4JFromMavenLogger(getLog());
//    this.data.setLog(slf4JLogger);
//
//  }
//
//  @Override
//  public void stop() throws StoppingException {
//    getLog().info("Stopping " + this.getClass().getName() + " component");
//  }
//}