package com.sparta.gourmate.domain.store.service;

import com.sparta.gourmate.domain.review.dto.ReviewResponseDto;
import com.sparta.gourmate.domain.review.entity.Review;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    // 가게 생성
    public StoreResponseDto createStore(StoreRequestDto requestDto, User user) {
        checkRole(user);    // 권한 확인
        Category category = checkCategory(requestDto.getCategoryId());  // 카테고리 확인

        Store store = new Store(requestDto, user, category);
        storeRepository.save(store);
        return new StoreResponseDto(store);
    }

    // 가게 목록 조회
    public Page<StoreResponseDto> getStoreList(String query, UUID categoryId, String sortBy,
                                               boolean isAsc, int page, int size) {
        // 정렬
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Store> storeList = storeRepository.findByCategoryIdAndNameContaining(categoryId, query, pageable);

        return storeList.map(StoreResponseDto::new);
    }

    // 가게 조회
    public StoreResponseDto getStore(UUID storeId) {
        Store store = checkStore(storeId);  // 가게 확인

        return new StoreResponseDto(store);
    }

    // 가게 리뷰 조회
    public Page<ReviewResponseDto> getReviewList(UUID storeId, String sortBy, boolean isAsc, int page, int size) {
        // 정렬
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Review> reviewList = reviewRepository.findAllByStoreId(storeId, pageable);

        return reviewList.map(ReviewResponseDto::new);
    }

    // 가게 수정
    public StoreResponseDto updateStore(UUID storeId, StoreRequestDto requestDto, User user) {
        Store store = checkStore(storeId);  // 가게 확인
        Category category = checkCategory(requestDto.getCategoryId());  // 카테고리 확인
        checkRole(user);    // 권한 확인
        checkUser(store, user); // 유저 확인

        store.update(requestDto, category);

        return new StoreResponseDto(store);
    }

    // 유저 확인
    private void checkUser(Store store, User user) {
        Long userId = store.getUser().getId();
        if (!Objects.equals(userId, user.getId())) {
            throw new CustomException(ErrorCode.AUTH_AUTHORIZATION_FAILED);
        }
    }

    // 가게 확인
    private Store checkStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
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