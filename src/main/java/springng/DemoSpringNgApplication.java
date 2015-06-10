package springng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DemoSpringNgApplication implements CommandLineRunner{

    public static void main(String[] args){
        SpringApplication.run(DemoSpringNgApplication.class, args);
    }
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String...strings) throws Exception{
    	jdbcTemplate.execute("drop table User if exists");
    	jdbcTemplate.execute("create table User ("
    			+ "id serial, name varchar(255) unique)");
    	
    	jdbcTemplate.execute("drop table Task if exists");
    	jdbcTemplate.execute("create table Task ("
    			+ "id serial, user int, task varchar(255))");
    	
    }
    
    
}
