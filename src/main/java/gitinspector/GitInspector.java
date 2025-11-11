package gitinspector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitInspector {

	private static final String FLAG = "ASVS 5.0.0";

	public static void main(String[] args) throws Exception {
		List<BlameData> result = process("C:\\Users\\119667631\\git\\HCOM_202501_CodificacaoSeguraV2pub");
		for(BlameData bd:result) {
			System.out.println(bd.getLine()+"\t"+bd.getFlag()+"\t"+bd.getAuthor());
		}
	}
	
	public static List<BlameData> process(String path) throws Exception {

		// look for ASVS at full path
		List<File> allFiles = new ArrayList<>();

		// Path to your local .git directory
		File repoDir = new File(path);

		traverseDirectory(repoDir, allFiles);
		
		List<BlameData> allData = new ArrayList<>();
		
		for(File f:allFiles) {
			Map<Integer,String> lines = grep(f,FLAG);
			for(Integer line:lines.keySet()) {
				String relativeFilePath = getRelativeFilePath(repoDir,f);
				BlameData data = getBlame(repoDir, relativeFilePath, line);	
				data.setFlag(lines.get(line));
				allData.add(data);
			}
		}

		return allData;
	}

	private static Map<Integer,String> grep(File f, String pattern) throws IOException {
		Map<Integer,String> matchingLines = new LinkedHashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(f.toPath())) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.contains(pattern)) {
                    matchingLines.put(lineNumber,line);
                }
            }
        }catch(Exception e) {
        	System.err.println("Could not read "+f.getAbsolutePath());
        }

        return matchingLines;
    }
	
	private static String getRelativeFilePath(File repoDir, File f) {
		Path repoRoot = Paths.get(repoDir.getAbsolutePath()).normalize();
        Path filePath = Paths.get(f.getAbsolutePath()).normalize();

        Path relativePath = repoRoot.relativize(filePath);

        return relativePath.toString().replace("\\", "/");
	}

	private static void traverseDirectory(File directory, List<File> allFiles) {
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile() && !file.getName().endsWith(".class")) {
						allFiles.add(file);
					} else if (file.isDirectory() && !file.getName().equals(".git")) {
						traverseDirectory(file, allFiles);
					}
				}
			}
		}
	}

	private static BlameData getBlame(File repoDir, String relativeFilePath, int lineNumber) throws Exception {

		BlameData data = new BlameData();

		try (Repository repository = new FileRepositoryBuilder().findGitDir(repoDir).setWorkTree(repoDir)
				.readEnvironment().build()) {

			try (Git git = new Git(repository)) {
				BlameResult blameResult = git.blame().setFilePath(relativeFilePath).call();
				

				if (blameResult != null) {
					RevCommit commit = blameResult.getSourceCommit(lineNumber - 1);
					PersonIdent author = blameResult.getSourceAuthor(lineNumber - 1);

					if (commit != null && author != null) {

						data.setLine(lineNumber);
						data.setAuthor(author.getName());
						data.setCommit(commit.getName());
						data.setDate(Date.from(author.getWhenAsInstant()));
						data.setMessage(commit.getShortMessage());

					} else {
						System.err.println("No commit info found for line " + lineNumber);
					}
				} else {
					System.err.println("Blame result is null for file " + relativeFilePath);
				}
			}
		}

		return data;

	}

}
