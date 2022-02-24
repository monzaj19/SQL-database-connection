package Application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AppController {

    @Autowired
    private EmployeeDAO dao;


    @RequestMapping(value = "/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }

    @RequestMapping("/employees")
    public String showEmployee(Model model) {
        model.addAttribute("employeeList", dao.list());
        return "employees";
    }

    @RequestMapping("/new")
    public String showNewForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "new_form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("employee") Employee employee) {
        dao.save(employee);
        return "redirect:/";
    }

    @RequestMapping("/delete/{employee_id}")
    public String delete(@PathVariable(name = "employee_id") int employee_id) {
        dao.delete(employee_id);
        return "redirect:/employees";
    }

    @RequestMapping("/edit/{employee_id}")
    public ModelAndView editForm(@PathVariable(name = "employee_id") int employee_id) {
        ModelAndView modelAndView = new ModelAndView("edit_form");
        Employee employee = dao.get(employee_id);
        modelAndView.addObject("employee", employee);
        return modelAndView;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute("employee") Employee employee){
        dao.update(employee);
        return "redirect:/";
    }


}
