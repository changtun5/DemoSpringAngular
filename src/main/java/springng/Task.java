package springng;

public class Task {
	private long id;
	private long user;
	private String task;
	
	public Task(long id, long user, String task){
		this.id = id;
		this.user = user;
		this.task = task;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	
	
}
