package com.caseb.demoblog.controllers;

import com.caseb.demoblog.models.Post;
import com.caseb.demoblog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    /*если в бд статьи нет, возвращаемся на дефолтную*/
    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long idPost, Model model) {
        Optional<Post> post = postRepository.findById(idPost);

        if (post.isEmpty()) {
            return "redirect:/blog"; // поста нет — редирект на главную блог-страницу
        }

        // пост есть — кладём его в список, если шаблон ожидает список
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "blog-details";
    }

    /*отслеживаем url адресс*/
    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long idPost, Model model) {
        Optional<Post> post = postRepository.findById(idPost);

        if (post.isEmpty()) {
            return "redirect:/blog"; // поста нет — редирект на главную блог-страницу
        }

        // пост есть — кладём его в список, если шаблон ожидает список
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long idPost,
                                 @RequestParam String title,
                                 @RequestParam String anons,
                                 @RequestParam String full_text,
                                 Model model) {
        Post post = postRepository.findById(idPost).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);

        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long idPost, Model model) {
        Post post = postRepository.findById(idPost).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }


}
