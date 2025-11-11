package gitinspector;

import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {

		List<BlameData> result = GitInspector.process(args[0]);

		for(BlameData bd:result) {
			System.out.println(bd.getLine()+"\t"+bd.getFlag()+"\t"+bd.getAuthor());
		}

	}

}
