package com.wootecobook.turkey.post.controller;

import com.wootecobook.turkey.commons.GoodResponse;
import com.wootecobook.turkey.commons.resolver.UserSession;
import com.wootecobook.turkey.post.service.PostService;
import com.wootecobook.turkey.post.service.dto.PostRequest;
import com.wootecobook.turkey.post.service.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;

    public PostApiController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid final PostRequest postRequest, final UserSession userSession) {
        PostResponse postResponse = postService.save(postRequest, userSession.getId());
        final URI uri = linkTo(PostApiController.class).toUri();
        return ResponseEntity.created(uri).body(postResponse);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> list(@PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) final Pageable pageable,
                                                   final UserSession userSession) {
        Page<PostResponse> postResponses = postService.findPostResponses(pageable, userSession.getId());
        return ResponseEntity.ok(postResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable final Long id,
                                               @RequestBody @Valid final PostRequest postRequest,
                                               final UserSession userSession) {
        PostResponse postResponse = postService.update(postRequest, id, userSession.getId());

        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final Long id, final UserSession userSession) {
        postService.delete(id, userSession.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/good")
    public ResponseEntity good(@PathVariable final Long id, final UserSession userSession) {
        GoodResponse goodResponse = postService.good(id, userSession.getId());
        return ResponseEntity.ok(goodResponse);
    }

    @GetMapping("/{postId}/good/count")
    public ResponseEntity countGood(@PathVariable final Long postId, final UserSession userSession) {
        GoodResponse goodResponse = postService.countPostGoodByPost(postId, userSession.getId());
        return ResponseEntity.ok(goodResponse);
    }
}
