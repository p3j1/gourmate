package com.sparta.gourmate.domain.store.controller;

import com.sparta.gourmate.domain.store.dto.StoreRequestDto;
import com.sparta.gourmate.domain.store.dto.StoreResponseDto;
import com.sparta.gourmate.domain.store.service.StoreService;
import com.sparta.gourmate.domain.user.entity.User;
import com.sparta.gourmate.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    // 가게 생성
    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@Valid @RequestBody StoreRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        StoreResponseDto responseDto = storeService.createStore(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}