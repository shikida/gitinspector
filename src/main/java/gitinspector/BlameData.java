package gitinspector;

import java.util.Date;

public class BlameData {

	private int line;
	private String commit;
	private String author;
	private Date date;
	private String message;
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public String getCommit() {
		return commit;
	}
	public void setCommit(String commit) {
		this.commit = commit;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "BlameData [line=" + line + ", commit=" + commit + ", author=" + author + ", date=" + date + ", message="
				+ message + "]";
	}
	
	
}
