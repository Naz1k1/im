package com.naz1k1.controller;

import com.naz1k1.model.request.user.FriendRequestDTO;
import com.naz1k1.model.response.FriendVO;
import com.naz1k1.model.response.Result;
import com.naz1k1.model.response.UserSearchVO;
import com.naz1k1.service.FriendRelationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend-relation")
public class FriendRelationController {

    private final FriendRelationService friendRelationService;

    public FriendRelationController(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
    }

    @GetMapping("/list")
    public Result<List<FriendVO>> getFriendList(@RequestAttribute Long userId) {
        List<FriendVO> friends = friendRelationService.getFriendList(userId);
        return Result.success(friends);
    }

    @GetMapping("/online")
    public Result<List<FriendVO>> getOnlineFriends(@RequestAttribute Long userId) {
        List<FriendVO> onlineFriends = friendRelationService.getOnlineFriends(userId);
        return Result.success(onlineFriends);
    }

    @GetMapping("/search-user")
    public Result<UserSearchVO> searchUser(
            @RequestParam String username,
            @RequestAttribute Long userId) {
        UserSearchVO vo = friendRelationService.searchUser(userId, username);
        return Result.success(vo);
    }

    @PostMapping("/add-friend")
    public Result<Void> addFriend(
            @RequestAttribute Long userId,
            @RequestBody FriendRequestDTO dto) {
        if (friendRelationService.addFriend(userId, dto.getFriendId(), dto.getRemark())) {
            return Result.success();
        }
        return Result.failed();
    }

    @PostMapping("/delete-friend")
    public Result<Void> deleteFriend(
            @RequestAttribute Long userId,
            @RequestParam Long friendId) {
        if (friendRelationService.deleteFriend(userId, friendId)) {
            return Result.success();
        }
        return Result.failed();
    }
}
