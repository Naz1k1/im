package com.naz1k1.controller;

import com.naz1k1.model.request.FriendRequestDTO;
import com.naz1k1.model.response.Result;
import com.naz1k1.model.response.UserSearchVO;
import com.naz1k1.service.FriendRelationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friend-relation/")
public class FriendRelationController {

    private final FriendRelationService friendRelationService;

    public FriendRelationController(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
    }

    @PostMapping("/search-user")
    public Result<?> searchUser(
            @RequestParam String username,
            @RequestAttribute Long userId) {
        UserSearchVO vo = friendRelationService.searchUser(userId, username);
        return Result.success(vo);
    }

    @PostMapping("/add-friend")
    public Result<?> addFriend(
            @RequestAttribute Long userId,
            @RequestBody FriendRequestDTO dto){
        if (friendRelationService.addFriend(userId,dto.getFriendId(),dto.getRemark())) {
            return Result.success();
        }
        return Result.failed();
    }

    @PostMapping("/delete-friend")
     public Result<?> deleteFriend(
             @RequestAttribute Long userId,
             @RequestParam Long friendId){
        if (friendRelationService.deleteFriend(userId,friendId)) {
            return Result.success();
        }
        return Result.failed();
    }
}
