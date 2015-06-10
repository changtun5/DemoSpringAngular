package springng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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
	public HashMap<Long, User> usersJson(){
		return getUsers();
	}
	
	@RequestMapping(value="/addUser/{userName}", method=RequestMethod.POST)
	public void addUser(@PathVariable String userName){
		jt.update("insert into User (name) values(?)", userName);
	}
	
	@Autowired
	JdbcTemplate jt;
	
	public HashMap<Long, User> getUsers(){
		HashMap<Long, User> outPut = new HashMap<Long, User>();
		List<User> results = jt.query("select id, name from User",
				new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException{
				return new User(rs.getLong("id"), rs.getString("name"));
			}
		});
		for(User u : results){
			outPut.put(u.getId(), u);
		}
		return outPut;
	}
	
}
