package com.pmart5a.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.pmart5a.controller.PostController;
import com.pmart5a.repository.PostRepository;
import com.pmart5a.repository.PostRepositoryStubImpl;
import com.pmart5a.service.PostService;

@Configuration
public class JavaConfig {

  @Bean
  public PostController postController(PostService service) {
    return new PostController(service);
  }

  @Bean
  public PostService postService(PostRepository repository) {
    return new PostService(repository);
  }

  @Bean
  public PostRepository postRepository() {
    return new PostRepositoryStubImpl();
  }
}