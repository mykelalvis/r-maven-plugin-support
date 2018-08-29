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
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.maven.plugin.logging.Log;
//
//public class TestLogHelper implements Log {
//
//  private List<Throwable> errors;
//
//  private List<CharSequence> infoMessages;
//
//  private List<CharSequence> warnMessages;
//
//  public TestLogHelper() {
//    warnMessages = new ArrayList<>();
//    infoMessages = new ArrayList<>();
//    errors = new ArrayList<>();
//  }
//
//  @Override
//  public void debug(CharSequence charSequence) {
//
//  }
//
//  @Override
//  public void debug(CharSequence charSequence, Throwable throwable) {
//
//  }
//
//  @Override
//  public void debug(Throwable throwable) {
//
//  }
//
//  @Override
//  public void error(CharSequence charSequence) {
//
//  }
//
//  @Override
//  public void error(CharSequence charSequence, Throwable throwable) {
//    errors.add(throwable);
//  }
//
//  @Override
//  public void error(Throwable throwable) {
//
//  }
//
//  public List<Throwable> getErrors() {
//    return errors;
//  }
//
//  public List<CharSequence> getInfoMessages() {
//    return infoMessages;
//  }
//
//  public List<CharSequence> getWarnMessages() {
//    return warnMessages;
//  }
//
//  @Override
//  public void info(CharSequence charSequence) {
//    infoMessages.add(charSequence);
//  }
//
//  @Override
//  public void info(CharSequence charSequence, Throwable throwable) {
//
//  }
//
//  @Override
//  public void info(Throwable throwable) {
//
//  }
//
//  @Override
//  public boolean isDebugEnabled() {
//    return true;
//  }
//
//  @Override
//  public boolean isErrorEnabled() {
//    return true;
//  }
//
//  @Override
//  public boolean isInfoEnabled() {
//    return true;
//  }
//
//  @Override
//  public boolean isWarnEnabled() {
//    return true;
//  }
//
//  @Override
//  public void warn(CharSequence charSequence) {
//    warnMessages.add(charSequence);
//  }
//
//  @Override
//  public void warn(CharSequence charSequence, Throwable throwable) {
//
//  }
//
//  @Override
//  public void warn(Throwable throwable) {
//
//  }
//}
