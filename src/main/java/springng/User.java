package springng;

import java.util.ArrayList;

public class User {
	
	private long id;
	private String first;
	private String last;
	private ArrayList<Task> todoList;
	
	public User(long id, String first, String last){
		this.id = id;
		this.first = first;
		this.last = last;
		this.todoList = new ArrayList<Task>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public ArrayList<Task> getTodoList() {
		return todoList;
	}

	public void setTodoList(ArrayList<Task> todoList) {
		this.todoList = todoList;
	}


	
	

}
