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
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//import java.util.UUID;
//
//import org.apache.maven.artifact.Artifact;
//import org.apache.maven.artifact.DefaultArtifact;
//import org.apache.maven.artifact.handler.ArtifactHandler;
//import org.apache.maven.monitor.logging.DefaultLog;
//import org.apache.maven.plugin.MojoExecution;
//import org.apache.maven.project.MavenProject;
//import org.apache.maven.settings.Server;
//import org.apache.maven.settings.Settings;
//import org.codehaus.plexus.DefaultPlexusContainer;
//import org.codehaus.plexus.PlexusConstants;
//import org.codehaus.plexus.PlexusContainer;
//import org.codehaus.plexus.archiver.manager.ArchiverManager;
//import org.codehaus.plexus.context.Context;
//import org.codehaus.plexus.context.DefaultContext;
//import org.codehaus.plexus.logging.console.ConsoleLogger;
//import org.codehaus.plexus.personality.plexus.lifecycle.phase.StoppingException;
//import org.joor.Reflect;
//import org.json.JSONObject;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import io.cotiviti.packer.InternalDependency;
//import io.cotiviti.packer.PackerException;
//import io.cotiviti.util.CotivitiIOUtilitiesLW;
//import io.cotiviti.util.auth.AuthenticationBean;
//import io.cotiviti.util.auth.AuthenticationConfigBean;
//import io.cotiviti.util.auth.AuthenticationProducer;
//import io.cotiviti.util.auth.AuthenticationProducerFactory;
//
//public class AbstractPackerBuildMojoSetup {
//
//  private Artifact a1;
//  private Artifact a2;
//  private AuthenticationConfigBean acb;
//  private Object authFactory;
//  private PlexusContainer container;
//  protected MojoExecution exec;
//  private ArtifactHandler handler;
//  private PackerImageBuilder image;
//  protected PackerBuildMojo m;
//  private ArchiverManager manager;
//  private InternalDependency r1;
//  private InternalDependency r2;
//  private InternalDependency r3;
//  private InternalDependency r4;
//  private List<InternalDependency> requirements;
//  private Set<Artifact> resolvedArtifacts;
//  protected Path target;
//  protected Path test;
//  private List<AuthenticationProducer> writers;
//  private Type type;
//
//  public AbstractPackerBuildMojoSetup() {
//    super();
//  }
//
//  @Before
//  public void setUp() throws Exception {
//    target = Paths.get(Optional.ofNullable(System.getProperty("target_directory")).orElse("./target")).toAbsolutePath();
//    test = target.resolve(UUID.randomUUID().toString());
//    this.container = new DefaultPlexusContainer();
//    Context context = new DefaultContext();
//    context.put(PlexusConstants.PLEXUS_KEY, this.container);
//    m = new PackerBuildMojo();
//    m.setEncoding(StandardCharsets.UTF_8.name());
//    m.setOverwrite(true);
//    Reflect.on(m).set("projectBuildDirectory", target.toAbsolutePath().toFile());
//
//    m.setSkipActualPackerRun(true);
//    m.setWorkingDir(test.toFile());
//    m.contextualize(context);
//    Settings settings = new Settings();
//    Server server = new Server();
//    server.setId("1");
//    server.setPassword("P");
//    server.setUsername("U");
//    settings.addServer(server);
//    m.setSettings(settings);
//    this.writers = this.container.lookupList(AuthenticationProducer.class);
//    m.setClassifier(null);
//    m.setBaseAuthentications(Collections.emptyList());
//    m.setAdditionalEnvironment(Collections.emptyMap());
//
//    MavenProject p = new MavenProject();
//    p.setName("projectName");
//    r1 = new InternalDependency();
//    r1.setGroupId("junit");
//    r1.setArtifactId("junit");
//    r1.setUnpack(false);
//
//    r2 = new InternalDependency();
//    r2.setGroupId("junit");
//    r2.setArtifactId("junit");
//    r2.setUnpack(true);
//    //    r3 = new InternalDependency();
//    //    r3.setGroupId("io.cotiviti.test.cmar");
//    //    r3.setArtifactId("test-plugin");
//    //    r3.setType("cmar");
//    //    r4 = new InternalDependency();
//    //    r4.setGroupId("io.cotiviti.infrastructure");
//    //    r4.setArtifactId("base-image-test");
//    //    r4.setType("packer");
//    this.requirements = Arrays.asList(r1, r2);
//    this.handler = this.container.lookup(ArtifactHandler.class);
//    this.manager = this.container.lookup(ArchiverManager.class);
//    Reflect.on(m).set("archiverManager", this.manager);
//    //    this.handler = new DefaultArtifactHandler();
//    a1 = new DefaultArtifact("junit", "junit", "4.12", "runtime", "jar", null, this.handler);
//    a1.setFile(target.resolve("test-classes").resolve("junit-4.12.jar").toFile());
//    a2 = new DefaultArtifact("io.cotiviti.infrastructure", "base-image-test", "0.2.0-SNAPSHOT", "runtime", "packer",
//        null, this.handler);
//    a2.setFile(target.resolve("test-classes").resolve("manifest-packer.json").toFile());
//    this.resolvedArtifacts = new HashSet<>(Arrays.asList(a1, a2));
//    p.setArtifacts(resolvedArtifacts);
//    p.setGroupId("X");
//    p.setArtifactId("Y");
//    p.setPackaging("packer");
//    p.setVersion("1.0.0");
//    m.setDescription("describe me");
//    m.setRequirements(requirements);
//    exec = new MojoExecution(null, "abc");
//    Reflect.on(m).set("mojo", exec);
//    m.setLog(new DefaultLog(new ConsoleLogger()));
//    image = new PackerImageBuilder();
//    List<Type> typeMap = new ArrayList<>();
//    type = new Type();
//    type.setHint("cotiviti-amazonebs");
//    typeMap.add(type);
//    image.setTypes(typeMap);
//    Map<String, String> tags = new HashMap<>();
//    tags.put("CostCenter", "LABS");
//    tags.put("Platform", "Linux");
//    image.setTags(tags);
//    this.authFactory = this.container.lookup(AuthenticationProducerFactory.class);
//    this.acb = new AuthenticationConfigBean();
//    this.acb.setCredentialsTempDirectory(test.toFile());
//    AuthenticationBean singleAuth = new AuthenticationBean();
//    singleAuth.setServerId("1");
//    singleAuth.setType("amazon-ebs");
//    singleAuth.setTarget("us-west-2");
//    this.acb.setAuths(Arrays.asList(singleAuth));
//    m.setAuthConfig(acb);
//    m.setImage(image);
//    m.setSettingsJSON(new JSONObject().put("1", new JSONObject().put("username", "x").put("password", "Y")));
//    m.setTmpDir(test.resolve(UUID.randomUUID().toString()).toFile());
//    Artifact art = new DefaultArtifact("X", "Y", "1.0.0", "runtime", "packer", null, handler);
//    p.setArtifact(art);
//    m.setProject(p);
//    m.setOutputDirectory(test.toFile());
//    m.setFinalName("jeff");
//    m.start();
//  }
//
//  @After
//  public void teardown() throws StoppingException {
//    m.stop();
//    CotivitiIOUtilitiesLW.deletePath(test);
//  }
//
//  @Test(expected = PackerException.class)
//  public void testSetPackerExecutable3() {
//    m.setPackerExecutable(target.relativize(target.resolve("packer")).toFile());
//  }
//
//}