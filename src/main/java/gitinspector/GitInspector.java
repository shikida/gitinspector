package gitinspector;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;

public class GitInspector {

	public static void main(String[] args)  throws Exception {
		// Path to your local .git directory
        File repoDir = new File("/path/to/your/repo/.git");

        // Path to file relative to repo root (e.g., "src/main/java/com/example/App.java")
        String relativeFilePath = "src/main/java/com/example/App.java";

        // Line number (1-based)
        int lineNumber = 42;

        // Open repository
        try (Repository repository = new FileRepositoryBuilder()
                .setGitDir(repoDir)
                .readEnvironment()
                .findGitDir()
                .build()) {

            try (Git git = new Git(repository)) {
                // Run blame command
                BlameResult blameResult = git.blame()
                        .setFilePath(relativeFilePath)
                        .call();
 
                if (blameResult != null) {
                    RevCommit commit = blameResult.getSourceCommit(lineNumber - 1);
                    PersonIdent author = blameResult.getSourceAuthor(lineNumber - 1);

                    if (commit != null && author != null) {
                        System.out.println("Line " + lineNumber + " last modified by:");
                        System.out.println("Commit: " + commit.getName());
                        System.out.println("Author: " + author.getName() + " <" + author.getEmailAddress() + ">");
                        System.out.println("Date: " + author.getWhen());
                        System.out.println("Message: " + commit.getShortMessage());
                    } else {
                        System.out.println("No commit info found for line " + lineNumber);
                    }
                } else {
                    System.out.println("Blame result is null for file " + relativeFilePath);
                }
            }
        }

	}

}
