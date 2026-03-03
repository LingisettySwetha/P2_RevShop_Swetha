package com.rev.app.service;

import com.rev.app.entity.User;
import com.rev.app.entity.Seller;
import com.rev.app.repository.ISellerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellerServiceImplTest {

    @Mock
    private ISellerRepository sellerRepository;

    @InjectMocks
    private SellerServiceImpl sellerService;

    @Test
    void testCreateSeller() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("seller@test.com");
        
        sellerService.createSeller(user, "Test Biz");
        
        verify(sellerRepository).save(any());
    }

    @Test
    void testCreateSeller_WhenExists_DoesNotInsertDuplicate() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("seller@test.com");

        Seller existing = new Seller();
        existing.setSellerId(100L);
        existing.setUser(user);

        when(sellerRepository.findByUserUserId(1L)).thenReturn(java.util.Optional.of(existing));

        sellerService.createSeller(user, "Test Biz");

        verify(sellerRepository, never()).save(any());
    }
}
