package gitinspector;

import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {

		List<BlameData> result = GitInspector.process(args[0]);

		System.out.println(result);
	}

}
