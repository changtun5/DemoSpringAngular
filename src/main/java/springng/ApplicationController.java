package springng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

	@RequestMapping(value="/users", method=RequestMethod.GET)
	public HashMap<Long, User> showUsers(){
		return getUsersWithTask();
	}
	
	@RequestMapping(value="/users/{userId}", method=RequestMethod.GET)
	public User showUsers(@PathVariable String userId){
		return getUsersWithTask(Long.valueOf(userId));
	}
	
	@RequestMapping(value="/addUser/{first}/{last}", method=RequestMethod.POST)
	public User insertUser(@PathVariable String first, @PathVariable String last){
		jt.update("insert into User (first, last) values(?,?)", first, last);
		return getUsers(first, last);
	}
	
	@RequestMapping(value="/editUser/{userId}/{first}/{last}", method=RequestMethod.POST)
	public void updateUser(@PathVariable String userId, @PathVariable String first,
			@PathVariable String last){
		jt.update("update User set first = ?, last = ? where id = ?", first,
				last, Long.valueOf(userId));
	}
	
	@RequestMapping(value="/users/{userId}/tasks", method=RequestMethod.GET)
	public List<Task> showUserTask(@PathVariable String userId){
		return getTasks(Long.valueOf(userId));
	}
	
	@RequestMapping(value="/removeUser/{userId}", method=RequestMethod.POST)
	public void deleteUser(@PathVariable String userId){
		jt.update("delete from User where id = ?", Long.valueOf(userId));
	}
	
	@RequestMapping(value="/addTask/{userId}/{task}", method=RequestMethod.POST)
	public List<Task> insertTask(@PathVariable String userId, @PathVariable String task){
		jt.update("insert into Task (user, task) values(?,?)",
				Long.valueOf(userId), task);
		return getTaskList();
	}
	
	@RequestMapping(value="/removeTask/{taskId}", method=RequestMethod.POST)
	public List<Task> deleteTask(@PathVariable String taskId){
		jt.update("delete from Task where id = ?", Long.valueOf(taskId));
		return getTaskList();
	}
	
	@RequestMapping(value="/tasks", method=RequestMethod.GET)
	public List<Task> showAllTasks(){
		return getTaskList();
	}
	
	
	
	@Autowired
	JdbcTemplate jt;
	
	//get all users
	public HashMap<Long, User> getUsers(){
		HashMap<Long, User> output = new HashMap<Long, User>();
		List<User> results = jt.query("select * from User", 
				new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new User(rs.getLong("id"), rs.getString("first"),
						rs.getString("last"));
			}
		}
				);
		for(User u : results){
			output.put(u.getId(), u);
		}
		return output;
	}
	
	//get specific user
	public HashMap<Long, User> getUsers(Long userId){
		HashMap<Long, User> output = new HashMap<Long, User>();
		List<User> results = jt.query("select * from User where id = ?", new Object[]{userId}, 
				new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new User(rs.getLong("id"), rs.getString("first"),
						rs.getString("last"));
			}
		}
				);
		for(User u : results){
			output.put(u.getId(), u);
		}
		return output;
	}
	
	//get all task with key as user id
	public HashMap<Long, ArrayList<Task>> getTasks(){
		HashMap<Long, ArrayList<Task>> output = new HashMap<Long, ArrayList<Task>>();
		List<Task> results = jt.query("select * from Task", 
				new RowMapper<Task>(){
			@Override
			public Task mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new Task(rs.getLong("id"), rs.getLong("user"),
						rs.getString("task"));
			}
		}
				);
		for(Task t : results){
			if(output.containsKey(t.getUser())){
				output.get(t.getUser()).add(t);
			}else{
				output.put(t.getUser(), new ArrayList<Task>());
				output.get(t.getUser()).add(t);
			}
		}
		return output;
	}
	
	//get all task
	public List<Task> getTaskList(){
		List<Task> results = jt.query("select * from Task order by id desc", 
				new RowMapper<Task>(){
			@Override
			public Task mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new Task(rs.getLong("id"), rs.getLong("user"),
						rs.getString("task"));
			}
		});
		return results;
	}
	
	//get task for specific user
	public List<Task> getTasks(Long userId){
		List<Task> results = jt.query("select * from Task where user = ?", 
				new Object[]{userId}, 
				new RowMapper<Task>(){
			@Override 
			public Task mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new Task(rs.getLong("id"), rs.getLong("user"),
						rs.getString("task"));
			}
		});
		return results;
	}
	
	//get the combined data of users and its associated tasks
	public HashMap<Long, User> getUsersWithTask(){
		HashMap<Long, User> output = new HashMap<Long, User>();
		HashMap<Long, User> users = getUsers();
		HashMap<Long,ArrayList<Task>> tasks = getTasks();
		Iterator it = users.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			output.put((Long)pair.getKey(), users.get((Long)pair.getKey()));
			if(tasks.containsKey((Long)pair.getKey())){
				output.get((Long)pair.getKey()).setTodoList(tasks.get((Long)pair.getKey()));
			}
			it.remove();
		}
		return output;
	}
	
	//get the combined data of a specific user and its associated tasks
	public User getUsersWithTask(long userId){
		HashMap<Long, User> usersWithTasks = getUsersWithTask();
		return usersWithTasks.get(userId);
	}
	
	//get user by first and last name for post method
	public User getUsers(String first, String last){
		List<User> result = jt.query("select * from User where first like ? and last like ?",
				new Object[]{first, last}, new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new User(rs.getLong("id"), rs.getString("first"),
						rs.getString("last"));
			}
		});
		
		return result.get(0);
	}
	
	//get task by task name and user id
	public Task getTasks(long userId, String name){
		List<Task> result = jt.query("select * from Task where user = ? and task like ?", 
				new Object[]{userId, name}, new RowMapper<Task>(){
			@Override
			public Task mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new Task(rs.getLong("id"), rs.getLong("user"), rs.getString("task"));
			}
		});
		
		return result.get(0);
	}
	
}
