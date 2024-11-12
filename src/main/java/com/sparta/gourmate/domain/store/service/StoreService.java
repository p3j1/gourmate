package com.sparta.gourmate.domain.store.service;

import com.sparta.gourmate.domain.review.repository.ReviewRepository;
import com.sparta.gourmate.domain.store.dto.StoreRequestDto;
import com.sparta.gourmate.domain.store.dto.StoreResponseDto;
import com.sparta.gourmate.domain.store.entity.Category;
import com.sparta.gourmate.domain.store.entity.Store;
import com.sparta.gourmate.domain.store.repository.CategoryRepository;
import com.sparta.gourmate.domain.store.repository.StoreRepository;
import com.sparta.gourmate.domain.user.entity.User;
import com.sparta.gourmate.domain.user.entity.UserRoleEnum;
import com.sparta.gourmate.global.exception.CustomException;
import com.sparta.gourmate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    // 가게 생성
    public StoreResponseDto createStore(StoreRequestDto requestDto, User user) {
        checkRole(user);    // 권한 확인
        Category category = checkCategory(requestDto.getCategoryId());  // 카테고리 확인

        Store store = new Store(requestDto, user, category);
        storeRepository.save(store);
        return new StoreResponseDto(store);
    }

    // 카테고리 확인
    private Category checkCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    // 권한 확인
    private void checkRole(User user) {
        UserRoleEnum role = user.getRole();

        if (role != UserRoleEnum.MANAGER) {
            throw new CustomException(ErrorCode.AUTH_AUTHORIZATION_FAILED);
        }
    }
}