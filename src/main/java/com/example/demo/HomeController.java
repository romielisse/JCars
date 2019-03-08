package com.example.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    CarRepository carRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CloudinaryConfig cloudc;

    /* first, create paths for car as usual */
    @RequestMapping("/")
    public String listCars(Model model){
        model.addAttribute("cars", carRepository.findAll());
        /* new addition for Category object */
        model.addAttribute("categories", categoryRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String carForm(Model model){
        model.addAttribute("car", new Car());
        /* new addition for Category object */
        model.addAttribute("categories", categoryRepository.findAll());
        return "carform";
    }

    /* add method for car picture */
    @PostMapping("/add")
    public String processActor(@ModelAttribute Car car, @RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcestype", "auto"));
            car.setCarpic(uploadResult.get("url").toString());
            carRepository.save(car);
        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
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
    @RequestMapping("/category")
    public String listCategories(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        return "listcategory";
    }

    /* new addition for Category object */
    @GetMapping("/addcategory")
    public String categoryForm(Model model){
        model.addAttribute("category", new Category());
        return "categoryform";
    }

    /* new addition for Category object */
    @PostMapping("/processcategory")
    public String processCategory(@Valid Category category, BindingResult result,
                                  Model model){
        if (result.hasErrors()){
            return "categoryform";
        }
        if (categoryRepository.findByCategoryName(category.getCategoryName()) != null){
            model.addAttribute("message", "You already have a category called " +
                    category.getCategoryName() + "! " + "Try something else.");
            return "categoryform";
        }
        categoryRepository.save(category);
        return "redirect:/";
    }

    /* lastly, the detail or update or delete paths as usual */
    @RequestMapping("/detailcar/{id}")
    public String showCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/updatecar/{id}")
    public String updateCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id).get());
        return "carform";
    }

    @RequestMapping("/deletecar/{id}")
    public String deleteCar(@PathVariable("id") long id){               // delete isn't working
        carRepository.deleteById(id);
        return "redirect:/";
    }



    /* and a version for category class */
    @RequestMapping("/detailcategory/{id}")
    public String showCategory(@PathVariable("id") long id, Model model){
        model.addAttribute("category", categoryRepository.findById(id).get());
        return "showcategory";
    }

    @RequestMapping("/updatecategory/{id}")
    public String updateCategory(@PathVariable("id") long id, Model model){
        model.addAttribute("category", categoryRepository.findById(id).get());
        return "categoryform";
    }

    @RequestMapping("/deletecategory/{id}")
    public String deleteCategory(@PathVariable("id") long id){
        categoryRepository.deleteById(id);
        return "redirect:/";
    }

     /*
    The PostConstruct annotation is used on a method that needs to be executed after dependency injection is done to perform any initialization.
    This method MUST be invoked before the class is put into service.
    */
     @PostConstruct
    public void fillTables(){
         Category c = new Category();
         c.setCategoryName("SUV");
         categoryRepository.save(c);

         c = new Category();
         c.setCategoryName("Compact");
         categoryRepository.save(c);

         c = new Category();
         c.setCategoryName("Hybrid");
         categoryRepository.save(c);

         c = new Category();
         c.setCategoryName("Pickup truck");
         categoryRepository.save(c);

         c = new Category();
         c.setCategoryName("Convertible");
         categoryRepository.save(c);

     }
}

