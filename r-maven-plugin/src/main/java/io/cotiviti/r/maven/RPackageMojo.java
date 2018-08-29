package io.cotiviti.r.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import io.cotiviti.util.artifacts.impl.CotivitiVersionImpl;

public class RPackageMojo extends AbstractRMojo {
  public  Descriptor fromProject(MavenProject project) {
    Descriptor d = new Descriptor();
    d.setPackageString(project.getArtifactId());
    d.setVersion(new CotivitiVersionImpl(project.getVersion()));
    d.setDescription(project.getDescription());
    d.setEncoding(getEncoding());
    return d;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    // TODO Auto-generated method stub

  }

}
