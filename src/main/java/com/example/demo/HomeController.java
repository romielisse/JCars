package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    CarRepository carRepository;

    @Autowired
    CategoryRepository categoryRepository;

    /* first, create paths for car as usual */
    @RequestMapping("/")
    public String listCars(Model model){
        model.addAttribute("cars", carRepository.findAll());
        /* new addition for Category object */
        model.addAttribute("categoies", categoryRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String carForm(Model model){
        model.addAttribute("car", new Car());
        /* new addition for Category object */
        model.addAttribute("categories", categoryRepository.findAll());
        return "carform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Car car, BindingResult result,
                              Model model){
        if (result.hasErrors()){
            return "carform";
        }
        carRepository.save(car);
        return "redirect:/";
    }

    /* then, add the paths for category. this is new :^) */

    /* new addition for Category object */
    @GetMapping("/addcategory")
    public String categoryForm(Model model){
        model.addAttribute("category", new Category());
        return "category";
    }

    /* new addition for Category object */
    @PostMapping("/processcategory")
    public String processCategory(@Valid Category category, BindingResult result,
                                  Model model){
        if (result.hasErrors()){
            return "category";
        }
        if (categoryRepository.findByName(category.getName()) != null){
            model.addAttribute("message", "You already have a category called " +
                    category.getName() + "!" + "Try something else.");
            return "category";
        }
        categoryRepository.save(category);
        return "redirect:/";
    }

    /* lastly, the detail or update or delete paths as usual */
    @RequestMapping("/detail/{id}")
    public String showCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id).get());
        return "carform";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") long id){
        carRepository.deleteById(id);
        return "redirect:/";
    }
}

