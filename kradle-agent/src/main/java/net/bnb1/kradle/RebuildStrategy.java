package net.bnb1.kradle;

import org.gradle.tooling.GradleConnectionException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProgressListener;
import org.gradle.tooling.ResultHandler;

import java.nio.file.Path;

public class RebuildStrategy implements ChangeStrategy {

    private final Path projectPath;

    public RebuildStrategy(Path projectPath) {
        this.projectPath = projectPath;
    }

    @Override
    @SuppressWarnings({"CallToPrintStackTrace", "resource"})
    public void onChangeDetected() {
        var connection = GradleConnector.newConnector()
                .forProjectDirectory(projectPath.toFile())
                .useBuildDistribution()
                .connect();
        System.out.print("DEBUG Rebuilding ");
        System.out.flush();
        connection.newBuild()
                .forTasks("classes")
                .addProgressListener((ProgressListener) event -> {
                    System.out.print(".");
                    System.out.flush();
                })
                .run(new ResultHandler<>() {
                    @Override
                    public void onComplete(Void result) {
                        System.out.println();
                        System.out.println("DEBUG Done");
                        connection.close();
                    }

                    @Override
                    public void onFailure(GradleConnectionException failure) {
                        System.err.println("ERROR Rebuild failed");
                        failure.printStackTrace();
                        connection.close();
                    }
                });
    }
}
