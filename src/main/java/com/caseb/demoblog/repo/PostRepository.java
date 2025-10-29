package com.caseb.demoblog.repo;

import com.caseb.demoblog.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
