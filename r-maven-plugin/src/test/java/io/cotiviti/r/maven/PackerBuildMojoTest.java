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
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import java.io.File;
//import java.nio.charset.StandardCharsets;
//import java.util.Collections;
//import java.util.Map;
//
//import org.apache.maven.plugin.MojoExecution;
//import org.apache.maven.plugin.MojoExecutionException;
//import org.apache.maven.plugin.MojoFailureException;
//import org.joor.Reflect;
//import org.junit.Test;
//
//import io.cotiviti.packer.PackerException;
//
//public class PackerBuildMojoTest extends AbstractPackerBuildMojoSetup {
//
//  @Test
//  public void testExecute() {
//    m.setPackerExecutable(target.resolve("packer").toAbsolutePath().toFile());
//    try {
//      this.m.execute();
//    } catch (MojoExecutionException | MojoFailureException e) {
//      fail("You suck at all games!");
//    }
//  }
//
//  @Test(expected = MojoFailureException.class)
//  public void testExecute2() throws MojoExecutionException, MojoFailureException {
//    MockedPackerBean b = new MockedPackerBean("hard", null);
//    Reflect.on(m).set("data", b);
//    m.setPackerExecutable(target.resolve("packer").toAbsolutePath().toFile());
//    this.m.execute();
//    fail("You suck at all games!");
//  }
//  @Test(expected = MojoFailureException.class)
//  public void testExecute3() throws MojoExecutionException, MojoFailureException {
//    MockedPackerBean b = new MockedPackerBean(null, null); // Returns nothing
//    Reflect.on(m).set("data", b);
//    m.setPackerExecutable(target.resolve("packer").toAbsolutePath().toFile());
//    this.m.execute();
//    fail("You suck at all games!");
//  }
//  @Test
//  public void testExecute4() throws MojoExecutionException, MojoFailureException {
//    MockedPackerBean b = new MockedPackerBean("fail", null); // Returns nothing
//    Reflect.on(m).set("data", b);
//    m.setPackerExecutable(target.resolve("packer").toAbsolutePath().toFile());
//    this.m.execute();
//    // Doesn't fail if soft-fails
//  }
//  @Test
//  public void testExecute5() throws MojoExecutionException, MojoFailureException {
//    MockedPackerBean b = new MockedPackerBean("fake", null); // Returns nothing
//    Reflect.on(m).set("data", b);
//    m.setPackerExecutable(target.resolve("packer").toAbsolutePath().toFile());
//    this.m.execute();
//    // Doesn't fail if soft-fails
//  }
//
//  @Test(expected = MojoExecutionException.class)
//  public void testExecuteBogusExecutionId() throws MojoExecutionException, MojoFailureException {
//    exec = new MojoExecution(null, "executionId");
//    Reflect.on(m).set("mojo", exec);
//    m.setPackerExecutable(target.resolve("packer").toAbsolutePath().toFile());
//    this.m.execute();
//  }
//
//  @Test
//  public void testExecuteSkip() {
//    m.setPackerExecutable(target.resolve("packer").toAbsolutePath().toFile());
//    m.setSkip(true);
//    try {
//      this.m.execute();
//    } catch (MojoExecutionException | MojoFailureException e) {
//      fail("You suck at all games!");
//    }
//  }
//
//  @Test
//  public void testMinorSetters() {
//    m.setEncoding("UTF_16");
//    assertEquals(StandardCharsets.UTF_16, m.getEncoding());
//    m.setTimeout("PT10S");
//    assertEquals("PT10S", m.getTimeout());
//    File f = target.resolve("f").toFile();
//    m.setVarFile(f);
//    assertEquals(f, m.getVarFile());
//    Map<String, String> vars = Collections.emptyMap();
//    m.setVars(vars);
//    assertEquals(vars, m.getVars());
//    assertFalse(m.isParallel());
//    assertFalse(m.isCleanupOnError());
//    assertFalse(m.isForce());
//    assertTrue(m.isOverwrite());
//    assertFalse(m.isSkipIfEmpty());
//  }
//
//  //  @Test
//  //  public void testResolve() {
//  //    List<PackerManifest> g = this.m.resolve(resolvedArtifacts);
//  //    assertNotNull(g);
//  //  }
//
//  @Test(expected = PackerException.class)
//  public void testSetPackerExecutable1() {
//    m.getPackerExecutable();
//  }
//
//  @Test(expected = PackerException.class)
//  public void testSetPackerExecutable2() {
//    m.setPackerExecutable(test.toFile());
//  }
//
//  //
//  //  @Test
//  //  public void testGetImage() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testContextualize() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testExecute() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetProperties() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetAdditionalEnvironment() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetAdditionalPrintStream() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetArchive() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetArchiverManager() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetArtifactHandler() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetArtifactHandlerManager() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetAuthFileWriter() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetBaseAuthentications() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetClassifier() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetCoordinates() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetDescription() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetEncoding() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetExcept() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetFinalName() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetOnly() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetOutputDirectory() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetPackerArchiver() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetPackerManifests() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetPackerExecutable() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetPlexusContainer() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetPlugin() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetProjectBuildDirectory() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetRemoteRepos() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetRepoSession() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetRequirements() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetSettingsJSON() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetTimeout() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetTmpDir() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetVarFile() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetVars() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetWorkingDir() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testGetWorkingDirectory() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testIsCleanupOnError() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testIsForce() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testIsOverwrite() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testIsParallel() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testIsSkip() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testIsSkipActualPackerRun() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testIsSkipIfEmpty() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testIsUnpackByDefault() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetAdditionalEnvironment() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetAetherRepositorySystem() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetAuthConfig() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetBaseAuthentications() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetClassifier() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetFinalName() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetOutputDirectory() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetPackerExecutable() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetRepoSession() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetSettings() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetTimeout() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetTmpDir() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testSetWorkingDir() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testStart() {
//  //    fail("Not yet implemented");
//  //  }
//  //
//  //  @Test
//  //  public void testStop() {
//  //    fail("Not yet implemented");
//  //  }
//
//}
