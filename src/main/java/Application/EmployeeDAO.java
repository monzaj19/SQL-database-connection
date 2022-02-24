package Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    NamedParameterJdbcTemplate template;

    public EmployeeDAO(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Employee> list() {
        String sql = "SELECT * FROM Employees";

        List<Employee> employeeList = jdbcTemplate.query(sql, new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
                Employee e = new Employee();
                e.setEmployee_id(rs.getInt(1));
                e.setFirst_name(rs.getString(2));
                e.setLast_name(rs.getString(3));
                e.setEmail(rs.getString(4));
                e.setPhone_number(rs.getString(5));
                e.setHire_date(rs.getDate(6));
                e.setJob_id(rs.getString(7));
                e.setSalary(rs.getDouble(8));
                e.setCommission_pct(rs.getDouble(9));
                e.setManager_id(rs.getInt(10));
                e.setDepartment_id(rs.getInt(11));
                return e;
            }
        });

        return employeeList;
    }

    public void save(Employee employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee_id", employee.getEmployee_id());
        map.put("first_name", employee.getFirst_name());
        map.put("last_name", employee.getLast_name());
        map.put("email", employee.getEmail());
        map.put("phone_number", employee.getPhone_number());
        map.put("hire_date", employee.getHire_date());
        map.put("job_id", employee.getJob_id());
        map.put("salary", employee.getSalary());
        map.put("commission_pct", employee.getCommission_pct());
        map.put("manager_id", employee.getManager_id());
        map.put("department_id", employee.getDepartment_id());

        template.update("insert into employees values (:employee_id, :first_name, :last_name, :email, :phone_number," +
                ":hire_date, :job_id, :salary, :commission_pct, :manager_id, :department_id)", map);
    }

    public void delete(int employee_id) {
        String sql = "DELETE FROM EMPLOYEES WHERE employee_id = ?";
        jdbcTemplate.update(sql, employee_id);
    }

    public Employee get(int employee_id) {
        String sql = "SELECT * FROM EMPLOYEES WHERE employee_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{employee_id}, (rs, rowNum) ->
                new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("hire_date"),
                        rs.getString("job_id"),
                        rs.getDouble("salary"),
                        rs.getDouble("commission_pct"),
                        rs.getInt("manager_id"),
                        rs.getInt("department_id")
                ));
    }

    public void update(Employee employee) {
        String sql = "UPDATE EMPLOYEES SET first_name=:first_name, last_name=:last_name, email=:email, phone_number=:phone_number, hire_date=:hire_date, job_id=:job_id, salary=:salary, commission_pct=:commission_pct, manager_id=:manager_id, department_id=:department_id WHERE employee_id=:employee_id";
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(employee);
        template = new NamedParameterJdbcTemplate(jdbcTemplate);
        template.update(sql, param);
    }
}
